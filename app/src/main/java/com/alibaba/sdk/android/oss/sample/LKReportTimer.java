package com.alibaba.sdk.android.oss.sample;

import com.aaron.android.codelibrary.utils.LogUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created on 2017/5/3
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class LKReportTimer {

    private static final String TAG = "LKReportTimer";

    //时间间隔(一天)
    private static final long PERIOD_DAY =  24 * 60 * 60 * 1000;
    private Timer mTimer;
    private Calendar mCalendar;
    private Date mDate;
    private TimerTask mTask;
    private boolean isStart = false;

    public CallBack mCallback;

    /**
     *
     * @param H 时
     * @param m 分
     * @param s 秒
     * @param callBack 回调
     */
    public LKReportTimer(int H, int m, int s, CallBack callBack) {
        this.mCallback = callBack;
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, H);
        mCalendar.set(Calendar.MINUTE, m);
        mCalendar.set(Calendar.SECOND, s);
        //第一次执行定时任务的时间
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        mDate = mCalendar.getTime();
        mTask = new TimerTask() {
            @Override
            public void run() {
                LogUtils.e(TAG, "上报一次 ----------------------");
                mCallback.doLoopTh();
            }
        };
        mTimer = new Timer();
    }


    public void start(){
        if (isStart) {
            return;
        }
        mTimer.schedule(mTask, new Date(mDate.getTime() + PERIOD_DAY), PERIOD_DAY);
        isStart = true;
    }

    public void putOnce(){
        mCallback.doOnceTh();
    }

    public void stop(){
        mTimer.cancel();
        isStart = false;
    }

    public interface CallBack{
        void doLoopTh();
        void doOnceTh();
    }

}
