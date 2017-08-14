package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.fragment.base.SerialPortFragment;
import com.liking.treadmill.message.SettingNextMessage;
import com.liking.treadmill.message.WifiMessage;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liking.treadmill.app.ThreadMillConstant.THREADMILL_SYSTEMSETTING;

/**
 * Created on 16/12/12.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class NetworkSettingFragment extends SerialPortFragment {
    @BindView(R.id.network_setting_step_txt)
    LinearLayout mLayoutSettingStep;
    @BindView(R.id.network_ImageView)
    ImageView mNetworkImageView;
    @BindView(R.id.network_connect_state)
    TextView mNetworkConnectState;
    @BindView(R.id.network_setting_hint)
    TextView mNetworkSettingHint;
    @BindView(R.id.layout_nonnet_operation)
    LinearLayout mlayoutNonnetOperation;
    @BindView(R.id.layout_next)
    LinearLayout mLayoutNext;
    @BindView(R.id.network_setting_line)
    View mNetworkSettingLine;
    @BindView(R.id.network_setting_line2)
    View mNetworkSettingLine2;
    private boolean isNetwork;
    private boolean isSetting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_network_setting, container, false);
        ButterKnife.bind(this, rootView);
        initData();
        return rootView;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if(bundle !=null ) {
            isSetting = bundle.getBoolean(THREADMILL_SYSTEMSETTING, false);
        }
    }

    @Override
    public boolean isInViewPager() {
        return !isSetting;
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
        initView();
    }

    private void initView() {
        SpannableStringBuilder ssbh = null;
        mNetworkSettingLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mNetworkSettingLine2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if(isSetting) {
            ((HomeActivity)getActivity()).setTitle("网络连接");
            mLayoutSettingStep.setVisibility(View.INVISIBLE);
            ssbh = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_system_update_operate_txt));
            ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_back);
            ssbh.setSpan(imageSpanBack, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
            mLayoutSettingStep.setVisibility(View.VISIBLE);
            ssbh = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_network_setting_operate_txt));
            ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_next);
            ssbh.setSpan(imageSpanBack, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        mNetworkSettingHint.setText(ssbh);
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
        mlayoutNonnetOperation.setVisibility(View.GONE);
        mLayoutNext.setVisibility(View.VISIBLE);
        isNetwork = true;
    }

    private void setNoWifiView() {
        mNetworkImageView.setBackgroundResource(R.drawable.icon_connect_fail);
        mNetworkConnectState.setText("网络未连接，请检查网络");
        mlayoutNonnetOperation.setVisibility(View.VISIBLE);
        if(!isSetting) {
            mLayoutNext.setVisibility(View.GONE);
        }
        isNetwork = false;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_NEXT) {//下一步
            if(!isSetting) {
                if (isNetwork) {
                    postEvent(new SettingNextMessage(1));
                }
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            if(isSetting) {
                ((HomeActivity) getActivity()).setTitle("");
                ((HomeActivity) getActivity()).launchFullFragment(new SettingFragment());
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_MODE_MODE) {
//            if(!isSetting) {
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
//                startActivity(intent);
//            }
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
