package com.aaron.android.framework.library.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.utils.EnvironmentUtils;

/**
 * Created on 15/7/23.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public abstract class AbsPreference {
    private static SharedPreferences sPreferences;

    /**
     * 初始化SharedPreferences
     */
    protected static SharedPreferences getInstance() {
        if (sPreferences == null) {
            sPreferences = BaseApplication.getInstance().getSharedPreferences(EnvironmentUtils.Config.getAppFlag(), Context.MODE_PRIVATE);
        }
        return sPreferences;
    }

    /**
     * 清空SharedPreferences
     */
    public static void clear() {
        getInstance().edit().clear();
    }

    /**
     * 根据Key移除SharedPreferences
     * @param key key
     */
    public static void remove(String key) {
        getInstance().edit().remove(key);
    }
}
