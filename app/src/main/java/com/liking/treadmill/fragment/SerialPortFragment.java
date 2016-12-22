package com.liking.treadmill.fragment;

import android.view.View;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.liking.treadmill.message.FanStateMessage;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;

import androidex.serialport.SerialPorManager;

/**
 * Created on 16/12/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public abstract class SerialPortFragment extends BaseFragment implements SerialPorManager.SerialPortCallback {
    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
        if (!isInViewPager()) {
            SerialPorManager.getInstance().setSerialPortCallback(this);
        }
    }

    public boolean isInViewPager() {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d(TAG, "------setUserVisibleHint():" + isVisibleToUser);
        if (!isInViewPager()) {
            return;
        }
        if (isVisibleToUser) {
            SerialPorManager.getInstance().setSerialPortCallback(this);
        } else {
            SerialPorManager.getInstance().setSerialPortCallback(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "------onPause()");
        if (!isInViewPager()) {
            SerialPorManager.getInstance().setSerialPortCallback(null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "------onStop()");
        if (!isInViewPager()) {
            SerialPorManager.getInstance().setSerialPortCallback(null);
        }
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        if (keyCode == LikingTreadKeyEvent.KEY_FAN) { //风扇控制
            switch (SerialPortUtil.getTreadInstance().getFanState()) {
                case SerialPortUtil.FanState.FAN_STATE_STOP:
                    SerialPortUtil.setFanState(SerialPortUtil.FanState.FAN_STATE_LOW_SPEED);
                    break;
                case SerialPortUtil.FanState.FAN_STATE_LOW_SPEED:
                    SerialPortUtil.setFanState(SerialPortUtil.FanState.FAN_STATE_HIGH_SPEED);
                    break;
                case SerialPortUtil.FanState.FAN_STATE_HIGH_SPEED:
                    SerialPortUtil.setFanState(SerialPortUtil.FanState.FAN_STATE_STOP);
                    break;
            }
        }
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
//            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        //风扇显示刷新
        switch (treadData.getFanState()) {
            case SerialPortUtil.FanState.FAN_STATE_STOP:
                postEvent(new FanStateMessage(View.GONE));
                break;
            case SerialPortUtil.FanState.FAN_STATE_LOW_SPEED:
            case SerialPortUtil.FanState.FAN_STATE_HIGH_SPEED:
                postEvent(new FanStateMessage(View.VISIBLE));
                break;
        }
    }
}
