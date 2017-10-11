package com.liking.socket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

import com.liking.socket.model.IHeaderAssemble;
import com.liking.socket.model.IHeaderResolver;
import com.liking.socket.model.message.MessageData;
import com.liking.socket.model.message.PingPongMsg;
import com.liking.socket.receiver.CmdResolver;
import com.liking.socket.utils.AESUtils;
import com.liking.socket.utils.BaseThread;
import com.liking.socket.utils.LogUtils;
import com.liking.socket.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public class SocketIO {
    /**
     * 发送队列
     */
    private BlockingQueue<MessageData> mSendQueue = new ArrayBlockingQueue<>(Constant.SENDER_QUEUE_SIZE);
    /**
     * 缓存队列
     */
    private HashMap<Long, MessageData> mCacheQueue = new LinkedHashMap<>(Constant.SENDER_QUEUE_SIZE);

    private String mDomain;
    private int mPort;

    private Context mContext;

    private IHeaderAssemble mHeaderAssemble;
    private IHeaderResolver mHeaderResolver;

    private SparseArray<CmdResolver> mResolver;
    private List<MessageData> mAutoSendMsg;
    private PingPongMsg mPingPong = new PingPongMsg();

    private Socket mSocket;
    private ReceiverWorker mReceiverWorker;
    private SenderWorker mSenderWorker;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.HANDLER_MSG:
                    handlerMsg(msg.getData());
                    break;
                case Constant.HANDLER_CONNECT:
                    handlerConnect(msg.getData());
                    break;

                default:
                    break;
            }
        }
    };

    private void handlerMsg(Bundle bundle) {
        byte cmd = bundle.getByte(Constant.KEY_MSG_CMD);
        boolean isError = bundle.getBoolean(Constant.KEY_MSG_IS_ERROR);
        String body = bundle.getString(Constant.KEY_MSG_DATA);

        CmdResolver resolver = mResolver.get(cmd);
        if (null != resolver) { // 默认处理的协议
            resolver.callBack(body, SocketIO.this);
        } else {
            LogUtils.print("Send Broadcast: %02X %s", cmd, body);

            Intent intent = new Intent(Constant.ACTION_MSG);
            intent.putExtra(Constant.KEY_MSG_CMD, cmd);
            intent.putExtra(Constant.KEY_MSG_IS_ERROR, isError);
            intent.putExtra(Constant.KEY_MSG_DATA, body);
            mContext.sendBroadcast(intent);
        }
    }

    private void handlerConnect(Bundle bundle) {
        Intent intent = new Intent(Constant.ACTION_CONNECT);
        mContext.sendBroadcast(intent);
    }

    /**
     * 重连
     */
    private void reconnect() {
        resetPingPong();

        mHandler.removeCallbacks(mConnectRunnable);
        mHandler.postDelayed(mConnectRunnable, Constant.DEFAULT_RECONNECT);
    }

    /**
     * 心跳
     */
    private void resetPingPong() {
        mHandler.removeCallbacks(mPingPongRunnable);
        mHandler.postDelayed(mPingPongRunnable, Constant.DEFAULT_PING_PONG);
    }

    /**
     * 失败重试
     */
    private void resetRetry() {
        mHandler.removeCallbacks(mRetryRunnable);
        mHandler.post(mRetryRunnable);
        mHandler.postDelayed(mRetryRunnable, Constant.DEFAULT_RETRY_INTERVAL);
    }

    private Runnable mConnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (null == mSocket || mSocket.isClosed()) {
                LogUtils.print("Connecting...");

                close(false);

                new InitSocket().start();
            }
        }
    };

    private Runnable mRetryRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCacheQueue.size() > 0) {
                LogUtils.print("Retry send message: %d", mCacheQueue.size());

                Iterator<Entry<Long, MessageData>> iterator = mCacheQueue.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<Long, MessageData> entry = iterator.next();
                    MessageData msg = entry.getValue();
                    if (msg.canRetry()) {
                        mSendQueue.add(msg);
                        msg.retry();
                    } else {
                        sendHandlerMessage(msg.cmd(), false, "Retry times out.");
                    }
                    iterator.remove();
                }
            }

            mHandler.postDelayed(mRetryRunnable, Constant.DEFAULT_RETRY_INTERVAL);
        }
    };

    private Runnable mPingPongRunnable = new Runnable() {
        @Override
        public void run() {
            // 此处可设置心跳开关

            if (mPingPong.canRetry()) {
                send(mPingPong);
                mPingPong.retry();

                resetPingPong();
            } else {
                reconnect();
            }
        }
    };

    private SocketIO(String domain, int port,
                     Context context,
                     IHeaderResolver headerResolver,
                     IHeaderAssemble headerAssemble,
                     SparseArray<CmdResolver> resolver,
                     List<MessageData> autoSendMsg) {
        mDomain = domain;
        mPort = port;
        mContext = context;
        mHeaderResolver = headerResolver;
        mHeaderAssemble = headerAssemble;
        mResolver = resolver;
        mAutoSendMsg = autoSendMsg;

        mHandler.post(mConnectRunnable);
    }

    private class InitSocket extends Thread {
        @Override
        public void run() {
            LogUtils.print("Connect start：%d", System.currentTimeMillis());

            try {
                // 创建Socket
                mSocket = new Socket(mDomain, mPort);

                // 接收线程
                mReceiverWorker = new ReceiverWorker(mHeaderResolver, mSocket, mSocket.getInputStream());
                mReceiverWorker.start();

                // 发送线程
                mSenderWorker = new SenderWorker(mSendQueue, mHeaderAssemble, mSocket, mSocket.getOutputStream());
                mSenderWorker.start();

                sendDefaultMessage();

                resetRetry();

                resetPingPong();

                sendConnectMessage();
            } catch (IOException e) {
                e.printStackTrace();

                reconnect();
            }

            LogUtils.print("Connect   end：%d", System.currentTimeMillis());
        }
    }

    /**
     * 发送默认消息
     */
    private void sendDefaultMessage() {
        // 保证先发送default消息
        while (mSendQueue.size() > 0) {
            try {
                MessageData msg = mSendQueue.take();
                if (msg.cmd() != Constant.CMD_PING_PONG) {
                    mCacheQueue.put(msg.getMsgId(), msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 发送链接建立后需要先发的消息
        mSendQueue.addAll(mAutoSendMsg);
    }

    public void close(boolean exit) {
        mHandler.removeCallbacksAndMessages(null);

        if (null != mSenderWorker) {
            mSenderWorker.quit();
        }
        mSenderWorker = null;

        if (null != mReceiverWorker) {
            mReceiverWorker.quit();
        }
        mReceiverWorker = null;

        try {
            if (null != mSocket) {
                mSocket.close();
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exit) {
            if (mSendQueue.size() > 0) {
                for (MessageData msg : mSendQueue) {
                    // msg.callBack(false, ""); // TODO: 2017/10/10
                }
                mSendQueue.clear();
            }

            if (mCacheQueue.size() > 0) {
                for (Entry<Long, MessageData> entry : mCacheQueue.entrySet()) {
                    // entry.getValue().callBack(false, "Exit reconnect"); // TODO: 2017/10/10  
                }
            }
        }
    }

    /**
     * 发送数据
     *
     * @param data 消息
     */
    public void send(MessageData data) {
        if (null == data) {
            return;
        }
        try {
            mSendQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class SenderWorker extends BaseThread {
        private Socket mSocket;
        private OutputStream mOut;

        private BlockingQueue<MessageData> mSendQueue;
        private IHeaderAssemble mHeaderAssemble;

        public SenderWorker(BlockingQueue<MessageData> sendQueue,
                            IHeaderAssemble assemble,
                            Socket socket,
                            OutputStream out) {
            mSendQueue = sendQueue;
            mHeaderAssemble = assemble;
            mSocket = socket;
            mOut = out;
        }

        @Override
        public void run() {
            MessageData msg;
            while (true) {
                try {
                    msg = mSendQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    LogUtils.print("Interrupted when end msg");
                    return; // 被中断就返回
                }

                try {
                    byte[] data = mHeaderAssemble.assemble(msg);

                    mOut.write(data);
                    mOut.flush();

                    LogUtils.print("Send msg: %02X %d %s", msg.cmd(), msg.getMsgId(), new String(msg.getData()));

                    if (msg.cmd() != Constant.CMD_PING_PONG && msg.cmd() != Constant.CMD_FEEDBACK) {
                        mCacheQueue.put(msg.getMsgId(), msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    LogUtils.print("OutputStream closed, reconnect");

                    if (Constant.CMD_PING_PONG != msg.cmd()) { // 心跳不重试
                        mCacheQueue.put(msg.getMsgId(), msg);
                    }

                    try {
                        mSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    quit();
                    reconnect();
                    return;
                }
            }
        }
    }

    private class ReceiverWorker extends BaseThread {
        private static final int BUFFER_SIZE = 1024 * 64; // TODO: 2017/9/9 最大的包

        private IHeaderResolver mHeader;
        private Socket mSocket;
        private InputStream mIn;

        private byte[] mBuffer = new byte[BUFFER_SIZE];
        private byte[] mHeaderBuffer;

        public ReceiverWorker(IHeaderResolver header, Socket socket, InputStream in) {
            mHeader = header;
            mSocket = socket;
            mIn = in;

            mHeaderBuffer = new byte[header.headerLength()];
        }

        @Override
        public void run() {
            int size, bodyLength;
            while (true) {
                try {
                    size = mIn.read(mHeaderBuffer);
                    if (size == -1) {
                        throw new IOException("InputStream closed!");
                    }
                    if (size != mHeader.headerLength()) {
                        throw new IOException("Header Read error!");
                    }

                    mHeader.resolver(mHeaderBuffer);

                    bodyLength = mHeader.bodyLength();

                    size = mIn.read(mBuffer, 0, bodyLength);
                    if (size == -1 || size < bodyLength) {
                        throw new IOException("Body read error!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    LogUtils.print("InputStream closed error: %s", e.getMessage());

                    try {
                        mSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    quit();
                    reconnect();
                    return;
                }

                resetPingPong();

                byte[] data = new byte[bodyLength];
                System.arraycopy(mBuffer, 0, data, 0, bodyLength);
                byte[] originData = AESUtils.decode(data);
                String body = new String(originData, Constant.DEFAULT_CHARSET);

                switch (mHeader.getCmd()) {
                    case Constant.CMD_PING_PONG:
                        LogUtils.print("Receive ping pong: %d", mHeader.getMsgID());
                        mPingPong.reset(); // // TODO: 2017/10/10
                        break;
                    case Constant.CMD_FEEDBACK:
                        try {
                            JSONObject obj = new JSONObject(body);
                            long msgID = obj.optLong("msg_id");
                            mCacheQueue.remove(msgID);

                            LogUtils.print("Feedback message: %d", msgID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        LogUtils.print("Receive message: %02X %d %s", mHeader.getCmd(), mHeader.getMsgID(), body);

                        sendFeedBack(mHeader);

                        sendHandlerMessage(mHeader.getCmd(), true, body);
                        break;
                }

                if (isQuit()) {
                    return;
                }
            }
        }
    }

    private void sendHandlerMessage(byte cmd, boolean isError, String body) {
        Message msg = mHandler.obtainMessage();
        msg.what = Constant.HANDLER_MSG;
        Bundle bundle = msg.getData();
        bundle.putByte(Constant.KEY_MSG_CMD, cmd);
        bundle.putBoolean(Constant.KEY_MSG_IS_ERROR, isError);
        bundle.putCharSequence(Constant.KEY_MSG_DATA, body);
        mHandler.sendMessage(msg);
    }

    private void sendConnectMessage() {
        Message msg = mHandler.obtainMessage();
        msg.what = Constant.HANDLER_CONNECT;
        mHandler.sendMessage(msg);
    }

    /**
     * 发送Feedback
     *
     * @param mHeader
     */
    private void sendFeedBack(final IHeaderResolver mHeader) {
        MessageData msg = new MessageData() {
            @Override
            public byte cmd() {
                return Constant.CMD_FEEDBACK;
            }

            @Override
            public byte[] getData() {
                JSONObject data = new JSONObject();
                try {
                    data.put("msg_id", mHeader.getMsgID());
                    data.put("device_id", SystemUtils.getDeviceID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data.toString().getBytes();
            }
        };
        send(msg);
    }

    /**
     * 构建SocketIO
     */
    public static final class Builder {
        private SparseArray<CmdResolver> mResolver = new SparseArray<>();// 同构命令字
        private List<MessageData> mDefaultMsg = new LinkedList<>(); // 重连后默认发送

        private String mDomain;
        private int mPort;

        private IHeaderResolver mHeaderResolver;
        private IHeaderAssemble mHeaderAssemble;

        private Context mContext;

        public Builder() {

        }

        public Builder connect(String domain, int port) {
            mDomain = domain;
            mPort = port;
            return this;
        }

        public Builder headerResolver(IHeaderResolver resolver) {
            mHeaderResolver = resolver;
            return this;
        }

        public Builder headerAssemble(IHeaderAssemble assemble) {
            mHeaderAssemble = assemble;
            return this;
        }

        public Builder addDefaultParse(CmdResolver resolver) {
            if (null != resolver) {
                mResolver.put(resolver.cmd(), resolver);
            }
            return this;
        }

        public Builder addDefaultSend(MessageData msg) {
            if (null != msg) {
                mDefaultMsg.add(msg);
            }
            return this;
        }

        public Builder bind(Context context) {
            mContext = context;
            return this;
        }

        public SocketIO build() {
            if (null == mDomain) {
                throw new IllegalStateException("IP is required.");
            }

            if (mPort <= 0) {
                throw new IllegalStateException("Port is required.");
            }

            if (null == mContext) {
                throw new IllegalStateException("Context is required.");
            }

            if (null == mHeaderResolver) {
                throw new IllegalStateException("IHeaderResolver is required.");
            }

            if (null == mHeaderAssemble) {
                throw new IllegalStateException("IHeaderAssemble is required.");
            }

            return new SocketIO(mDomain, mPort,
                    mContext,
                    mHeaderResolver, mHeaderAssemble,
                    mResolver,
                    mDefaultMsg);
        }
    }
}
