package com.liking.treadmill.fragment;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import androidex.serialport.SerialPortUtil;

/**
 * Created on 16/12/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public abstract class SerialPortFragment extends BaseFragment implements SerialPortUtil.SerialPortCallback {
    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("SerialPortFragment", "------onResume()");
        SerialPortUtil.getInstance().setSerialPortCallback(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d("SerialPortFragment", "------setUserVisibleHint():" + isVisibleToUser);
        if (isVisibleToUser) {
            SerialPortUtil.getInstance().setSerialPortCallback(this);
        } else {
            SerialPortUtil.getInstance().setSerialPortCallback(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("SerialPortFragment", "------onPause()");
        SerialPortUtil.getInstance().setSerialPortCallback(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("SerialPortFragment", "------onStop()");
        SerialPortUtil.getInstance().setSerialPortCallback(null);
    }

    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {

    }
}
