package com.liking.treadmill.activity;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.socket.Constant;
import com.liking.treadmill.R;
import com.liking.treadmill.db.entity.AdvEntity;
import com.liking.treadmill.db.entity.Member;
import com.liking.treadmill.db.service.AdvService;
import com.liking.treadmill.fragment.*;
import com.liking.treadmill.message.*;
import com.liking.treadmill.mvp.presenter.UserLoginPresenter;
import com.liking.treadmill.mvp.view.UserLoginView;
import com.liking.treadmill.service.ThreadMillService;
import com.liking.treadmill.socket.LKMessageBackReceiver;
import com.liking.treadmill.socket.ThreadMillServiceApi;
import com.liking.treadmill.socket.result.DefaultAdResult;
import com.liking.treadmill.socket.result.NewAdResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.MemberHelper;
import com.liking.treadmill.utils.UsbUpdateUtils;
import com.liking.treadmill.widget.IToast;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.liking.treadmill.app.LikingThreadMillApplication.mLKAppSocketLogQueue;

public class HomeActivity extends LikingTreadmillBaseActivity implements UserLoginView {

    public Handler mHandler = new Handler();

    public long delayedInterval = 3000;

    private boolean isUpdate = false;//是否更新

    public boolean isLogin = false;//是否登录

    public UserLoginPresenter mUserLoginPresenter = null;

    private AudioManager audioManager = null; // Audio管理器

    private LKMessageBackReceiver mMessageBackReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMessageReceiver();
        launchInit();
        initPlayWork();
        if (mUserLoginPresenter == null) {
            mUserLoginPresenter = new UserLoginPresenter(this, this);
        }
        mLKAppSocketLogQueue.put(TAG, "onCreate(), 初始化主界面");
    }

    private void initMessageReceiver() {
        mMessageBackReceiver = new LKMessageBackReceiver();
        IntentFilter receiverFilter = new IntentFilter();
        receiverFilter.addAction(Constant.ACTION_MSG);
        registerReceiver(mMessageBackReceiver, receiverFilter);
    }

    private void unregisterMessReceiver() {
        if(mMessageBackReceiver != null) {
            unregisterReceiver(mMessageBackReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检测是否是U盘更新
        if (UsbUpdateUtils.isNeedUSBUpdate()) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("升级中,请勿刷卡...");
            dialog.show();
            mHandler.postDelayed(new Runnable() { //需等待Apk完全显示才能进行更新
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
        if (Preference.getIsStartingUp()) {
            //首次开机
            launchFragment(new WelcomeFragment());
            mHandler.postDelayed(new Runnable() {
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

    @Override
    public void onStart() {
        Intent intent = new Intent(this, ThreadMillService.class);
        startService(intent);
        super.onStart();
    }

    @Override
    public void onDestroy() {
        mLKAppSocketLogQueue.put(TAG, "onDestroy(), 主界面回收，应用关闭");
        unregisterMessReceiver();
        super.onDestroy();
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
        isUpdate = true;
        LogUtils.d(TAG, HomeActivity.class.getSimpleName() + "get updateMessage");
        launchFragment(new UpdateFragment());
        mLKAppSocketLogQueue.put(TAG, "app开始更新");
    }

    /**
     * 更新完成
     *
     * @param message
     */
    public void onEvent(UpdateCompleteMessage message) {
        isUpdate = false;
        setTitle("");
        LogUtils.d(TAG, HomeActivity.class.getSimpleName() + "Update Complete");
        if (Preference.getIsStartingUp()) {
            launchFragment(new StartSettingFragment());
        } else {
            launchFragment(new AwaitActionFragment());
        }
        mLKAppSocketLogQueue.put(TAG, "app更新完成");
    }

    /**
     * 用户刷卡登录成功
     *
     * @param loginUserInfoMessage
     */
    public void onEvent(UserLoginInfoMessage loginUserInfoMessage) {
        if (mUserLoginPresenter != null) {
            if (loginUserInfoMessage.mUserResult != null &&
                    StringUtils.isEmpty(loginUserInfoMessage.mUserResult.getErrMsg())) {
                isLogin = true;
            }
            mUserLoginPresenter.userLoginResult(loginUserInfoMessage);
        }
        mLKAppSocketLogQueue.put(TAG, "会员刷卡登录成功");
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
     * 场馆绑定成功
     *
     * @param message
     */
    public void onEvent(GymBindSuccessMessage message) {
        if (mHandler != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    launchFragment(new AwaitActionFragment());
                }
            }, delayedInterval);
        }
        mLKAppSocketLogQueue.put(TAG, "场馆绑定成功");
    }

    /**
     * 场馆解绑成功
     *
     * @param message
     */
    public void onEvent(GymUnBindSuccessMessage message) {

        if (mHandler != null) {
            //Logout  退出当前管理员登录状态
            if (SerialPortUtil.getTreadInstance().getUserInfo() != null) {
                if (isLogin) {
                    userLogout(SerialPortUtil.getTreadInstance().getUserInfo().mBraceletId);
                    isLogin = false;
                }
                SerialPortUtil.getTreadInstance().resetUserInfo();
            }

            //清空跑步机数据以及场馆数据
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SerialPortUtil.getTreadInstance().reset();//清空跑步数据
                    MemberHelper.getInstance().deleteMembersFromLocal(new MemberHelper.DeleteMembersListener() {
                        @Override
                        public void onMembersDeleteResult(boolean result) {
                            EventBus.getDefault().post(new MembersDeleteMessage());
                        }
                    });
                    launchFragment(new StartSettingFragment());

                    AdvService.getInstance().deleteAll(new AdvService.CallBack<Boolean>() {
                        @Override
                        public void onBack(Boolean aBoolean) {
                            LogUtils.e(TAG, String.format("adv deleteAll %b", aBoolean));
                        }
                    });
                }
            }, delayedInterval);
        }
        mLKAppSocketLogQueue.put(TAG, "场馆解除绑定");
    }

    /**
     * 用户刷卡登录登录
     *
     * @param cardno
     */
    @Override
    public void userLogin(String cardno) {
        try {
            ThreadMillServiceApi.Companion.INSTANCE().userLogin(cardno);
            mLKAppSocketLogQueue.put(TAG, "会员登录");
        } catch (Exception e) {
            e.printStackTrace();
            if (mUserLoginPresenter != null) {
                mUserLoginPresenter.userLoginFail();
            }
            IToast.show(ResourceUtils.getString(R.string.read_card_error));
        }
    }

    /**
     * 刷卡成功页面
     */
    @Override
    public void launchStartFragment() {
        launchFragment(new StartFragment());
    }

    /**
     * 刷卡失败
     */
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
        try {
            ThreadMillServiceApi.Companion.INSTANCE().userLogOut(cardNo);
            mLKAppSocketLogQueue.put(TAG, "会员退出");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端下发会员列表请求命令
     *
     * @param message
     */
    public void onEvent(RequestMembersMessage message) {
//        if (iBackService != null) {
//            try {
//                iBackService.requestMembersCommand();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
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
                MemberHelper.getInstance().updateMembersFromMemory(m);
            }
        }
    }

    /**
     * 下发结束事件 Memory cache -> DB Caching
     *
     * @param message
     */
    public void onEvent(MemberNoneMessage message) {
//        MemberHelper.getInstance().updateMembersFromLocal();
//        if (iBackService != null) {
//            try {
//                iBackService.membersStateReplyCommand();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 删除状态回复
     *
     * @param message
     */
    public void onEvent(MembersDeleteMessage message) {
//        if (iBackService != null) {
//            try {
//                iBackService.membersStateReplyCommand();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void onEvent(AdvResultMessage message) {
        switch (message.what) {
            case AdvResultMessage.ADV_DEFAULT:
                DefaultAdResult.DataBean defaultBean = (DefaultAdResult.DataBean) message.obj1;
                List<AdvEntity> defaultEntities = new ArrayList<>();

                if(defaultBean.getHome() != null) {
                    for (DefaultAdResult.DataBean.DefaultAdBean bean : defaultBean.getHome()) {
                        defaultEntities.add(new AdvEntity(0L, bean.getUrl(), AdvEntity.TYPE_HOME,
                                "0", 0, 0, (long) bean.getExhibition_id(), AdvEntity.DEFAULT));
                    }
                }

                if(defaultBean.getLogin() != null) {
                    for (DefaultAdResult.DataBean.DefaultAdBean bean : defaultBean.getLogin()) {
                        defaultEntities.add(new AdvEntity(0L, bean.getUrl(), AdvEntity.TYPE_LOGIN,
                                "0", 0, 0, (long) bean.getExhibition_id(), AdvEntity.DEFAULT));
                    }
                }

                if(defaultBean.getQuick_start() != null) {
                    for (DefaultAdResult.DataBean.DefaultAdBean bean : defaultBean.getQuick_start()) {
                        defaultEntities.add(new AdvEntity(0L, bean.getUrl(), AdvEntity.TYPE_QUICK_START,
                                "0", bean.getStaytime(), bean.getInterval(), (long) bean.getExhibition_id(), AdvEntity.DEFAULT));
                    }
                }

                if(defaultBean.getSet_mode() != null) {
                    for (DefaultAdResult.DataBean.DefaultAdBean bean : defaultBean.getSet_mode()) {
                        defaultEntities.add(new AdvEntity(0L, bean.getUrl(), AdvEntity.TYPE_SET_MODE,
                                "0", bean.getStaytime(), bean.getInterval(), (long) bean.getExhibition_id(), AdvEntity.DEFAULT));
                    }
                }

                final List<AdvEntity> finalEntity = defaultEntities;
                AdvService.getInstance().deleteAdvByIsDefault(AdvEntity.DEFAULT, new AdvService.CallBack<Boolean>() {
                    @Override
                    public void onBack(Boolean aBoolean) {
                        LogUtils.e(TAG, "delete success !!!!!!!! + default" + aBoolean);
                        AdvService.getInstance().insertAdvList(finalEntity, new AdvService.CallBack<Boolean>() {
                            @Override
                            public void onBack(Boolean aBoolean) {
                                LogUtils.i(TAG, String.format("default insert is %b", aBoolean));
                                postEvent(new AdvRefreshMessage());
                            }
                        });
                    }
                });
                break;
            case AdvResultMessage.ADV_NEW:
                NewAdResult.DataBean dataBean = (NewAdResult.DataBean) message.obj1;
                List<AdvEntity> entities = new ArrayList<>();

                if(dataBean.getHome() != null) {
                    for (NewAdResult.DataBean.NewAdBean bean : dataBean.getHome()) {
                        entities.add(new AdvEntity((long) bean.getAdv_id(), bean.getUrl(), AdvEntity.TYPE_HOME,
                                bean.getEndtime(), bean.getStaytime(), 0, (long) bean.getExhibition_id(), AdvEntity.NOT_DEFAULT));
                    }
                }

                if(dataBean.getLogin() != null) {
                    for (NewAdResult.DataBean.NewAdBean bean : dataBean.getLogin()) {
                        entities.add(new AdvEntity((long) bean.getAdv_id(), bean.getUrl(), AdvEntity.TYPE_LOGIN,
                                bean.getEndtime(), bean.getStaytime(), 0, (long) bean.getExhibition_id(), AdvEntity.NOT_DEFAULT));
                    }
                }

                if(dataBean.getQuick_start() != null) {
                    for (NewAdResult.DataBean.NewAdBean bean : dataBean.getQuick_start()) {
                        entities.add(new AdvEntity((long) bean.getAdv_id(), bean.getUrl(), AdvEntity.TYPE_QUICK_START,
                                bean.getEndtime(), bean.getStaytime(), bean.getInterval(), (long) bean.getExhibition_id(), AdvEntity.NOT_DEFAULT));
                    }
                }

                if(dataBean.getSet_mode() != null) {
                    for (NewAdResult.DataBean.NewAdBean bean : dataBean.getSet_mode()) {
                        entities.add(new AdvEntity((long) bean.getAdv_id(), bean.getUrl(), AdvEntity.TYPE_SET_MODE,
                                bean.getEndtime(), bean.getStaytime(), bean.getInterval(), (long) bean.getExhibition_id(), AdvEntity.NOT_DEFAULT));
                    }
                }
                final List<AdvEntity> finalNewEntity = entities;
                AdvService.getInstance().deleteAdvByIsDefault(AdvEntity.NOT_DEFAULT, new AdvService.CallBack<Boolean>() {
                    @Override
                    public void onBack(Boolean aBoolean) {
                        LogUtils.e(TAG, "delete success !!!!!!!! + default" + aBoolean);
                        AdvService.getInstance().insertAdvList(finalNewEntity, new AdvService.CallBack<Boolean>() {
                            @Override
                            public void onBack(Boolean aBoolean) {
                                LogUtils.i(TAG, String.format("new insert is %b", aBoolean));
                                postEvent(new AdvRefreshMessage());
                            }
                        });
                    }
                });
                break;
        }
    }

    /**
     * 初始化播放器、音量数据等相关工作
     */
    private void initPlayWork() {
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
    }

    // 调用此方法增加音量
    public void volumeAdd() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    // 调用此方法减小音量
    public void volumeSubtract() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

}
