package com.liking.treadmill.utils;

import java.lang.reflect.Method;

public class SystemPropertiesInvoke {
    private static final String TAG = "SystemPropertiesInvoke";

    public static void setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}