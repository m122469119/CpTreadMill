package com.liking.treadmill.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
                    MessageHandlerHelper.INSTANCE.getMessageHandler()
                            .obtainMessage(MessageHandlerHelper.INSTANCE.getHANDLE_WHAT_ACTION_MSG(), data)
                            .sendToTarget();
                    break;
                case Constant.ACTION_CONNECT:
                    LogUtils.e("LKMessageBackReceiver", "CONNECT SUCCESS!!!");
                    MessageHandlerHelper.INSTANCE.getMessageHandler()
                            .obtainMessage(MessageHandlerHelper.INSTANCE.getHANDLE_WHAT_ACTION_CONNECT())
                            .sendToTarget();
                    break;
            }
        }
    }
}
