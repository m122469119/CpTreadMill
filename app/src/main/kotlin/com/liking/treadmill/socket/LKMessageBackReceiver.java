package com.liking.treadmill.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created on 2017/08/14
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class LKMessageBackReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals(SocketService.MESSAGE_ACTION)) {

        }
    }
}
