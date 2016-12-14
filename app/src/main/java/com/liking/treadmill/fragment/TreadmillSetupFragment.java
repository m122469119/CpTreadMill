package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.BaseFragment;
import com.liking.treadmill.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/12/12.
 * 跑步机启动设置
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class TreadmillSetupFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.treadmill_rfid_TextView)
    TextView mTreadmillRfidTextView;
    @BindView(R.id.treadmill_setup_visitor_ImageView)
    ImageView mTreadmillSetupVisitorImageView;
    @BindView(R.id.treadmill_setup_visitor_TextView)
    TextView mTreadmillSetupVisitorTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treadmill_setup, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        Spanned text = Html.fromHtml("在 " + "\"" + "<font color=#25ff8c><b>RFID</b></font>" + "\"" + " 区域刷卡并识别有效访问后，才能启动跑步机");
        mTreadmillRfidTextView.setText(text);
        Spanned visitorText = Html.fromHtml("允许所有用户通过" + "<font color=#25ff8c><b>PROG</b></font>" + "+" + "<font color=#25ff8c><b>QUICK START</b></font>" + "的组合按键启动锻炼，锻炼5分钟后将自动停止");
        mTreadmillSetupVisitorTextView.setText(visitorText);
    }

    @Override
    public void onClick(View v) {

    }
}
