package com.liking.treadmill.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.message.WifiMessage;
import com.liking.treadmill.service.NetworkStateService;

/**
 * Created on 16/12/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingTreadmillBaseActivity extends AppBarActivity {
    private ImageView mWifiImageView;
    public ImageView mFanImageView;
    public ImageView mCooldownImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        initToolBarViews();
    }

    private void initToolBarViews() {
        hideHomeUpIcon();
        View customToolBarView = getLayoutInflater().inflate(R.layout.view_main_toolbar, null, false);
        mWifiImageView = (ImageView)customToolBarView.findViewById(R.id.wifi_imageView);
        int drawStateId  = NetworkStateService.isNetworkAvailable(this)? R.drawable.main_wifi : R.drawable.fail_wifi;
        mWifiImageView.setImageResource(drawStateId);
        mFanImageView = (ImageView)customToolBarView.findViewById(R.id.fan_imageView);
        mCooldownImageView = (ImageView)customToolBarView.findViewById(R.id.cooldown_imageView);
        setCustomToolBar(customToolBarView);
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(WifiMessage message) {
        if (message != null && message.isHaveWifi()) {
            setHaveWifiView();
        } else {
            setNoWifiView();
        }
    }

    private void setHaveWifiView() {
        mWifiImageView.setImageResource(R.drawable.main_wifi);
    }

    private void setNoWifiView() {
        mWifiImageView.setImageResource(R.drawable.fail_wifi);
    }

}
