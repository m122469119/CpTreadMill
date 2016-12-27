package com.liking.treadmill.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.liking.treadmill.R;
import com.liking.treadmill.message.WifiMessage;
import com.liking.treadmill.service.ThreadMillService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/12/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingTreadmillBaseActivity extends AppBarActivity {
    @BindView(R.id.left_ad_imageView)
    HImageView mLeftAdImageView;
    @BindView(R.id.right_ad_imageView)
    HImageView mRightAdImageView;
    private ImageView mWifiImageView;
    private ImageView mFanImageView;
    private ImageView mCooldownImageView;
    private TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liking_base);
        ButterKnife.bind(this);
        setSwipeBackEnable(false);
        initToolBarViews();
    }

    private void initToolBarViews() {
        hideHomeUpIcon();
        View customToolBarView = getLayoutInflater().inflate(R.layout.view_main_toolbar, null, false);
        mWifiImageView = (ImageView) customToolBarView.findViewById(R.id.wifi_imageView);
        int drawStateId = ThreadMillService.isNetworkAvailable(this) ? R.drawable.main_wifi : R.drawable.fail_wifi;
        mWifiImageView.setImageResource(drawStateId);
        mFanImageView = (ImageView) customToolBarView.findViewById(R.id.fan_imageView);
        mCooldownImageView = (ImageView) customToolBarView.findViewById(R.id.cooldown_imageView);
        mTitleView = (TextView) customToolBarView.findViewById(R.id.title_textView);
        setCustomToolBar(customToolBarView);
    }

    public void setTitle(String value) {
        if (mTitleView != null) {
            mTitleView.setText(value);
        }
    }

    @Override
    public void launchFragment(Fragment fragment) {
        launchFragment(R.id.treadmill_container_layout, fragment);
    }


    public void launchFullFragment(Fragment fragment) {
        super.launchFragment(fragment);
    }

    public void setFanViewVisibility(int visibility) {
        if (mFanImageView != null) {
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
