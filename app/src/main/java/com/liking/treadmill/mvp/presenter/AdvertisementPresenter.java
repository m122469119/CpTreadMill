package com.liking.treadmill.mvp.presenter;

import android.content.Context;
import android.content.Intent;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.library.downloadprovider.FileDownloadService;
import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.liking.treadmill.message.AdvertisementMessage;
import com.liking.treadmill.receiver.AdvertisementReceiver;
import com.liking.treadmill.service.ApkDownloadService;
import com.liking.treadmill.socket.SocketHelper;
import com.liking.treadmill.socket.result.AdvertisementResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.utils.AlarmManagerUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午10:21
 */

public class AdvertisementPresenter {

    private String advPath = DiskStorageManager.getInstance().getFilePath();

    /**
     * 下载广告资源
     */
    public void advertisementResDownload(Context context, String advUrl) {
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.putExtra(ApkDownloadService.EXTRA_DOWNLOAD_URL, advUrl);
        intent.putExtra(ApkDownloadService.EXTRA_DOWNLOAD_PATH, advPath);
        context.startService(intent);
    }

    public boolean loadAdvertisementlocalRes() {
        return false;
    }

    public boolean loadAdvertisementRemoteRes() {
        return false;
    }


    public void loadAdvertisementRes(Context context, List<AdvertisementResult.AdvUrlResource.Resource> resources) {
    }
}
