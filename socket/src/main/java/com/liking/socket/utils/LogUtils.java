package com.liking.socket.utils;

import android.os.Process;

/**
 * Created by ttdevs
 * 2017-09-10 (SocketDemo)
 * https://github.com/ttdevs
 */
public class LogUtils {
    public static final boolean isShowLog = true;

    public static void print(String msg) {
        if(isShowLog){
            System.out.println(Process.myPid() + ">>>>>" + msg);
        }
    }
}
