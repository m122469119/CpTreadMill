package com.liking.treadmill.fragment;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

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
        LogUtils.d("SerialPortFragment", "------onResume()");
        SerialPorManager.getInstance().setSerialPortCallback(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d("SerialPortFragment", "------setUserVisibleHint():" + isVisibleToUser);
        if (isVisibleToUser) {
            SerialPorManager.getInstance().setSerialPortCallback(this);
        } else {
            SerialPorManager.getInstance().setSerialPortCallback(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("SerialPortFragment", "------onPause()");
        SerialPorManager.getInstance().setSerialPortCallback(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("SerialPortFragment", "------onStop()");
        SerialPorManager.getInstance().setSerialPortCallback(null);
    }

    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {

    }

    @Override
    public void fanState(String fanState) {
        
    }
}
