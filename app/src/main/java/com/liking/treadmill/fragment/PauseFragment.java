package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liking.treadmill.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:暂停界面
 * Author : shaozucheng
 * Time: 上午11:39
 * version 1.0.0
 */

public class PauseFragment extends SerialPortFragment {
    @BindView(R.id.count_down_TextView)
    TextView mCountDownTextView;
    @BindView(R.id.go_on_prompt_TextView)
    TextView mGoOnPromptTextView;
    @BindView(R.id.stop_prompt_TextView)
    TextView mStopPromptTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pause, container, false);
        ButterKnife.bind(this, view);
        initViewData();
        return view;
    }

    private void initViewData() {
        Spanned text = Html.fromHtml("点击 " + "<font color=#25ff8c><b>START</b></font>" + " 进入下一步");
        mGoOnPromptTextView.setText(text);
        Spanned visitorText = Html.fromHtml("点击 " + "<font color=#25ff8c><b>STOP</b></font>" + " 进入下一步");
        mStopPromptTextView.setText(visitorText);
    }



}
