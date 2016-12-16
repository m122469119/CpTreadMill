package com.liking.treadmill.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.liking.treadmill.service.ApkDownloadService;
import com.liking.treadmill.storge.Preference;

/**
 *
 * @author chenlei
 */
public class ApkDownloaderManager {

    private DownloadNewApkBroadcast mDownloadNewApkBroadcast;
    private Context mContext;

    private ApkDownloadListener mDownloadListener = null;

    public ApkDownloaderManager(Context context, ApkDownloadListener listener) {
        mContext = context;
        mDownloadListener = listener;
    }

    public void downloadFile(String downloadUrl, String downloadPath) {
        LogUtils.d("socket", "FileDownloaderManager");
        if(!Preference.getDownloadAppFile()){
            registerDownloadNewApkBroadcast();
            startDownloadApk(downloadUrl, downloadPath);
        }
    }

    public void setDownloadListener(ApkDownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    private void startDownloadApk(String downloadUrl, String downloadPath) {
        Intent intent = new Intent(mContext, ApkDownloadService.class);
        intent.putExtra(ApkDownloadService.EXTRA_DOWNLOAD_URL, downloadUrl);
        intent.putExtra(ApkDownloadService.EXTRA_DOWNLOAD_PATH, downloadPath);
        mContext.startService(intent);
    }

    public class DownloadNewApkBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.isEmpty(action)) {
                return;
            }
            if (mDownloadListener != null) {
                if (action.equals(ApkDownloadService.ACTION_DOWNLOADING)) {
                    int progress = intent.getIntExtra(ApkDownloadService.EXTRA_PROGRESS, 0);
                    mDownloadListener.onDownloading(progress);
                } else if (action.equals(ApkDownloadService.ACTION_START_DOWNLOAD)) {
//                    mDownloadApkProgressBar.setMax(intent.getIntExtra(FileDownloadService.EXTRA_APK_LENGTH, 0));
                    mDownloadListener.onStartDownload(intent.getIntExtra(ApkDownloadService.EXTRA_APK_LENGTH, 0));
                } else if (action.equals(ApkDownloadService.ACTION_DOWNLOAD_COMPLETE)) {
                    mDownloadListener.onDownloadComplete();
                    unregisterDownloadNewApkBroadcast();
                    if(!ApkController.install(intent.getStringExtra(ApkDownloadService.EXTRA_INSTALL_APK_PATH), mContext)){
                        mDownloadListener.ononDownloadFail();
                    }
                } else if(action.equals(ApkDownloadService.ACTION_DOWNLOAD_FAIL)) {
                    mDownloadListener.ononDownloadFail();
                    unregisterDownloadNewApkBroadcast();
                }
            }
        }
    }

    private void registerDownloadNewApkBroadcast() {
        if (mDownloadNewApkBroadcast == null) {
            mDownloadNewApkBroadcast = new DownloadNewApkBroadcast();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ApkDownloadService.ACTION_DOWNLOADING);
        intentFilter.addAction(ApkDownloadService.ACTION_START_DOWNLOAD);
        intentFilter.addAction(ApkDownloadService.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(ApkDownloadService.ACTION_DOWNLOAD_FAIL);
        mContext.registerReceiver(mDownloadNewApkBroadcast, intentFilter);
    }

    public void unregisterDownloadNewApkBroadcast() {
        if (mDownloadNewApkBroadcast != null) {
            mContext.unregisterReceiver(mDownloadNewApkBroadcast);
            mDownloadNewApkBroadcast = null;
        }
    }

    public interface ApkDownloadListener {
        void onStartDownload(int apklength);
        void onDownloading(int progress);
        void onDownloadComplete();
        void ononDownloadFail();
    }
}
