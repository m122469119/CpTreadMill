package com.liking.treadmill.activity;

import android.app.ProgressDialog;
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
import com.liking.treadmill.app.LikingThreadMillApplication;
import com.liking.treadmill.db.entity.Member;
import com.liking.treadmill.fragment.AwaitActionFragment;
import com.liking.treadmill.fragment.StartFragment;
import com.liking.treadmill.fragment.StartSettingFragment;
import com.liking.treadmill.fragment.UpdateFragment;
import com.liking.treadmill.fragment.WelcomeFragment;
import com.liking.treadmill.media.MediaFragment;
import com.liking.treadmill.message.AdvertisementMessage;
import com.liking.treadmill.message.FanStateMessage;
import com.liking.treadmill.message.GymBindSuccessMessage;
import com.liking.treadmill.message.GymUnBindSuccessMessage;
import com.liking.treadmill.message.LoginUserInfoMessage;
import com.liking.treadmill.message.MemberListMessage;
import com.liking.treadmill.message.MemberNoneMessage;
import com.liking.treadmill.message.RequestMembersMessage;
import com.liking.treadmill.message.UpdateAppMessage;
import com.liking.treadmill.message.UpdateCompleteMessage;
import com.liking.treadmill.mvp.presenter.UserLoginPresenter;
import com.liking.treadmill.mvp.view.UserLoginView;
import com.liking.treadmill.service.ThreadMillService;
import com.liking.treadmill.socket.MessageBackReceiver;
import com.liking.treadmill.socket.SocketService;
import com.liking.treadmill.socket.result.AdvertisementResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.test.IBackService;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.MemberUtils;
import com.liking.treadmill.utils.UsbUpdateUtils;
import com.liking.treadmill.widget.IToast;

import java.io.File;
import java.util.List;

import static com.liking.treadmill.app.LikingThreadMillApplication.mLKAppSocketLogQueue;

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

    public boolean isLogin = false;//是否登录

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

    public UserLoginPresenter mUserLoginPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchInit();
        if (mUserLoginPresenter == null) {
            mUserLoginPresenter = new UserLoginPresenter(this, this);
        }
        initAdViews();
        mLKAppSocketLogQueue.put(TAG, "onCreate(), 初始化主界面");

        mDelayedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                launchFragment(new MediaFragment());
            }
        }, 4000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检测是否是U盘更新
        if (UsbUpdateUtils.isNeedUSBUpdate()) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("升级中,请勿刷卡...");
            dialog.show();
            mDelayedHandler.postDelayed(new Runnable() { //需等待Apk完全显示才能进行更新
                @Override
                public void run() {
                    if (dialog != null && !HomeActivity.this.isFinishing() && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    UsbUpdateUtils.checkUSBUpdate(HomeActivity.this);
                }
            }, 3000);
        }
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
        HImageLoaderSingleton.getInstance().loadImage(mLeftAdImageView, R.drawable.run_bg_ad);
        HImageLoaderSingleton.getInstance().loadImage(mRightAdImageView, R.drawable.run_bg_ad);
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
        mLKAppSocketLogQueue.put(TAG, "onDestroy(), 主界面回收，应用关闭");
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
        mLKAppSocketLogQueue.put(TAG, "app开始更新");
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
        setTitle("");
        mLKAppSocketLogQueue.put(TAG, "app更新完成");
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
     *
     * @param loginUserInfoMessage
     */
    public void onEvent(LoginUserInfoMessage loginUserInfoMessage) {
        mLKAppSocketLogQueue.put(TAG, "会员刷卡登录成功");
        if (mUserLoginPresenter != null) {
            if (loginUserInfoMessage.mUserData != null && loginUserInfoMessage.mUserData.getErrcode() == 0) {
                isLogin = true;
            }
            mUserLoginPresenter.userLoginResult(loginUserInfoMessage);
        }
    }

    /**
     * 风扇显示
     */
    public void onEvent(FanStateMessage fanStateMessage) {
        if (fanStateMessage != null) {
            setFanViewVisibility(fanStateMessage.visibility);
        }
    }

    /**
     * 绑定成功
     *
     * @param message
     */
    public void onEvent(GymBindSuccessMessage message) {
        mLKAppSocketLogQueue.put(TAG, "场馆绑定成功");
        if (mDelayedHandler != null) {
            mDelayedHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    launchFragment(new AwaitActionFragment());
                }
            }, delayedInterval);
        }
    }

    /**
     * 解绑成功
     *
     * @param message
     */
    public void onEvent(GymUnBindSuccessMessage message) {
        mLKAppSocketLogQueue.put(TAG, "场馆解除绑定");
        if (mDelayedHandler != null) {
            //Logout
            if (SerialPortUtil.getTreadInstance().getUserInfo() != null) {
                if (isLogin) {
                    userLogout(SerialPortUtil.getTreadInstance().getUserInfo().mBraceletId);
                    isLogin = false;
                }
                SerialPortUtil.getTreadInstance().resetUserInfo();
            }

            mDelayedHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SerialPortUtil.getTreadInstance().reset();//清空数据
                    MemberUtils.getInstance().deleteMembersFromLocal();
                    launchFragment(new StartSettingFragment());
                }
            }, delayedInterval);
        }
    }

    /**
     * 广告下发
     *
     * @param message
     */
    public void onEvent(AdvertisementMessage message) {
        if (message.mResources != null) {

            AdvertisementResult.AdvUrlResource.Resource leftAdv = getAdvResource(0, message.mResources);
            if (leftAdv != null) {
                if (leftAdv.isOpen()) {
                    HImageLoaderSingleton.getInstance().loadImage(mLeftAdImageView, leftAdv.getUrl());
                } else {
                    showDefaultAdvLeftImg();
                }
            } else {
                showDefaultAdvLeftImg();
            }

            AdvertisementResult.AdvUrlResource.Resource rightAdv = getAdvResource(1, message.mResources);
            if (rightAdv != null) {
                if (rightAdv.isOpen()) {
                    HImageLoaderSingleton.getInstance().loadImage(mRightAdImageView, rightAdv.getUrl());
                } else {
                    showDefaultAdvRightImg();
                }
            } else {
                showDefaultAdvRightImg();
            }
        }
    }

    /**
     * 左边默认广告
     */
    public void showDefaultAdvLeftImg() {
        HImageLoaderSingleton.getInstance().loadImage(mLeftAdImageView, R.drawable.run_bg_ad);
    }

    /**
     * 右边默认广告
     */
    public void showDefaultAdvRightImg() {
        HImageLoaderSingleton.getInstance().loadImage(mRightAdImageView, R.drawable.run_bg_ad);
    }

    private AdvertisementResult.AdvUrlResource.Resource getAdvResource(int poistion, List<AdvertisementResult.AdvUrlResource.Resource> resources) {
        if (poistion >= 0 && poistion < resources.size()) {
            return resources.get(poistion);
        }
        return null;
    }

    @Override
    public void userLogin(String cardno) {
        try {
            mLKAppSocketLogQueue.put(TAG, "会员登录");
            iBackService.userLogin(cardno);
        } catch (RemoteException e) {
            e.printStackTrace();
            if (mUserLoginPresenter != null) {
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

    /**
     * 会员退出
     *
     * @param cardNo
     */
    public void userLogout(String cardNo) {
        if (iBackService != null) {
            try {
                mLKAppSocketLogQueue.put(TAG, "会员退出");
                iBackService.userLogOut(cardNo);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务端下发会员列表请求命令
     *
     * @param message
     */
    public void onEvent(RequestMembersMessage message) {
        if (iBackService != null) {
            try {
                iBackService.requestMembersCommand();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下发的成员列表 -> Memory Caching
     *
     * @param message
     */
    public void onEvent(MemberListMessage message) {
        List<Member> members = message.mMemberList;
        if (members != null && !members.isEmpty()) {
            for (Member m : members) {
                MemberUtils.getInstance().updateMembersFromMemory(m);
            }
        }
    }

    /**
     * 下发结束 Memory cache -> DB Caching
     *
     * @param message
     */
    public void onEvent(MemberNoneMessage message) {
        MemberUtils.getInstance().updateMembersFromLocal();
    }

}
