package com.liking.treadmill.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.aaron.android.codelibrary.utils.LogUtils;


/**
 * 说明:
 * Author: shaozucheng
 * Time: 上午11:43
 */

public class AlarmManagerUtils {

    public static final String REQUESTCODE = "requestcode";
    /**
     * 添加一个广告显示闹铃
     * @param context
     * @param intent
     * @param time
     */
    public static void addAdvertisementAlarm(Context context, Intent intent, long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        LogUtils.d("aaron", "addAdvertisementAlarm ---- requestcode: " + intent.getIntExtra(REQUESTCODE, 0) + ";;;time :" + time);
        PendingIntent pendingTiming = PendingIntent.getBroadcast(context, intent.getIntExtra(REQUESTCODE, 0), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            LogUtils.d("aaron", "addAdvertisementAlarm ----  alarmManager.setExact ");
            alarmManager.setExact(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + time,
                    pendingTiming);
        } else {
            alarmManager.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + time,
                    pendingTiming);
        }
    }

    public static void removeAdvertisementAlarm(Context context, Intent intent) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendIntent = PendingIntent.getBroadcast(context, intent.getIntExtra(REQUESTCODE, 0), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 与intent匹配（filterEquals(intent)）的闹钟会被取消
        alarmMgr.cancel(pendIntent);
    }
}
