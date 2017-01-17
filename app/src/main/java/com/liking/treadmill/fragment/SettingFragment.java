package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liking.treadmill.app.ThreadMillConstant.THREADMILL_SYSTEMSETTING;

/**
 * Created on 16/12/27.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class SettingFragment extends SerialPortFragment {
    private static final int INDEX_START = 0;
    private static final int INDEX_AWAIT_TIME = INDEX_START + 1;
    private static final int INDEX_SPORT_PARAMS = INDEX_AWAIT_TIME + 1;
//    private static final int INDEX_USER_SETTING = INDEX_SPORT_PARAMS + 1;
//    private static final int INDEX_LANGUAGE_SETTING = INDEX_USER_SETTING + 1;
    private static final int INDEX_GYM_BINDING = INDEX_SPORT_PARAMS + 1;
    private static final int INDEX_NETWORK_CONNECTION = INDEX_GYM_BINDING + 1;
    private static final int INDEX_UPDATE = INDEX_NETWORK_CONNECTION + 1;
    @BindView(R.id.layout_start_mode)
    View mStartModeView;
    @BindView(R.id.layout_await_time)
    View mAwaitTimeView;
    @BindView(R.id.layout_sport_params)
    View mSportParamsView;
//    @BindView(R.id.layout_user_setting)
//    View mUserSettingView;
//    @BindView(R.id.layout_language_setting)
//    View mLanguageSettingView;
    @BindView(R.id.layout_gym_binding)
    View mGymBindingView;
    @BindView(R.id.layout_network_connection)
    View mNetworkConnectionView;
    @BindView(R.id.layout_update)
    View mUpdateView;
    @BindView(R.id.setting_description_textView)
    TextView mSettingDescriptionTextView;

    private int mCurrentSelectSettingIndex = 0;

    private Map<Integer, View> mSettingItemMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_setting, container, false);
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setTitle("系统设置");
        startActiveMonitor();
    }

    private void initViews() {
        initSettingViews();
        initSettingCard(mSettingItemMap.get(INDEX_START), R.string.setting_start_mode, R.drawable.setting_start);
        initSettingCard(mSettingItemMap.get(INDEX_AWAIT_TIME), R.string.setting_await_time, R.drawable.setting_await);
        initSettingCard(mSettingItemMap.get(INDEX_SPORT_PARAMS), R.string.setting_sport_params, R.drawable.setting_sport);
//        initSettingCard(mSettingItemMap.get(INDEX_USER_SETTING), R.string.setting_user_setting, R.drawable.setting_user_param);
//        initSettingCard(mSettingItemMap.get(INDEX_LANGUAGE_SETTING), R.string.setting_language, R.drawable.setting_language);
        initSettingCard(mSettingItemMap.get(INDEX_GYM_BINDING), R.string.setting_gym_binding, R.drawable.setting_gym_bind);
        initSettingCard(mSettingItemMap.get(INDEX_NETWORK_CONNECTION), R.string.setting_network_connection, R.drawable.setting_network);
        initSettingCard(mSettingItemMap.get(INDEX_UPDATE), R.string.setting_update, R.drawable.setting_update);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_setting_description_txt));
        ImageSpan isSpeedLast = new ImageSpan(getActivity(), R.drawable.key_last);
        ImageSpan isSpeedAdd = new ImageSpan(getActivity(), R.drawable.key_next);
        ImageSpan isSpeedCut = new ImageSpan(getActivity(), R.drawable.key_mode);
        spannableStringBuilder.setSpan(isSpeedLast, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(isSpeedAdd, 6, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(isSpeedCut, 19, 21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mSettingDescriptionTextView.setText(spannableStringBuilder);
        setCurrentSettingItem(INDEX_START);
    }

    private void initSettingViews() {
        mSettingItemMap.put(INDEX_START, mStartModeView);
        mSettingItemMap.put(INDEX_AWAIT_TIME, mAwaitTimeView);
        mSettingItemMap.put(INDEX_SPORT_PARAMS, mSportParamsView);
//        mSettingItemMap.put(INDEX_USER_SETTING, mUserSettingView);
//        mSettingItemMap.put(INDEX_LANGUAGE_SETTING, mLanguageSettingView);
        mSettingItemMap.put(INDEX_GYM_BINDING, mGymBindingView);
        mSettingItemMap.put(INDEX_NETWORK_CONNECTION, mNetworkConnectionView);
        mSettingItemMap.put(INDEX_UPDATE, mUpdateView);
    }

    private void initSettingCard(View settingCardView, @StringRes int settingTitle, @DrawableRes int drawableId) {
        TextView titleView = (TextView) settingCardView.findViewById(R.id.setting_cart_title);
        View iconView = settingCardView.findViewById(R.id.setting_card_icon);
        titleView.setText(settingTitle);
        iconView.setBackgroundResource(drawableId);
//        if (settingCardView == mLanguageSettingView || settingCardView == mUserSettingView) {
//            titleView.setTextColor(Color.parseColor("#759b9b9b"));
//        }
    }

    private void setCurrentSettingItem(int index) {
        View lastSelectedView = mSettingItemMap.get(mCurrentSelectSettingIndex);
        lastSelectedView.setBackgroundResource(R.drawable.setting_car_normal);
        View currentView = mSettingItemMap.get(index);
        currentView.setBackgroundResource(R.drawable.setting_card_selected);
        mCurrentSelectSettingIndex = index;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            ((HomeActivity) getActivity()).setTitle("");
            ((HomeActivity) getActivity()).launchFragment(new StartFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_LAST) {
            startActiveMonitor();
            selectLast();
        } else if (keyCode == LikingTreadKeyEvent.KEY_NEXT) {
            startActiveMonitor();
            selectNext();
        } else if (keyCode == LikingTreadKeyEvent.KEY_MODE) {
            gotoSubSettingFragment();
        }
    }

    private void gotoSubSettingFragment() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(THREADMILL_SYSTEMSETTING, true);
        switch (mCurrentSelectSettingIndex) {
            case INDEX_START:
                TreadmillSetupFragment treadmillSetupFragment = new TreadmillSetupFragment();
                treadmillSetupFragment.setArguments(bundle);
                launchFragment(treadmillSetupFragment);
                break;
            case INDEX_AWAIT_TIME:
                launchFragment(new StandbyTimeSettingFragment());
                break;
            case INDEX_SPORT_PARAMS:
                launchFragment(new MotionParamSettingFragment());
                break;
            case INDEX_GYM_BINDING:
                BindGymFragment bindGymFragment = new BindGymFragment();
                bindGymFragment.setArguments(bundle);
                launchFragment(bindGymFragment);
                break;
            case INDEX_NETWORK_CONNECTION:
                NetworkSettingFragment settingFragment = new NetworkSettingFragment();
                settingFragment.setArguments(bundle);
                launchFragment(settingFragment);
                break;
            case INDEX_UPDATE:
                launchFragment(new UpdateFragment());
                break;
            default:
                break;
        }
    }

    private void selectLast() {
        int currentIndex = mCurrentSelectSettingIndex;
        currentIndex = currentIndex == INDEX_START ? INDEX_UPDATE : currentIndex - 1;
        setCurrentSettingItem(currentIndex);
    }

    private void selectNext() {
        int currentIndex = mCurrentSelectSettingIndex;
        currentIndex = currentIndex == INDEX_UPDATE ? INDEX_START : currentIndex + 1;
        setCurrentSettingItem(currentIndex);
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
    }

    public void launchFragment(Fragment fragment) {
        ((HomeActivity)getActivity()).launchFullFragment(fragment);
    }
}
