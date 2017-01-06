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
import android.widget.TextView;

import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.timewheelview.OnWheelChangedListener;
import com.liking.treadmill.widget.timewheelview.WheelItemAdapter;
import com.liking.treadmill.widget.timewheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:  运动参数设置---跑步最长时间
 * Author: chenlei
 * Time: 下午4:11
 */

public class MotionParamSettingFragment extends SerialPortFragment {


    @BindView(R.id.motion_param_icon_ImageView)
    ImageView mMotionParamIcon;
    @BindView(R.id.motion_param_value_TextView)
    TextView mMotionParamValue;
    @BindView(R.id.motion_param_unit_TextView)
    TextView mMotionParamUnit;
    @BindView(R.id.motion_param_hint1_TextView)
    TextView mHint1TextView;
    @BindView(R.id.motion_param_hint2_TextView)
    TextView mHint2TextView;
    private int maxRuntime ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_setting_motionparam, container, false);
        ButterKnife.bind(this, rootView);
        initData();
        initView();
        return rootView;
    }


    private void initData() {
        maxRuntime = Preference.getMotionParamMaxRunTime();
    }

    private void initView() {
        mMotionParamIcon.setImageResource(R.drawable.icon_motion_param_time);
        mMotionParamValue.setText(String.valueOf(maxRuntime));
        mMotionParamUnit.setText("min");

        SpannableStringBuilder ssbh1 = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_motion_param_maxtime_hint_txt));
        ImageSpan isGradeUp = new ImageSpan(getActivity(), R.drawable.key_grade_up);
        ImageSpan isGradeDown = new ImageSpan(getActivity(), R.drawable.key_grade_down);
        ssbh1.setSpan(isGradeUp, 5, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssbh1.setSpan(isGradeDown, 8, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mHint1TextView.setText(ssbh1);

        SpannableStringBuilder ssbh2 = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_standby_time_operate2_txt));
        ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_back);
        ssbh2.setSpan(imageSpanBack, 8, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mHint2TextView.setText(ssbh2);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setTitle(ResourceUtils.getString(R.string.threadmill_motion_param_title_txt));
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            Preference.setMotionParamMaxRunTime(maxRuntime);
            ((HomeActivity) getActivity()).setTitle("");
            ((HomeActivity) getActivity()).launchFullFragment(new SettingFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS) {
            maxRuntime = maxRuntime + 10;
            mMotionParamValue.setText(String.valueOf(maxRuntime));
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE) {
            maxRuntime = maxRuntime - 10;
            if(maxRuntime < 0) {
                maxRuntime = 0;
            }
            mMotionParamValue.setText(String.valueOf(maxRuntime));
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
    }

}
