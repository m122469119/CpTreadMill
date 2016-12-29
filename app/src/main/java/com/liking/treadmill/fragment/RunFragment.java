package com.liking.treadmill.fragment;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.RunTimeUtil;
import com.liking.treadmill.widget.ColorfulRingProgressView;
import com.liking.treadmill.widget.IToast;

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
    @BindView(R.id.dashboard_imageView)
    ImageView mDashboardImageView;
    @BindView(R.id.layout_run)
    RelativeLayout mLayoutRun;
    @BindView(R.id.current_distance_TextView)
    TextView mCurrentDistanceTextView;
    @BindView(R.id.current_user_time)
    TextView mCurrentUserTime;
    @BindView(R.id.run_speed_ImageView)
    ImageView mRunSpeedImageView;

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
    @BindView(R.id.this_run_finish_prompt)
    TextView mRunFinishPromptextView;
    @BindView(R.id.run_time_TextView)
    TextView mRunTimeTextView;

    @BindView(R.id.run_finish_ImageView)
    ImageView mRunCompleteImg;
    @BindView(R.id.run_progress_layout)
    FrameLayout mRunProgressLayout;
    @BindView(R.id.colorfulring_progress)
    ColorfulRingProgressView mRunProgressView;
    @BindView(R.id.run_progress)
    TextView mRunPrgressValue;
    @BindView(R.id.layout_setting)
    View mSettingLayout;


    private View mRootView;
    private TextView mGradeInfoTextView;
    private TextView mSpeedInfoTextView;
    private TextView mStepNumberInfoTextView;
    private TextView mHotInfoTextView;
    private TextView mHeartRateInfoTextView;
    private float mHotInfo = 0;
    private int mHeartRate = 0;
    private int mSpeed = 0;
    private int mGrade = 0;

//    private int totalGrade; //总坡度

    private TextView mDistanceTextView;//距离
    private TextView mUseTimeTextView; //用时
//    private TextView mAverageGradientTextView; //平均坡度
    private TextView mAvergageSpeedTextView;   //平均速度
    private TextView mTotalStepNumberTextView; //总步数
    private TextView mConsumeKcalTextView;    //消耗热量
    private TextView mAvergHraetRateTextView; //平均心率

    private Typeface mTypeFace;//字体
    private PauseCountdownTime mPauseCountdownTime;//60s 倒计时类
    private CompleteCountdownTime completeCountdownTime;// 120s 倒计时
    private boolean isPause;//是否暂停
    private long currentDateSecond;//当前时间
    private volatile boolean isStart = false; //跑步机是否计数

    private float maxTotalTime;  //系统设置的最长跑步时间
    private float totalTime;  //目标设置的总时间
    private float totalKilometre;//目标设置的总距离
    private float totalKcal;//目标设置的总卡路里
    private String totalTarget = "";
    private int THREADMILL_MODE_SELECT = ThreadMillConstant.THREADMILL_MODE_SELECT_QUICK_START;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_run, container, false);
        ButterKnife.bind(this, mRootView);
        mTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Impact.ttf");
        mPauseCountdownTime = new PauseCountdownTime(60000, 1000);
        initData();
        initPauseView();
        setViewTypeFace();
        start();
        return mRootView;
    }

    private void start() {
        startTreadMill(SerialPortUtil.DEFAULT_SPEED, SerialPortUtil.DEFAULT_GRADE);
        startRunThread();
    }

    public void initData() {
        maxTotalTime = Preference.getMotionParamMaxRunTime();
        Bundle bundle = getArguments();
        if (bundle != null) {
            totalTime = bundle.getFloat(ThreadMillConstant.GOALSETTING_RUNTIME, totalTime);
            totalKilometre = bundle.getFloat(ThreadMillConstant.GOALSETTING_KILOMETRE, 0);
            totalKcal = bundle.getFloat(ThreadMillConstant.GOALSETTING_KCAL, 0);

            if (totalTime > 0) {
                totalTarget = String.valueOf((int) totalTime) + "min";
            } else if (totalKilometre > 0) {
                totalTarget = StringUtils.getDecimalString(totalKilometre, 1) + "Km";
            } else if (totalKcal > 0) {
                totalTarget = String.valueOf((int) totalKcal) + "Kcal";
            }
            setGoalSttingValue(totalTarget);
        }
    }

    public void setGoalSttingValue(String value) {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        if (homeActivity != null) {
            THREADMILL_MODE_SELECT = ThreadMillConstant.THREADMILL_MODE_SELECT_GOAL_SETTING;
            homeActivity.setTitle("目标:" + value);
        }
    }

    /**
     * 设置暂停界面
     */
    private void initPauseView() {
        mGoOnLeftPromptTextView.setText("点击 ");
        mGoOnPromptTextView.setText("QUICK" + "\n" + "START");
        mGoOnRightPromptTextView.setText(" 继续运动");
        Spanned visitorText = Html.fromHtml("点击 " + "<font color=#25ff8c><b>STOP</b></font>" + " 完成锻炼");
        mStopPromptTextView.setText(visitorText);
    }

    public void setViewTypeFace() {
        mCurrentDistanceTextView.setTypeface(mTypeFace);
        mCurrentUserTime.setTypeface(mTypeFace);
    }


    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (isInRunUI()) { //正在跑步界面
            if (keyCode == LikingTreadKeyEvent.KEY_PAUSE) {
                pauseTreadmill();
            } else if (keyCode == LikingTreadKeyEvent.KEY_STOP) {
                finishExercise();
            } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_PLUS) {
                setSpeed(SerialPortUtil.getTreadInstance().getCurrentSpeed() + 1);
            } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_REDUCE) {
                setSpeed(SerialPortUtil.getTreadInstance().getCurrentSpeed() - 1);
            } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS) {
                setGrade(SerialPortUtil.getTreadInstance().getCurrentGrade() + 1);
            } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE) {
                setGrade(SerialPortUtil.getTreadInstance().getCurrentGrade() - 1);
            } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_3) {
                setSpeed(30);
            } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_6) {
                setSpeed(60);
            } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_9) {
                setSpeed(90);
            } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_12) {
                setSpeed(120);
            } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_15) {
                setSpeed(150);
            } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_3) {
                setGrade(3);
            } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_6) {
                setGrade(6);
            } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_9) {
                setGrade(9);
            } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_12) {
                setGrade(12);
            } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_15) {
                setGrade(15);
            }
        } else if (isInFinishUI()) {
            if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
                ResetTreadmill();
                ((HomeActivity) getActivity()).setTitle("");
                ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
            } else if (keyCode == LikingTreadKeyEvent.KEY_CARD) {
                cardLogin();
            } else if (keyCode == LikingTreadKeyEvent.KEY_PROGRAM) {
                showSettingUI();
            }
        } else if (isInPauseUI()) {
            if (keyCode == LikingTreadKeyEvent.KEY_START) {
                startTreadMill(SerialPortUtil.DEFAULT_SPEED, SerialPortUtil.getTreadInstance().getCurrentGrade());
            } else if (keyCode == LikingTreadKeyEvent.KEY_STOP) {
                finishExercise();
            }
        }
    }

    public void showToast(String n, int value) {
        IToast.show(String.format(ResourceUtils.getString(R.string.run_thread_set_txt), n, value));
    }

    /**
     * 刷卡登录
     */
    private void cardLogin() {
        isStart = false;
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.setTitle("");
        if (homeActivity.mUserLoginPresenter != null) {
            homeActivity.mUserLoginPresenter.userLogin();
        }
    }

    /**
     * 当前是否在跑步暂停界面
     *
     * @return
     */
    private boolean isInPauseUI() {
        return mPauseLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 当前是否在训练结束界面
     *
     * @return
     */
    private boolean isInFinishUI() {
        return mFinishLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 当前是否在正在跑步的界面
     *
     * @return
     */
    private boolean isInRunUI() {
        return mLayoutRun.getVisibility() == View.VISIBLE;
    }

    private boolean isInSettingUI() {
        return mSettingLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 开始跑步
     */
    public void startTreadMill(int speed, int grade) {
        SerialPortUtil.startTreadMill(speed, grade);
        isPause = false;
        destroyPauseCountTime();
        mPauseLayout.setVisibility(View.GONE);
        mLayoutRun.setVisibility(View.VISIBLE);
        mFinishLayout.setVisibility(View.GONE);
        mSettingLayout.setVisibility(View.GONE);

    }

    /**
     * 暂停跑步机
     */
    private void pauseTreadmill() {
        startPauseCountTime();
        isPause = true;
        mPauseLayout.setVisibility(View.VISIBLE);
        mLayoutRun.setVisibility(View.GONE);
        mFinishLayout.setVisibility(View.GONE);
        mSettingLayout.setVisibility(View.GONE);
        SerialPortUtil.stopTreadMill();//暂停命令
    }

    private void showSettingUI() {
        ((HomeActivity) getActivity()).setTitle("系统设置");
        mPauseLayout.setVisibility(View.GONE);
        mLayoutRun.setVisibility(View.GONE);
        mFinishLayout.setVisibility(View.GONE);
        mSettingLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 结束锻炼
     */
    private void finishExercise() {
        destroyPauseCountTime();
        isPause = true;
        SerialPortUtil.stopTreadMill();
        mPauseLayout.setVisibility(View.GONE);
        mLayoutRun.setVisibility(View.GONE);
        mFinishLayout.setVisibility(View.VISIBLE);
        mSettingLayout.setVisibility(View.GONE);

        //运动结束跳转到完成界面
        try {
            //上传锻炼数据
            ((HomeActivity) getActivity()).iBackService.reportExerciseData();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        statisticsRunData();
    }


    /**
     * 设置跑步机速度
     *
     * @param speed
     */
    private void setSpeed(int speed) {
        if (speed > 0 && speed <= 200) {
            mSpeed = speed;
            SerialPortUtil.setSpeedInRunning(mSpeed);
            showToast("速度", mSpeed);
        }
    }

    /**
     * 设置跑步机速度
     *
     * @param grade
     */
    private void setGrade(int grade) {
        if (grade > 0 && grade <= 25) {
            mGrade = grade;
            SerialPortUtil.setGradeInRunning(mGrade);
            showToast("坡度", mGrade);
        }
    }

    /**
     * 根据速度改变圆圈的状态
     *
     * @param speed
     */
    private void setSpeedBack(int speed) {
        float currentSpeed = (float) speed / 10.0f;
        if (currentSpeed > 0f && currentSpeed <= 6f) {//快走
            mRunSpeedImageView.setBackgroundResource(R.drawable.fast_11);
        } else if (currentSpeed > 6f && currentSpeed < 8.5f) {//慢跑
            mRunSpeedImageView.setBackgroundResource(R.drawable.slow_run);
        } else if (currentSpeed > 8.5f) {//快跑
            mRunSpeedImageView.setBackgroundResource(R.drawable.fast_run);
        }
        mSpeedInfoTextView.setText(StringUtils.getDecimalString(currentSpeed, 1));
    }

    /***
     * 跑步结束统计 距离 、用时、平均坡度、平均速度、消耗热量，平均心率
     */

    private void statisticsRunData() {
        SerialPortUtil.TreadData TreadData = SerialPortUtil.getTreadInstance();
        //用时
        String userTime = RunTimeUtil.secToTime(SerialPortUtil.getTreadInstance().getRunTime());
        mUseTimeTextView.setText(userTime);
        float totalDistance = TreadData.getDistance();//米
        float totalDistanceKm = getKmDistance(totalDistance);
        //总距离
        mDistanceTextView.setText(StringUtils.getDecimalString(totalDistanceKm, 2));
        //平均坡度
//        if (totalGrade > 0) {
//            float averagerGrade = totalGrade / SerialPortUtil.getTreadInstance().getRunTime();
//            mAverageGradientTextView.setText(StringUtils.getDecimalString(averagerGrade, 2));
//        }
        //平均速度
        if (totalDistance > 0) {
            float h = (float) (SerialPortUtil.getTreadInstance().getRunTime() / 3600.0);
            float avergageSpeed = totalDistanceKm / h;
            mAvergageSpeedTextView.setText(StringUtils.getDecimalString(avergageSpeed, 2));
        }
        mTotalStepNumberTextView.setText(SerialPortUtil.getTreadInstance().getStepNumber());
        //消耗热量
        mConsumeKcalTextView.setText(StringUtils.getDecimalString(SerialPortUtil.getTreadInstance().getKCAL(), 2));
        //平均心率
        mAvergHraetRateTextView.setText(SerialPortUtil.getTreadInstance().getHeartRate() + "");
        showRunResult(SerialPortUtil.getTreadInstance().getRunTime(), totalDistanceKm, SerialPortUtil.getTreadInstance().getKCAL());
        completeCountdownTime = new CompleteCountdownTime(122 * 1000, 1000);
        completeCountdownTime.start();
        ResetTreadmill();
    }

    /**
     * 验证跑步结果
     *
     * @param time
     * @param distanceKm
     * @param kcal
     */
    public void showRunResult(float time, float distanceKm, float kcal) {
        if (totalTime > 0) {
            showfinishedView(time / (totalTime * 60));
        } else if (totalKilometre > 0) {
            showfinishedView(distanceKm / totalKilometre);
        } else if (totalKcal > 0) {
            showfinishedView(kcal / totalKcal);
        } else {
            showfinishedView(-1);
        }
    }

    /**
     * 显示未完成界面
     *
     * @param percentage
     */
    public void showfinishedView(float percentage) {
        if(percentage == -1) {
            mRunFinishPromptextView.setTextColor(ResourceUtils.getColor(R.color.c25ff8c));
            mRunFinishPromptextView.setText(ResourceUtils.getString(R.string.this_run_finish));
        } else if (percentage < 1) {
            int percent = Math.round(percentage * 100);
            mRunCompleteImg.setVisibility(View.GONE);
            mRunTimeTextView.setVisibility(View.GONE);
            mRunProgressLayout.setVisibility(View.VISIBLE);
            mRunProgressView.setPercent(percent);
            String percents = percent + "%";
            mRunPrgressValue.setText(percents);
            String promp = String.format(ResourceUtils.getString(R.string.run_result_unfinished_txt_hint),percents);
            mRunFinishPromptextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mRunFinishPromptextView.setText(Html.fromHtml(promp));
        } else  {
            mRunProgressLayout.setVisibility(View.GONE);
            mRunCompleteImg.setVisibility(View.VISIBLE);
            mRunTimeTextView.setVisibility(View.VISIBLE);
            mRunFinishPromptextView.setTextColor(ResourceUtils.getColor(R.color.c25ff8c));
            mRunFinishPromptextView.setText(ResourceUtils.getString(R.string.this_run_attainment_target));
            mRunTimeTextView.setText(DateUtils.formatDate("yyyy-MM-dd HH:mm", new Date()));
        }
    }


    /**
     * 重置跑步机设置
     */
    public void ResetTreadmill() {
        isStart = false;
        if (completeCountdownTime != null) {
            completeCountdownTime.cancel();
        }
        SerialPortUtil.setCardNoUnValid();//设置无效卡
        SerialPortUtil.getTreadInstance().reset();//清空数据
    }

    /**
     * 开启跑步计时和相关计算的线程
     */
    private void startRunThread() {
        isStart = true;
        currentDateSecond = DateUtils.currentDataSeconds();
        RunThread runThread = new RunThread();
        runThread.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                float kmDistance = getKmDistance(SerialPortUtil.getTreadInstance().getDistance());
                mCurrentDistanceTextView.setText(StringUtils.getDecimalString(kmDistance, 2));
                int runTime = SerialPortUtil.getTreadInstance().getRunTime();
                String time = RunTimeUtil.secToTime(runTime);
                mCurrentUserTime.setText(time);
                float kcal = SerialPortUtil.getTreadInstance().getKCAL();
                checkRunResult(runTime, kmDistance, kcal);
                LogUtils.d("dddd", "distance: " + SerialPortUtil.getTreadInstance().getDistance() + " kcal: " + kcal);
            }
        }
    };

    public float getKmDistance(float distance) {
        return (float) (distance / 1000.0);
    }

    /**
     * 验证跑步结果
     */
    public void checkRunResult(float time, float distance, float kcal) {
        if(time >= maxTotalTime * 60) { //超过跑步的最长时间
            SerialPortUtil.stopTreadMill();
            finishExercise();
        }
        if(totalTime > 0 && time >= (totalTime * 60)
         ||totalKilometre > 0 && distance >= totalKilometre
         ||totalKcal > 0 && kcal >= totalKcal) {
            IToast.show(ResourceUtils.getString(R.string.this_run_attainment_target));
        }
    }

    /**
     * 开启一个线程记录时间和距离等数据
     */
    private class RunThread extends Thread {
        @Override
        public void run() {
            while (isStart) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isPause) {
                    SerialPortUtil.getTreadInstance().setRunTime(SerialPortUtil.getTreadInstance().getRunTime() + 1);
//                    totalGrade = totalGrade + SerialPortUtil.getTreadInstance().getCurrentGrade();
                    LogUtils.d(TAG, "increment: Speed: " + SerialPortUtil.getTreadInstance().getCurrentSpeed());
                    float mDistanceIncrement = SerialPortUtil.getTreadInstance().measureDistanceIncrement();
                    float mKcalIncrement = SerialPortUtil.getTreadInstance().measureKcalIncrement();
                    float distance = SerialPortUtil.getTreadInstance().getDistance() + mDistanceIncrement;
                    float kcal = SerialPortUtil.getTreadInstance().getKCAL() + mKcalIncrement;
                    LogUtils.d(TAG, "increment: Distance: " + mDistanceIncrement + " KCAL: " + mKcalIncrement + ";;Grade:" + SerialPortUtil.getTreadInstance().getCurrentGrade());
                    LogUtils.d(TAG, "total: " + SerialPortUtil.getTreadInstance().getDistance() + " KCAL: " + SerialPortUtil.getTreadInstance().getKCAL());

                    SerialPortUtil.getTreadInstance().setDistance(distance);
                    SerialPortUtil.getTreadInstance().setKCAL(kcal);
                    mHandler.sendEmptyMessage(0);
                }
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
        if (mGrade != treadData.getCurrentGrade()) {
            mGrade = treadData.getCurrentGrade();
            mGradeInfoTextView.setText(String.valueOf(mGrade));
        }
        if (mHeartRate != treadData.getHeartRate()) {
            mHeartRate = treadData.getHeartRate();
            mHeartRateInfoTextView.setText(String.valueOf(mHeartRate));
        }
        if (mSpeed != treadData.getCurrentSpeed()) {
            mSpeed = treadData.getCurrentSpeed();
            mSpeedInfoTextView.setText(StringUtils.getDecimalString((float) (mSpeed / 10.0), 2));
        }
        if (mHotInfo != treadData.getKCAL()) {
            mHotInfo = treadData.getKCAL();
            mHotInfoTextView.setText(StringUtils.getDecimalString(mHotInfo, 1));
        }
        mStepNumberInfoTextView.setText(treadData.getStepNumber());
        setSpeedBack(mSpeed);
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
        setupRunInfoCell(gradeCell, "坡度", R.drawable.icon_run_grade);
        setupRunInfoCell(speedCell, "速度(KM/H)", R.drawable.icon_run_speed);
        setupRunInfoCell(speedCell, "步数", R.drawable.icon_run_step_number);
        setupRunInfoCell(hotCell, "消耗热量(KCAL)", R.drawable.icon_run_kcal);
        setupRunInfoCell(heartRateCell, "心率(BPM)", R.drawable.icon_run_bpm);
        mGradeInfoTextView.setText("0");
        mSpeedInfoTextView.setText("0");
        mStepNumberInfoTextView.setText("0");
        mHotInfoTextView.setText("0.0");
        mHeartRateInfoTextView.setText("0");
    }

    private void setupRunInfoCell(View cellView, String title, int titleIcon) {
        TextView titleTextView = (TextView) cellView.findViewById(R.id.info_title_textView);
        if(titleIcon > 0) {
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(titleIcon, 0, 0, 0);
        }
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
            case R.id.cell_step_number:
                mStepNumberInfoTextView = contentTextView;
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
//        View averageGradientView = mRootView.findViewById(R.id.layout_average_gradient);
        View avergageSpeedView = mRootView.findViewById(R.id.layout_average_speed);
        View consumeKcalView = mRootView.findViewById(R.id.layout_consume_kcal);
        View avergHraetRateView = mRootView.findViewById(R.id.layout_average_heart_rate);

        setupRunFinishData(distanceView, "距离(KM)", 24f, 32f, R.drawable.icon_run_distance);
        setupRunFinishData(useTimeView, "用时", 24f, 32f, R.drawable.icon_run_time);
//        setupRunFinishData(averageGradientView, "平均坡度", 20f, 24f, R.drawable.icon_run_grade);
        setupRunFinishData(avergageSpeedView, "平均配速(KM/H)", 20f, 24f, R.drawable.icon_run_speed);
        setupRunFinishData(avergageSpeedView, "步数", 20f, 24f, R.drawable.icon_run_step_number);
        setupRunFinishData(consumeKcalView, "消耗热量(KCAL)", 20f, 24f, R.drawable.icon_run_kcal);
        setupRunFinishData(avergHraetRateView, "平均心率(BPM)", 20f, 24f, R.drawable.icon_run_bpm);
        mRunTimeTextView.setText(DateUtils.formatDate("yyyy-MM-dd HH:mm", new Date()));

        mDistanceTextView.setText("0");
        mUseTimeTextView.setText("0");
//        mAverageGradientTextView.setText("0");
        mAvergageSpeedTextView.setText("0");
        mTotalStepNumberTextView.setText("0");
        mConsumeKcalTextView.setText("0");
        mAvergHraetRateTextView.setText("0");
    }

    private void setupRunFinishData(View view, String title, float titleSize, float contentSize, int titleIcon) {
        TextView titleTextView = (TextView) view.findViewById(R.id.info_title_textView);
        if(titleIcon > 0) {
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(titleIcon, 0, 0, 0);
        }
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
//            case R.id.layout_average_gradient:
//                mAverageGradientTextView = contentTextView;
//                break;
            case R.id.layout_average_speed:
                mAvergageSpeedTextView = contentTextView;
                break;
            case R.id.layout_total_step_number:
                mTotalStepNumberTextView = contentTextView;
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
            finishExercise();
        }
    }

    /**
     * 120s 结束页面  倒计时结束
     */
    class CompleteCountdownTime extends CountDownTimer {

        public CompleteCountdownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            LogUtils.e(TAG, "完成倒计时:" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            ((HomeActivity) getActivity()).setTitle("");
            ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
        }
    }
}
