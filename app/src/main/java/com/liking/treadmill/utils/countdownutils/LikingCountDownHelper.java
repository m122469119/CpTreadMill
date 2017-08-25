package com.liking.treadmill.utils.countdownutils;


/**
 * Created by chenlei on 16/11/27.
 */

public class LikingCountDownHelper {

    private long mMillisInFuture;
    private long mCountdownInterval;

    private LikingCountDownTimer mCountDownTimer = null;
    private CountDownListener mCountDownListener = null;

    public boolean isRunning;

    /**
     *  创建定时器
     */
    private void countDownCreate() {
        if (mCountDownTimer != null) return;
        mCountDownTimer = new LikingCountDownTimer(mMillisInFuture, mCountdownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                mMillisInFuture = millisUntilFinished; // 重新设置剩余时间
                onLikingTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                mMillisInFuture = 0;
                onLikingFinish();
            }
        };
    }

    public void start() {
        if (mCountDownTimer == null) {
            countDownCreate();
        }
        mCountDownTimer.start();
        isRunning = true;
    }

    public void cancel() {
        if (mCountDownTimer == null) return;
        mCountDownTimer.cancel();
        isRunning = false;
        mCountDownListener = null;
        mCountDownTimer = null;
    }

//    public void continueCountDown() {
//        if (mCountDownTimer == null) return;
//        reset(mMillisInFuture, mCountdownInterval);
//    }

    /**
     * 重新设置时间、间隔
     * @param millisInFuture
     * @param countDownInterval
     */
    public void reset(long millisInFuture, long countDownInterval) {
        cancel();
        setMillisInFuture(millisInFuture);
        setCountdownInterval(countDownInterval);
    }

    public void onLikingTick(long millisUntilFinished) {
        if (mCountDownListener != null) {
            mCountDownListener.onTick(millisUntilFinished);
        }
    }

    public void onLikingFinish() {
        if (mCountDownTimer !=null) {
            mCountDownTimer = null;
        }
        if (mCountDownListener != null) {
            mCountDownListener.onFinish();
        }
    }

    public void setCountDownListener(CountDownListener mCountDownListener) {
        this.mCountDownListener = mCountDownListener;
    }

    public void setMillisInFuture(long mMillisInFuture) {
        this.mMillisInFuture = mMillisInFuture;
    }

    public void setCountdownInterval(long mCountdownInterval) {
        this.mCountdownInterval = mCountdownInterval;
    }

}
