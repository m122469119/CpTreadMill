package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.ButterKnife;

/**
 * Created on 16/12/15.
 *
 */

public class AwaitActionFragment extends SerialPortFragment {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_awaitaction, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_SET) {
            ((HomeActivity) getActivity()).launchFragment(SettingFragment.instantiate(getActivity(), SettingFragment.class.getName()));
        } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
//            getSupportFragmentManager().popBackStack();
            PopupUtils.showToast("Return");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
        initViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "------onPause()");

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "------onStop()");
    }

    private void initViews() {

    }

}
