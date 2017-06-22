package com.liking.treadmill.app;

import android.net.wifi.WifiManager;

import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.aaron.android.framework.utils.DeviceUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.alibaba.sdk.android.oss.sample.LKLogQueue;
import com.liking.treadmill.BuildConfig;

import java.io.File;


/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/26 下午2:59
 */
public class LikingThreadMillApplication extends BaseApplication {

    public static String ANDROID_ID = "LikingThreadMill";
    public static LKLogQueue mLKSocketLogQueue, mLKAppSocketLogQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        ANDROID_ID = DeviceUtils.getDeviceInfo(this);
        mLKSocketLogQueue = new LKLogQueue.LKLogQueueBuilder()
                .setApplicationContext(this)
                .setFileDir("threadmill_logs")
                .setQueueCount(10)
                .build();
        mLKAppSocketLogQueue = new LKLogQueue.LKLogQueueBuilder()
                .setApplicationContext(this)
                .setFileDir("threadmill_app")
                .setQueueCount(10)
                .build();
        LogUtils.setEnable(BuildConfig.LOGGER);
        disableWifi();
    }

    private void disableWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

    @Override
    protected void initialize() {
        LogUtils.i(TAG, "initialize---");
    }

    @Override
    protected void backgroundInitialize() {
        LogUtils.i(TAG, "backgroundInitialize---");
        String apkPath = DiskStorageManager.getInstance().getApkFileStoragePath();
        int s = FileUtils.clearFolder(new File(apkPath));
        LogUtils.i(TAG, "clearFolder -> ApkFile path:" + apkPath + " size:" + s);
    }

    @Override
    protected EnvironmentUtils.Config.ConfigData buildConfigData() {
        EnvironmentUtils.Config.ConfigData configData = new EnvironmentUtils.Config.ConfigData();
        configData.setAppFlag(BuildConfig.APP_FLAG);
        configData.setAppVersionCode(BuildConfig.VERSION_CODE);
        configData.setAppVersionName(BuildConfig.VERSION_NAME);
        configData.setIsTestMode(BuildConfig.TEST_MODE);
        configData.setUrlHost(BuildConfig.HTTP_HOST);
        return configData;
    }

}
