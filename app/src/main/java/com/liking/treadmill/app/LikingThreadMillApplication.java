package com.liking.treadmill.app;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.liking.treadmill.BuildConfig;


/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/26 下午2:59
 */
public class LikingThreadMillApplication extends BaseApplication {

    public static String APP_KEY = "ccfdec3a9fb849bdb252b2ca37960dc0"; // 2015/10/29

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void initialize() {
        LogUtils.i(TAG, "initialize---");
    }

    @Override
    protected void backgroundInitialize() {
        LogUtils.i(TAG, "backgroundInitialize---");
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
