package com.liking.treadmill.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.liking.treadmill.R;
import com.liking.treadmill.message.ToolBarTimeMessage;
import com.liking.treadmill.message.WifiMessage;
import com.liking.treadmill.service.ThreadMillService;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private TextView mTimeTextView;
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
        mTimeTextView = (TextView) customToolBarView.findViewById(R.id.time_textView);
        mTimeTextView.setText(getToolBarTime());
        setCustomToolBar(customToolBarView);
    }

    public void setTitle(String value) {
        if (mTitleView != null) {
            mTitleView.setText(value);
        }
    }

    @Override
    public void launchFragment(Fragment fragment) {
        if(mLeftAdImageView.getVisibility() == View.INVISIBLE) {
            mLeftAdImageView.setVisibility(View.VISIBLE);
        }
        if(mRightAdImageView.getVisibility() == View.INVISIBLE) {
            mRightAdImageView.setVisibility(View.VISIBLE);
        }
        launchFragment(R.id.treadmill_container_layout, fragment);
    }

    public void launchFullFragment(Fragment fragment) {
        launchFragment(fragment);
        if(mLeftAdImageView.getVisibility() == View.VISIBLE) {
            mLeftAdImageView.setVisibility(View.INVISIBLE);
        }
        if(mRightAdImageView.getVisibility() == View.VISIBLE) {
            mRightAdImageView.setVisibility(View.INVISIBLE);
        }
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

    /**
     * wifi状态
     * @param message
     */
    public void onEvent(WifiMessage message) {
        if (message != null && message.isHaveWifi()) {
            setHaveWifiView();
        } else {
            setNoWifiView();
        }
    }

    /**
     * toolbar时间刷新
     * @param message
     */
    public void onEvent(ToolBarTimeMessage message) {
        mTimeTextView.setText(getToolBarTime());
    }

    /**
     * 获取时间
     * @return
     */
    public String getToolBarTime () {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    private void setHaveWifiView() {
        mWifiImageView.setImageResource(R.drawable.main_wifi);
    }

    private void setNoWifiView() {
        mWifiImageView.setImageResource(R.drawable.fail_wifi);
    }

}
