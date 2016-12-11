package com.liking.treadmill.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.liking.treadmill.R;
import com.liking.treadmill.socket.MessageBackReceiver;
import com.liking.treadmill.socket.SocketService;
import com.liking.treadmill.test.IBackService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RunActivity extends LikingTreadmillBaseActivity {
    public MessageBackReceiver mMessageBackReceiver = new MessageBackReceiver();
    @BindView(R.id.left_ad_imageView)
    HImageView mLeftAdImageView;
    @BindView(R.id.right_ad_imageView)
    HImageView mRightAdImageView;
    @BindView(R.id.dashboard_imageView)
    ImageView mDashboardImageView;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter mIntentFilter;
    //标记是否已经进行了服务绑定与全局消息注册
    private boolean mFlag;
    //通过调用该接口中的方法来实现数据发送
    public IBackService iBackService;
    private Intent mServiceIntent;

    private View mWifiImageView;
    private View mFanImageView;
    private View mCooldownImageView;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iBackService = IBackService.Stub.asInterface(iBinder);
            LogUtils.d(SocketService.TAG, "service is connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.d(SocketService.TAG, "service is disconnected");
            iBackService = null;
        }
    };
    private TextView mGradeInfoTextView;
    private TextView mSpeedInfoTextView;
    private TextView mHotInfoTextView;
    private TextView mHeartRateInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        initToolBarViews();
        initAdViews();
        initDashboardImageView();
        initRunInfoViews();
    }

    private void initRunInfoViews() {
        View gradeCell = findViewById(R.id.cell_grade);
        View speedCell = findViewById(R.id.cell_speed);
        View hotCell = findViewById(R.id.cell_hot);
        View heartRateCell = findViewById(R.id.cell_heart_rate);
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
        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Impact.ttf");
        titleTextView.setText(title);
        TextView contentTextView = (TextView) cellView.findViewById(R.id.info_content_textView);
        contentTextView.setTypeface(typeFace);
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

    private void initDashboardImageView() {
        AnimationDrawable animationDrawable = (AnimationDrawable) mDashboardImageView.getBackground();
        animationDrawable.start();
    }

    private void initToolBarViews() {
        hideHomeUpIcon();
        View customToolBarView = getLayoutInflater().inflate(R.layout.view_main_toolbar, null, false);
        mWifiImageView = customToolBarView.findViewById(R.id.wifi_imageView);
        mFanImageView = customToolBarView.findViewById(R.id.fan_imageView);
        mCooldownImageView = customToolBarView.findViewById(R.id.cooldown_imageView);
        setCustomToolBar(customToolBarView);
    }

    private void initAdViews() {
        HImageLoaderSingleton.getInstance().loadImage(mLeftAdImageView, R.drawable.image_ad_run_left);
        HImageLoaderSingleton.getInstance().loadImage(mRightAdImageView, R.drawable.image_ad_run_right);
    }

    @Override
    public void onStart() {
        mFlag = false;
        if (mMessageBackReceiver != null || iBackService == null) {
            mFlag = true;
            initSocket();
            localBroadcastManager.registerReceiver(mMessageBackReceiver, mIntentFilter);
            bindService(mServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (mFlag) {
            unbindService(mServiceConnection);
            localBroadcastManager.unregisterReceiver(mMessageBackReceiver);
        }
        super.onDestroy();
    }

    public void initSocket() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mServiceIntent = new Intent(this, SocketService.class);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SocketService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(SocketService.MESSAGE_ACTION);
    }

}
