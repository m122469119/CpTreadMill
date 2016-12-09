package com.liking.treadmill.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aaron.android.codelibrary.utils.LogUtils;

public class MessageBackReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SocketService.HEART_BEAT_ACTION)) {
                LogUtils.d(SocketService.TAG, "receive a heart beat");
            } else if (action.equals(SocketService.MESSAGE_ACTION)) {
                String jsonMessage = intent.getStringExtra("message");
                LogUtils.d(SocketService.TAG, "receive a socket message : " + jsonMessage);
                SocketHelper.handlerSocketReceive(context, jsonMessage);
            }
        }
    }