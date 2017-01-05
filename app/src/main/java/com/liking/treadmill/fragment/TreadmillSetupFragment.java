package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.message.SettingNextMessage;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liking.treadmill.app.ThreadMillConstant.THREADMILL_SYSTEMSETTING;

/**
 * Created on 16/12/12.
 * 跑步机启动设置
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class TreadmillSetupFragment extends SerialPortFragment {

    @BindView(R.id.treadmill_setup_step_txt)
    LinearLayout mLayoutTreadmillSetupStep;
    @BindView(R.id.treadmill_rfid_TextView)
    TextView mTreadmillRfidTextView;
    @BindView(R.id.treadmill_setup_visitor_ImageView)
    ImageView mTreadmillSetupVisitorImageView;
    @BindView(R.id.treadmill_setup_visitor_TextView)
    TextView mTreadmillSetupVisitorTextView;
    @BindView(R.id.treadmill_setup_hint1)
    TextView mTreadmillSetupHint1;
    @BindView(R.id.treadmill_setup_hint2)
    TextView mTreadmillSetupHint2;

    private boolean isSelectModel = false;
    private boolean isSetting ; //是否从系统设置进入

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treadmill_setup, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d(TAG, "------setUserVisibleHint():" + isVisibleToUser);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if(bundle !=null ) {
            isSetting = bundle.getBoolean(THREADMILL_SYSTEMSETTING, false);
        }
        Spanned text = Html.fromHtml("在<font color=#25ff8c>RFID</font>区域刷卡并识别有效访问后，才能启动跑步机");
        mTreadmillRfidTextView.setText(text);
        Spanned visitorText = Html.fromHtml("允许所有用户通过" + "<font color=#25ff8c>PROG</font>" + "+" + "<font color=#25ff8c>QUICK START</font>" + "的组合按键启动锻炼，锻炼5分钟后将自动停止");
        mTreadmillSetupVisitorTextView.setText(visitorText);

        if(Preference.isVisitorMode()) {
            isSelectModel = true;
            mTreadmillSetupVisitorImageView.setBackgroundResource(R.drawable.icon_visitor_checked);
        }

    }

    private void initView() {

        SpannableStringBuilder ssbh1 = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_setup_operate1_txt));
        ImageSpan imageSpanNext = new ImageSpan(getActivity(), R.drawable.key_mode);
        ssbh1.setSpan(imageSpanNext, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mTreadmillSetupHint1.setText(ssbh1);

        SpannableStringBuilder ssbh2 = null;
        if(isSetting) {
            ((HomeActivity)getActivity()).setTitle("启动方式设置");
            mLayoutTreadmillSetupStep.setVisibility(View.INVISIBLE);

            ssbh2 = new SpannableStringBuilder(ResourceUtils.getString(R.string.goalsetting_step_setting_hint2));
            ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_back);
            ssbh2.setSpan(imageSpanBack, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
            mLayoutTreadmillSetupStep.setVisibility(View.VISIBLE);

            ssbh2 = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_network_setting_operate_txt));
            ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_next);
            ssbh2.setSpan(imageSpanBack, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        if(ssbh2 != null) {
            mTreadmillSetupHint2.setText(ssbh2);
        }

    }

    @Override
    public boolean isInViewPager() {
        return !isSetting;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if(keyCode == LikingTreadKeyEvent.KEY_LAST) {
            if(isSetting) return;
            postEvent(new SettingNextMessage(0));
        } else if (keyCode == LikingTreadKeyEvent.KEY_NEXT) {//下一步
            if(isSetting) return;
            postEvent(new SettingNextMessage(2));
        } else if (keyCode == LikingTreadKeyEvent.KEY_MODE) {//选中访客模式
            if (!isSelectModel) {
                Preference.setIsVisitorMode(true);
                mTreadmillSetupVisitorImageView.setBackgroundResource(R.drawable.icon_visitor_checked);
            } else {
                Preference.setIsVisitorMode(false);
                mTreadmillSetupVisitorImageView.setBackgroundResource(R.drawable.icon_visitor_no_checked);
            }
            isSelectModel = !isSelectModel;
        } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            if(isSetting) {
                ((HomeActivity) getActivity()).setTitle("");
                ((HomeActivity) getActivity()).launchFragment(new SettingFragment());
            }
        }
    }
}
