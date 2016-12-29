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
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;
import com.liking.treadmill.widget.timewheelview.OnWheelChangedListener;
import com.liking.treadmill.widget.timewheelview.WheelItemAdapter;
import com.liking.treadmill.widget.timewheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:  待机时间设置
 * Author: chenlei
 * Time: 下午4:11
 */

public class StandbyTimeSettingFragment extends SerialPortFragment {

    @BindView(R.id.setting_standbytime_time)
    WheelView mTimeWheelView;
    @BindView(R.id.setting_standbytime_hint1_TextView)
    TextView mHint1TextView;
    @BindView(R.id.setting_standbytime_hint2_TextView)
    TextView mHint2TextView;

    private List<String> PLANETS = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_setting_standbytime, container, false);
        ButterKnife.bind(this, rootView);
        initData();
        initView();
        return rootView;
    }


    private void initData() {
        PLANETS.add("01");
        PLANETS.add("02");
        PLANETS.add("03");
        PLANETS.add("04");
        PLANETS.add("05");
    }

    private void initView() {
        mTimeWheelView.setViewAdapter(new WheelItemAdapter(getActivity(), PLANETS));
        mTimeWheelView.setCyclic(true);
        mTimeWheelView.setCurrentItem(1);
        mTimeWheelView.addChangingListener(new OnWheelChangedListener(){
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int time = 1000 ;
                if("02".equals(PLANETS.get(newValue))) {
                    time = 2000;
                } else if("03".equals(PLANETS.get(newValue))) {
                    time = 3000;
                } else if("04".equals(PLANETS.get(newValue))) {
                    time = 4000;
                } else if("05".equals(PLANETS.get(newValue))) {
                    time = 5000;
                }
                Preference.setStandbyTime(time * 60);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setTitle(ResourceUtils.getString(R.string.threadmill_standby_time_title_txt));
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            ((HomeActivity) getActivity()).setTitle("");
            ((HomeActivity) getActivity()).launchFragment(new SettingFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS) {
            int index = mTimeWheelView.getCurrentItem();
            index ++ ;
//            if(index < PLANETS.size())index ++;
            mTimeWheelView.setCurrentItem(index, true);
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE) {
            int index = mTimeWheelView.getCurrentItem();
            index --;
//            if(index < 0 )index = 0;
            mTimeWheelView.setCurrentItem(index, true);
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
    }

}
