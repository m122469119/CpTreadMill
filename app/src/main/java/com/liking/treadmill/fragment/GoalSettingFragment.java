package com.liking.treadmill.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.SpannedString;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;

import butterknife.ButterKnife;

import static com.liking.treadmill.R.id.step1_mode_text;

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
    private LinearLayout mRunTimeModeLayout;
    private LinearLayout mKilometreModeLayout;
    private LinearLayout mKcalModeLayout;
    private TextView mStep1Hint;

    /** mode setting*/
    private View mViewModeSetting;
    private TextView modeSettingIcon;
    private TextView modeSettingType;
    private TextView modeSettingValue;
    private TextView modeSettingUnit;
    private TextView modeSettingHint1;
    private TextView modeSettingHint2;
    private TextView modeSettingHint3;

    private float totalTime = 0;
    private float totalKilometre = 0;
    private float totalKcal = 0;
    private String totalTarget = "";

    private int mode_runtime_grade_increment = 10 ;//设置跑步时间 坡度以10分钟上下调整
    private int mode_runtime_speed_increment = 1 ;//设置跑步时间 速度以1分钟上下调整

    private int mode_kilometre_grade_increment = 1 ;//设置跑步时间 坡度以10分钟上下调整
    private float mode_kilometre_speed_increment = 0.1f;//设置跑步时间 速度以1分钟上下调整

    private int mode_kcal_grade_increment = 100 ;//设置跑步时间 坡度以10分钟上下调整
    private int mode_kcal_speed_increment = 10 ;//设置跑步时间 速度以1分钟上下调整

    private Handler mHandler = new Handler();

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
            if(isModeSelect) {
                modeSelect();
            }
        } else if(keyCode == LikingTreadKeyEvent.KEY_RETURN) { //返回
            if(isModeSetting) {
                isModeSelect = true;
                isModeSetting = false;
                showSettingView();
                restSetting();
            } else {
                ((HomeActivity) getActivity()).launchFragment(new RunFragment());
            }
        } else if(keyCode == LikingTreadKeyEvent.KEY_MODE_MODE) { //双击mode

        } else if (keyCode == LikingTreadKeyEvent.KEY_MODE) {
            if(isModeSelect) {
                //双击MODE 处理
                isModeSelect = false;
                isModeSetting = true;
                showModeView();
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_PLUS  //速度+
                || keyCode == LikingTreadKeyEvent.KEY_SPEED_REDUCE //速度-
                || keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS  //坡度+
                || keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE //坡度-
                ) {
            if(isModeSetting) {
                setNumerical(keyCode);
            }
        } else if(keyCode == LikingTreadKeyEvent.KEY_START) {
            if(isModeSetting) {
                if(totalTime > 0) {
                    goToRun(ThreadMillConstant.GOALSETTING_RUNTIME, totalTime);
                } else if(totalKilometre > 0) {
                    goToRun(ThreadMillConstant.GOALSETTING_KILOMETRE, totalKilometre);
                } else if(totalKcal > 0) {
                    goToRun(ThreadMillConstant.GOALSETTING_KCAL, totalKcal);
                }
                restSetting();
            }
        }
    }

    public void goToRun(String key, float value) {
        RunFragment run = new RunFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat(key, value);
        run.setArguments(bundle);
        ((HomeActivity)getActivity()).launchFragment(run);
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
        mHandler.postDelayed(mRunnable,1500);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(isModeSelect) {
                modeSelect();
            }
            mHandler.postDelayed(mRunnable,1500);
        }
    };

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
                mRunTimeModeLayout.setBackgroundResource(R.drawable.shape_step_frame_none);
                mKilometreModeLayout.setBackgroundResource(R.drawable.shape_step_frame_select);
                mKcalModeLayout.setBackgroundResource(R.drawable.shape_step_frame_none);
                mCurrMode = GOAL_SETTING_MODE_KILOMETRE;
                break;
            case GOAL_SETTING_MODE_KILOMETRE:
                mRunTimeModeLayout.setBackgroundResource(R.drawable.shape_step_frame_none);
                mKilometreModeLayout.setBackgroundResource(R.drawable.shape_step_frame_none);
                mKcalModeLayout.setBackgroundResource(R.drawable.shape_step_frame_select);
                mCurrMode = GOAL_SETTING_MODE_KCAL;
                break;
            case GOAL_SETTING_MODE_KCAL:
                mRunTimeModeLayout.setBackgroundResource(R.drawable.shape_step_frame_select);
                mKilometreModeLayout.setBackgroundResource(R.drawable.shape_step_frame_none);
                mKcalModeLayout.setBackgroundResource(R.drawable.shape_step_frame_none);
                mCurrMode = GOAL_SETTING_MODE_RUNTIME;
                break;
        }
    }
    /**
     * 设定目标step1
     */
    private void showSettingView () {
        if(mViewSetting == null) {
            mViewSetting = LayoutInflater.from(getActivity()).inflate(R.layout.layout_goalsetting_step1, null);
            mStep1Hint = (TextView) mViewSetting.findViewById(R.id.goalsetting_step1_hint);
            View viewModeTiem =  mViewSetting.findViewById(R.id.step1_mode_runtime);
            mRunTimeModeLayout = (LinearLayout) viewModeTiem.findViewById(R.id.goal_setting_layout);
            setModeView(viewModeTiem, R.drawable.icon_goal_mode_time,
                    ResourceUtils.getString(R.string.goalsetting_step_run_time));

            View viewModeKilometre =  mViewSetting.findViewById(R.id.step1_mode_kilometre);
            mKilometreModeLayout = (LinearLayout) viewModeKilometre.findViewById(R.id.goal_setting_layout);
            setModeView(viewModeKilometre, R.drawable.icon_goal_mode_kilometre,
                    ResourceUtils.getString(R.string.goalsetting_step_kilometre));

            View viewModeKcal =  mViewSetting.findViewById(R.id.step1_mode_kcal);
            mKcalModeLayout = (LinearLayout) viewModeKcal.findViewById(R.id.goal_setting_layout);
            setModeView(viewModeKcal, R.drawable.icon_goal_mode_kcal,
                    ResourceUtils.getString(R.string.goalsetting_step_kcal));

        }
        showView(mViewSetting);
    }

    public void setModeView(View view,int iconId, String modename) {
        ImageView modeImg = (ImageView) view.findViewById(R.id.step1_mode_img);
        TextView modeName = (TextView) view.findViewById(step1_mode_text);
        modeImg.setImageResource(iconId);
        modeName.setText(modename);
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
            modeSettingValue.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );//下划线
            modeSettingUnit = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_unit);
            modeSettingHint1 = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_hint1);
            modeSettingHint2 = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_hint2);
            modeSettingHint3 = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_hint3);

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
//                modeSettingIcon.
                modeSettingValue.setText("0");
                modeSettingUnit.setText("min");
                break;
            case GOAL_SETTING_MODE_KILOMETRE:
                modeSettingType.setText(ResourceUtils.getString(R.string.goalsetting_step_mode_kilometre));
                modeSettingValue.setText("0.0");
                modeSettingUnit.setText("Km");
                break;
            case GOAL_SETTING_MODE_KCAL:
                modeSettingType.setText(ResourceUtils.getString(R.string.goalsetting_step_mode_kcl));
                modeSettingValue.setText("0");
                modeSettingUnit.setText("Kcal");
                break;
        }
    }

    /**
     * 设置
     * @param keyCode
     */
    public void setNumerical(int keyCode) {
        float value = 0;
        switch (mCurrMode) {
            case GOAL_SETTING_MODE_RUNTIME:
                switch (keyCode) {
                    case LikingTreadKeyEvent.KEY_SPEED_PLUS:
                        value = mode_runtime_speed_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_SPEED_REDUCE:
                        value = -mode_runtime_speed_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_GRADE_PLUS:
                        value = mode_runtime_grade_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_GRADE_REDUCE:
                        value = -mode_runtime_grade_increment;
                        break;
                }
                totalTime +=value;
                if(totalTime < 0) totalTime = 0;
                totalTarget = String.valueOf((int)totalTime);
                break;
            case GOAL_SETTING_MODE_KILOMETRE:
                switch (keyCode) {
                    case LikingTreadKeyEvent.KEY_SPEED_PLUS:
                        value = mode_kilometre_speed_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_SPEED_REDUCE:
                        value = -mode_kilometre_speed_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_GRADE_PLUS:
                        value = mode_kilometre_grade_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_GRADE_REDUCE:
                        value = -mode_kilometre_grade_increment;
                        break;
                }
                totalKilometre += value;
                if(totalKilometre < 0) totalKilometre = 0;
                totalTarget = StringUtils.getDecimalString(totalKilometre,1);
                break;
            case GOAL_SETTING_MODE_KCAL:
                switch (keyCode) {
                    case LikingTreadKeyEvent.KEY_SPEED_PLUS:
                        value = mode_kcal_speed_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_SPEED_REDUCE:
                        value = -mode_kcal_speed_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_GRADE_PLUS:
                        value = mode_kcal_grade_increment;
                        break;
                    case LikingTreadKeyEvent.KEY_GRADE_REDUCE:
                        value = -mode_kcal_grade_increment;
                        break;
                }
                totalKcal += value;
                if(totalKcal < 0) totalKcal = 0;
                totalTarget = String.valueOf((int) totalKcal);
                break;
        }
        modeSettingValue.setText(totalTarget);
    }

    private void restSetting() {
        totalTime = 0;
        totalKilometre = 0;
        totalKcal = 0;
        totalTarget = "";
    }

}
