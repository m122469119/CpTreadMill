package com.liking.treadmill.socket;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.utils.DeviceUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.google.gson.Gson;
import com.liking.treadmill.app.LikingThreadMillApplication;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.db.entity.Member;
import com.liking.treadmill.message.AdvertisementMessage;
import com.liking.treadmill.message.GymBindSuccessMessage;
import com.liking.treadmill.message.GymUnBindSuccessMessage;
import com.liking.treadmill.message.LoginUserInfoMessage;
import com.liking.treadmill.message.MemberListMessage;
import com.liking.treadmill.message.MemberNoneMessage;
import com.liking.treadmill.message.QrCodeMessage;
import com.liking.treadmill.message.RequestMembersMessage;
import com.liking.treadmill.message.UpdateAppMessage;
import com.liking.treadmill.receiver.AdvertisementReceiver;
import com.liking.treadmill.socket.result.AdvertisementResult;
import com.liking.treadmill.socket.result.ApkUpdateResult;
import com.liking.treadmill.socket.result.BaseSocketResult;
import com.liking.treadmill.socket.result.BindUserResult;
import com.liking.treadmill.socket.result.MemberListResult;
import com.liking.treadmill.socket.result.QrcodeResult;
import com.liking.treadmill.socket.result.ServiceTimeResult;
import com.liking.treadmill.socket.result.UserInfoResult;
import com.liking.treadmill.socket.result.UserLogOutResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.AlarmManagerUtils;
import com.liking.treadmill.utils.ApkUpdateUtils;
import com.liking.treadmill.utils.MemberUtils;

import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class SocketHelper {

    private static final String TYPE_QRCODE_BIND = "bind_qcode";
    private static final String TYPE_QRCODE_UNBIND = "unbind_qcode";
    private static final String TYPE_UPDATE_NOTIFY = "update";
    private static final String TYPE_BIND = "bind";
    private static final String TYPE_UNBIND = "unbind";
    private static final String TYPE_USERLOGIN = "login";
    private static final String TYPE_USERLOGIN_OUT = "logout";
    private static final String TYPE_MEMBER_LIST = "member_list";
    private static final String TYPE_EXERCISE_DATA = "data";
    private static final String TYPE_SERVICE_TIME = "time";
    private static final String TYPE_ADVERTISMENT = "advertisement";
    private static final String TYPE_REQUEST_MEMBER = "request_member";
    private static final String REPORT_LOG_CMD = "report_log";
    private static final String REPORT_CLEAR_MEMBER_LIST_CMD = "member_list_clear";

    private static final String mTcpVersion = "v1.2";

    public static final String HEART_BEAT_STRING = "{\"type\":\"ping\",\"version\":\"" + mTcpVersion + "\",\"data\":{}, \"msg_id\":\"\"}\\r\\n";//心跳包内容
    public static final String HEART_BEAT_PONG_STRING = "{\"type\":\"pong\",\"data\":{}, \"msg_id\":\"\"}";//心跳包内容
    public static final String REBIND_STRING = "{\"type\":\"bind\",\"version\":" + mTcpVersion + ",\"data\":{}}";//心跳包内容
    public static final String CONFIRM_STRING = "{\"type\":\"confirm\",\"data\":{}}";//心跳包内容

    public static long sTimestampOffset = 0;


    public static void handlerSocketReceive(Context context, String jsonStr) {

        Gson gson = new Gson();
        String jsonText = jsonStr;//jsonStr.substring(0, jsonStr.length() - 4);
        LogUtils.d("aaron", "result: " + jsonText);
        BaseSocketResult result = gson.fromJson(jsonText, BaseSocketResult.class);
        String type = result.getType();
        LogUtils.d("aaron", "result: " + result.getType());
        if (TYPE_QRCODE_BIND.equals(type) || TYPE_QRCODE_UNBIND.equals(type)) {//二维码展示消息
            QrcodeResult qrcodeResult = gson.fromJson(jsonText, QrcodeResult.class);
            String codeUrl = qrcodeResult.getQrcodeData().getCodeUrl();
            LogUtils.d("codeUrl === ", codeUrl);
            boolean codeUrlIsSucces = Preference.setQcodeUrl(codeUrl);
            if (codeUrlIsSucces) {
                LogUtils.d("aaron", "true");
            } else {
                LogUtils.d("aaron", "false");
            }
            EventBus.getDefault().post(new QrCodeMessage());
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
                if (!StringUtils.isEmpty(updateData.getMd5())) {
                    Preference.setApkMd5(updateData.getMd5());
                }
                if(!StringUtils.isEmpty(updateData.getSize())) {
                    Preference.setApkSize(updateData.getSize());
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
                String gymId = Preference.getBindUserGymId();
                if (bindUserData.getErrCode() == 0 && StringUtils.isEmpty(gymId) || bindUserData.getErrCode() == 0 && "0".equals(gymId)) {
                    Preference.setIsStartingUp(false);
                    Preference.setBindUserGymId(bindUserData.getGymId());
                    Preference.setBindUserGymName(bindUserData.getGymName());
                    LogUtils.d(SocketService.TAG, " gmyId =" + Preference.getBindUserGymId());
                    EventBus.getDefault().post(new GymBindSuccessMessage());
                }
            }
        } else if(TYPE_UNBIND.equals(type)) {//成功解绑场馆
            Preference.setUnBindRest();
            EventBus.getDefault().post(new GymUnBindSuccessMessage());
        } else if (TYPE_USERLOGIN.equals(type)) {//刷卡用户登录成功返回
            LoginUserInfoMessage loginUserInfoMessage = new LoginUserInfoMessage();
            UserInfoResult userInfoResult = gson.fromJson(jsonText, UserInfoResult.class);
            UserInfoResult.UserData userData = userInfoResult.getUserInfoData();
            if (userData != null) {
                loginUserInfoMessage.mUserData = userData;
                if(userData.getUserInfoData() != null) {
                    //缓存一条登录状态
                    String time = String.valueOf(DateUtils.currentDataSeconds());
                    String bid = userData.getUserInfoData().getBraceletId();
                    String msgId = bid + new Date().getTime();
                    String data_logout = "{\"type\":\"logout\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\""+ msgId + "\",\"data\":{\"bracelet_id\":" + bid + ",\"timestamp\":\"" + time + "\",\"device_id\":\"" + DeviceUtils.getDeviceInfo(BaseApplication.getInstance()) + "\",\"gym_id\":\"" + Preference.getBindUserGymId() + "\"}}\\r\\n";
                    FileUtils.store(data_logout, ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + bid);
                }
            }
            EventBus.getDefault().post(loginUserInfoMessage);

        } else if(TYPE_USERLOGIN_OUT.equals(type)) {

            UserLogOutResult logOutResult = gson.fromJson(jsonText, UserLogOutResult.class);
            if(logOutResult != null && logOutResult.getData() != null) {
                String bid = logOutResult.getData().getBraceletId();
                if(!StringUtils.isEmpty(bid)) {
                    FileUtils.delete(ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + bid);
                }
            }

        } else if (TYPE_EXERCISE_DATA.equals(type)) {//上传训练数据成功
            if(!StringUtils.isEmpty(result.getMsgId()) && !"0".equals(result.getMsgId())) {
                FileUtils.delete(ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE + result.getMsgId());
            }
        } else if(TYPE_SERVICE_TIME.equals(type)) {//服务器时间差
            LogUtils.d("aaron", "TYPE_SERVICE_TIME :" + jsonText);
            ServiceTimeResult timeResult = gson.fromJson(jsonText, ServiceTimeResult.class);
            ServiceTimeResult.ServiceTime serviceTime = timeResult.getServiceTime();
            if(serviceTime != null) {
                String time = serviceTime.getTime();
                LogUtils.d("aaron", "serviceTime :" + time);
                if(!StringUtils.isEmpty(time)) {
                    try {
                        long t = Long.parseLong(time);
                        LogUtils.d("aaron", "t:" + t);
                        setSystemTime(context, t);
                    }catch (Exception e) {
                        LogUtils.d("aaron", e.getMessage());
                    }
                }
            }
        } else if (TYPE_ADVERTISMENT.equals(type)) { //下发广告
            LogUtils.d("aaron", "TYPE_ADVERTISMENT :" + jsonText);
            AdvertisementResult advertisementResult = gson.fromJson(jsonText, AdvertisementResult.class);
            AdvertisementResult.AdvUrlResource advUrlResource = advertisementResult.getUrlResources();
            if(advUrlResource != null) {
                int requestcode = 1000;

                long serviceTime = DateUtils.currentDateMilliseconds() + sTimestampOffset;
                List<AdvertisementResult.AdvUrlResource.Resource> resources = advUrlResource.getResources();
                LogUtils.d("aaron", "request: " + resources.size());
                for (AdvertisementResult.AdvUrlResource.Resource resource:resources) {
                    LogUtils.d("aaron", "request: " + resource.getUrl());
                    long advTime = DateUtils.parseString("yyyyMMdd", resource.getEndtime()).getTime();
                    LogUtils.d("aaron", "request: advTime:" + advTime + "; serviceTime" + serviceTime);
                    long timeOffset = advTime - serviceTime ;

                    Intent intent = new Intent(context, AdvertisementReceiver.class);
                    intent.putExtra(AdvertisementReceiver.ADVERTISEMENT_URL, resource.getUrl());
                    intent.putExtra(AdvertisementReceiver.ADVERTISEMENT_ENDTIME, resource.getEndtime());
                    intent.putExtra(AlarmManagerUtils.REQUESTCODE, requestcode);

                    if(timeOffset > 0) {
                        AlarmManagerUtils.addAdvertisementAlarm(context.getApplicationContext(), intent, timeOffset);
                    } else {
                        resource.setOpen(false);
                        AlarmManagerUtils.removeAdvertisementAlarm(context.getApplicationContext(), intent);
                    }
                    requestcode = requestcode + 1;
                    LogUtils.d("aaron", "serviceTime:" +serviceTime + ";; Advertisement: " + resource.getUrl() + ";;time:" + resource.getEndtime() + ";;timeOffset" + timeOffset);
                }
                //save Preference
                Preference.setAdvertisementResource(gson.toJson(advertisementResult));
                EventBus.getDefault().post(new AdvertisementMessage(resources));
            }
        } else if (TYPE_REQUEST_MEMBER.equals(type)) { //服务端下发会员列表请求命令
            makeRequestMemberList();
        } else if (TYPE_MEMBER_LIST.equals(type)) {//当前场馆会员id、手环
            LogUtils.d("aaron", "TYPE_MEMBER_LIST :" + jsonText);
            MemberListResult memberListResult = gson.fromJson(jsonText, MemberListResult.class);
            MemberListResult.MembersData datas = memberListResult.getData();
            if(datas != null) {
                List<Member> members = datas.getMember();
                if(members != null && !members.isEmpty()) {
                    EventBus.getDefault().post(new MemberListMessage(members)); //下发的成员列表事件
                }
                if(datas.isNextPage()) {
                    makeRequestMemberList();
                } else {
                    EventBus.getDefault().post(new MemberNoneMessage());//下发结束 事件
                }
            }
        } else if (REPORT_LOG_CMD.equals(type)) {
            LikingThreadMillApplication.mLKAppSocketLogQueue.put("aaron","REPORT_LOG, 上报log操作");
            LikingThreadMillApplication.mLKSocketLogQueue.putOnce();
            LikingThreadMillApplication.mLKAppSocketLogQueue.putOnce();
        } else if(REPORT_CLEAR_MEMBER_LIST_CMD.equals(type)) {
            MemberUtils.getInstance().deleteMembersFromLocal();
        }
    }

    /**
     * 获取成员列表
     */
    public static void makeRequestMemberList() {
        String gymId = Preference.getBindUserGymId();
        if(!StringUtils.isEmpty(gymId) && !"0".equals(gymId)) {
            EventBus.getDefault().post(new RequestMembersMessage());
        }
    }

    public  static  void setSystemTime(Context context, long time) {
        AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setTimeZone("GMT+08:00");

        boolean ct = SystemClock.setCurrentTimeMillis(time * 1000);//系统同步服务器时间
        LogUtils.d("aaron", ct == true ? "同步成功":"同步失败");
    }
    /**
     * 场馆绑定
     * @return
     */
    public static String bind() {
        return "{\"type\":\"bind_qcode\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\"\",\"data\":{\"device_id\":\"" +
                DeviceUtils.getDeviceInfo(BaseApplication.getInstance()) + "\"}}\\r\\n";
    }

    /**
     * 场馆解绑
     * @return
     */
    public static String unBind() {
        return "{\"type\":\"unbind_qcode\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\"\",\"data\":{\"device_id\":\"" +
                DeviceUtils.getDeviceInfo(BaseApplication.getInstance()) + "\"}}\\r\\n";
    }

    public static String initString() {
        return "{\"type\":\"init\",\"data\":{\"token\":\"" + Preference.getToken() + "\"}}\\r\\n";
    }

    public static String reportDevicesString() {
        return "{\"type\":\"treadmill\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\"\",\"data\":{\"device_id\":\"" +
                DeviceUtils.getDeviceInfo(BaseApplication.getInstance()) + "\"," +
                "\"gym_id\":\""+Preference.getBindUserGymId()+"\",\"mac\":\"" + EnvironmentUtils.Network.wifiMac() + "\",\"app_version\":\"" + EnvironmentUtils.Config.getAppVersionName() + "\"," +
                "\"total_distance\":\"0\",\"total_time\":\"0\",\"power_times\":\"0\"}}\\r\\n";
    }

    /**
     * 用户刷卡登录
     *
     * @param cardno
     * @return
     */
    public static String userloginString(String cardno) {
        String time = String.valueOf(DateUtils.currentDataSeconds());
        String msgId = SerialPortUtil.getTreadInstance().getCardNo() + new Date().getTime();
        String data = "{\"type\":\"login\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\""+ msgId +"\",\"data\":{\"bracelet_id\":" + cardno + ",\"timestamp\":\"" + time + "\",\"device_id\":\"" + DeviceUtils.getDeviceInfo(BaseApplication.getInstance()) + "\",\"gym_id\":\"" + Preference.getBindUserGymId() + "\"}}\\r\\n";
        return data;
    }

    public static String userlogoutString(String cardno) {
        String time = String.valueOf(DateUtils.currentDataSeconds());
        String msgId = cardno + new Date().getTime();
        String data = "{\"type\":\"logout\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\""+ msgId + "\",\"data\":{\"bracelet_id\":" + cardno + ",\"timestamp\":\"" + time + "\",\"device_id\":\"" + DeviceUtils.getDeviceInfo(BaseApplication.getInstance()) + "\",\"gym_id\":\"" + Preference.getBindUserGymId() + "\"}}\\r\\n";

        String dataCache = FileUtils.load(ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + cardno);
        if(!StringUtils.isEmpty(dataCache)) {
            FileUtils.store(data, ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + cardno);
        }

        return data;
    }

    /**
     *  上报跑步数据
     * @param type //1=>快速启动， 2=>设定目标， 3=>预设课程
     * @param aimType //1=>设定运动时间， 2=>设定公里数， 3=>设定卡路里
     * @param achieve //0=>设定目标未完成， 1=>设定目标完成
     * @return
     */

    public static String reportExerciseData(int type, int aimType, float aim, int achieve) {
        String time = String.valueOf(DateUtils.currentDataSeconds());
        String msgId = SerialPortUtil.getTreadInstance().getCardNo() + new Date().getTime();
        String data = "{\"type\":\"data\",\"version\":\"" + mTcpVersion + "\",\"msg_id\":\"" + msgId + "\",\"data\":{\"bracelet_id\":\"" + SerialPortUtil.getTreadInstance().getCardNo() + "\"" +
                ",\"gym_id\":\"" + Preference.getBindUserGymId() + "\",\"device_id\":\"" + DeviceUtils.getDeviceInfo(BaseApplication.getInstance()) + "\"" +
                ",\"period\":\"" + SerialPortUtil.getTreadInstance().getRunTime() + "\"" +
                ",\"distance\":\"" + SerialPortUtil.getTreadInstance().getDistance() + "\"" +
                ",\"cal\":\"" + SerialPortUtil.getTreadInstance().getKCAL() + "\"" +
                ",\"type\":\"" + type + "\"" +
                ",\"aim_type\":\"" + aimType + "\"" +
                ",\"aim\":\"" + aim + "\"" +
                ",\"achieve\":\"" + achieve + "\"" +
                ",\"timestamp\":\"" + time + "\"" +
                "}}\\r\\n";
        FileUtils.store(data, ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE + msgId);
        return data;
    }

    /**
     * 客户端发起请求会员列表命令
     * @return
     */

    public static String buildRequestMemberParam() {
        String lastMemberId = MemberUtils.getInstance().getLastMemberId();;
        String gymId = Preference.getBindUserGymId();

        String data = "{\"type\":\"member_list\",\"version\":\"" + mTcpVersion + "\",\"data\":{ " +
                "\"id\":\"" + lastMemberId + "\"" +
                ",\"gym_id\":\"" + gymId + "\"" +
                " }}\\r\\n";
        return data;
    }
}
