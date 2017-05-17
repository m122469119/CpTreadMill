package com.alibaba.sdk.android.oss.sample;

import android.content.Context;
import android.os.Environment;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.liking.treadmill.app.LikingThreadMillApplication;
import com.liking.treadmill.utils.TimeUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created on 2017/5/3
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class FileHelper {

    private static final String TAG = "FileHelper";

    public static final String ACTION_DOWNLOADING = "download_progress";
    public static final String ACTION_START_DOWNLOAD = "download_start";
    public static final String ACTION_DOWNLOAD_COMPLETE = "downloadcomplete";
    public static final String EXTRA_INSTALL_APK_PATH = "apkpath";
    public static final String EXTRA_DOWNLOAD_URL = "downloadUrl";
    public static final String EXTRA_DOWNLOAD_PATH = "downloadPath";
    public static final String EXTRA_PROGRESS = "downloadprogress";
    public static final String EXTRA_APK_LENGTH = "apkLength";
    public static final String EXTRA_APK_MD5 = "apkmd5";

    private final Object LOCK = new Object();

    private String mFirDir;
    private Context mContext;

    public FileHelper(Context context, String fileDir) {
        this.mContext = context;
        mFirDir = fileDir;
    }


    public void save(String log) {
        synchronized (LOCK) {
            File file = new File(getFilePath());
            FileWriter writer = null;
            BufferedWriter bw = null;

            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                writer = new FileWriter(file, true);
                bw = new BufferedWriter(writer);
                bw.write(log);
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null)
                        writer.close();
                    if (bw != null) {
                        bw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public void deleteFile(File file) {
        synchronized (LOCK) {
            if (!file.exists()) {
                return;
            }
            file.delete();
        }
    }


    public void deleteFile(String path) {
        synchronized (LOCK) {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            file.delete();
        }
    }


    public File[] getAllFileName() {
        File file = new File(getDefaultFile());
        return file.listFiles();
    }


    public String getDefaultFile() {
        return mContext.getFilesDir().getAbsolutePath() + File.separator + mFirDir;
    }


    public String getFilePath() {
        String time = DateUtils.formatDate("yyyy.MM.dd", new Date(TimeUtils.currentTime() * 1000));
        return getDefaultFile() + "/" + mFirDir + "-" + time + "-" + LikingThreadMillApplication.ANDROID_ID + ".log";
    }




    private String getDownloadFileName(String mDirectoryPath) {
        if (StringUtils.isEmpty(mDirectoryPath)) {
            mDirectoryPath = FileUtils.createFolder(getDiskCacheDir()).getPath();
        }
        return mDirectoryPath + File.separator + "newApk.apk";
    }

    private String getDiskCacheDir() {
        String cachePath;
        Context context = BaseApplication.getInstance();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


    public String downloadFile(InputStream inputStream) {
        FileOutputStream outputStream = null;
        String mTempFilePath;
        String mFilePath;
        mFilePath = getDownloadFileName("");
        mTempFilePath = mFilePath + ".tmp";
        try {
            outputStream = new FileOutputStream(mTempFilePath);
            byte[] buffer = new byte[1024 * 8];
            int temp;
            while ((temp = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, temp);
            }
            FileUtils.rename(mTempFilePath, mFilePath);
            return mFilePath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
