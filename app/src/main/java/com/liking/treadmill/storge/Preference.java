package com.liking.treadmill.storge;

import android.content.SharedPreferences;

import com.aaron.android.codelibrary.utils.SecurityUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.library.storage.AbsPreference;
import com.aaron.android.framework.utils.DeviceUtils;
import com.google.gson.Gson;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.socket.result.MemberListResult;

import java.util.Date;
import java.util.List;

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
    public static final String QCODE_URL = "qcode_url";
    public static final String IS_FIRST_STARTING_UP = "isFirstStartingUp";
    public static final String STANDBY_TIME = "standbytime";//待机时间
    public static final String MOTION_PARAM_MAX_RUNTIME = "motionparammaxruntime";//运动最大时间
    public static final String TREADMILL_SETUP_ISVISITOR = "treadmillsetupvisitor";
    public static final String ADVERTISEMENT =  "advertisement";//广告
    public static final String LASTMEMBERID = "lastmemberid";//最后的成员ID
    public static final String APP_UPDATE_MD5 = "appupdatemd5";
    public static final String APP_UPDATE_APK_SIZE = "appupdateapksize";
    public static final String APP_UPDATE_APK_FAIL_COUNT = "appupdateapkfailcount";

    public static final String APP_SERVER_VERSION = "setserverversion";//服务器版本
    public static final String APP_SERVER_VERSION_URL = "setserverversionurl";//服务器版本地址
    public static final String USER_GYM_ID = "USER_GYM_ID";
    public static final String USER_GYM_NAME = "USER_GYM_NAME";
    public static final String MEMBER_ID_LIST = new Date().getTime() + DeviceUtils.getDeviceInfo(BaseApplication.getInstance()) + "";

    public static final String CURRENT_TIME_DIFF = "current_time_diff";

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

    /**
     * 保存二维码url
     *
     * @param qcode_url url
     * @return
     */
    public static boolean setQcodeUrl(String qcode_url) {
        return setObject(QCODE_URL, qcode_url);
    }

    /**
     * 获取二维码url
     *
     * @return
     */
    public static String getQCodeUrl() {
        String qCodeUrl = (String) getObject(QCODE_URL, "");
        return qCodeUrl;
    }

    /**
     * 服务器版本号
     * @param sversion
     * @return
     */
    public static boolean setServerVersion(String sversion) {
        return setObject(APP_SERVER_VERSION, sversion);
    }

    public static String getServerVersion() {
        return (String) getObject(APP_SERVER_VERSION, "");
    }

    /**
     * 是否下载apk
     * @return
     */
    public static boolean getDownloadAppFile() {
        return (boolean) getObject(DOWNLOAD_APP_FILE, false);
    }

    public static boolean setDownloadAppFile(boolean isDownload) {
        return setObject(DOWNLOAD_APP_FILE, isDownload);
    }

    /**
     * 服务器版本apk下载地址
     * @return
     */
    public static boolean setServerVersionUrl(String sversionurl) {
        return setObject(APP_SERVER_VERSION_URL, sversionurl);
    }

    public static String getServerVersionUrl() {
        return (String) getObject(APP_SERVER_VERSION_URL, "");
    }

    /**
     * 是否第一次开机
     * @param isStartUp
     * @return
     */
    public static boolean setIsStartingUp(boolean isStartUp) {
        return setObject(IS_FIRST_STARTING_UP, isStartUp);
    }

    public static boolean getIsStartingUp() {
        return (boolean) getObject(IS_FIRST_STARTING_UP, true);
    }

    /**
     * 用户所在场馆的gymId
     *
     * @param gymId 场馆id
     * @return
     */
    public static boolean setBindUserGymId(String gymId) {
        return setObject(USER_GYM_ID, gymId);
    }

    /**
     * 获取所在场馆的gymId
     *
     * @return gymId
     */
    public static String getBindUserGymId() {
        return (String) getObject(USER_GYM_ID, "");
    }

    /**
     * 用户所在场馆的gymName
     *
     * @param gymName 场馆NAME
     * @return
     */
    public static boolean setBindUserGymName(String gymName) {
        return setObject(USER_GYM_NAME, gymName);
    }

    /**
     * 获取所在场馆的gymName
     *
     * @return gymName
     */
    public static String getBindUserGymName() {
        return (String) getObject(USER_GYM_NAME, "");
    }

//    /**
//     * 保存当前场馆所有会员id
//     *
//     * @param memberList
//     * @return
//     */
//    public static boolean setMemberList(String memberList) {
//        return setObject(MEMBER_ID_LIST, memberList);
//    }
//
//    /**
//     * 获取当前场馆所有会员id
//     *
//     * @return
//     */
//    public static String getMemberList() {
//        String jsonString = (String) getObject(MEMBER_ID_LIST, "");
//        return jsonString;
//    }

    /**
     * 设置待机时间 s为单位
     * @param time
     * @return
     */
    public static boolean setStandbyTime(int time) {
        return setObject(STANDBY_TIME, time);
    }

    /**
     * 获取待机时间
     * @return
     */
    public static int getStandbyTime() {
        return (int) getObject(STANDBY_TIME, ThreadMillConstant.THREADMILL_DEFAULT_SYSTEM_STANBYTIME);
    }

    /**
     * 设置跑步的最长时间 min为单位
     * @param time
     * @return
     */
    public static boolean setMotionParamMaxRunTime(int time) {
        return setObject(MOTION_PARAM_MAX_RUNTIME, time);
    }

    /**
     * 获取跑步的最长时间
     */
    public static int getMotionParamMaxRunTime() {
        return (int) getObject(MOTION_PARAM_MAX_RUNTIME, ThreadMillConstant.THREADMILL_DEFAULT_RUNNING_TIME);
    }

    /**
     * 设置是否为访客模式
     * @param isVisitor
     * @return
     */
    public static boolean setIsVisitorMode(boolean isVisitor) {
        return setObject(TREADMILL_SETUP_ISVISITOR, isVisitor);
    }

    /**
     * 是否为访客模式
     * @return
     */
    public static boolean isVisitorMode() {
        return (boolean) getObject(TREADMILL_SETUP_ISVISITOR, false);
    }

    /**
     * 解绑后重置操作
     */
    public static void setUnBindRest () {
        Preference.setIsStartingUp(true);
        Preference.setBindUserGymId("");
        Preference.setBindUserGymName("");
//        Preference.setMemberList("");
        Preference.setLastMemberId("0");
        Preference.setIsVisitorMode(false);
        Preference.setStandbyTime(ThreadMillConstant.THREADMILL_DEFAULT_SYSTEM_STANBYTIME);
        Preference.setMotionParamMaxRunTime(ThreadMillConstant.THREADMILL_DEFAULT_RUNNING_TIME);
    }

    /**
     * 保存广告数据
     * @param advs
     */
    public static boolean setAdvertisementResource(String advs) {
        return setObject(ADVERTISEMENT, advs);
    }

    /**
     * 获取广告
     * @param
     * @return
     */
    public static String getAdvertisementResource() {
        return (String) getObject(ADVERTISEMENT, "");
    }

    /**
     * 设置获取到的最后成员Id
     * @return
     */
    public static boolean setLastMemberId(String memberId) {
        return setObject(LASTMEMBERID, memberId);
    }

    /**
     * 获取最后的成员Id
     * @return
     */
    public static String getLastMemberId() {
        return (String) getObject(LASTMEMBERID, "0");
    }

    public static long getCurrentTimeDiff(){
        return Long.parseLong((String) getObject(CURRENT_TIME_DIFF, "0"));
    }

    public static void setCurrentTimeDiff(long diff){
        setObject(CURRENT_TIME_DIFF, String.valueOf(diff));
    }

    /**
     * 服务器最新 apk md5值
     * @param apkMd5
     */
    public static void setApkMd5(String apkMd5) {
        setObject(APP_UPDATE_MD5, apkMd5);
    }

    public static String getApkMd5(){
        return (String) getObject(APP_UPDATE_MD5,"");
    }

    /**
     * 服务器最新 apk 大小
     * @param apkSize
     */
    public static void setApkSize(String apkSize) {
        setObject(APP_UPDATE_APK_SIZE, apkSize);
    }

    public static long getApkSize() {
        return Long.parseLong((String) getObject(APP_UPDATE_APK_SIZE, "0"));
    }

    /**
     * 记录下载失败次数
     * @param failCount
     */
    public static void setAppDownloadFailCount(int failCount) {
        setObject(APP_UPDATE_APK_FAIL_COUNT, failCount);
    }

    public static int getAppDownloadFailCount() {
        return (int) getObject(APP_UPDATE_APK_FAIL_COUNT, 0);
    }

}
