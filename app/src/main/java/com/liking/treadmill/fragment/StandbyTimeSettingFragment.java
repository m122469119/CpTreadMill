package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.liking.treadmill.widget.IToast;
import com.liking.treadmill.widget.TimeWheelView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:  待机时间设置
 * Author: chenlei
 * Time: 下午4:11
 */

public class StandbyTimeSettingFragment extends SerialPortFragment {

    @BindView(R.id.setting_standbytime_time)
    TimeWheelView mTimeWheelView;
    @BindView(R.id.setting_standbytime_hint1_TextView)
    TextView mHint1TextView;
    @BindView(R.id.setting_standbytime_hint2_TextView)
    TextView mHint2TextView;

    private static final String[] PLANETS = new String[]{"01", "02", "03"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_setting_standbytime, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        mTimeWheelView.setOffset(1);
        mTimeWheelView.setItems(Arrays.asList(PLANETS));
        mTimeWheelView.setOnWheelViewListener(new TimeWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                IToast.show(item);
            }
        });

        SpannableStringBuilder ssbh1 = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_standby_time_operate1_txt));
        ImageSpan isGradeUp = new ImageSpan(getActivity(), R.drawable.key_grade_up);
        ImageSpan isGradeDown = new ImageSpan(getActivity(), R.drawable.key_grade_down);
        ssbh1.setSpan(isGradeUp, 5, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ssbh1.setSpan(isGradeDown, 8, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mHint1TextView.setText(ssbh1);

        SpannableStringBuilder ssbh2 = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_standby_time_operate2_txt));
        ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_back);
        ssbh2.setSpan(imageSpanBack, 8, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mHint2TextView.setText(ssbh2);
//        mTimeWheelView.setSeletion();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            ((HomeActivity) getActivity()).setTitle("");
            ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
    }

}
