package com.liking.treadmill.utils;

import android.content.Context;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.liking.treadmill.storge.Preference;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:45
 */

public class ApkUpdateUtils {

    /**
     * 是否需要更新
     * @return
     */
    public static boolean isApkUpdate() {
        String serverVersion = Preference.getServerVersion();//服务器当前版本
        String currentVersion = EnvironmentUtils.Config.getAppVersionName();
        return !StringUtils.isEmpty(serverVersion) && !serverVersion.equals(currentVersion);
    }

    /**
     * 开始下载
     */
    public static boolean startDownloadApk(Context context, ApkDownloaderManager.ApkDownloadListener listener) {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {//升级
            ApkDownloaderManager mFileDownloaderManager = new ApkDownloaderManager(context,listener);
            mFileDownloaderManager.downloadFile(Preference.getServerVersionUrl(),
                    DiskStorageManager.getInstance().getApkFileStoragePath());
            return true;
        }
        return false;
    }

}
