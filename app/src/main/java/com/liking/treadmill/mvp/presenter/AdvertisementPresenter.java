package com.liking.treadmill.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.library.downloadprovider.FileDownloadService;
import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.google.gson.Gson;
import com.liking.treadmill.message.AdvertisementMessage;
import com.liking.treadmill.receiver.AdvertisementReceiver;
import com.liking.treadmill.service.ApkDownloadService;
import com.liking.treadmill.socket.SocketHelper;
import com.liking.treadmill.socket.result.AdvertisementResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.utils.AlarmManagerUtils;

import java.io.File;
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
    public void advResDownload(Context context, String advUrl) {
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.putExtra(FileDownloadService.EXTRA_DOWNLOAD_URL, advUrl);
        intent.putExtra(FileDownloadService.EXTRA_DOWNLOAD_PATH, advPath);
        context.startService(intent);
    }

    public boolean loadAdvlocalRes() {

        return false;
    }

    public boolean loadAdvRemoteRes() {
        return false;
    }


    public void showAdvRes(Context context, boolean isCacheData, String res) {
        Gson gson = new Gson();

        try {
            AdvertisementResult advertisementResult = gson.fromJson(res, AdvertisementResult.class);
            AdvertisementResult.AdvUrlResource advUrlResource = advertisementResult.getUrlResources();
            if(advUrlResource != null) {
                int requestcode = 1000;
                long serviceTime = DateUtils.currentDataSeconds() + SocketHelper.sTimestampOffset;
                List<AdvertisementResult.AdvUrlResource.Resource> reslist = advUrlResource.getResources();
                LogUtils.d("aaron", "request: " + reslist.size());
                for (AdvertisementResult.AdvUrlResource.Resource resource: reslist) {
                    LogUtils.d("aaron", "request: " + resource.getUrl());
                    long advTime = DateUtils.parseString("yyyyMMdd", resource.getEndtime()).getTime();
                    long timeOffset = advTime - serviceTime * 1000;

                    Intent intent = new Intent(context, AdvertisementReceiver.class);
                    intent.putExtra(AdvertisementReceiver.ADVERTISEMENT_URL, resource.getUrl());
                    intent.putExtra(AdvertisementReceiver.ADVERTISEMENT_ENDTIME, resource.getEndtime());
                    intent.putExtra(AlarmManagerUtils.REQUESTCODE, requestcode);

                    if(timeOffset > 0) {
                        if(!isCacheData) {
                            String downPath = getDownloadFilePath(advPath, resource.getUrl());
                            resource.setLocalPath(downPath);
                            advResDownload(context,resource.getUrl());
                        }
                        AlarmManagerUtils.addAdvertisementAlarm(context.getApplicationContext(), intent, timeOffset);
                    } else {
                        resource.setOpen(false);
                        AlarmManagerUtils.removeAdvertisementAlarm(context.getApplicationContext(), intent);
                    }
                    requestcode = requestcode + 1;
                }
                //save Preference
                Preference.setAdvertisementResource(gson.toJson(advertisementResult));
                EventBus.getDefault().post(new AdvertisementMessage(reslist));
            }
        }catch (Exception e) {}
    }

    private String getDownloadFilePath(String directoryPath, String url) {
        if (StringUtils.isEmpty(directoryPath)) {
            directoryPath = FileUtils.createFolder(getDiskCacheDir()).getPath();
        }
        return directoryPath + File.separator + FileUtils.getFileName(url);
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
}
