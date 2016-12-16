package com.aaron.android.framework.library.storage;

import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.DiskStorageUtils;

import java.io.File;

/**
 * Created on 15/9/11.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class DiskStorageManager {
    protected static final String TAG = "DiskStorageManager";
    private String mAppStoragePath; //应用目录
    private String mImageStoragePath; //图片缓存目录
    private String mFileStoragePath;//file的文件总目录
    private String mApkFileStoragePath;//file的文件总目录
    private static final String PATH_STORAGE_IMAGE_CACHE = "/image/";
    private static final String PATH_STORAGE_FILE = "/file/";
    private static final String PATH_STORAGE_FILE_APK = "apk/";

    /**
     * 使用静态内部类来生成DiskStorageManager单例,由jvm来保证线程的安全性
     */
    private static class DemoDiskStorageManagerHolder {
        private static DiskStorageManager sDiskStorageManager = new DiskStorageManager();
    }

    /**
     * 单例
     *
     * @return DiskStorageManager
     */
    public static DiskStorageManager getInstance() {
        return DemoDiskStorageManagerHolder.sDiskStorageManager;
    }

    /**
     * 磁盘存储初始化操作，主要是用来创建文件目录
     */
    public void init(String folderName) {
        mAppStoragePath = getDeviceRootPath() + File.separator + folderName;
        mImageStoragePath = mAppStoragePath + PATH_STORAGE_IMAGE_CACHE;
        mFileStoragePath = mAppStoragePath + PATH_STORAGE_FILE;
        mApkFileStoragePath = mFileStoragePath + PATH_STORAGE_FILE_APK;
        createAppFolder(mAppStoragePath);
        createAppFolder(mImageStoragePath);
        createAppFolder(mFileStoragePath);
        createAppFolder(mApkFileStoragePath);
    }

    public String getApkFileStoragePath() {
        return mApkFileStoragePath;
    }

    /**
     * 获取图片存放目录
     *
     * @return
     */
    public String getImagePath() {
        return mImageStoragePath;
    }

    public String getAppPath() {
        return mAppStoragePath;
    }

    /**
     * 获取file文件总目录
     *
     * @return
     */
    public String getFilePath() {
        return mFileStoragePath;
    }


    /**
     * 创建目录
     *
     * @param path 目录路径
     */
    private static void createAppFolder(String path) {
        File file = FileUtils.createFolder(path);
        if (file != null) {
            LogUtils.i(TAG, "root path: " + file.getPath());
        } else {
            LogUtils.i(TAG, "create root path fail");
        }
    }

    /**
     * @return 返回应用存储根目录 sdcard存在的情况下返回sdcard目录，如果不存在返回手机本地data目录
     */
    private static String getDeviceRootPath() {
        String path = DiskStorageUtils.getSdcardDirectoryPath();
        if (path == null) {
            path = DiskStorageUtils.getInternalDataStoragePath();
        }
        LogUtils.i(TAG, "device root path: " + path);
        return path;
    }

}
