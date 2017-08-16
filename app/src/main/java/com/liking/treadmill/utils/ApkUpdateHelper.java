package com.liking.treadmill.utils;

import android.content.Context;

import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.liking.treadmill.apkupdate.ApkDownloaderTask;
import com.liking.treadmill.storge.Preference;

import java.util.ArrayList;

/**
 * 说明:APK更新工具类
 * Author: chenlei
 * Time: 下午3:45
 */

public class ApkUpdateHelper {

//    private static final String downloadPath = "/mnt/internal_sd/Download/";
    private static final String TAG = "ApkUpdateUtils";

    /**
     * 检测跑步机状态,使用中退出更新 true使用中, false 待机
     */
    public boolean checkTreadMillState() {
        return false;
    }

    /**
     * 是否需要更新
     * @return
     */
    public static boolean isApkUpdate() {
        String serverVersion = Preference.getServerVersion();//服务器当前版本
        String currentVersion = EnvironmentUtils.Config.getAppVersionName();
        return checkVersion(serverVersion, currentVersion);
    }

    /**
     * 开启下载
     */
    public static boolean startDownloadApk(Context context, ApkDownloaderTask.ApkDownloadListener listener) {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {//升级
            ApkDownloaderTask mFileDownloaderManager = new ApkDownloaderTask(context,listener);
            mFileDownloaderManager.downloadFile(
                    Preference.getServerVersionUrl(),
                    DiskStorageManager.getInstance().getApkFileStoragePath());
            return true;
        }
        return false;
    }

    private static boolean checkVersion(String serverVersion, String currentVersion) {

        LogUtils.i(TAG, "lastappVersion== " + serverVersion + "currentVersion == " + currentVersion);
        if (!StringUtils.isEmpty(serverVersion)) {
            String lastversion[] = serverVersion.split("\\.");
            String currentversion[] = currentVersion.split("\\.");

            //将数组转为list集合
            ArrayList<String> lastVersionList = new ArrayList<>();
            for(int i=0 ;i<lastversion.length;i++){
                lastVersionList.add(lastversion[i]);
            }

            ArrayList<String> currentVersionList = new ArrayList<>();
            for(int i=0;i<currentversion.length;i++){
                currentVersionList.add(currentversion[i]);
            }

            int length = currentVersionList.size() - lastVersionList.size();
            if (length > 0) {
                lastVersionList.add("0");
            } else if (length < 0) {
                currentVersionList.add("0");
            }
            for (int i = 0; i < currentVersionList.size(); i++) {
                int number = Integer.parseInt(lastVersionList.get(i)) - Integer.parseInt(currentVersionList.get(i));
                if(number == 0) {
                    continue;
                }
                if (number > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean update(String apkFilePath) {
        if(isApkUpdate() && FileUtils.fileExists(apkFilePath)) {
            String md5 = Preference.getApkMd5();
            long size = Preference.getApkSize();
            try {
                if(md5.equals(ApkFileSVUtils.getMD5Checksum(apkFilePath)) && size == ApkFileSVUtils.getFileSize(apkFilePath)) {
                    //只要成功安装之后一定会重启app，这边无需返回true
                    ApkController.execCommand("pm","install","-r", apkFilePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}