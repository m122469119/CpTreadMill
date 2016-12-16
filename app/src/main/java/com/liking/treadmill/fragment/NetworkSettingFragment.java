package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.message.SettingNextMessage;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_network_setting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setNetWorkState();
    }

    private void setNetWorkState() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mNetworkImageView.setBackgroundResource(R.drawable.icon_connect_success);
            mNetworkConnectState.setText("网络已连接");
        } else {
            mNetworkImageView.setBackgroundResource(R.drawable.icon_connect_fail);
            mNetworkConnectState.setText("网络未连接，请检查网络");
        }
    }

    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode.equals(LikingTreadKeyEvent.KEY_NEXT)) {
            postEvent(new SettingNextMessage(1));
        }
    }


}
