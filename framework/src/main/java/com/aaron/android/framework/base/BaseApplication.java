package com.aaron.android.framework.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.aaron.android.codelibrary.imageloader.ImageCacheParams;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.aaron.android.framework.library.thread.TaskScheduler;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.facebook.common.util.ByteConstants;

/**
 * Created on 15/6/2.
 *
 * @author ran.huang
 * @version 3.0.1
 *
 *
 * 基类，处理完全退出，系统全局异常等
 */
public abstract class BaseApplication extends Application {

    private static final int MAX_DISK_CACHE_SIZE = 10 * ByteConstants.MB;
    private static BaseApplication sApplication;
    protected final String TAG = getClass().getSimpleName();

    /**
     * 子类实现其他相关初始化操作(由主线程处理，可做非耗时初始化操作)
     */
    protected abstract void initialize();

    /**
     * 子类实现其他相关初始化操作(由子线程处理，可做耗时初始化操作)
     */
    protected abstract void backgroundInitialize();

    protected abstract EnvironmentUtils.Config.ConfigData buildConfigData();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "onCreate----");
        init();
    }

    private void init() {
        sApplication = this;
        /**环境配置信息初始化*/
        EnvironmentUtils.init(this, buildConfigData());
        DiskStorageManager.getInstance().init(EnvironmentUtils.Config.getAppFlag());
        /**图片加载器初始化*/
        ImageCacheParams imageCacheParams = new ImageCacheParams(this);
        imageCacheParams.setDirectoryPath(DiskStorageManager.getInstance().getImagePath());
        imageCacheParams.setDirectoryName(EnvironmentUtils.Config.getAppVersionName());
        imageCacheParams.setMaxDiskCacheSize(MAX_DISK_CACHE_SIZE);
        imageCacheParams.setMaxMemoryCacheSize(getMaxCacheSize());
        HImageLoaderSingleton.getInstance().initialize(imageCacheParams);
        /**设备信息初始化*/
        DisplayUtils.init(this);
        /**根据配置信息判断是否打印日志信息*/
        LogUtils.setEnable(EnvironmentUtils.Config.isTestMode());
        doBackgroundInit();
        initialize();
        printInfo();
    }

    private void printInfo() {
        LogUtils.d(TAG, "\n---ApkInfo---\nappFlag: " + EnvironmentUtils.Config.getAppFlag() + "\nappVersionName: " + EnvironmentUtils.Config.getAppVersionName() +
                "\nappVersionCode: " + EnvironmentUtils.Config.getAppVersionCode() + "\nappHostUrl: " + EnvironmentUtils.Config.getHttpRequestUrlHost());
        LogUtils.d(TAG, "\n---ScreenInfo---\nscreenWidth: " + DisplayUtils.getWidthPixels() + "\nscreenHeight: " + DisplayUtils.getHeightPixels()
                + "\ndensityDpi: " + DisplayUtils.getDensityDpi() + "\ndensity: " + DisplayUtils.getDensity() + "\ndensityStr: " + DisplayUtils.getBitmapDensityStr());
    }

    /**
     *
     * @return 获取可用的最大内存
     */
    private int getMaxCacheSize() {
        final int maxMemory =
                Math.min(((ActivityManager) (BaseApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE))).getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
        int memory;
        if (maxMemory < 32 * ByteConstants.MB) {
            memory = 4 * ByteConstants.MB;
        } else if (maxMemory < 64 * ByteConstants.MB) {
            memory = 6 * ByteConstants.MB;
        } else {
            // We don't want to use more ashmem on Gingerbread for now, since it doesn't respond well to
            // native memory pressure (doesn't throw exceptions, crashes app, crashes phone)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                memory = 8 * ByteConstants.MB;
            } else {
                memory = maxMemory / 8;
            }
        }
        LogUtils.i("aaron", "memory: " + memory / ByteConstants.MB + "M");
        return memory;
    }

    private void doBackgroundInit() {
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                backgroundInitialize();

            }
        });
    }

    /**
     * 获取Application实例
     * @return BaseApplication
     */
    public static BaseApplication getInstance() {
        return sApplication;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
