package com.liking.treadmill.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.liking.treadmill.mvp.view.AppUpdateView;
import com.liking.treadmill.utils.ApkUpdateHelper;
import com.liking.treadmill.apkupdate.ApkDownloaderTask;

/**
 * 说明:更新
 * Author: chenlei
 * Time: 上午11:59
 */

public class AppUpdatePresenter extends BasePresenter<AppUpdateView> {

    private int apkSize = 0;

    public AppUpdatePresenter(Context context, AppUpdateView mainView) {
        super(context, mainView);
    }

    /**
     * 检查更新是否需要更新,
     * @return
     */
    public boolean checkAppVersion() {
        return ApkUpdateHelper.isApkUpdate();
    }

    /**
     * 启动服务下载
     */
    public void startDownloadApk() {
        if (!ApkUpdateHelper.startDownloadApk(mContext, mApkDownloadListener)) {
            if(mView != null) {
                mView.updateFailView();
            }
        }
    }

    /**
     * 下载监听
     */

    private ApkDownloaderTask.ApkDownloadListener mApkDownloadListener = new ApkDownloaderTask.ApkDownloadListener() {
        @Override
        public void onStartDownload(int apklength) {
            if(mView != null) {
                Log.e("info", "apklength :"+apklength);
                apkSize = apklength;
                mView.startUpdateView();
            }
        }

        @Override
        public void onDownloading(int progress) {
            if(mView != null) {
                int percent = progress * 100 / apkSize;
                mView.updateProgressView(percent);
            }
        }

        @Override
        public void onDownloadComplete() {
            if(mView != null) {
                mView.updateDownloadCompleteView();
            }
        }

        @Override
        public void onDownloadFail() {
            if(mView != null) {
                mView.updateFailView();
            }
        }
    };
}
