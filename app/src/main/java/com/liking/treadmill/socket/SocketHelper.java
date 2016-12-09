package com.liking.treadmill.socket;

import android.content.Context;

import com.aaron.android.codelibrary.utils.SecurityUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.utils.DeviceUtils;
import com.google.gson.Gson;
import com.liking.treadmill.storge.Preference;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class SocketHelper {

    private static final String TYPE_HEART_BEAT = "replay";
    private static final String TYPE_QRCODE_SHOW = "qrcode_show";
    private static final String TYPE_CHECK_CONFIRM = "check_confim";
    private static final String TYPE_VEDIO_PLAY_WARN = "vedio_play_warn";
    private static final String TYPE_VEDIO_PLAY = "vedio_begin_play";
    private static final String TYPE_ENTER_WARN = "enter_warn";
    private static final String TYPE_SETCONFIG = "setconfig";
    private static final String UPDATE_NOTIFY = "update_notify";
    private static final String mTcpVersion = "v1.0";

    public static final String HEART_BEAT_STRING = "{\"type\":\"ping\",\"version\":\"" + mTcpVersion + "\",\"data\":{}, \"msg_id\":0}\\r\\n";//心跳包内容
    public static final String HEART_BEAT_PONG_STRING = "{\"type\":\"pong\",\"data\":{}, \"msg_id\":0}";//心跳包内容
    public static final String REBIND_STRING = "{\"type\":\"bind\",\"version\":" + mTcpVersion + ",\"data\":{}}";//心跳包内容
    public static final String CONFIRM_STRING = "{\"type\":\"confirm\",\"data\":{}}";//心跳包内容

    public static void handlerSocketReceive(Context context, String jsonStr) {
        Gson gson = new Gson();
//        BaseSocketResult result = gson.fromJson(jsonStr, BaseSocketResult.class);
//        String type = result.getType();
//        LogUtils.d("aaron", "result: " + result.getType());
//        if (TYPE_QRCODE_SHOW.equals(type)) {//二维码展示消息
//
//        } else if (TYPE_CHECK_CONFIRM.equals(type)) {//验证消息
//
//        } else if (TYPE_VEDIO_PLAY_WARN.equals(type)) {//视频播放前提醒消息
//
//        } else if (TYPE_VEDIO_PLAY.equals(type)) {//视频播开始播放提醒
//
//        } else if (TYPE_ENTER_WARN.equals(type)) {//会员进入提醒
//
//        } else if (TYPE_SETCONFIG.equals(type)) {//配置数据推送
//
//        } else if (UPDATE_NOTIFY.equals(type)) {//系统升级通知
//
//        }
    }

    public static String loginString() {
        return "{\"type\":\"login\",\"data\":{\"device_id\":\"" +
                SecurityUtils.MD5.get16MD5String(DeviceUtils.getDeviceInfo(BaseApplication.getInstance())) + "\"}}";
    }

    public static String initString() {
        return "{\"type\":\"init\",\"data\":{\"token\":\"" + Preference.getToken() + "\"}}";
    }

}
