package com.liking.treadmill.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.google.gson.Gson;
import com.liking.treadmill.message.AdvertisementMessage;
import com.liking.treadmill.socket.result.AdvertisementResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.utils.AlarmManagerUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 说明:
 * Author: shaozucheng
 * Time: 下午2:36
 */

public class AdvertisementReceiver extends BroadcastReceiver{

    public static final String ADVERTISEMENT_URL = "advertisement_url";

    public static final String ADVERTISEMENT_ENDTIME = "advertisement_endtime";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent != null) {
            String advUrl = intent.getStringExtra(ADVERTISEMENT_URL);
            if(!StringUtils.isEmpty(advUrl)) {
                Gson gson = new Gson();
                LogUtils.e("aaron", "AdvertisementReceiver 闹铃结束:" + advUrl);
                String advJson = Preference.getAdvertisementResource();
                if (!StringUtils.isEmpty(advJson)) {
                    AdvertisementResult advertisementResult = gson.fromJson(advJson, AdvertisementResult.class);
                    AdvertisementResult.AdvUrlResource advUrlResource = advertisementResult.getUrlResources();
                    if(advUrlResource != null) {
                        List<AdvertisementResult.AdvUrlResource.Resource> resources = advUrlResource.getResources();
                        for (AdvertisementResult.AdvUrlResource.Resource resource:resources) {
                            if(advUrl.equals(resource.getUrl())) {
                                LogUtils.e("aaron", "AdvertisementReceiver 关闭广告:" + advUrl);
                                resource.setOpen(false);
                            }
                        }
                        //刷新广告显示
                        Preference.setAdvertisementResource(gson.toJson(advertisementResult));
                        EventBus.getDefault().post(new AdvertisementMessage(resources));
                    }
                }
            }
            //取消这次闹铃
            AlarmManagerUtils.removeAdvertisementAlarm(context, intent);
        }

    }
}
