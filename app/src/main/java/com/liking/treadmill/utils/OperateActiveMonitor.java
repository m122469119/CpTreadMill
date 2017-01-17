package com.liking.treadmill.utils;

import android.os.Handler;
import android.os.Message;

/**
 * 说明: 无操作倒计时
 * Author: chenlei
 * Time: 下午4:44
 */

public class OperateActiveMonitor {

    private static int MONITOR_WHAT = 11;

    private OperateMonitorListener mOperateMonitorListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MONITOR_WHAT && mOperateMonitorListener != null) {
                mOperateMonitorListener.onNoneOperate();
            }
        }
    };

    public void startMonitor(long second) {
        mHandler.removeMessages(MONITOR_WHAT);
        mHandler.sendEmptyMessageDelayed(MONITOR_WHAT, second * 1000);
    }

    public void stopMonitor() {
        mHandler.removeMessages(MONITOR_WHAT);
    }

    public interface OperateMonitorListener {
        void onNoneOperate();
    }

    public OperateMonitorListener getOperateMonitorListener() {
        return mOperateMonitorListener;
    }

    public void setOperateMonitorListener(OperateMonitorListener operateMonitorListener) {
        mOperateMonitorListener = operateMonitorListener;
    }
}
