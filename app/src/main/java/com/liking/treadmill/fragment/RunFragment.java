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
import com.liking.treadmill.mvp.presenter.UserLoginPresenter;
import com.liking.treadmill.mvp.view.UserLoginView;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.RunTimeUtil;
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

public class RunFragment extends SerialPortFragment implements UserLoginView {
    @BindView(R.id.left_ad_imageView)
    HImageView mLeftAdImageView;
    @BindView(R.id.dashboard_imageView)
    ImageView mDashboardImageView;
    @BindView(R.id.right_ad_imageView)
    HImageView mRightAdImageView;
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
    private float mHotInfo = 0;
    private int mHeartRate = 0;
    private int mSpeed = 0;
    private int mGrade = 0;

    private int totalGrade ; //总坡度

    private TextView mDistanceTextView;//距离
    private TextView mUseTimeTextView; //用时
    private TextView mAverageGradientTextView; //平均坡度
    private TextView mAvergageSpeedTextView;   //平均速度
    private TextView mConsumeKcalTextView;    //消耗热量
    private TextView mAvergHraetRateTextView; //平均心率

    private Typeface mTypeFace;//字体
    private PauseCountdownTime mPauseCountdownTime;//60s 倒计时类
    private CompleteCountdownTime completeCountdownTime;// 120s 倒计时
    private boolean isPause;//是否暂停
    private long currentDateSecond;//当前时间
    private volatile boolean isStart = false; //跑步机是否计数

    private UserLoginPresenter mUserLoginPresenter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_run, container, false);
        ButterKnife.bind(this, mRootView);
        mTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Impact.ttf");
        mPauseCountdownTime = new PauseCountdownTime(60000, 1000);
        initPauseView();
        if(mUserLoginPresenter == null) {//刷卡登录
            mUserLoginPresenter = new UserLoginPresenter(getActivity(), this);
        }
        return mRootView;
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


    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_SET) {//参数设置
//            ((HomeActivity) getActivity()).launchFragment(SettingFragment.instantiate(getActivity(), SettingFragment.class.getName()));
        } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {//返回
//            getSupportFragmentManager().popBackStack();
            if(mFinishLayout.getVisibility() == View.VISIBLE  || mStartLayout.getVisibility() == View.VISIBLE) {
                runRest();
                ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_START) {
            if (mStartLayout.getVisibility() == View.VISIBLE) {//开始跑步
                SerialPortUtil.startTreadMill();
                isPause = false;
                destroyPauseCountTime();
                mPauseLayout.setVisibility(View.GONE);
                mLayoutRun.setVisibility(View.VISIBLE);
                mFinishLayout.setVisibility(View.GONE);
                mStartLayout.setVisibility(View.GONE);
                currentDateSecond = DateUtils.currentDataSeconds();
                startRunThread();//计时开始
            } else if (mLayoutRun.getVisibility() == View.VISIBLE) {//正在跑步界面

            } else if (mPauseLayout.getVisibility() == View.VISIBLE) {//暂停界面
                SerialPortUtil.startTreadMill();
                destroyPauseCountTime();
                isPause = false;
                mPauseLayout.setVisibility(View.GONE);
                mLayoutRun.setVisibility(View.VISIBLE);
                mFinishLayout.setVisibility(View.GONE);
                mStartLayout.setVisibility(View.GONE);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_PAUSE) {//暂停
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                startPauseCountTime();
                isPause = true;
                mPauseLayout.setVisibility(View.VISIBLE);
                mLayoutRun.setVisibility(View.GONE);
                mFinishLayout.setVisibility(View.GONE);
                mStartLayout.setVisibility(View.GONE);
                SerialPortUtil.stopTreadMill();//暂停命令
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_STOP) {//结束
            if (mLayoutRun.getVisibility() == View.VISIBLE || mPauseLayout.getVisibility() == View.VISIBLE) {
                finishExercise();
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_PLUS) {//速度加
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setSpeed(SerialPortUtil.getTreadInstance().getCurrentSpeed() + 1);
                setSpeedBack(mSpeed);
            }

        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_REDUCE) {//速度减
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setSpeed(SerialPortUtil.getTreadInstance().getCurrentSpeed() - 1);
                setSpeedBack(mSpeed);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS) {//坡度+
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                int grade = SerialPortUtil.getTreadInstance().getCurrentGrade();
                if (grade < 25) {
                    mGrade = grade + 1;
                }
                SerialPortUtil.setGradeInRunning(mGrade);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE) {//坡度减
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                int grade = SerialPortUtil.getTreadInstance().getCurrentGrade();
                if (grade > 0) {
                    mGrade = grade - 1;
                }
                SerialPortUtil.setGradeInRunning(mGrade);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_3) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
               setSpeed(30);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_6) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setSpeed(60);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_9) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setSpeed(90);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_12) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setSpeed(120);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_15) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setSpeed(150);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_3) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setGrade(3);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_6) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setGrade(6);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_9) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setGrade(9);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_12) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setGrade(12);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_15) {
            if (mLayoutRun.getVisibility() == View.VISIBLE) {
                setGrade(15);
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_CARD) {//刷卡
            if(mFinishLayout.getVisibility() == View.VISIBLE  || mStartLayout.getVisibility() == View.VISIBLE) {
                isStart = false;
                if(mUserLoginPresenter != null) {
                    mUserLoginPresenter.userLogin();
                }
            }
        }
    }

    private void setSpeed(int speed) {
        if (speed > 0 && speed <= 200) {
            mSpeed = speed;
            SerialPortUtil.setSpeedInRunning(mSpeed);
        }
    }

    private void setGrade(int grade) {
        if (grade > 0 && grade <= 25) {
            mGrade = grade;
            SerialPortUtil.setGradeInRunning(mGrade);
        }
    }

    /**
     * 根据速度改变圆圈的状态
     *
     * @param speed
     */
    private void setSpeedBack(int speed) {
        float currentSpeed = (float) speed / 10f;
        if (currentSpeed > 0f && currentSpeed <= 6f) {//快走
            mRunSpeedImageView.setBackgroundResource(R.drawable.fast_11);
        } else if (currentSpeed > 6f && currentSpeed < 8.5f) {//慢跑
            mRunSpeedImageView.setBackgroundResource(R.drawable.slow_run);
        } else if (currentSpeed > 8.5f) {//快跑
            mRunSpeedImageView.setBackgroundResource(R.drawable.fast_run);
        }
        mSpeedInfoTextView.setText(StringUtils.getDecimalString(currentSpeed, 1));
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
        mStartLayout.setVisibility(View.GONE);
        //运动结束跳转到完成界面
        try {
            //上传锻炼数据
            ((HomeActivity) getActivity()).iBackService.reportExerciseData();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        statisticsRunData();
    }


    /***
     * 跑步结束统计 距离 、用时、平均坡度、平均速度、消耗热量，平均心率
     */

    private void statisticsRunData() {
        SerialPortUtil.TreadData TreadData = SerialPortUtil.getTreadInstance();
        //用时
        String userTime = RunTimeUtil.secToTime(SerialPortUtil.getTreadInstance().getRunTime());
        mUseTimeTextView.setText(userTime);
        float totalDistance = TreadData.getDistance();
        //总距离
        mDistanceTextView.setText(StringUtils.getDecimalString(totalDistance, 2));
        float time = (float) SerialPortUtil.getTreadInstance().getRunTime() / 3600;
        //平均坡度
        if (totalGrade > 0) {
            float averagerGrade = totalGrade / SerialPortUtil.getTreadInstance().getRunTime();
            mAverageGradientTextView.setText(StringUtils.getDecimalString(averagerGrade, 2));
        }
        //平均速度
        if (totalDistance > 0) {
            float avergageSpeed = totalDistance / time;
            mAvergageSpeedTextView.setText(StringUtils.getDecimalString(avergageSpeed, 2));
        }
        //消耗热量
        mConsumeKcalTextView.setText(StringUtils.getDecimalString(SerialPortUtil.getTreadInstance().getKCAL(), 2));
        //平均心率
        mAvergHraetRateTextView.setText(SerialPortUtil.getTreadInstance().getHeartRate() + "");
        CompleteCountdownTime completeCountdownTime = new CompleteCountdownTime(122 * 1000, 1000);
        completeCountdownTime.start();
        runRest();
    }

    /**
     * 重置跑步机设置
     */
    public void runRest() {
        isStart = false;
        if(completeCountdownTime != null) {
            completeCountdownTime.cancel();
        }
        SerialPortUtil.setCardNoUnValid();//设置无效卡
        SerialPortUtil.getTreadInstance().reset();//清空数据
    }

    /**
     * 开启跑步线程
     */
    private void startRunThread() {
        isStart = true;
        RunThread runThread = new RunThread();
        runThread.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mCurrentDistanceTextView.setText(StringUtils.getDecimalString(SerialPortUtil.getTreadInstance().getDistance(), 2));
                String userTime = RunTimeUtil.secToTime(SerialPortUtil.getTreadInstance().getRunTime());
                mCurrentUserTime.setText(userTime);
                LogUtils.d("dddd", "distance: " + SerialPortUtil.getTreadInstance().getDistance() + " kcal: " + SerialPortUtil.getTreadInstance().getKCAL());
            }
        }
    };

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
                    float distance = SerialPortUtil.getTreadInstance().getDistance() + (float) (SerialPortUtil.getTreadInstance().getCurrentSpeed() / 36000.0);
                    float kcal = SerialPortUtil.getTreadInstance().getKCAL() +
                            (float) (0.0703 * (1 + SerialPortUtil.getTreadInstance().getCurrentSpeed() / 100) * SerialPortUtil.getTreadInstance().getDistance());
                    totalGrade =  totalGrade + SerialPortUtil.getTreadInstance().getCurrentGrade();
                    LogUtils.d("rrrr", "distance: " + distance + " KCAL: " + kcal);
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
        if (SerialPortUtil.getTreadInstance().getUserInfo() != null) {
            mUserNameTextView.setText(SerialPortUtil.getTreadInstance().getUserInfo().mUserName);
            HImageLoaderSingleton.getInstance().loadImage(mHeadHImageView, SerialPortUtil.getTreadInstance().getUserInfo().mAvatar);
        }
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

        mDistanceTextView.setText("0");
        mUseTimeTextView.setText("0");
        mAverageGradientTextView.setText("0");
        mAvergageSpeedTextView.setText("0");
        mConsumeKcalTextView.setText("0");
        mAvergHraetRateTextView.setText("0");
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
     * 刷卡登录
     * @param cardno
     */
    @Override
    public void userLogin(String cardno) {
        try {
            ((HomeActivity) getActivity()).iBackService.userLogin(cardno);
        } catch (RemoteException e) {
            e.printStackTrace();
            if(mUserLoginPresenter != null) {
                mUserLoginPresenter.userLoginFail();
            }
            IToast.show(ResourceUtils.getString(R.string.read_card_error));
        }
    }

    /**
     * 重新开始跑步
     */
    @Override
    public void launchRunFragment() {
        ((HomeActivity) getActivity()).launchFragment(new RunFragment());
    }

    /**
     * 用户刷卡失败
     */
    @Override
    public void userLoginFail() {
        ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
    }

    @Override
    public void handleNetworkFailure() {

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
            LogUtils.e(TAG,"完成倒计时:" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
        }
    }
}
