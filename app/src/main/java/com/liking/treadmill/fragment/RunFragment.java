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
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
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

    @BindView(R.id.user_head_imageView)
    HImageView mUserHeadImageView;
    @BindView(R.id.user_name_TextView)
    TextView mUserNameTextView;
    @BindView(R.id.layout_finish)
    RelativeLayout mFinishLayout;
    @BindView(R.id.this_run_finish_prompt)
    TextView mRunFinishPromptextView;
//    @BindView(R.id.run_time_TextView)
//    TextView mRunTimeTextView;
    @BindView(R.id.run_finish_hint)
    TextView mFinishHintTextView;

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
    @BindView(R.id.fast_level_1_TextView)
    TextView mFastleve1TextView;
    @BindView(R.id.fast_level_2_TextView)
    TextView mFastleve2TextView;
    @BindView(R.id.fast_level_3_TextView)
    TextView mFastleve3TextView;
    @BindView(R.id.fast_level_4_TextView)
    TextView mFastleve4TextView;

    @BindView(R.id.layout_prepare)
    View mPrepareLayout;
    @BindView(R.id.prepare_count_down_TextView)
    TextView mPrepareCountDownTextView;

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

    private TextView mDistanceTextView;//距离
    private TextView mUseTimeTextView; //用时
    private TextView mAvergageSpeedTextView;   //平均速度
    private TextView mTotalStepNumberTextView; //总步数
    private TextView mConsumeKcalTextView;    //消耗热量
    private TextView mAvergHraetRateTextView; //平均心率

    private Typeface mTypeFace;//字体
    private PauseCountdownTime mPauseCountdownTime;//60s 倒计时类
    private boolean isPause;//是否暂停
    private long currentDateSecond;//当前时间
    private volatile boolean isStart = false; //跑步机是否计数
    private volatile boolean isFinish = false; //跑步机结束

    private boolean isTargetCmp = false;
    private float maxTotalTime;  //系统设置的最长跑步时间 /min
    private float totalTime;  //目标设置的总时间 /min
    private float totalKilometre;//目标设置的总距离
    private float totalKcal;//目标设置的总卡路里
    private String totalTarget = "";
    private int THREADMILL_MODE_SELECT = ThreadMillConstant.THREADMILL_MODE_SELECT_QUICK_START;//启动方式
    private int GOAL_TYPE = 0;//设定目标时的类型
    private float GOAL_VALUE = 0;
    private int ACHIEVE_TYPE = 0;//设定目标时完成情况
    private PrepareCountdownTime mPrepareCountdownTime;
    private Animation animation;
    private int mCurrStepNumber = -1;//当前步数
    private long mRunlastTime;

    private float mCurrKmDistance ;//当前已跑总距离
    private int mCurrRunTime;//当前已跑总时间
    private float mCurrKcal;//当前消耗总卡路里

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_run, container, false);
        ButterKnife.bind(this, mRootView);
        mTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Impact.ttf");
        mPrepareCountdownTime = new PrepareCountdownTime(5000,1000);
        mPauseCountdownTime = new PauseCountdownTime(Preference.getStandbyTime() * 1000, 1000);
        initData();
        initPauseView();
        setViewTypeFace();
        return mRootView;
    }

    private void start() {
        startTreadMill(SerialPortUtil.DEFAULT_SPEED, SerialPortUtil.DEFAULT_GRADE);
        startRunThread();
    }

    public void initData() {
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.count_down_exit);
        maxTotalTime = Preference.getMotionParamMaxRunTime();
        Bundle bundle = getArguments();
        if (bundle != null) {
            totalTime = bundle.getFloat(ThreadMillConstant.GOALSETTING_RUNTIME, totalTime);
            totalKilometre = bundle.getFloat(ThreadMillConstant.GOALSETTING_KILOMETRE, 0);
            totalKcal = bundle.getFloat(ThreadMillConstant.GOALSETTING_KCAL, 0);

            if (totalTime > 0) {
                GOAL_TYPE = 1;
                GOAL_VALUE = totalTime;
                totalTarget = String.valueOf((int) totalTime) + "min";
            } else if (totalKilometre > 0) {
                GOAL_TYPE = 2;
                GOAL_VALUE = totalKilometre;
                totalTarget = StringUtils.getDecimalString(totalKilometre, 1) + "Km";
            } else if (totalKcal > 0) {
                GOAL_TYPE = 3;
                GOAL_VALUE = totalKcal;
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
        if (isInRunUI() && !isPrepareUI()) { //正在跑步界面
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
            } else if (keyCode == LikingTreadKeyEvent.KEY_HAND_SHANK_SPEED_PLUS) {
                setSpeed(SerialPortUtil.getTreadInstance().getCurrentSpeed() + 10);
            } else if (keyCode == LikingTreadKeyEvent.KEY_HAND_SHANK_SPEED_REDUCE) {
                setSpeed(SerialPortUtil.getTreadInstance().getCurrentSpeed() - 10);
            } else if (keyCode == LikingTreadKeyEvent.KEY_HAND_SHANK_GRADE_PLUS) {
                setGrade(SerialPortUtil.getTreadInstance().getCurrentGrade() + 1);
            } else if (keyCode == LikingTreadKeyEvent.KEY_HAND_SHANK_GRADE_REDUCE) {
                setGrade(SerialPortUtil.getTreadInstance().getCurrentGrade() - 1);
            }
        } else if (isInFinishUI()) {
            if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
                ResetTreadmill();
                ((HomeActivity) getActivity()).setTitle("");
                ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
            } else if (keyCode == LikingTreadKeyEvent.KEY_CARD) {
                cardLogin();
            } else if (keyCode == LikingTreadKeyEvent.KEY_PROGRAM) {
                SerialPortUtil.TreadData.UserInfo userInfo = SerialPortUtil.getTreadInstance().getUserInfo();
                if(userInfo != null && userInfo.isManager) {
                    showSettingUI();
                }
            }
        } else if (isInPauseUI()) {
            if (keyCode == LikingTreadKeyEvent.KEY_START) {
                startTreadMill(SerialPortUtil.DEFAULT_SPEED, mGrade);
            } else if (keyCode == LikingTreadKeyEvent.KEY_STOP) {
                finishExercise();
            }
        }
    }

    public void showToast(String n, String value) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyPauseCountTime();
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

    private boolean isPrepareUI() {
        return mPrepareLayout.getVisibility() == View.VISIBLE;
    }

    private boolean isInSettingUI() {
        return mSettingLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 开始跑步
     */
    public void startTreadMill(int speed, int grade) {
        LogUtils.e(TAG,"speed : " + speed + ";;;grade : " + grade);
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
        LogUtils.e("zanting","----zanting-----");
        SerialPortUtil.stopTreadMill();//暂停命令
        mPauseLayout.setVisibility(View.VISIBLE);
        mLayoutRun.setVisibility(View.GONE);
        mFinishLayout.setVisibility(View.GONE);
        mSettingLayout.setVisibility(View.GONE);
    }

    private void showSettingUI() {
        ((HomeActivity) getActivity()).launchFragment(new SettingFragment());
    }

    /**
     *  结束锻炼
     */
    private void finishExercise() {
        destroyPauseCountTime();
        destroyPrepareCountTime();
        isFinish = true;
        isPause = true;
        SerialPortUtil.stopTreadMill();
        mPrepareLayout.setVisibility(View.GONE);
        mPauseLayout.setVisibility(View.GONE);
        mLayoutRun.setVisibility(View.GONE);
        mFinishLayout.setVisibility(View.VISIBLE);
        mSettingLayout.setVisibility(View.GONE);
        //运动结束跳转到完成界面
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
            showToast("速度", StringUtils.getDecimalString(mSpeed / 10.0f, 1));
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
            showToast("坡度", String.valueOf(mGrade));
        }
    }

    /**
     * 根据速度改变圆圈的状态
     *
     * @param speed
     */
    private void setSpeedBack(int speed) {
        float currentSpeed = (float) speed / 10.0f;
        if (currentSpeed > 0f && currentSpeed <= 5f) {//慢走
            mRunSpeedImageView.setBackgroundResource(R.drawable.fast_level_1);
            mFastleve1TextView.setTextColor(ResourceUtils.getColor(R.color.c35EA81));
            mFastleve2TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve3TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve4TextView.setTextColor(ResourceUtils.getColor(R.color.white));
        } else if(currentSpeed > 5f && currentSpeed <= 6.5f) {//快走
            mRunSpeedImageView.setBackgroundResource(R.drawable.fast_level_2);
            mFastleve1TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve2TextView.setTextColor(ResourceUtils.getColor(R.color.c35EA81));
            mFastleve3TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve4TextView.setTextColor(ResourceUtils.getColor(R.color.white));
        } else if (currentSpeed > 6.5f && currentSpeed <= 9f) {//慢跑
            mRunSpeedImageView.setBackgroundResource(R.drawable.fast_level_3);
            mFastleve1TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve2TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve3TextView.setTextColor(ResourceUtils.getColor(R.color.c35EA81));
            mFastleve4TextView.setTextColor(ResourceUtils.getColor(R.color.white));
        } else if (currentSpeed > 9f) {//疾跑
            mRunSpeedImageView.setBackgroundResource(R.drawable.fast_level_4);
            mFastleve1TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve2TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve3TextView.setTextColor(ResourceUtils.getColor(R.color.white));
            mFastleve4TextView.setTextColor(ResourceUtils.getColor(R.color.c35EA81));
        }
        mSpeedInfoTextView.setText(StringUtils.getDecimalString(currentSpeed, 1));
    }

    /***
     * 跑步结束统计 距离 、用时、平均坡度、平均速度、消耗热量，平均心率
     */

    private void statisticsRunData() {
        SerialPortUtil.TreadData TreadData = SerialPortUtil.getTreadInstance();
        //用时
        int runTime = SerialPortUtil.getTreadInstance().getRunTime();
        if(runTime != 0) {
            String userTime = RunTimeUtil.secToTime(runTime);
            mUseTimeTextView.setText(userTime);
        }
        float totalDistance = TreadData.getDistance();//米
        float totalDistanceKm = getKmDistance(totalDistance);
        //总距离
        if(totalDistanceKm > 0.0f) {
            mDistanceTextView.setText(StringUtils.getDecimalString(totalDistanceKm, 2));
        }
        //平均速度
        if (totalDistance > 0.0f) {
            float h = (float) (SerialPortUtil.getTreadInstance().getRunTime() / 3600.0);
            float avergageSpeed = totalDistanceKm / h;
            mAvergageSpeedTextView.setText(StringUtils.getDecimalString(avergageSpeed, 2));
        }
        int stepNumber = SerialPortUtil.getTreadInstance().getStepNumber();
        if(stepNumber != 0) {
            mTotalStepNumberTextView.setText(String.valueOf(stepNumber));
        }
        //消耗热量
        float kcal = SerialPortUtil.getTreadInstance().getKCAL();
        if(kcal > 0.0f) {
            mConsumeKcalTextView.setText(StringUtils.getDecimalString(kcal, 2));
        }
        //平均心率
        int heartRate = SerialPortUtil.getTreadInstance().getHeartRate();
        if(heartRate != 0) {
            mAvergHraetRateTextView.setText(heartRate + "");
        }
        //安全锁打开时,会清除所有数据
        if(runTime == 0 || totalDistanceKm == 0.0f || kcal == 0.0f) {
            LogUtils.e(TAG, "数据被清除.....");
            showRunResult(mCurrRunTime, mCurrKmDistance , mCurrKcal);
        } else {
            showRunResult(SerialPortUtil.getTreadInstance().getRunTime(), totalDistanceKm, SerialPortUtil.getTreadInstance().getKCAL());

        }

        SerialPortUtil.TreadData.UserInfo userInfo = SerialPortUtil.getTreadInstance().getUserInfo();
        if(userInfo != null && !userInfo.isVisitor) { //非访客模式
            try {
                //上传锻炼数据
                ((HomeActivity) getActivity()).iBackService.reportExerciseData(THREADMILL_MODE_SELECT, GOAL_TYPE, GOAL_VALUE, ACHIEVE_TYPE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        startActiveMonitor(12);
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
//            mRunFinishPromptextView.setTextColor(ResourceUtils.getColor(R.color.c25ff8c));
            mRunFinishPromptextView.setText(ResourceUtils.getString(R.string.this_run_finish));
        } else if (percentage < 1) {
            int percent = Math.round(percentage * 100);
            mRunCompleteImg.setVisibility(View.GONE);
            mRunProgressLayout.setVisibility(View.VISIBLE);
            mRunProgressView.setPercent(percent);
            String percents = percent + "%";
            mRunPrgressValue.setText(percents);
//            String promp = String.format(ResourceUtils.getString(R.string.run_result_unfinished_txt_hint),percents);
//            mRunFinishPromptextView.setTextColor(ResourceUtils.getColor(R.color.white));
//            Html.fromHtml(promp)
            mRunFinishPromptextView.setText(ResourceUtils.getString(R.string.run_result_unfinished_txt_hint));
        } else  {
            ACHIEVE_TYPE = 1;
            mRunProgressLayout.setVisibility(View.GONE);
            mRunCompleteImg.setVisibility(View.VISIBLE);
//            mRunFinishPromptextView.setTextColor(ResourceUtils.getColor(R.color.c25ff8c));
            mRunFinishPromptextView.setText(ResourceUtils.getString(R.string.this_run_attainment_target));
//            mRunTimeTextView.setText(DateUtils.formatDate("MM-dd HH:mm", new Date()));
        }
        if (SerialPortUtil.getTreadInstance().getUserInfo() != null) {
            mUserNameTextView.setText(SerialPortUtil.getTreadInstance().getUserInfo().mUserName);
            HImageLoaderSingleton.getInstance().loadImage(mUserHeadImageView, SerialPortUtil.getTreadInstance().getUserInfo().mAvatar);
        }
        SpannableStringBuilder ssbh = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_run_complete_hint));
        ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_back);
        ssbh.setSpan(imageSpanBack, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mFinishHintTextView.setText(ssbh);
    }


    /**
     * 重置跑步机设置
     */
    public void ResetTreadmill() {
        isStart = false;
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
                float distance = SerialPortUtil.getTreadInstance().getDistance();
                if(distance > 0.0f) {
                    mCurrKmDistance = getKmDistance(distance);
                }
                mCurrentDistanceTextView.setText(StringUtils.getDecimalString(mCurrKmDistance, 2));
                int runTime = SerialPortUtil.getTreadInstance().getRunTime();
                String time = RunTimeUtil.secToTime(runTime);
                if(runTime > 0) {
                    mCurrRunTime = runTime;
                    mCurrentUserTime.setText(time);
                }
                float kcal = SerialPortUtil.getTreadInstance().getKCAL();
                if(kcal > 0.0f) {
                    mCurrKcal = kcal;
                }
                checkRunResult(mCurrRunTime, mCurrKmDistance, mCurrKcal);
                LogUtils.d("dddd", "distance: " + SerialPortUtil.getTreadInstance().getDistance() + " kcal: " + mCurrKcal);
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
            finishExercise();
        }

        if(totalTime > 0 && time >= (totalTime * 60)
         ||totalKilometre > 0 && distance >= totalKilometre
         ||totalKcal > 0 && kcal >= totalKcal) {
            if(!isTargetCmp) {
                isTargetCmp = true;
                IToast.show(ResourceUtils.getString(R.string.this_run_attainment_target));
            }
        }

        //在锻炼期即将达成目标时，当前画面上方出现toast消息进行提示
        if(totalTime * 60 > 300 && (totalTime * 60 - time) == 300 ){
            IToast.show(String.format(ResourceUtils.getString(R.string.run_attainment_target_upcoming),"继续5分钟"));
        } else if(totalKilometre > 0.5 && totalKilometre - distance < 0.51 && totalKilometre - distance > 0.49) {
            IToast.show(String.format(ResourceUtils.getString(R.string.run_attainment_target_upcoming),"跑步0.5公里"));
        } else if(totalKcal > 50 && (int)(totalKcal - kcal)  == 50) {
            IToast.show(String.format(ResourceUtils.getString(R.string.run_attainment_target_upcoming),"消耗50卡路里"));
        }

        //如果为访客模式 超过五分钟结束跑步
        SerialPortUtil.TreadData.UserInfo userInfo = SerialPortUtil.getTreadInstance().getUserInfo();
        if(userInfo != null && userInfo.isVisitor) {
            if(time >= 5 * 60) {
                finishExercise();
            }
        }

        //判断是否空跑
        if(!checkUserIsRunning()) {
//            IToast.show("空跑...");
            finishExercise();
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

    /**
     * 销毁动画倒计时
     */
    private void destroyPrepareCountTime() {
        if (mPrepareCountdownTime != null) {
            mPrepareCountdownTime.cancel();
        }

    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
        if(isFinish) return;
        if(treadData.getSafeLock() == SerialPortUtil.SaveLock.SAVE_LOCK_OPEN) {
            finishExercise();
        } else if(treadData.getSafeLock() == SerialPortUtil.SaveLock.SAVE_LOCK_CLOSE){
            LogUtils.e(TAG,"SAVE_LOCK_CLOSE");
        }
        if (mGrade != treadData.getCurrentGrade() && treadData.getCurrentGrade() != 0) {
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
        mStepNumberInfoTextView.setText(String.valueOf(treadData.getStepNumber()));
        setSpeedBack(mSpeed);
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        mPrepareCountdownTime.start();
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
//        destroyPauseCountTime();
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
        View stepNumberCell = mRootView.findViewById(R.id.cell_step_number);
        View hotCell = mRootView.findViewById(R.id.cell_hot);
        View heartRateCell = mRootView.findViewById(R.id.cell_heart_rate);
        setupRunInfoCell(gradeCell, "坡度", R.drawable.icon_run_grade);
        setupRunInfoCell(speedCell, "速度(KM/H)", R.drawable.icon_run_speed);
        setupRunInfoCell(stepNumberCell, "步数", R.drawable.icon_run_step_number);
        setupRunInfoCell(hotCell, "消耗卡路里(KCAL)", R.drawable.icon_run_kcal);
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
        View totalStepNumberView = mRootView.findViewById(R.id.layout_total_step_number);
        View stepNumberCell = mRootView.findViewById(R.id.cell_step_number);
        View consumeKcalView = mRootView.findViewById(R.id.layout_consume_kcal);
        View avergHraetRateView = mRootView.findViewById(R.id.layout_average_heart_rate);

        setupRunFinishData(distanceView, "公里(KM)", 24f, 32f, R.drawable.icon_run_distance);
        setupRunFinishData(useTimeView, "用时", 24f, 32f, R.drawable.icon_run_time);
//        setupRunFinishData(averageGradientView, "平均坡度", 20f, 24f, R.drawable.icon_run_grade);
        setupRunFinishData(avergageSpeedView, "平均配速(KM/H)", 20f, 24f, R.drawable.icon_run_speed);
        setupRunFinishData(totalStepNumberView, "步数", 20f, 24f, R.drawable.icon_run_step_number);
        setupRunFinishData(consumeKcalView, "消耗卡路里(KCAL)", 20f, 24f, R.drawable.icon_run_kcal);
        setupRunFinishData(avergHraetRateView, "平均心率(BPM)", 20f, 24f, R.drawable.icon_run_bpm);
//        mRunTimeTextView.setText(DateUtils.formatDate("MM-dd HH:mm", new Date()));

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
     * 倒计时 3s 准备
     */
    class PrepareCountdownTime extends CountDownTimer {

        public PrepareCountdownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String value = "";
            int second = (int) ((millisUntilFinished - 1000)/ 1000);
            value = String.valueOf(second);
            if(second == 0) {
                value = "GO";
            }
            mPrepareCountDownTextView.setText(value);
            bigAnimation();
        }

        @Override
        public void onFinish() {
            destroyPrepareCountTime();
            mPrepareCountdownTime = null;
            if(!isFinish) {
                RunFragment.this.start();
            }
            mPrepareLayout.setVisibility(View.GONE);
        }
    }

    public void bigAnimation() {
        animation.reset();
        animation.setFillAfter(true);
        animation.setRepeatCount(1);
        mPrepareCountDownTextView.startAnimation(animation);
    }

    /**
     * 判断用户是否空跑
     */
    public boolean checkUserIsRunning() {
        int stepNumber = SerialPortUtil.getTreadInstance().getStepNumber();
        long currentTime = System.currentTimeMillis();
        boolean isRun = true;
        if (mCurrStepNumber != stepNumber) {
            mCurrStepNumber = stepNumber;
            mRunlastTime = currentTime;
        }  else if (mCurrStepNumber == stepNumber && currentTime - mRunlastTime > Preference.getStandbyTime() * 1000) {
            isRun = false;
        }
        return isRun;
    }
}
