package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;

import butterknife.ButterKnife;

/**
 * 说明: 目标设定
 * Author: chenlei
 * Time: 下午2:57
 */

public class GoalSettingFragment extends SerialPortFragment {

    public static final int GOAL_SETTING_MODE_RUNTIME = 0 ;//运动时间
    public static final int GOAL_SETTING_MODE_KILOMETRE = 1 ;//公里
    public static final int GOAL_SETTING_MODE_KCAL = 2 ;//消耗卡路里

    private boolean isModeSelect = true;//选择模式
    private boolean isModeSetting = false;//模式设置

    private int mCurrMode = 0 ;//当前mode

    private FrameLayout mRootView;

    /** step1 view*/
    private View mViewSetting;
    private TextView mTvRunTimeMode;
    private TextView mTvKilometreMode;
    private TextView mTvKcalMode;

    /** mode setting*/
    private View mViewModeSetting;
    private TextView modeSettingIcon;
    private TextView modeSettingType;
    private TextView modeSettingValue;
    private TextView modeSettingUnit;
    private TextView modeSettingHint1;
    private TextView modeSettingHint2;

    private float totalTime = 0;
    private float totalKilometre = 0;
    private float totalKcal = 0;

    private int mode_runtime_grade_increment = 10 ;//设置跑步时间 坡度以10分钟上下调整
    private int mode_runtime_speed_increment = 1 ;//设置跑步时间 速度以1分钟上下调整

    private float mode_kilometre_grade_increment = 1 ;//设置跑步时间 坡度以10分钟上下调整
    private float mode_kilometre_speed_increment = 0.1f ;//设置跑步时间 速度以1分钟上下调整

    private int mode_kcal_grade_increment = 100 ;//设置跑步时间 坡度以10分钟上下调整
    private int mode_kcal_speed_increment = 10 ;//设置跑步时间 速度以1分钟上下调整

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (FrameLayout) inflater.inflate(R.layout.fragment_goalsetting, container, false);
        ButterKnife.bind(this, mRootView);
        initViews();
        return mRootView;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_NEXT) {//模式选择:下一步
            modeSelect();
        } else if(keyCode == LikingTreadKeyEvent.KEY_RETURN) { //返回
            if(isModeSetting) {
                isModeSelect = true;
                isModeSetting = false;
                showSettingView();
            }
        } else if(keyCode == LikingTreadKeyEvent.KEY_MODE_MODE) { //双击mode

        } else if (keyCode == LikingTreadKeyEvent.KEY_MODE) {
            if(isModeSetting) return;
            //双击MODE 处理
            IToast.show("双击");
            isModeSelect = false;
            isModeSetting = true;
            showModeView();
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_PLUS) {//速度+

        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_REDUCE) {//速度-

        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS) {//坡度+

        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE) {//坡度-

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "------onPause()");

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "------onStop()");
    }

    private void initViews() {
        showSettingView();
    }

    private void showView(View view) {
        if(mRootView != null && view != null) {
            mRootView.removeAllViews();
            mRootView.addView(view);
        }
    }


    public void modeSelect() {
        if(!isModeSelect) return;
        switch (mCurrMode) {
            case GOAL_SETTING_MODE_RUNTIME:
                mTvRunTimeMode.setBackgroundResource(R.drawable.shape_step_frame_none);
                mTvKilometreMode.setBackgroundResource(R.drawable.shape_step_frame_select);
                mTvKcalMode.setBackgroundResource(R.drawable.shape_step_frame_none);
                mCurrMode = GOAL_SETTING_MODE_KILOMETRE;
                IToast.show("公里");
                break;
            case GOAL_SETTING_MODE_KILOMETRE:
                mTvRunTimeMode.setBackgroundResource(R.drawable.shape_step_frame_none);
                mTvKilometreMode.setBackgroundResource(R.drawable.shape_step_frame_none);
                mTvKcalMode.setBackgroundResource(R.drawable.shape_step_frame_select);
                mCurrMode = GOAL_SETTING_MODE_KCAL;
                IToast.show("消耗卡路里");
                break;
            case GOAL_SETTING_MODE_KCAL:
                mTvRunTimeMode.setBackgroundResource(R.drawable.shape_step_frame_select);
                mTvKilometreMode.setBackgroundResource(R.drawable.shape_step_frame_none);
                mTvKcalMode.setBackgroundResource(R.drawable.shape_step_frame_none);
                mCurrMode = GOAL_SETTING_MODE_RUNTIME;
                IToast.show("运动时间");
                break;
        }
    }
    /**
     * 设定目标step1
     */
    private void showSettingView () {
        if(mViewSetting == null) {
            mViewSetting = LayoutInflater.from(getActivity()).inflate(R.layout.layout_goalsetting_step1, null);
            mTvRunTimeMode = (TextView) mViewSetting.findViewById(R.id.step1_mode_runtime);
            mTvKilometreMode = (TextView) mViewSetting.findViewById(R.id.step1_mode_kilometre);
            mTvKcalMode = (TextView) mViewSetting.findViewById(R.id.step1_mode_kcal);
        }
        showView(mViewSetting);
    }

    /**
     * 模式设置界面
     */
    private void showModeSettingView () {
        if(mViewModeSetting == null) {
            mViewModeSetting = LayoutInflater.from(getActivity()).inflate(R.layout.layout_goalsetting_mode, null);
            modeSettingIcon = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_icon);
            modeSettingType = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_type);
            modeSettingValue = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_value);
            modeSettingUnit = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_unit);
            modeSettingHint1 = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_hint1);
            modeSettingHint2 = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_hint2);

        }
        showView(mViewModeSetting);
    }

    /**
     * 显示模式设置
     */
    private void showModeView() {
        showModeSettingView();
        switch (mCurrMode) {
            case GOAL_SETTING_MODE_RUNTIME:
                modeSettingType.setText(ResourceUtils.getString(R.string.goalsetting_step_mode_time));
                modeSettingUnit.setText("min");
                break;
            case GOAL_SETTING_MODE_KILOMETRE:
                modeSettingType.setText(ResourceUtils.getString(R.string.goalsetting_step_mode_kilometre));
                modeSettingUnit.setText("Km");
                break;
            case GOAL_SETTING_MODE_KCAL:
                modeSettingType.setText(ResourceUtils.getString(R.string.goalsetting_step_mode_kcl));
                modeSettingUnit.setText("Kcal");
                break;
        }
    }

    public void setNumerical(int keyCode) {
        float value = 0;
        switch (mCurrMode) {
            case GOAL_SETTING_MODE_RUNTIME:
                switch (keyCode) {
                    case LikingTreadKeyEvent.KEY_SPEED_PLUS:
                        value = mode_runtime_speed_increment;
                    break;
                }
                totalTime +=value;
                modeSettingValue.setText(String.valueOf(totalTime));
                break;
            case GOAL_SETTING_MODE_KILOMETRE:
                totalKilometre += value;
                modeSettingValue.setText(String.valueOf(totalKilometre));
                break;
            case GOAL_SETTING_MODE_KCAL:
                totalKcal += value;
                modeSettingValue.setText(String.valueOf(totalKcal));
                break;
        }
    }

}
