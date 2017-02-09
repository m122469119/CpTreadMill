package com.liking.treadmill.utils;

import android.content.Context;
import android.content.Intent;

import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.liking.treadmill.service.ApkDownloadService;

/**
 * 说明:
 * Author: shaozucheng
 * Time: 上午10:21
 */

public class AdvertisementUtils {

    private String advPath = DiskStorageManager.getInstance().getFilePath();

    /**
     * 下载广告资源
     */
    public void advertisementResDownload(Context context, String advUrl) {
        Intent intent = new Intent(context, ApkDownloadService.class);
        intent.putExtra(ApkDownloadService.EXTRA_DOWNLOAD_URL, advUrl);
        intent.putExtra(ApkDownloadService.EXTRA_DOWNLOAD_PATH, advPath);
        context.startService(intent);
    }
}
