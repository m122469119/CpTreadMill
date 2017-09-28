package com.liking.socket.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ttdevs
 * 2017-09-20 (Socket)
 * https://github.com/ttdevs
 */
public class BaseReceiver extends BroadcastReceiver {
    public static final String ACTION_MSG = "com.likingfit.socket.model.message";
    public static final String KEY_DATA = "key_msg_data";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(!ACTION_MSG.equals(action)){
            return ;
        }

        resolverMessage(context, intent);
    }

    private void resolverMessage(Context context, Intent intent) {
        String data = intent.getStringExtra(KEY_DATA);

    }
}
