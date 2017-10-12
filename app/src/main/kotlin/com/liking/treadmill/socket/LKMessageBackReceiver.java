package com.liking.treadmill.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.liking.socket.Constant;

/**
 * Created on 2017/08/14
 * desc: 接收 消息
 *
 * @author: chenlei
 * @version:1.0
 */

public class LKMessageBackReceiver extends BroadcastReceiver {

    private final int HANDLE_WHAT_ACTION_MSG = 10;

    private final int HANDLE_WHAT_ACTION_CONNECT = 11;

    private HandlerThread messageHandlerThread = null;
    private Handler messageHandler = null;

    public LKMessageBackReceiver() {
        if (messageHandlerThread == null) {
            messageHandlerThread = new HandlerThread("MessageHandlerThread");
        }
        messageHandlerThread.start();

        messageHandler = new Handler(messageHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg != null) {
                    switch (msg.what) {
                        case HANDLE_WHAT_ACTION_MSG:
                            try {
                                Bundle bundle = (Bundle) msg.obj;
                                if (bundle != null) {
                                    byte cmd = bundle.getByte(Constant.KEY_MSG_CMD);
                                    String body = bundle.getString(Constant.KEY_MSG_DATA);
                                    MessageHandlerHelper.INSTANCE.handlerReceive(cmd, body);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case HANDLE_WHAT_ACTION_CONNECT:
                            try {
                                CmdRequestManager.INSTANCE.reportedAllUserExerciseRequest();
                                CmdRequestManager.INSTANCE.reportedLogOutRequest();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            switch (intent.getAction()) {
                case Constant.ACTION_MSG:
                    Bundle bundle = intent.getExtras();
                    byte cmd = bundle.getByte(Constant.KEY_MSG_CMD);
                    String body = bundle.getString(Constant.KEY_MSG_DATA);
                    LogUtils.e("LKMessageBackReceiver", String.format("%02X %s", cmd, body));

                    Bundle data = new Bundle();
                    data.putByte(Constant.KEY_MSG_CMD, cmd);
                    data.putString(Constant.KEY_MSG_DATA, body);
                    messageHandler.obtainMessage(HANDLE_WHAT_ACTION_MSG, data).sendToTarget();
                    break;
                case Constant.ACTION_CONNECT:
                    LogUtils.e("LKMessageBackReceiver", "CONNECT SUCCESS!!!");
                    messageHandler.obtainMessage(HANDLE_WHAT_ACTION_CONNECT).sendToTarget();
                    break;
            }
        }
    }
}
