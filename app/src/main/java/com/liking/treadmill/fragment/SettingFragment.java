package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/12/27.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class SettingFragment extends SerialPortFragment {
    @BindView(R.id.layout_start_mode)
    View mStartModeView;
    @BindView(R.id.layout_await_time)
    View mAwaitTimeView;
    @BindView(R.id.layout_sport_params)
    View mSportParamsView;
    @BindView(R.id.layout_user_setting)
    View mUserSettingView;
    @BindView(R.id.layout_language_setting)
    View mLanguageSettingView;
    @BindView(R.id.layout_gym_binding)
    View mGymBindingView;
    @BindView(R.id.layout_network_connection)
    View mNetworkConnectionView;
    @BindView(R.id.layout_update)
    View mUpdateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_setting, container, false);
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    private void initViews() {
        initSettingCard(mStartModeView, R.string.setting_start_mode, R.drawable.setting_start);
        initSettingCard(mAwaitTimeView, R.string.setting_await_time, R.drawable.setting_await);
        initSettingCard(mSportParamsView, R.string.setting_sport_params, R.drawable.setting_sport);
        initSettingCard(mUserSettingView, R.string.setting_user_setting, R.drawable.setting_user_param);
        initSettingCard(mLanguageSettingView, R.string.setting_language, R.drawable.setting_language);
        initSettingCard(mGymBindingView, R.string.setting_gym_binding, R.drawable.setting_gym_bind);
        initSettingCard(mNetworkConnectionView, R.string.setting_network_connection, R.drawable.setting_network);
        initSettingCard(mUpdateView, R.string.setting_update, R.drawable.setting_update);
    }

    private void initSettingCard(View settingCardView, @StringRes int settingTitle, @DrawableRes int drawableId) {
        TextView titleView = (TextView) settingCardView.findViewById(R.id.setting_cart_title);
        View iconView = settingCardView.findViewById(R.id.setting_card_icon);
        titleView.setText(settingTitle);
        iconView.setBackgroundResource(drawableId);
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
    }
}
