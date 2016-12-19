package com.liking.treadmill.socket;

import android.content.Context;
import android.content.Intent;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.SecurityUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.utils.DeviceUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.google.gson.Gson;
import com.liking.treadmill.activity.AwaitActionActivity;
import com.liking.treadmill.message.UpdateAppMessage;
import com.liking.treadmill.socket.result.ApkUpdateResult;
import com.liking.treadmill.socket.result.BaseSocketResult;
import com.liking.treadmill.socket.result.BindUserResult;
import com.liking.treadmill.socket.result.MemberListResult;
import com.liking.treadmill.socket.result.QrcodeResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.ApkUpdateUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class SocketHelper {

    private static final String TYPE_QRCODE_SHOW = "qcode";
    private static final String TYPE_UPDATE_NOTIFY = "update";
    private static final String TYPE_BIND = "bind";
    private static final String TYPE_MEMBER_LIST = "member_list";

    private static final String mTcpVersion = "v1.0";

    public static final String HEART_BEAT_STRING = "{\"type\":\"ping\",\"version\":\"" + mTcpVersion + "\",\"data\":{}, \"msg_id\":0}\\r\\n";//心跳包内容
    public static final String HEART_BEAT_PONG_STRING = "{\"type\":\"pong\",\"data\":{}, \"msg_id\":0}";//心跳包内容
    public static final String REBIND_STRING = "{\"type\":\"bind\",\"version\":" + mTcpVersion + ",\"data\":{}}";//心跳包内容
    public static final String CONFIRM_STRING = "{\"type\":\"confirm\",\"data\":{}}";//心跳包内容

    public static void handlerSocketReceive(Context context, String jsonStr) {
        Gson gson = new Gson();
        String jsonText = jsonStr.substring(0, jsonStr.length() - 4);
        LogUtils.d("aaron", "result: " + jsonText);
        BaseSocketResult result = gson.fromJson(jsonText, BaseSocketResult.class);
        String type = result.getType();
        LogUtils.d("aaron", "result: " + result.getType());
        if (TYPE_QRCODE_SHOW.equals(type)) {//二维码展示消息
            QrcodeResult qrcodeResult = gson.fromJson(jsonText, QrcodeResult.class);
            String codeUrl = qrcodeResult.getQrcodeData().getCodeUrl();
            LogUtils.d("codeUrl === ", codeUrl);
            boolean codeUrlIsSucces = Preference.setQcodeUrl(codeUrl);
            if (codeUrlIsSucces) {
                LogUtils.d("aaron", "true");
            } else {
                LogUtils.d("aaron", "false");
            }
        } else if (TYPE_UPDATE_NOTIFY.equals(type)) { //系统升级通知
            ApkUpdateResult updateResult = gson.fromJson(jsonText, ApkUpdateResult.class);
            ApkUpdateResult.ApkUpdateData updateData = updateResult.getApkUpdateData();
            if (updateData != null) {
                if (!StringUtils.isEmpty(updateData.getVersion())) {
                    Preference.setServerVersion(updateData.getVersion());
                }
                if (!StringUtils.isEmpty(updateData.getUrl())) {
                    Preference.setServerVersionUrl(updateData.getUrl());
                }
            }
            LogUtils.d(SocketService.TAG, "send updateMessage");
            if (ApkUpdateUtils.isApkUpdate() && !SerialPortUtil.getTreadInstance().isRunning()) {//需要更新并且跑步机没有运行
                EventBus.getDefault().post(new UpdateAppMessage());
            }
        } else if (TYPE_BIND.equals(type)) {//绑定用户
            BindUserResult bindUserResult = gson.fromJson(jsonText, BindUserResult.class);
            BindUserResult.BindUserData bindUserData = bindUserResult.getData();
            if (bindUserData != null) {
                if (bindUserData.getErrCode() == 0) {
                    Preference.setBindUserGymId(bindUserData.getGymId());
                    Intent intent = new Intent(context, AwaitActionActivity.class);
                    context.startActivity(intent);
                }
            }
        } else if (TYPE_MEMBER_LIST.equals(type)) {//当前用户所在场馆的所有会员id
            MemberListResult memberListResult = gson.fromJson(jsonText, MemberListResult.class);
            MemberListResult.MemberData memberData = memberListResult.getData();
            if (memberData != null) {
                List<String> memberListId = memberData.getBraceletId();
                Preference.setMemberList(memberListId);
            }
        }
    }

    public static String loginString() {
        return "{\"type\":\"qcode\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\"0\",\"data\":{\"device_id\":\"" +
                SecurityUtils.MD5.get16MD5String(DeviceUtils.getDeviceInfo(BaseApplication.getInstance())) + "\"}}\\r\\n";
    }

    public static String initString() {
        return "{\"type\":\"init\",\"data\":{\"token\":\"" + Preference.getToken() + "\"}}\\r\\n";
    }

    public static String reportDevicesString() {
        return "{\"type\":\"treadmill\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\"0\",\"data\":{\"device_id\":\"" +
                SecurityUtils.MD5.get16MD5String(DeviceUtils.getDeviceInfo(BaseApplication.getInstance())) + "\"," +
                "\"gym_id\":\"0\",\"mac\":\"" + EnvironmentUtils.Network.wifiMac() + "\",\"app_version\":\"" + EnvironmentUtils.Config.getAppVersionName() + "\"," +
                "\"total_distance\":\"0\",\"total_time\":\"0\",\"power_times\":\"0\"}}\\r\\n";
    }

}
