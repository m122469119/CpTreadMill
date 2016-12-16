package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.DisplayUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author chenlei
 */

public class UpdateFragment extends SerialPortFragment {

    private View mRootView;
    @BindView(R.id.update_layout)
    FrameLayout mUpdateLayout;
    @BindView(R.id.update_hint)
    TextView updateHint;
    @BindView(R.id.update_progress)
    TextView updateProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_update, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {
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
        int height = DisplayUtils.getHeightPixels();
        int mProgressHeight = height / 2;
        mUpdateLayout.getLayoutParams().width = mProgressHeight;
        mUpdateLayout.getLayoutParams().height = mProgressHeight;
        updateHint.setText("检测更新中");
        updateProgress.setText("80%");
    }

}
