package com.liking.socket.utils;

/**
 * Created by ttdevs
 * 2017-09-27 (Socket)
 * https://github.com/ttdevs
 */
public class BaseThread extends Thread {

    private boolean mQuit;

    public final void quit() {
        mQuit = true;
        interrupt();
    }

    public final boolean isQuit() {
        return mQuit;
    }
}
