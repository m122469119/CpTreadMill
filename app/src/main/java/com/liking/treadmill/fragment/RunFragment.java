package com.liking.treadmill.fragment;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.RunActivity;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/12/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class RunFragment extends SerialPortFragment {
    @BindView(R.id.left_ad_imageView)
    HImageView mLeftAdImageView;
    @BindView(R.id.dashboard_imageView)
    ImageView mDashboardImageView;
    @BindView(R.id.right_ad_imageView)
    HImageView mRightAdImageView;
    @BindView(R.id.layout_run)
    RelativeLayout mLayoutRun;

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
    @BindView(R.id.layout_pause)
    LinearLayout mPauseLayout;

    @BindView(R.id.layout_finish)
    RelativeLayout mFinishLayout;
    @BindView(R.id.run_time_TextView)
    TextView mRunTimeTextView;


    private View mRootView;
    private TextView mGradeInfoTextView;
    private TextView mSpeedInfoTextView;
    private TextView mHotInfoTextView;
    private TextView mHeartRateInfoTextView;

    private Typeface mTypeFace;
    private PauseCountdownTime mPauseCountdownTime;//60s 倒计时类

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_run, container, false);
        ButterKnife.bind(this, mRootView);
        mTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Impact.ttf");
        mPauseCountdownTime = new PauseCountdownTime(60000, 1000);
        initPauseView();
        return mRootView;
    }

    /**
     * 设置暂停界面
     */
    private void initPauseView() {
        mGoOnLeftPromptTextView.setText("点击 ");
        mGoOnPromptTextView.setText("QUICK" + "\n" + "START");
        mGoOnRightPromptTextView.setText(" 进入下一步");
        Spanned visitorText = Html.fromHtml("点击 " + "<font color=#25ff8c><b>STOP</b></font>" + " 进入下一步");
        mStopPromptTextView.setText(visitorText);
    }


    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode.equals(LikingTreadKeyEvent.KEY_SET)) {//参数设置
            ((RunActivity) getActivity()).launchFragment(SettingFragment.instantiate(getActivity(), SettingFragment.class.getName()));
        } else if (keyCode.equals(LikingTreadKeyEvent.KEY_RETURN)) {//返回
//            getSupportFragmentManager().popBackStack();
            PopupUtils.showToast("Return");
        } else if (keyCode.equals(LikingTreadKeyEvent.KEY_PAUSE)) {//暂停
            mPauseLayout.setVisibility(View.VISIBLE);
            mLayoutRun.setVisibility(View.GONE);
            startPauseCountTime();
        } else if (keyCode.equals(LikingTreadKeyEvent.KEY_START)) {//继续
            //继续回到上一次跑步界面
            mPauseLayout.setVisibility(View.GONE);
            mLayoutRun.setVisibility(View.VISIBLE);
            destroyPauseCountTime();
        } else if (keyCode.equals(LikingTreadKeyEvent.KEY_STOP)) {//结束
            //运动结束跳转到完成界面
            destroyPauseCountTime();
        }
    }

    /**
     * 开启倒计时
     */
    private void startPauseCountTime() {
        if (mPauseCountdownTime != null) {
            mPauseCountdownTime.start();
        }
    }

    /**
     * 销毁倒计时
     */
    private void destroyPauseCountTime() {
        if (mPauseCountdownTime != null) {
            mPauseCountdownTime.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
        initViews();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyPauseCountTime();
    }

    private void initViews() {
        initAdViews();
        initDashboardImageView();
        initRunInfoViews();
        initRunFinishViews();
    }

    private void initRunInfoViews() {
        View gradeCell = mRootView.findViewById(R.id.cell_grade);
        View speedCell = mRootView.findViewById(R.id.cell_speed);
        View hotCell = mRootView.findViewById(R.id.cell_hot);
        View heartRateCell = mRootView.findViewById(R.id.cell_heart_rate);
        setupRunInfoCell(gradeCell, "坡度");
        setupRunInfoCell(speedCell, "速度(KM/H)");
        setupRunInfoCell(hotCell, "消耗热量(KCAL)");
        setupRunInfoCell(heartRateCell, "心率(BPM)");
        mGradeInfoTextView.setText("5.5");
        mSpeedInfoTextView.setText("6");
        mHotInfoTextView.setText("3464");
        mHeartRateInfoTextView.setText("100");
    }

    private void setupRunInfoCell(View cellView, String title) {
        TextView titleTextView = (TextView) cellView.findViewById(R.id.info_title_textView);
        titleTextView.setText(title);
        TextView contentTextView = (TextView) cellView.findViewById(R.id.info_content_textView);
        contentTextView.setTypeface(mTypeFace);
        switch (cellView.getId()) {
            case R.id.cell_grade:
                mGradeInfoTextView = contentTextView;
                break;
            case R.id.cell_speed:
                mSpeedInfoTextView = contentTextView;
                break;
            case R.id.cell_hot:
                mHotInfoTextView = contentTextView;
                break;
            case R.id.cell_heart_rate:
                mHeartRateInfoTextView = contentTextView;
                break;
            default:
                break;
        }
    }


    private void initRunFinishViews() {
        View distanceView = mRootView.findViewById(R.id.layout_distance);
        View useTimeView = mRootView.findViewById(R.id.layout_user_time);
        View averageGradientView = mRootView.findViewById(R.id.layout_average_gradient);
        View avergageSpeedView = mRootView.findViewById(R.id.layout_average_speed);
        View consumeKcalView = mRootView.findViewById(R.id.layout_consume_kcal);
        View avergHraetRateView = mRootView.findViewById(R.id.layout_average_heart_rate);

        setupRunFinishData(distanceView, "距离(KM)", "5.5", 24f, 32f);
        setupRunFinishData(useTimeView, "用时", "02:33:33", 24f, 32f);
        setupRunFinishData(averageGradientView, "平均坡度", "5.5", 20f, 24f);
        setupRunFinishData(avergageSpeedView, "平均速度(KM/H)", "5.5", 20f, 24f);
        setupRunFinishData(consumeKcalView, "消耗热烈(KCAL)", "5000", 20f, 24f);
        setupRunFinishData(avergHraetRateView, "平均心率(BPM)", "100", 20f, 24f);
        mRunTimeTextView.setText("2016-12-19 14:45");
    }

    private void setupRunFinishData(View view, String title, String content, float titleSize, float contentSize) {
        TextView titleTextView = (TextView) view.findViewById(R.id.info_title_textView);
        TextView contentTextView = (TextView) view.findViewById(R.id.info_content_textView);
        titleTextView.setText(title);
        contentTextView.setText(content);
        contentTextView.setTypeface(mTypeFace);
        titleTextView.setTextSize(titleSize);
        contentTextView.setTextSize(contentSize);
    }

    private void initDashboardImageView() {
        AnimationDrawable animationDrawable = (AnimationDrawable) mDashboardImageView.getBackground();
        animationDrawable.start();
    }


    private void initAdViews() {
        HImageLoaderSingleton.getInstance().loadImage(mLeftAdImageView, R.drawable.image_ad_run_left);
        HImageLoaderSingleton.getInstance().loadImage(mRightAdImageView, R.drawable.image_ad_run_right);
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
