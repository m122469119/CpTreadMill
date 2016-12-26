package com.liking.treadmill.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.liking.treadmill.R;
import com.liking.treadmill.message.WifiMessage;
import com.liking.treadmill.service.ThreadMillService;

/**
 * Created on 16/12/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingTreadmillBaseActivity extends AppBarActivity {
    private ImageView mWifiImageView;
    private ImageView mFanImageView;
    private ImageView mCooldownImageView;
    private TextView mCentreTv;

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
        int drawStateId  = ThreadMillService.isNetworkAvailable(this)? R.drawable.main_wifi : R.drawable.fail_wifi;
        mWifiImageView.setImageResource(drawStateId);
        mFanImageView = (ImageView)customToolBarView.findViewById(R.id.fan_imageView);
        mCooldownImageView = (ImageView)customToolBarView.findViewById(R.id.cooldown_imageView);
        mCentreTv = (TextView)customToolBarView.findViewById(R.id.centre_textView);
        setCustomToolBar(customToolBarView);
    }

    public void setCentreTvText(String value) {
        if(mCentreTv != null) {
            mCentreTv.setText(value);
        }
    }

    public void setFanViewVisibility(int visibility) {
        if(mFanImageView != null) {
            mFanImageView.setVisibility(visibility);
        }
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
