package com.liking.treadmill.app

import com.aaron.android.codelibrary.utils.LogUtils
import com.aaron.android.framework.base.BaseApplication
import com.aaron.android.framework.utils.DeviceUtils
import com.aaron.android.framework.utils.EnvironmentUtils
import com.liking.treadmill.BuildConfig
import com.liking.treadmill.utils.Mac

/**
 * Created on 2017/08/02
 * desc:
 * @author: chenlei
 * @version:1.0
 */
//class ThreadMillApplication :BaseApplication(){
//
//    override fun onCreate() {
////        AbsPreference.init(this)
//        super.onCreate()
//
//        //start socket service
//    }
//
//    override fun initialize() {
//        LogUtils.i(TAG, "initialize---")
//        sDeviceId = DeviceUtils.getDeviceInfo(this)
//        sMac = Mac.getMacAddress(this)
//        sInstance = this
//    }
//
//    override fun backgroundInitialize() {
//        LogUtils.i(TAG, "backgroundInitialize")
//    }
//
//    override fun buildConfigData(): EnvironmentUtils.Config.ConfigData {
//        val configData = EnvironmentUtils.Config.ConfigData()
//        configData.appFlag = BuildConfig.APP_FLAG
//        configData.appVersionCode = BuildConfig.VERSION_CODE
//        configData.appVersionName = BuildConfig.VERSION_NAME
//        configData.setIsTestMode(BuildConfig.TEST_MODE)
//        configData.urlHost = BuildConfig.HTTP_HOST
//        return configData
//    }
//
//    companion object {
//        lateinit var sInstance: ThreadMillApplication
//        lateinit var sDeviceId: String
//        lateinit var sMac: String
//    }
//}