package com.liking.treadmill.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.liking.treadmill.fragment.RunFragment;
import com.liking.treadmill.fragment.SettingFragment;
import com.liking.treadmill.fragment.UpdateFragment;
import com.liking.treadmill.message.UpdateAppMessage;
import com.liking.treadmill.socket.MessageBackReceiver;
import com.liking.treadmill.socket.SocketService;
import com.liking.treadmill.test.IBackService;
import com.liking.treadmill.utils.ApkUpdateUtils;

public class RunActivity extends LikingTreadmillBaseActivity {
    public MessageBackReceiver mMessageBackReceiver = new MessageBackReceiver();
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter mIntentFilter;
    //标记是否已经进行了服务绑定与全局消息注册
    private boolean mFlag;
    //通过调用该接口中的方法来实现数据发送
    public IBackService iBackService;
    private Intent mServiceIntent;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iBackService = IBackService.Stub.asInterface(iBinder);
            LogUtils.d(SocketService.TAG, "service is connected");
            try {
                LogUtils.d(SocketService.TAG, "上报设备信息start");
                iBackService.reportDevices();
                iBackService.login();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.d(SocketService.TAG, "service is disconnected");
            iBackService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchFragment(new RunFragment());
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

    /**
     * 更新
     * @param message
     */
    public void onEvent(UpdateAppMessage message) {
        LogUtils.d(SocketService.TAG, RunActivity.class.getSimpleName() + "get updateMessage");
        launchFragment(new UpdateFragment());
//        if (updateApp) {
//            DownLoadAppUtil.updateApp(this);
//        }
    }
}
