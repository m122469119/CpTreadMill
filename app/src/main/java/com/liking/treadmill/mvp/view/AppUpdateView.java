package com.liking.treadmill.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午11:58
 */

public interface AppUpdateView extends BaseNetworkLoadView {
    /**
     * 开始更新
     */
    void startUpdateView();

    /**
     * 进度更新
     * @param percent
     */
    void updateProgressView(float percent);

    /**
     * 下载完成
     */
    void updateDownloadCompleteView();

    /**
     * 下载失败
     */
    void updateFailView();

    /**
     * 推出更新
     */
    void exitUpdateView();
}
