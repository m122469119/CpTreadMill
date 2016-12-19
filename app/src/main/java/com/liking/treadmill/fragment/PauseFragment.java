package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liking.treadmill.R;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

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
    @BindView(R.id.go_on_right_prompt_TextView)
    TextView mGoOnRightPromptTextView;
    @BindView(R.id.go_on_left_prompt_TextView)
    TextView mGoOnLeftPromptTextView;

    private PauseCountdownTime mPauseCountdownTime;//60s 倒计时类

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_pause, container, false);
        ButterKnife.bind(this, view);
        initViewData();
        mPauseCountdownTime = new PauseCountdownTime(60000, 1000);
        startPauseCountTime();
        return view;
    }

    private void initViewData() {
        mGoOnLeftPromptTextView.setText("点击 ");
        mGoOnPromptTextView.setText("QUICK" + "\n" + "START");
        mGoOnRightPromptTextView.setText(" 进入下一步");
        Spanned visitorText = Html.fromHtml("点击 " + "<font color=#25ff8c><b>STOP</b></font>" + " 进入下一步");
        mStopPromptTextView.setText(visitorText);
    }


    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode.equals(LikingTreadKeyEvent.KEY_START)) {//继续
            //继续回到上一次跑步界面

        } else if (keyCode.equals(LikingTreadKeyEvent.KEY_STOP)) {//结束
            //运动结束跳转到完成界面
        }
    }

    private void startPauseCountTime() {
        mPauseCountdownTime.start();
    }

    private void destoryPauseCountTime() {
        if (mPauseCountdownTime != null) {
            mPauseCountdownTime.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destoryPauseCountTime();
    }

    /**
     * 60s倒计时
     */
    class PauseCountdownTime extends CountDownTimer {

        public PauseCountdownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCountDownTextView.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            //倒计时结束，运动结束跳转到完成界面
        }
    }


}
