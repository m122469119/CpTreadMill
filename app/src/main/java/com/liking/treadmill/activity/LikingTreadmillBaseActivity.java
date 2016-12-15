package com.liking.treadmill.activity;

import android.os.Bundle;
import android.view.View;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.liking.treadmill.R;

/**
 * Created on 16/12/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingTreadmillBaseActivity extends AppBarActivity {
    private View mWifiImageView;
    private View mFanImageView;
    private View mCooldownImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        initToolBarViews();
    }

    private void initToolBarViews() {
        hideHomeUpIcon();
        View customToolBarView = getLayoutInflater().inflate(R.layout.view_main_toolbar, null, false);
        mWifiImageView = customToolBarView.findViewById(R.id.wifi_imageView);
        mFanImageView = customToolBarView.findViewById(R.id.fan_imageView);
        mCooldownImageView = customToolBarView.findViewById(R.id.cooldown_imageView);
        setCustomToolBar(customToolBarView);
    }
}
