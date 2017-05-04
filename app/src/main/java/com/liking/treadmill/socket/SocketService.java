package com.liking.treadmill.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;

import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.test.IBackService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created on 16/11/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class SocketService extends Service {
    public static final String TAG = "LikingSocket";
    //心跳包频率
    private static final long HEART_BEAT_RATE = 2 * 60 * 1000;
    //测试环境地址：120.24.177.134   正式环境地址：112.74.27.162
    public static final String HOST = EnvironmentUtils.Config.isDebugMode() ? "120.24.177.134" : "112.74.27.162";
    public static final int PORT = 8192;

    public static final String MESSAGE_ACTION = "com.liking.threadmill.socket";
    public static final String HEART_BEAT_ACTION = "com.liking.threadmill.socket.heart";
    private static final int HEART_BEAT_MESSAGE = 0;
    private static final int UP_STREAM_MESSAGE = 1;
    private static final int RE_CONNECT_MESSAGE = 2;
    public static final long RECONNECT_DELAY = 5 * 1000;

    private ReadThread mReadThread;

    private LocalBroadcastManager mLocalBroadcastManager;

    private WeakReference<Socket> mSocket;
    private HandlerThread mSendHandlerThread;

    // For heart Beat
    private Handler mHandler;

    private StringBuilder cacheSb = new StringBuilder();
    String sTag = "{\"type\":";
    String eTag = "\\r\\n";

    private void sendHeartMessageDelayed() {
        Message message = mHandler.obtainMessage(HEART_BEAT_MESSAGE);
        mHandler.sendMessageDelayed(message, HEART_BEAT_RATE);
    }

    private void sendUpStreamMessage(String text) {
        Message message = mHandler.obtainMessage(UP_STREAM_MESSAGE);
        message.obj = text;
        mHandler.sendMessage(message);
    }

    private void sendReConnectMesasage(long delay) {
        Message message = mHandler.obtainMessage(RE_CONNECT_MESSAGE);
        mHandler.sendMessageDelayed(message, delay);
    }

    private long sendTime = 0L;
    private IBackService.Stub mIBackService = new IBackService.Stub() {

        @Override
        public void rebind() throws RemoteException {
            sendUpStreamMessage(SocketHelper.REBIND_STRING);
            sendUpStreamMessage(SocketHelper.reportDevicesString());
        }

        @Override
        public void init() throws RemoteException {
            sendUpStreamMessage(SocketHelper.initString());
        }

        @Override
        public void bind() throws RemoteException {
            sendUpStreamMessage(SocketHelper.bind());
        }

        @Override
        public void unBind() throws RemoteException {
            sendUpStreamMessage(SocketHelper.unBind());
        }

        @Override
        public void confirm() throws RemoteException {
            sendUpStreamMessage(SocketHelper.CONFIRM_STRING);
        }

        @Override
        public void reportDevices() throws RemoteException {
            LogUtils.d(SocketService.TAG, "设备信息" + SocketHelper.reportDevicesString());
            sendUpStreamMessage(SocketHelper.reportDevicesString());
        }

        @Override
        public void userLogin(String cardno) throws RemoteException {
            sendUpStreamMessage(SocketHelper.userloginString(cardno));
        }

        @Override
        public void userLogOut(String cardno) throws RemoteException {
            sendUpStreamMessage(SocketHelper.userlogoutString(cardno));
        }

        @Override
        public void reportExerciseCacheData(String data) throws RemoteException {
            LogUtils.d(SocketService.TAG, "缓存锻炼数据：" + data);
            sendUpStreamMessage(data);
        }

        public void reportExerciseData(int type, int aimType, float aim, int achieve) throws RemoteException {
            String result = SocketHelper.reportExerciseData(type, aimType, aim, achieve);
            LogUtils.d(SocketService.TAG, "锻炼数据：" + result);
            sendUpStreamMessage(result);
        }

        @Override
        public void requestMembersCommand() throws RemoteException {
            String result = SocketHelper.buildRequestMemberParam();
            LogUtils.d(SocketService.TAG, "成员分页请求：" + result);
            sendUpStreamMessage(result);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return mIBackService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "socketService  onCreate()");
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mSendHandlerThread = new HandlerThread("tcpSendThread");
        mSendHandlerThread.start();
        mHandler = new Handler(mSendHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == HEART_BEAT_MESSAGE) {
                    if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                        boolean isSuccess = sendMsg(SocketHelper.HEART_BEAT_STRING);//就发送一个HEART_BEAT_STRING过去 如果发送失败，就重新初始化一个socket
                        LogUtils.d(TAG, "send heart beat success: " + isSuccess);
                        if (!isSuccess) {
                            reConnectSocket();
                        }
                    }
                    sendHeartMessageDelayed();
                } else if (msg.what == UP_STREAM_MESSAGE) {
                    String sendMessage = (String) msg.obj;
                    sendMsg(sendMessage);
                } else if (msg.what == RE_CONNECT_MESSAGE) {
                    reConnectSocket();
                }
            }
        };
        new InitSocketThread().start();
    }

    private void reConnectSocket() {
        mHandler.removeMessages(HEART_BEAT_MESSAGE);
        if (mReadThread != null) {
            mReadThread.release();
        }
        new InitSocketThread().start();
    }

    public boolean sendMsg(String msg) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        try {
            if (!soc.isClosed() && !soc.isOutputShutdown()) {
                OutputStream os = soc.getOutputStream();
                String message = msg;
                os.write(message.getBytes());
                os.flush();
                LogUtils.d(TAG, "sendMsg: " + msg);
                sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void initSocket() {//初始化Socket
        try {
            Socket so = new Socket(HOST, PORT);
            mSocket = new WeakReference<>(so);
            mReadThread = new ReadThread(so);
            mReadThread.start();
//            if (Preference.isLogin()) {
//                    mIBackService.init();
//            }
            LogUtils.d(TAG, "initSocket");
            sendHeartMessageDelayed();//初始化成功后，就准备发送心跳包
            if (mIBackService != null) {
                mIBackService.reportDevices();
                reportedData(new File(ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE));
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "server socket is disconnect....." + e.getMessage());
            sendReConnectMesasage(RECONNECT_DELAY);
        }
    }

    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk == null) {
                    return;
                }
                if (!sk.isClosed()) {
                    sk.close();
                }
                sk = null;
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }

    // Thread to read content from Socket
    class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket(mWeakSocket);
        }

        @Override
        public void run() {
            super.run();
            Socket socket = mWeakSocket.get();
            if (null != socket) {
                try {
                    InputStream is = socket.getInputStream();
                    byte[] buffer = new byte[1024 * 8];
                    int length = 0;
                    LogUtils.d(TAG, "read socket1111111 : " + socket.hashCode());
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && ((length = is.read(buffer)) != -1)) {
                        if (length > 0) {
                            String message = new String(Arrays.copyOf(buffer,
                                    length)).trim();
//                            LogUtils.d(TAG, message);
                            LogUtils.d(TAG, "read socket : " + socket.hashCode());

                            //收到服务器过来的消息，就通过Broadcast发送出去
                            if (message.equals(SocketHelper.HEART_BEAT_PONG_STRING)) {//处理心跳回复
                                Intent intent = new Intent(HEART_BEAT_ACTION);
                                mLocalBroadcastManager.sendBroadcast(intent);
                            } else {
                                //其他消息回复
                                LogUtils.d(TAG, "其他消息回复 : " + message);
                                handleSocketResult(message);
                            }
                            buffer = null;
                            buffer = new byte[1024 * 8];
                        }
                    }
                    sendReConnectMesasage(0);
                    LogUtils.d(TAG, "read socket2 : " + socket.hashCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 提交未成功的数据
     * @param file
     */
    public void reportedData(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    String data = FileUtils.load(f.getAbsolutePath());
                    if(mIBackService != null) {
                        try {
                            mIBackService.reportExerciseCacheData(data);
                        } catch (RemoteException e) {
                        }
                    }
                    LogUtils.e("info", "reported_data : " + data);
                }
            }
        }
    }

    private void sendMessage(String message) {
        Intent intent = new Intent(MESSAGE_ACTION);
        intent.putExtra("message", message);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    private void handleSocketResult(String message) {
        if(StringUtils.isEmpty(message)) {
            return;
        }
        int eIndex = message.length() - eTag.length();
        if(checkSocketResult(message, eIndex)) {
            sendMessage(message);
            cacheSb.setLength(0);
        } else {
            cacheSb.append(message);
            message = cacheSb.toString();
            eIndex = message.length() - eTag.length();
            if(checkSocketResult(cacheSb.toString(), eIndex)) {
                cacheSb.setLength(0);
                sendMessage(message);
            }
        }
    }

    private boolean checkSocketResult(String message, int endIndex) {
        int start = message.indexOf(sTag);
        int end = message.lastIndexOf(eTag);
        return start == 0 && (end == endIndex);
    }
}
