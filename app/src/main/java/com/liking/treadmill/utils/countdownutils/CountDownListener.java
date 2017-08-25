package com.liking.treadmill.utils.countdownutils;

public interface CountDownListener {

    void onTick(long millisUntilFinished);

    void onFinish();
}