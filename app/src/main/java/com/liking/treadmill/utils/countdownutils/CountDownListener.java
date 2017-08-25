package com.liking.anthropometer.utils.countdownutils;

public interface CountDownListener {

    void onTick(long millisUntilFinished);

    void onFinish();
}