package com.liking.socket;

import android.text.TextUtils;

import com.liking.socket.model.IHeaderAssemble;
import com.liking.socket.model.IHeaderResolver;
import com.liking.socket.model.message.MessageData;
import com.liking.socket.model.message.PingPongMsg;
import com.liking.socket.receiver.CmdResolver;
import com.liking.socket.utils.AESUtils;
import com.liking.socket.utils.BaseThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public class SocketIO {
    private static boolean mDebug = false;

    /**
     * 发送队列
     */
    private BlockingQueue<MessageData> mSendQueue = new ArrayBlockingQueue<>(Constant.SENDER_QUEUE_SIZE);
    /**
     * 缓存队列
     */
    private BlockingQueue<MessageData> mCacheQueue = new ArrayBlockingQueue<>(Constant.SENDER_QUEUE_SIZE);

    private String mDomain;
    private int mPort;

    private IHeaderAssemble mHeaderAssemble;
    private IHeaderResolver mHeaderResolver;

    private Map<Byte, CmdResolver> mResolver;
    private List<MessageData> mDefaultMsg;
    private MessageData mPingPong;

    private Socket mSocket;
    private ReceiverWorker mReceiverWorker;
    private SenderWorker mSenderWorker;

    private static final ScheduledExecutorService mExecutor = Executors.newScheduledThreadPool(2);
    private static final Timer mTimer = new Timer();

    private void reconnect() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (null == mSocket || mSocket.isClosed()) {
                    close(false);
                    new InitSocket().start();
                }
            }
        }, Constant.DEFAULT_RECONNECT);
    }

    private SocketIO(String domain, int port,
                     IHeaderResolver headerResolver,
                     IHeaderAssemble headerAssemble,
                     Map<Byte, CmdResolver> resolver,
                     List<MessageData> msg,
                     MessageData pingPong) {
        mDomain = domain;
        mPort = port;
        mHeaderResolver = headerResolver;
        mHeaderAssemble = headerAssemble;
        mResolver = resolver;
        mDefaultMsg = msg;
        mPingPong = pingPong;

        new InitSocket().start();
    }

    private void sendDefaultMsg() {
        for (MessageData msg : mDefaultMsg) {
            send(msg);
        }
    }

    class InitSocket extends Thread {
        @Override
        public void run() {
            System.out.println(">>>>start " + System.currentTimeMillis());
            try {
                mSocket = new Socket(mDomain, mPort);

                // 接收
                mReceiverWorker = new ReceiverWorker(mHeaderResolver, mSocket, mSocket.getInputStream());
                mReceiverWorker.start();

                // 发送
                mSenderWorker = new SenderWorker(mSendQueue, mHeaderAssemble, mSocket, mSocket.getOutputStream());
                mSenderWorker.start();

                // 重试
                mExecutor.scheduleAtFixedRate(new RetryTask(), 0, Constant.DEFAULT_RETRY, TimeUnit.MILLISECONDS);

                // 心跳 // TODO: 2017/9/27
                if(null != mPingPong){
                    mExecutor.scheduleAtFixedRate(new PingPongTask(), 0, Constant.DEFAULT_PING_PONG, TimeUnit.MILLISECONDS);
                }

                sendDefaultMsg();
            } catch (IOException e) {
                e.printStackTrace();

                reconnect();
            }
            System.out.println(">>>>end  " + System.currentTimeMillis());
        }
    }

    public void setDebug(boolean isDebug) {
        mDebug = isDebug;
    }

    public void close(boolean exit) {
//        mExecutor.shutdown(); // TODO: 2017/9/27
//        mTimer.cancel(); // TODO: 2017/9/27

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
                for (MessageData msg : mCacheQueue) {
                    msg.callBack(false, "");
                }
                mCacheQueue.clear();
            }
        }
    }

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

    class RetryTask extends TimerTask {

        @Override
        public void run() {
            try {
                while (mCacheQueue.size() > 0) {
                    MessageData msg = mCacheQueue.take();
                    mSendQueue.add(msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class PingPongTask extends TimerTask {

        @Override
        public void run() {
            send(mPingPong);
        }
    }

    class SenderWorker extends BaseThread {
        private Socket mSocket;
        private OutputStream mOut;

        private BlockingQueue<MessageData> mSendQueue;
        private IHeaderAssemble mHeaderAssemble;

        public SenderWorker(BlockingQueue<MessageData> sendQueue,
                            IHeaderAssemble assemble,
                            Socket socket, OutputStream out) {
            mSendQueue = sendQueue;
            mHeaderAssemble = assemble;
            mSocket = socket;
            mOut = out;
        }

        @Override
        public void run() {
            MessageData data = null;
            while (true) {
                try {
                    data = mSendQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return; // 被中断就返回
                }

                try {
                    byte[] result = mHeaderAssemble.setData(data);

                    mOut.write(result);
                    mOut.flush();

                    if (data.needFeedback()) {
                        mCacheQueue.add(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    if (null != data) {
                        mSendQueue.add(data);
                    }

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

    class ReceiverWorker extends BaseThread {
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
                } catch (IOException e) {
                    e.printStackTrace();

                    try {
                        mSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    quit();
                    reconnect();
                    return;
                }

                CmdResolver resolver = mResolver.get(mHeader.getCmd());
                if (null != resolver) {
                    byte[] data = new byte[bodyLength];
                    System.arraycopy(mBuffer, 0, data, 0, bodyLength);
                    byte[] originData = AESUtils.decode(data);
                    resolver.callBack(originData, SocketIO.this);

                    String msgID = resolver.finish();
                    if (!TextUtils.isEmpty(msgID)) {
                        for (MessageData msg : mCacheQueue) {
                            if (msgID.equals(msg.getKey())) {
                                mCacheQueue.remove(msg);
                            }
                        }
                    }
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
        private Map<Byte, CmdResolver> mResolver = new HashMap<>(); // 同构命令字
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

        public Builder addPingPongMsg(MessageData pingPong){
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
