package com.liking.treadmill.fragment;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;

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
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
//            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {

    }
}
