package com.liking.treadmill.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.fragment.AwaitActionFragment;
import com.liking.treadmill.fragment.BindGymFragment;
import com.liking.treadmill.fragment.SettingFragment;
import com.liking.treadmill.fragment.StartFragment;
import com.liking.treadmill.fragment.StartSettingFragment;
import com.liking.treadmill.fragment.UpdateFragment;
import com.liking.treadmill.fragment.WelcomeFragment;
import com.liking.treadmill.message.FanStateMessage;
import com.liking.treadmill.message.GymBindSuccessMessage;
import com.liking.treadmill.message.GymUnBindSuccessMessage;
import com.liking.treadmill.message.LoginUserInfoMessage;
import com.liking.treadmill.message.UpdateAppMessage;
import com.liking.treadmill.message.UpdateCompleteMessage;
import com.liking.treadmill.mvp.presenter.UserLoginPresenter;
import com.liking.treadmill.mvp.view.UserLoginView;
import com.liking.treadmill.service.ThreadMillService;
import com.liking.treadmill.socket.MessageBackReceiver;
import com.liking.treadmill.socket.SocketService;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.test.IBackService;
import com.liking.treadmill.widget.IToast;

public class HomeActivity extends LikingTreadmillBaseActivity implements UserLoginView {
    public MessageBackReceiver mMessageBackReceiver = new MessageBackReceiver();
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter mIntentFilter;
    //标记是否已经进行了服务绑定与全局消息注册
    private boolean mFlag;
    //通过调用该接口中的方法来实现数据发送
    public IBackService iBackService;
    private Intent mServiceIntent;

    public Handler mDelayedHandler = new Handler();
    public long delayedInterval = 3000;
    private boolean isUpdate = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iBackService = IBackService.Stub.asInterface(iBinder);
            LogUtils.d(SocketService.TAG, "service is connected");
//            try {
//                LogUtils.d(SocketService.TAG, "上报设备信息start");
//                // iBackService.reportDevices();
//                iBackService.bind();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.d(SocketService.TAG, "service is disconnected");
            iBackService = null;
        }
    };

    public UserLoginPresenter mUserLoginPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchInit();
        if(mUserLoginPresenter == null) {
            mUserLoginPresenter = new UserLoginPresenter(this, this);
        }
        initAdViews();
    }

    public void launchInit() {
        if (Preference.getIsStartingUp()) {  //首次开机
            launchFragment(new WelcomeFragment());
            mDelayedHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isUpdate) {
                        launchFragment(new StartSettingFragment());
                    }
                }
            }, delayedInterval);
        } else {
            launchFragment(new AwaitActionFragment());
        }
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
        //广播监听服务
        Intent intent = new Intent(this, ThreadMillService.class);
        startService(intent);
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

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    /**
     * 开启更新
     *
     * @param message
     */
    public void onEvent(UpdateAppMessage message) {
        LogUtils.d(SocketService.TAG, HomeActivity.class.getSimpleName() + "get updateMessage");
        isUpdate = true;
        launchFragment(new UpdateFragment());
    }

    /**
     * 更新完成
     *
     * @param message
     */
    public void onEvent(UpdateCompleteMessage message) {
        LogUtils.d(SocketService.TAG, HomeActivity.class.getSimpleName() + "Update Complete");
        isUpdate = false;
        if (Preference.getIsStartingUp()) {
            launchFragment(new StartSettingFragment());
        } else {
            launchFragment(new AwaitActionFragment());
        }
    }

    /**
     * 刷卡登录成功监听
     * @param loginUserInfoMessage
     */
    public void onEvent(LoginUserInfoMessage loginUserInfoMessage) {
        if(mUserLoginPresenter != null) {
            mUserLoginPresenter.userLoginResult(loginUserInfoMessage);
        }
    }

    /**
     * 风扇显示
     */
    public void onEvent(FanStateMessage fanStateMessage) {
        if(fanStateMessage != null) {
            setFanViewVisibility(fanStateMessage.visibility);
        }
    }


    @Override
    public void userLogin(String cardno) {
        try {
            iBackService.userLogin(cardno);
        } catch (RemoteException e) {
            e.printStackTrace();
            if(mUserLoginPresenter != null) {
                mUserLoginPresenter.userLoginFail();
            }
            IToast.show(ResourceUtils.getString(R.string.read_card_error));
        }
    }

    @Override
    public void launchStartFragment() {
        launchFragment(new StartFragment());
    }

    @Override
    public void userLoginFail() {
        launchFragment(new AwaitActionFragment());
    }

    @Override
    public void handleNetworkFailure() {

    }

}
