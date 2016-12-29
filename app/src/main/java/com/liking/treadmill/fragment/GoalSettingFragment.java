package com.liking.treadmill.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.ImageSpan;
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

    private int mCurrMode = GOAL_SETTING_MODE_KCAL ;//当前mode

    private FrameLayout mRootView;

    /** step1 view*/
    private View mViewSetting;
    private LinearLayout mRunTimeModeLayout;
    private TextView runTimeLine;
    private LinearLayout mKilometreModeLayout;
    private TextView kilometreLine;
    private LinearLayout mKcalModeLayout;
    private TextView kcalLine;
    private TextView mStep1Hint;
    private TextView mStep1Hint2;

    /** mode setting*/
    private View mViewModeSetting;
    private ImageView modeSettingIcon;
    private TextView modeSettingType;
    private TextView modeSettingValue;
    private TextView modeSettingUnit;
    private TextView modeSettingHint1;
    private TextView modeSettingHint1After;
    private TextView modeSettingHint2;
    private TextView modeSettingHint3;

    private float totalTime = 30;
    private float totalKilometre = 5.0f;
    private float totalKcal = 80;
    private String totalTarget = "";

    private int mode_runtime_grade_increment = 10 ;//设置跑步时间 坡度以10分钟上下调整
    private int mode_runtime_speed_increment = 1 ;//设置跑步时间 速度以1分钟上下调整

    private String mode_runtime_grade_increment_str = "10分钟";
    private String mode_runtime_speed_increment_str = "1分钟" ;

    private int mode_kilometre_grade_increment = 1 ;
    private float mode_kilometre_speed_increment = 0.1f;

    private String mode_kilometre_grade_increment_str = "1公里" ;
    private String mode_kilometre_speed_increment_str = "0.1公里";

    private int mode_kcal_grade_increment = 100 ;
    private int mode_kcal_speed_increment = 10 ;

    private String mode_kcal_grade_increment_str = "100卡路里" ;
    private String mode_kcal_speed_increment_str = "10卡路里" ;

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
                ((HomeActivity) getActivity()).launchFragment(new StartFragment());
            }
        } else if(keyCode == LikingTreadKeyEvent.KEY_MODE_MODE) { //双击mode
            if(isModeSelect) {
                //双击MODE 处理
                isModeSelect = false;
                isModeSetting = true;
                showModeView();
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_MODE) {

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
        modeSelect();
    }

//    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if(isModeSelect) {
//                modeSelect();
//            }
//            mHandler.postDelayed(mRunnable,1500);
//        }
//    };

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
                mRunTimeModeLayout.setBackgroundResource(R.drawable.setting_car_normal);
                mKilometreModeLayout.setBackgroundResource(R.drawable.setting_card_selected);
                mKcalModeLayout.setBackgroundResource(R.drawable.setting_car_normal);

                runTimeLine.setBackgroundColor(ResourceUtils.getColor(R.color.c3a4d65));
                kilometreLine.setBackgroundColor(ResourceUtils.getColor(R.color.c21554C));
                kcalLine.setBackgroundColor(ResourceUtils.getColor(R.color.c3a4d65));
                mCurrMode = GOAL_SETTING_MODE_KILOMETRE;
                break;
            case GOAL_SETTING_MODE_KILOMETRE:
                mRunTimeModeLayout.setBackgroundResource(R.drawable.setting_car_normal);
                mKilometreModeLayout.setBackgroundResource(R.drawable.setting_car_normal);
                mKcalModeLayout.setBackgroundResource(R.drawable.setting_card_selected);

                runTimeLine.setBackgroundColor(ResourceUtils.getColor(R.color.c3a4d65));
                kilometreLine.setBackgroundColor(ResourceUtils.getColor(R.color.c3a4d65));
                kcalLine.setBackgroundColor(ResourceUtils.getColor(R.color.c21554C));
                mCurrMode = GOAL_SETTING_MODE_KCAL;
                break;
            case GOAL_SETTING_MODE_KCAL:
                mRunTimeModeLayout.setBackgroundResource(R.drawable.setting_card_selected);
                mKilometreModeLayout.setBackgroundResource(R.drawable.setting_car_normal);
                mKcalModeLayout.setBackgroundResource(R.drawable.setting_car_normal);

                runTimeLine.setBackgroundColor(ResourceUtils.getColor(R.color.c21554C));
                kilometreLine.setBackgroundColor(ResourceUtils.getColor(R.color.c3a4d65));
                kcalLine.setBackgroundColor(ResourceUtils.getColor(R.color.c3a4d65));
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
            mStep1Hint2 = (TextView) mViewSetting.findViewById(R.id.goalsetting_step1_hint2);
            View viewModeTiem =  mViewSetting.findViewById(R.id.step1_mode_runtime);
            mRunTimeModeLayout = (LinearLayout) viewModeTiem.findViewById(R.id.goal_setting_layout);
            runTimeLine = (TextView) viewModeTiem.findViewById(R.id.goal_setting_line);
            setModeView(viewModeTiem, R.drawable.icon_goal_mode_time,
                    ResourceUtils.getString(R.string.goalsetting_step_run_time));

            View viewModeKilometre =  mViewSetting.findViewById(R.id.step1_mode_kilometre);
            mKilometreModeLayout = (LinearLayout) viewModeKilometre.findViewById(R.id.goal_setting_layout);
            kilometreLine = (TextView) viewModeKilometre.findViewById(R.id.goal_setting_line);
            setModeView(viewModeKilometre, R.drawable.icon_goal_mode_kilometre,
                    ResourceUtils.getString(R.string.goalsetting_step_kilometre));

            View viewModeKcal =  mViewSetting.findViewById(R.id.step1_mode_kcal);
            mKcalModeLayout = (LinearLayout) viewModeKcal.findViewById(R.id.goal_setting_layout);
            kcalLine = (TextView) viewModeKcal.findViewById(R.id.goal_setting_line);
            setModeView(viewModeKcal, R.drawable.icon_goal_mode_kcal,
                    ResourceUtils.getString(R.string.goalsetting_step_kcal));

            SpannableStringBuilder ssbh1 = new SpannableStringBuilder(ResourceUtils.getString(R.string.goalsetting_step_setting_hint1));
            ImageSpan imageSpanNext = new ImageSpan(getActivity(), R.drawable.key_next);
            ImageSpan imageSpanMode = new ImageSpan(getActivity(), R.drawable.key_mode);
            ssbh1.setSpan(imageSpanNext, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ssbh1.setSpan(imageSpanMode, 24, 26, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mStep1Hint.setText(ssbh1);

            SpannableStringBuilder ssbh2 = new SpannableStringBuilder(ResourceUtils.getString(R.string.goalsetting_step_setting_hint2));
            ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_back);
            ssbh2.setSpan(imageSpanBack, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mStep1Hint2.setText(ssbh2);
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
            modeSettingIcon = (ImageView) mViewModeSetting.findViewById(R.id.mode_setting_icon);
            modeSettingType = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_type);
            modeSettingValue = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_value);
            modeSettingUnit = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_unit);
            modeSettingHint1 = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_hint1);
            modeSettingHint1After = (TextView) mViewModeSetting.findViewById(R.id.mode_setting_hint1_after);
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
        String hint1 = "";
        String hint2 = "";
        switch (mCurrMode) {
            case GOAL_SETTING_MODE_RUNTIME:
                modeSettingType.setText(ResourceUtils.getString(R.string.goalsetting_step_mode_time));
                modeSettingIcon.setImageResource(R.drawable.goal_mode_time_img);
                totalTarget = String.valueOf((int)totalTime);
                modeSettingUnit.setText("min");
                hint1 = mode_runtime_grade_increment_str;
                hint2 = mode_runtime_speed_increment_str;
                break;
            case GOAL_SETTING_MODE_KILOMETRE:
                modeSettingType.setText(ResourceUtils.getString(R.string.goalsetting_step_mode_kilometre));
                modeSettingIcon.setImageResource(R.drawable.goal_mode_kilometre_img);
                totalTarget = StringUtils.getDecimalString(totalKilometre,1);
                modeSettingUnit.setText("Km");
                hint1 = mode_kilometre_grade_increment_str;
                hint2 = mode_kilometre_speed_increment_str;
                break;
            case GOAL_SETTING_MODE_KCAL:
                modeSettingType.setText(ResourceUtils.getString(R.string.goalsetting_step_mode_kcl));
                modeSettingIcon.setImageResource(R.drawable.goal_mode_kcal_img);
                totalTarget = String.valueOf((int) totalKcal);
                modeSettingUnit.setText("Kcal");
                hint1 = mode_kcal_grade_increment_str;
                hint2 = mode_kcal_speed_increment_str;
                break;
        }
        modeSettingValue.setText(totalTarget);

        SpannableStringBuilder ssbh1 = new SpannableStringBuilder(ResourceUtils.getString(R.string.goalsetting_step_mode_hint1_part1));
        ImageSpan isGradeUp = new ImageSpan(getActivity(), R.drawable.key_grade_up);
        ImageSpan isGradeDown = new ImageSpan(getActivity(), R.drawable.key_grade_down);
        ssbh1.setSpan(isGradeUp, 7, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssbh1.setSpan(isGradeDown, 10, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssbh1.append(hint1);
        modeSettingHint1.setText(ssbh1);

        SpannableStringBuilder ssbh1After = new SpannableStringBuilder(ResourceUtils.getString(R.string.goalsetting_step_mode_hint1_part2));
        ImageSpan isSpeedAdd = new ImageSpan(getActivity(), R.drawable.key_speed_add);
        ImageSpan isSpeedCut = new ImageSpan(getActivity(), R.drawable.key_speed_cut);
        ssbh1After.setSpan(isSpeedAdd, 10, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssbh1After.setSpan(isSpeedCut, 13, 15, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssbh1After.append(hint2);
        ssbh1After.append(ResourceUtils.getString(R.string.goalsetting_step_mode_hint1_part3));
        modeSettingHint1After.setText(ssbh1After);

        SpannableStringBuilder ssbh2 = new SpannableStringBuilder(ResourceUtils.getString(R.string.goalsetting_step_mode_hint2));
        ImageSpan isQuickStart = new ImageSpan(getActivity(), R.drawable.key_quick_start);
        ssbh2.setSpan(isQuickStart, 8, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        modeSettingHint2.setText(ssbh2);

        SpannableStringBuilder ssbh3 = new SpannableStringBuilder(ResourceUtils.getString(R.string.goalsetting_step_mode_hint3));
        ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_back);
        ssbh3.setSpan(imageSpanBack, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        modeSettingHint3.setText(ssbh3);
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
        totalTime = 30;
        totalKilometre = 5.0f;
        totalKcal = 80;
        totalTarget = "";
    }

}
