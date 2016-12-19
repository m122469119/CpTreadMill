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

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.RunTimeUtil;

import java.util.Date;

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

    @BindView(R.id.layout_start)
    LinearLayout mStartLayout;
    @BindView(R.id.head_image)
    HImageView mHeadHImageView;
    @BindView(R.id.user_name_TextView)
    TextView mUserNameTextView;


    private View mRootView;
    private TextView mGradeInfoTextView;
    private TextView mSpeedInfoTextView;
    private TextView mHotInfoTextView;
    private TextView mHeartRateInfoTextView;
    private int mCurrentGrade = 0;
    private int mCurrentSpeed = 0;
    private float mHotInfo = 0;
    private int mHeartRate = 0;
    private int mSpeed = 0;
    private int mGrade = 0;

    private TextView mDistanceTextView;//距离
    private TextView mUseTimeTextView; //用时
    private TextView mAverageGradientTextView; //平均坡度
    private TextView mAvergageSpeedTextView;   //平均速度
    private TextView mConsumeKcalTextView;    //消耗热量
    private TextView mAvergHraetRateTextView; //平均心率

    private Typeface mTypeFace;//字体
    private PauseCountdownTime mPauseCountdownTime;//60s 倒计时类
    private boolean isPause;//是否暂停
    private long currentDateSecond;//当前时间

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
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_SET) {//参数设置
            ((HomeActivity) getActivity()).launchFragment(SettingFragment.instantiate(getActivity(), SettingFragment.class.getName()));
        } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {//返回
//            getSupportFragmentManager().popBackStack();
            ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_START) {//开始跑步
            if (mStartLayout.getVisibility() == View.VISIBLE) {//不做任何处理
                SerialPortUtil.startTreadMill();
            } else if (mLayoutRun.getVisibility() == View.VISIBLE) {//正在跑步界面
                mPauseLayout.setVisibility(View.GONE);
                mLayoutRun.setVisibility(View.VISIBLE);
                mFinishLayout.setVisibility(View.GONE);
                mStartLayout.setVisibility(View.GONE);
                currentDateSecond = DateUtils.currentDataSeconds();
                isPause = false;
                startRunThread();//计时开始
                destroyPauseCountTime();
            } else if (mPauseLayout.getVisibility() == View.VISIBLE) {//暂停界面
                mPauseLayout.setVisibility(View.GONE);
                mLayoutRun.setVisibility(View.VISIBLE);
                mFinishLayout.setVisibility(View.GONE);
                mStartLayout.setVisibility(View.GONE);
                isPause = true;
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_PAUSE) {//暂停
            mPauseLayout.setVisibility(View.VISIBLE);
            mLayoutRun.setVisibility(View.GONE);
            mFinishLayout.setVisibility(View.GONE);
            mStartLayout.setVisibility(View.GONE);
            SerialPortUtil.stopTreadMill();//暂停命令
            isPause = true;
            startPauseCountTime();
        } else if (keyCode == LikingTreadKeyEvent.KEY_STOP) {//结束
            SerialPortUtil.stopTreadMill();
            mPauseLayout.setVisibility(View.GONE);
            mLayoutRun.setVisibility(View.GONE);
            mFinishLayout.setVisibility(View.VISIBLE);
            mStartLayout.setVisibility(View.GONE);
            isPause = true;
            //运动结束跳转到完成界面
            destroyPauseCountTime();
            statisticsRunData();
            SerialPortUtil.getTreadInstance().reset();//情况数据
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_PLUS) {//速度加
            mSpeed += 10;
            SerialPortUtil.setSpeedInRunning(mSpeed);
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_REDUCE) {//速度减
            mSpeed -= 10;
            SerialPortUtil.setSpeedInRunning(mSpeed);
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS) {//坡度+
            mGrade++;
            SerialPortUtil.setGradeInRunning(mGrade);
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE) {//坡度减
            mGrade--;
            SerialPortUtil.setGradeInRunning(mGrade);
        }
    }


    /***
     * 跑步结束统计 距离 、用时、平均坡度、平均速度、消耗热量，平均心率
     */

    private void statisticsRunData() {
        //用时
        String userTime = RunTimeUtil.secToTime(SerialPortUtil.getTreadInstance().getRunTime());
        mUseTimeTextView.setText(userTime);
    }


    /**
     * 开启跑步线程
     */
    private void startRunThread() {
        RunThread runThread = new RunThread();
        runThread.start();
    }

    /**
     * 开启一个线程记录时间和距离等数据
     */
    private class RunThread extends Thread {
        @Override
        public void run() {
            while (!isPause) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SerialPortUtil.getTreadInstance().setRunTime(1);
            }
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
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
        if (mCurrentGrade != treadData.getCurrentGrade()) {
            mCurrentGrade = treadData.getCurrentGrade();
            mGradeInfoTextView.setText(String.valueOf(mCurrentGrade));
        }
        if (mCurrentSpeed != treadData.getCurrentSpeed()) {
            mCurrentSpeed = treadData.getCurrentSpeed();
            mSpeedInfoTextView.setText(String.valueOf(mCurrentSpeed));
        }
        if (mHeartRate != treadData.getHeartRate()) {
            mHeartRate = treadData.getHeartRate();
            mHeartRateInfoTextView.setText(String.valueOf(mHeartRate));
        }
        if (mHotInfo != treadData.getHeartRate()) {
            mHotInfo = treadData.getKCAL();
            mHotInfoTextView.setText(String.valueOf(mHotInfo));
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

    /***
     * 初始化主界面
     */
    private void initRunInfoViews() {
        View gradeCell = mRootView.findViewById(R.id.cell_grade);
        View speedCell = mRootView.findViewById(R.id.cell_speed);
        View hotCell = mRootView.findViewById(R.id.cell_hot);
        View heartRateCell = mRootView.findViewById(R.id.cell_heart_rate);
        setupRunInfoCell(gradeCell, "坡度");
        setupRunInfoCell(speedCell, "速度(KM/H)");
        setupRunInfoCell(hotCell, "消耗热量(KCAL)");
        setupRunInfoCell(heartRateCell, "心率(BPM)");
        mGradeInfoTextView.setText("0");
        mSpeedInfoTextView.setText("0");
        mHotInfoTextView.setText("0.0");
        mHeartRateInfoTextView.setText("0");
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


    /**
     * 初始化结束跑步run
     */
    private void initRunFinishViews() {
        View distanceView = mRootView.findViewById(R.id.layout_distance);
        View useTimeView = mRootView.findViewById(R.id.layout_user_time);
        View averageGradientView = mRootView.findViewById(R.id.layout_average_gradient);
        View avergageSpeedView = mRootView.findViewById(R.id.layout_average_speed);
        View consumeKcalView = mRootView.findViewById(R.id.layout_consume_kcal);
        View avergHraetRateView = mRootView.findViewById(R.id.layout_average_heart_rate);

        setupRunFinishData(distanceView, "距离(KM)", 24f, 32f);
        setupRunFinishData(useTimeView, "用时", 24f, 32f);
        setupRunFinishData(averageGradientView, "平均坡度", 20f, 24f);
        setupRunFinishData(avergageSpeedView, "平均速度(KM/H)", 20f, 24f);
        setupRunFinishData(consumeKcalView, "消耗热量(KCAL)", 20f, 24f);
        setupRunFinishData(avergHraetRateView, "平均心率(BPM)", 20f, 24f);
        mRunTimeTextView.setText(DateUtils.formatDate("yyyy-MM-dd HH:mm", new Date()));

        mDistanceTextView.setText("5.5");
        mUseTimeTextView.setText("02:30:22");
        mAverageGradientTextView.setText("5.5");
        mAvergageSpeedTextView.setText("33");
        mConsumeKcalTextView.setText("4555");
        mAvergHraetRateTextView.setText("80");
    }

    private void setupRunFinishData(View view, String title, float titleSize, float contentSize) {
        TextView titleTextView = (TextView) view.findViewById(R.id.info_title_textView);
        TextView contentTextView = (TextView) view.findViewById(R.id.info_content_textView);
        titleTextView.setText(title);
        contentTextView.setTypeface(mTypeFace);
        titleTextView.setTextSize(titleSize);
        contentTextView.setTextSize(contentSize);
        switch (view.getId()) {
            case R.id.layout_distance:
                mDistanceTextView = contentTextView;
                break;
            case R.id.layout_user_time:
                mUseTimeTextView = contentTextView;
                break;
            case R.id.layout_average_gradient:
                mAverageGradientTextView = contentTextView;
                break;
            case R.id.layout_average_speed:
                mAvergageSpeedTextView = contentTextView;
                break;
            case R.id.layout_consume_kcal:
                mConsumeKcalTextView = contentTextView;
                break;
            case R.id.layout_average_heart_rate:
                mAvergHraetRateTextView = contentTextView;
                break;

        }
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
            SerialPortUtil.stopTreadMill();
            mPauseLayout.setVisibility(View.GONE);
            mLayoutRun.setVisibility(View.GONE);
            mFinishLayout.setVisibility(View.VISIBLE);
            mStartLayout.setVisibility(View.GONE);
            isPause = true;
            //运动结束跳转到完成界面
            statisticsRunData();
            SerialPortUtil.getTreadInstance().reset();//情况数据
        }
    }
}
