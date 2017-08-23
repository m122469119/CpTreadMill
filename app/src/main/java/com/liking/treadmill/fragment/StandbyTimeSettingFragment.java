package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import com.liking.treadmill.fragment.base.SerialPortFragment;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.picker.CarouselPicker;
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
    CarouselPicker mTimePickerView;
    @BindView(R.id.setting_standbytime_hint1_TextView)
    TextView mHint1TextView;
    @BindView(R.id.setting_standbytime_hint2_TextView)
    TextView mHint2TextView;

    CarouselPicker.CarouselViewAdapter mTextAdapter;
    List<CarouselPicker.PickerItem> mTextItems = new ArrayList<>();

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
        mTextItems.add(new CarouselPicker.TextItem("1", 20));
        mTextItems.add(new CarouselPicker.TextItem("2", 20));
        mTextItems.add(new CarouselPicker.TextItem("3", 20));
        mTextAdapter = new CarouselPicker.CarouselViewAdapter(getActivity(), mTextItems, 0);
        mTimePickerView.setAdapter(mTextAdapter);
    }

    private void initView() {
        int standbyTime = Preference.getStandbyTime();
        if (standbyTime == 60) {
            mTimePickerView.setCurrentItem(0);
        } else if (standbyTime == 120) {
            mTimePickerView.setCurrentItem(1);
        } else if (standbyTime == 180) {
            mTimePickerView.setCurrentItem(2);
        }
        mTimePickerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int time = Integer.parseInt(mTextItems.get(position).getText());
                Preference.setStandbyTime(time * 60);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        ssbh2.setSpan(imageSpanBack, 9, 11, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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
            ((HomeActivity) getActivity()).launchFullFragment(new SettingFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS) {
            int index = mTimePickerView.getCurrentItem();
            index++;
//            if(index < PLANETS.size())index ++;
            mTimePickerView.setCurrentItem(index, true);
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE) {
            int index = mTimePickerView.getCurrentItem();
            index--;
//            if(index < 0 )index = 0;
            mTimePickerView.setCurrentItem(index, true);
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
    }

}
