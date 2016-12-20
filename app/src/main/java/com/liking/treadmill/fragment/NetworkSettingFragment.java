package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.message.SettingNextMessage;
import com.liking.treadmill.message.WifiMessage;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/12/12.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class NetworkSettingFragment extends SerialPortFragment {
    @BindView(R.id.network_ImageView)
    ImageView mNetworkImageView;
    @BindView(R.id.network_connect_state)
    TextView mNetworkConnectState;
    @BindView(R.id.layout_next)
    LinearLayout mLayoutNext;
    private boolean isNetwork;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_network_setting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public boolean isInViewPager() {
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d(TAG, "------setUserVisibleHint():" + isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
        setNetWorkState();
    }

    private void setNetWorkState() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            setHaveWifiView();
        } else {
            setNoWifiView();
        }
    }

    private void setHaveWifiView() {
        mNetworkImageView.setBackgroundResource(R.drawable.icon_connect_success);
        mNetworkConnectState.setText("网络已连接");
        mLayoutNext.setVisibility(View.VISIBLE);
        isNetwork = true;
    }

    private void setNoWifiView() {
        mNetworkImageView.setBackgroundResource(R.drawable.icon_connect_fail);
        mNetworkConnectState.setText("网络未连接，请检查网络");
        mLayoutNext.setVisibility(View.GONE);
        isNetwork = false;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_NEXT) {//下一步
            if (isNetwork) {
                postEvent(new SettingNextMessage(1));
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SET) {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
//            startActivity(intent);
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
}
