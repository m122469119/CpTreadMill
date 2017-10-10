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
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class LKMessageBackReceiver extends BroadcastReceiver {

    private HandlerThread messageHandlerThread = null;
    private Handler messageHandler = null;

    public LKMessageBackReceiver() {
        if (messageHandlerThread == null) {
            messageHandlerThread = new HandlerThread("MessageHandlerThread");
        }
        messageHandlerThread.start();

        messageHandler = new Handler(messageHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg != null) {
                    MessageHandlerHelper.INSTANCE.handlerReceive((byte) msg.what, (String) msg.obj);
                }
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (action.equals(Constant.ACTION_MSG)) {
                byte cmd = bundle.getByte(Constant.KEY_MSG_CMD);
                boolean isError = bundle.getBoolean(Constant.KEY_MSG_IS_ERROR);
                String body = bundle.getString(Constant.KEY_MSG_DATA);
                LogUtils.e("LKMessageBackReceiver", "cmd:" + cmd + "; isError:" + isError
                        + "; body:" + body);
                messageHandler.obtainMessage(cmd, isError ? 0 : 1, 0, body);
            }
        }
    }
}
