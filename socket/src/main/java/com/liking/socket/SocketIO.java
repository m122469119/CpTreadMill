package com.liking.socket;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.liking.socket.model.IHeaderAssemble;
import com.liking.socket.model.IHeaderResolver;
import com.liking.socket.model.message.MessageData;
import com.liking.socket.receiver.CmdResolver;
import com.liking.socket.utils.AESUtils;
import com.liking.socket.utils.BaseThread;
import com.liking.socket.utils.LogUtils;

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
    private HashMap<String, MessageData> mCacheQueue = new LinkedHashMap<>(Constant.SENDER_QUEUE_SIZE);

    private String mDomain;
    private int mPort;

    private IHeaderAssemble mHeaderAssemble;
    private IHeaderResolver mHeaderResolver;

    private SparseArray<CmdResolver> mResolver;
    private List<MessageData> mAutoSendMsg;
    private MessageData mPingPong;

    private Socket mSocket;
    private ReceiverWorker mReceiverWorker;
    private SenderWorker mSenderWorker;

    private boolean isAutoSendMsg;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private void reconnect() {
        mHandler.removeCallbacks(mConnectRunnable);
        mHandler.postDelayed(mConnectRunnable, Constant.DEFAULT_RECONNECT);
    }

    private void resetPingPong() {
        mHandler.removeCallbacks(mPingPongRunnable);
        mHandler.postDelayed(mPingPongRunnable, Constant.DEFAULT_PING_PONG);
    }

    private Runnable mConnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (null == mSocket || mSocket.isClosed()) {
                LogUtils.print("reconnect");

                close(false);
                new InitSocket().start();
            }
        }
    };

    private Runnable mRetryRunnable = new Runnable() {
        @Override
        public void run() {
            LogUtils.print("retry send msg:" + mCacheQueue.size());

            Iterator<Entry<String, MessageData>> iterator = mCacheQueue.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, MessageData> entry = iterator.next();
                MessageData msg = entry.getValue();
                if (msg.canRetry()) {
                    mSendQueue.add(msg);
                    msg.retry();
                } else {
                    msg.callBack(false, "No response error: " + msg.cmd());
                }
                iterator.remove();
            }

            mHandler.postDelayed(mRetryRunnable, Constant.DEFAULT_RETRY_INTERVAL);
        }
    };

    private Runnable mPingPongRunnable = new Runnable() {
        @Override
        public void run() {
            send(mPingPong);
            LogUtils.print("send PingPong");

            resetPingPong();
        }
    };

    private SocketIO(String domain, int port,
                     IHeaderResolver headerResolver,
                     IHeaderAssemble headerAssemble,
                     SparseArray<CmdResolver> resolver,
                     List<MessageData> autoSendMsg,
                     MessageData pingPong) {
        mDomain = domain;
        mPort = port;
        mHeaderResolver = headerResolver;
        mHeaderAssemble = headerAssemble;
        mResolver = resolver;
        mAutoSendMsg = autoSendMsg;
        mPingPong = pingPong;

        // 发送连上需要先发的消息
        mSendQueue.addAll(mAutoSendMsg);
        new InitSocket().start();
    }

    private class InitSocket extends Thread {
        @Override
        public void run() {
            LogUtils.print("connect start：" + System.currentTimeMillis());
            try {
                mSocket = new Socket(mDomain, mPort);

                // 接收线程
                mReceiverWorker = new ReceiverWorker(mHeaderResolver, mSocket, mSocket.getInputStream());
                mReceiverWorker.start();

                // 发送线程
                mSenderWorker = new SenderWorker(mSendQueue, mHeaderAssemble, mSocket, mSocket.getOutputStream());
                mSenderWorker.start();

                // 失败重试
                mHandler.postDelayed(mRetryRunnable, Constant.DEFAULT_RETRY_INTERVAL);

                // 发送链接建立后需要先发的消息
                if (isAutoSendMsg) {
                    mSendQueue.addAll(mAutoSendMsg);
                }
                isAutoSendMsg = true;

                // 心跳
                if (null != mPingPong) {
                    mHandler.postDelayed(mPingPongRunnable, Constant.DEFAULT_PING_PONG);
                }
            } catch (IOException e) {
                e.printStackTrace();

                reconnect();
            }
            LogUtils.print("connect   end：" + System.currentTimeMillis());
        }
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
                    msg.callBack(false, "");
                }
                mSendQueue.clear();
            }

            if (mCacheQueue.size() > 0) {
                for (Entry<String, MessageData> entry : mCacheQueue.entrySet()) {
                    entry.getValue().callBack(false, "Exit reconnect");
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

                    LogUtils.print("Send msg:" + msg.cmd() + " " + new String(msg.getData()));
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    LogUtils.print("Interrupted when end msg");
                    return; // 被中断就返回
                }

                try {
                    byte[] data = mHeaderAssemble.assemble(msg);

                    mOut.write(data);
                    mOut.flush();

                    if (msg.needFeedback()) {
                        mCacheQueue.put(msg.getKey(), msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    LogUtils.print("OutputStream closed reconnect");

                    mSendQueue.add(msg);

                    try {
                        mSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
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
                    if (size == -1 || size != mHeader.headerLength()) {
                        throw new IOException("Header Read error!");
                    }

                    mHeader.resolver(mHeaderBuffer);

                    bodyLength = mHeader.bodyLength();

                    size = mIn.read(mBuffer, 0, bodyLength);
                    if (size == -1 || size < bodyLength) {
                        throw new IOException("Body read error!");
                    }

                    LogUtils.print("Read msg package:" + mHeader.getCmd());
                } catch (IOException e) {
                    e.printStackTrace();

                    LogUtils.print("InputStream closed error:" + e.getMessage());

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
                String bodyString = new String(originData, Constant.DEFAULT_CHARSET);

                LogUtils.print("Receive: " + mHeader.getCmd() + " : " + bodyString);

                // 需要回调的消息
                String msgKey = mHeader.getMsgKey();
                MessageData msg = mCacheQueue.remove(msgKey);
                if (null != msg) {
                    msg.callBack(true, bodyString);
                }

                CmdResolver resolver = mResolver.get(mHeader.getCmd());
                if (null != resolver) { // 默认处理的协议
                    resolver.callBack(bodyString, SocketIO.this);
                } else {
                    // TODO: 2017/9/20 send broadcast
                }

                if (isQuit()) {
                    return;
                }
            }
        }
    }

    public static final class Builder {
        private SparseArray<CmdResolver> mResolver = new SparseArray<>();// 同构命令字
        private List<MessageData> mDefaultMsg = new LinkedList<>(); // 重连后默认发送
        private MessageData mPingPong;

        private String mDomain;
        private int mPort;

        private IHeaderResolver mHeaderResolver;
        private IHeaderAssemble mHeaderAssemble;

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

        public Builder addPingPongMsg(MessageData pingPong) {
            mPingPong = pingPong;
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

        public SocketIO build() {
            if (null == mDomain) {
                throw new IllegalStateException("IP is required.");
            }

            if (mPort <= 0) {
                throw new IllegalStateException("Port is required.");
            }

            if (null == mHeaderResolver) {
                throw new IllegalStateException("IHeaderResolver is required.");
            }

            if (null == mHeaderAssemble) {
                throw new IllegalStateException("IHeaderAssemble is required.");
            }

            return new SocketIO(mDomain, mPort,
                    mHeaderResolver, mHeaderAssemble,
                    mResolver,
                    mDefaultMsg,
                    mPingPong);
        }
    }
}
