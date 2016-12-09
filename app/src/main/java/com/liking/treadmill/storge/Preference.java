package com.liking.treadmill.storge;

import android.content.SharedPreferences;

import com.aaron.android.framework.library.storage.AbsPreference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/7 上午11:49
 */
public class Preference extends AbsPreference {
    private static final String TAG = "Preference";
    public static final String TOKEN = "token";
    public static final String NAME = "name";
    public static final String REGISTRATION_ID = "registration_id";
    public static final String BASE_CONFIG = "base_config";
    public static final String PATCH_DATA = "patchData";
    public static final String APP_UPDATE = "appUpdate";
    public static final String SCREEN_SAVE = "screenSaver";
    public static final String DOWNLOAD_APP_FILE = "downloadAppFile";

    /**
     * 清空SharedPreferences
     */
    public static void clear() {
        getInstance().edit().clear().commit();
    }

    /**
     * 根据Key移除SharedPreferences
     *
     * @param key key
     */
    public static void remove(String key) {
        getInstance().edit().remove(key).commit();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    Key
     * @param object 需要存的引用对象
     * @return 数据是否成功存储
     */
    private static boolean setObject(String key, Object object) {
        if (object == null) {
            return false;
        }
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = getInstance().edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        return editor.commit(); //为保证后面取值的正确性,这里使用同步存储(线程阻塞)commit方法
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           Key
     * @param defaultObject 默认值
     * @return 取值
     */
    private static Object getObject(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return getInstance().getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return getInstance().getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return getInstance().getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return getInstance().getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return getInstance().getLong(key, (Long) defaultObject);
        }

        return null;
    }


    /**
     * 获取token
     *
     * @return token
     */
    public static String getToken() {
        String token = (String) getObject(TOKEN, "");
        return token;
    }

    /**
     * 保存token
     *
     * @param token token
     */
    public static boolean setToken(String token) {
        return setObject(TOKEN, token);
    }
}
