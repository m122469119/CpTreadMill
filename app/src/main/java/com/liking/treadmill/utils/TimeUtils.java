package com.liking.treadmill.utils;

import com.liking.treadmill.storge.Preference;

/**
 * Created on 2017/4/6
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class TimeUtils {
    public static long currentTime(){
        return System.currentTimeMillis() / 1000 - Preference.getCurrentTimeDiff();
    }

    public static void setCurrentTimeDiff(long diff){
        Preference.setCurrentTimeDiff(diff);
    }
}
