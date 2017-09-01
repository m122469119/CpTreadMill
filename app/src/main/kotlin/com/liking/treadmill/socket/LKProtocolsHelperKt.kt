package com.liking.treadmill.socket

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.aaron.android.codelibrary.utils.DateUtils
import com.aaron.android.codelibrary.utils.FileUtils
import com.aaron.android.codelibrary.utils.LogUtils
import com.aaron.android.codelibrary.utils.StringUtils
import com.aaron.android.framework.base.BaseApplication
import com.aaron.android.framework.utils.DeviceUtils
import com.aaron.android.framework.utils.EnvironmentUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.liking.treadmill.app.LikingThreadMillApplication
import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.message.*
import com.liking.treadmill.receiver.AdvertisementReceiver
import com.liking.treadmill.socket.data.TreadProtocolData
import com.liking.treadmill.socket.data.request.*
import com.liking.treadmill.socket.result.*
import com.liking.treadmill.storge.Preference
import com.liking.treadmill.treadcontroller.SerialPortUtil
import com.liking.treadmill.utils.*
import com.liking.treadmill.utils.UUID
import com.liking.treadmill.widget.IToast
import de.greenrobot.event.EventBus
import java.io.File
import java.util.*

/**
 * Created on 2017/08/07
 * desc:
 * @author: chenlei
 * @version:1.0
 */
object LKProtocolsHelperKt {

    val TAG = "LKProtocolsHelperKt"

    val flag = "\\r\\n"

    /*接口版本*/
    val VERSION = "v1.3"

    /**s上报设备信息*/
    val TYPE_TREADMILL = "treadmill"

    val TYPE_INIT = "init"

    val TYPE_CONFIRM = "confirm"

    val TYPE_PING = "ping"

    /*绑定二维码获取*/
    val TYPE_QRCODE_BIND = "bind_qcode"

    /*解绑二维码获取*/
    val TYPE_QRCODE_UNBIND = "unbind_qcode"

    /*更新*/
    val TYPE_UPDATE_NOTIFY = "update"

    /*绑定响应*/
    val TYPE_BIND = "bind"

    /*解绑响应*/
    val TYPE_UNBIND = "unbind"

    /*刷卡登录响应*/
    val TYPE_USERLOGIN = "login"

    /*刷卡退出响应*/
    val TYPE_USERLOGOUT = "logout"

    /*场馆会员下发*/
    val TYPE_MEMBER_LIST = "member_list"

    /*上传训练数据响应*/
    val TYPE_EXERCISE_DATA = "data"

    /*会员列表状态回复*/
    val TYPE_LOCAL_MEMBER = "local_member"

    /*服务器时间差*/
    val TYPE_SERVICE_TIME = "time"

    /*下发广告*/
    val TYPE_ADVERTISMENT = "advertisement"

    /*服务端下发会员列表请求命令*/
    val TYPE_REQUEST_MEMBER = "request_member"

    /*日志上报*/
    val REPORT_LOG_CMD = "report_log"

    /*清除场馆会员*/
    val REPORT_CLEAR_MEMBER_LIST_CMD = "clean_member"

    /*新的广告图*/
    private val NEW_ADVERTISEMENT_CMD = "new_advertisement"

    /*默认广告图*/
    private val DEFAULT_ADVERTISEMENT_CMD = "default_advertisement"

    private val NOTIFY_USER_CMD = "notify_user"

    private val NOTIFY_FOLLOWER_CMD = "notify_follower"


    val mGson = GsonBuilder().disableHtmlEscaping().create()

    val resultHandler = Handler(Looper.getMainLooper())

    var sTimestampOffset: Long = 0


    fun postEvent(event: Any) {
        resultHandler.post {
            EventBus.getDefault().post(event)
        }
    }

    /**
     * 消息返回处理
     */
    fun handlerSocketReceive(result: String) {

        val baseResult = mGson.fromJson(result, BaseSocketResult::class.java)
        val type = baseResult.type

        when (type) {
        //绑定时获取二维码返回
            TYPE_QRCODE_BIND -> {
                val qrcodeResult = mGson.fromJson(result, QrcodeResult::class.java)
                val codeUrl = qrcodeResult.qrcodeData.codeUrl
                Preference.setQcodeUrl(codeUrl)
                postEvent(QrCodeMessage())
            }

            //解绑
            TYPE_QRCODE_UNBIND -> {
                val qrcodeResult = mGson.fromJson(result, QrcodeResult::class.java)
                val codeUrl = qrcodeResult.qrcodeData.codeUrl
                Preference.setQcodeUrl(codeUrl)
                postEvent(QrCodeMessage())
            }

        //升级通知
            TYPE_UPDATE_NOTIFY -> {
                val updateResult = mGson.fromJson(result, ApkUpdateResult::class.java)
                val updateData = updateResult.apkUpdateData
                Preference.setServerVersion(updateData?.version)
                Preference.setServerVersionUrl(updateData?.url)
                Preference.setApkMd5(updateData?.md5)
                Preference.setApkSize(updateData?.size)
                //需要更新并且跑步机没有运行
                if (ApkUpdateHelper.isApkUpdate() && !SerialPortUtil.getTreadInstance().isRunning) {
                    postEvent(UpdateAppMessage())
                }
                Preference.setAppDownloadFailCount(0)
            }

        //场馆绑定推送
            TYPE_BIND -> {
                val bindUserResult = mGson.fromJson(result, BindUserResult::class.java)
                val bindUserData = bindUserResult.data

                val gymId = Preference.getBindUserGymId()
                if (bindUserData!!.errCode == 0 && StringUtils.isEmpty(gymId)
                        || bindUserData.errCode == 0 && "0" == gymId) {
                    Preference.setIsStartingUp(false)
                    Preference.setBindUserGymId(bindUserData.gymId)
                    Preference.setBindUserGymName(bindUserData.gymName)
                    postEvent(GymBindSuccessMessage())
                }
            }

        //解绑场馆返回
            TYPE_UNBIND -> {
                Preference.setUnBindRest()
                postEvent(GymUnBindSuccessMessage())
            }

        //刷卡登录返回
            TYPE_USERLOGIN -> {
                val userInfoResult = mGson.fromJson(result, UserInfoResult::class.java)
                val message = LoginUserInfoMessage()
                val userData = userInfoResult.userInfoData

                userData?.let {
                    message.mUserData = userData
                    if(userData.userInfoData != null) {
                        //缓存一条登录状态
                        val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
                        val time = DateUtils.currentDataSeconds().toString()
                        val braceletId = userData.userInfoData.braceletId
                        val gymId = Preference.getBindUserGymId()

                        val userLogout = UserLogInOutData(braceletId.toLong(), time, deviceId, gymId)
                        val json = mGson.toJson(TreadProtocolData(TYPE_USERLOGOUT, UUID.getMsgId(), VERSION, userLogout))
                        FileUtils.store(json, ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
                    }
                    postEvent(message)
                }
            }

        //用户退出返回
            TYPE_USERLOGOUT -> {
                val logOutResult = mGson.fromJson(result, UserLogOutResult::class.java)
                if (logOutResult != null && logOutResult.data != null) {
                    val braceletId = logOutResult.data.braceletId
                    if (!StringUtils.isEmpty(braceletId)) {
                        FileUtils.delete(ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
                    }
                }
            }

        //同步训练数据返回
            TYPE_EXERCISE_DATA -> {
                if (!StringUtils.isEmpty(baseResult.msgId) && "0" != baseResult.msgId) {
                    FileUtils.delete(ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE + baseResult.msgId)
                }
            }

        //服务器时间差
            TYPE_SERVICE_TIME -> {
                val timeResult = mGson.fromJson(result, ServiceTimeResult::class.java)
                val serviceTime = timeResult.serviceTime
                if (serviceTime != null) {
                    var time = serviceTime.time
                    LogUtils.d("aaron", "serviceTime :" + time)

                    if (!StringUtils.isEmpty(time)) {
                        try {
                            var t = time.toLong()
                            setSystemTime(t)
                        } catch (e: Exception) {
                        }
                    }
                }
            }

        //下发广告
            TYPE_ADVERTISMENT -> {
                val advertisementResult = mGson.fromJson(result, AdvertisementResult::class.java)
                val advUrlResource = advertisementResult.urlResources
                advUrlResource?.let {
                    handlerAdvertisement(advUrlResource)
                    Preference.setAdvertisementResource(mGson.toJson(advertisementResult))
                    postEvent(AdvertisementMessage(advUrlResource.resources))
                }
            }

        //服务端下发会员列表请求命令
            TYPE_REQUEST_MEMBER -> {
                makeRequestMemberList()
            }

        //当前场馆会员id、手环
            TYPE_MEMBER_LIST -> {
                val memberListResult = mGson.fromJson(result, MemberListResult::class.java)
                val datas = memberListResult.data
                datas?.let {
                    var members = datas.member
                    if (members != null && members.size > 0) {
                        postEvent(MemberListMessage(members))//下发的成员列表事件
                    }

                    if (datas.isNextPage) { //下一页
                        makeRequestMemberList()
                    } else {
                        //下发结束 事件
                        Preference.setMemberSynTimestamp(DateUtils.currentDataSeconds())
                        postEvent(MemberNoneMessage())
                    }
                }
            }

            //日志上报
            REPORT_LOG_CMD -> {
                LikingThreadMillApplication.mLKAppSocketLogQueue.put("aaron", "REPORT_LOG, 上报log操作")
                LikingThreadMillApplication.mLKSocketLogQueue.putOnce()
                LikingThreadMillApplication.mLKAppSocketLogQueue.putOnce()
            }

            //场馆会员清除
            REPORT_CLEAR_MEMBER_LIST_CMD -> {
                MemberHelper.getInstance().deleteMembersFromLocal { result ->
                    if (result) {
                        LogUtils.d("aaron", "删除成功")
                    } else {
                        LogUtils.d("aaron", "删除失败")
                    }
                    postEvent(MembersDeleteMessage())
                }
            }
            NEW_ADVERTISEMENT_CMD -> {
                val newAdResult = mGson.fromJson(result, NewAdResult::class.java)
                val data = newAdResult.data
                val newAdvResultMessage = AdvResultMessage(AdvResultMessage.ADV_NEW)
                newAdvResultMessage.obj1 = data
                postEvent(newAdvResultMessage)
            }
            DEFAULT_ADVERTISEMENT_CMD -> {
                val defaultAdResult = mGson.fromJson(result, DefaultAdResult::class.java)
                val data = defaultAdResult.data
                val defaultAdvResultMessage = AdvResultMessage(AdvResultMessage.ADV_DEFAULT)
                defaultAdvResultMessage.obj1 = data
                postEvent(defaultAdvResultMessage)
            }

            //
            NOTIFY_USER_CMD -> {
               val notifyUserResult = mGson.fromJson(result, NotifyUserResult::class.java)
                notifyUserResult.let {
                    notifyUserResult.data.let {
                        LogUtils.e(TAG, "NOTIFY_USER:".plus(it.name))
                    }
                }
            }

            //
            NOTIFY_FOLLOWER_CMD -> {
                val notifyFollowerResult = mGson.fromJson(result, NotifyFollowerResult::class.java)
                notifyFollowerResult.let {
                    notifyFollowerResult.data.let {
                        LogUtils.e(TAG, "NOTIFY_FOLLOWER:".plus(it.name))
                    }
                }
            }
        }
    }

    /**
     * 系统同步服务器时间
     */
    fun setSystemTime(time: Long) {
        val mAlarmManager = BaseApplication.getInstance().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarmManager.setTimeZone("GMT+08:00")

        val ct = SystemClock.setCurrentTimeMillis(time * 1000)
        LogUtils.d("aaron", if (ct) "同步成功" else "同步失败")
    }

    /**
     * 广告处理
     */
    fun handlerAdvertisement(advUrlResource: AdvertisementResult.AdvUrlResource) {

        var requestcode = 1000
        val serviceTime = DateUtils.currentDateMilliseconds() + sTimestampOffset
        advUrlResource.resources.map {
            val advTime = DateUtils.parseString("yyyyMMdd", it.endtime)!!.time
            val timeOffset = advTime - serviceTime

            val context = BaseApplication.getInstance()
            val intent = Intent(context, AdvertisementReceiver::class.java)
            intent.putExtra(AdvertisementReceiver.ADVERTISEMENT_URL, it.url)
            intent.putExtra(AdvertisementReceiver.ADVERTISEMENT_ENDTIME, it.endtime)
            intent.putExtra(AlarmManagerUtils.REQUESTCODE, requestcode)

            if (timeOffset > 0) {
                AlarmManagerUtils.addAdvertisementAlarm(context, intent, timeOffset)
            } else {
                it.isOpen = false
                AlarmManagerUtils.removeAdvertisementAlarm(context, intent)
            }
            requestcode += 1
        }
    }

    /**
     * 拉取成员列表
     */
    fun makeRequestMemberList() {
        val gymId = Preference.getBindUserGymId()
        if (!StringUtils.isEmpty(gymId) && "0" != gymId) {
            postEvent(RequestMembersMessage())
        }
    }

    fun <T> toJson(t: T, cmd: String): String? {
        val gateWayRequest = TreadProtocolData(cmd, /*UUID.getMsgId()*/"", VERSION, t)
        val json = mGson.toJson(gateWayRequest)
        return toJson(json)
    }

    fun toJson(json: String): String? {
        LogUtils.i(TAG, "send no AES $json")
        val encode = AESUtils.encode(json, AESUtils.DEFAULT_KEY)

        if (encode == "") {
            return null
        }
        val result = RequestDataWrapper(encode,
                EnvironmentUtils.Config.getAppVersionName(), VERSION)
        val toJson: String = mGson.toJson(result)
        LogUtils.i(TAG, "send AES $toJson")
        return toJson + flag
    }

    /**
     * 验证加密文件是否正确，并别解析成出来
     */
    fun isResultSuccess(data: String): String? {
        if (StringUtils.isEmpty(data)) {
            return null
        }
        return AESUtils.decode(data, AESUtils.DEFAULT_KEY)
    }

    /**
     * ping 包
     * {"type":"ping","version":"1.3","data":{},"msg_id":""}
     */
    fun getPingRequest(): String? {
        return toJson(PingData(), TYPE_PING)
    }

    fun getRebindRequest(): String? {
        return toJson(RebindData(), TYPE_BIND)
    }

    /**
     * 设备信息上报
     *
     */
    fun getReportDevicesRequest(): String? {
        val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
        val gymId = Preference.getBindUserGymId()
        val appVersion = EnvironmentUtils.Config.getAppVersionName()
        //会员列表最后同步时间
        val msyntime = Preference.getMemberSynTimestamp()
        //本地会员列表状态
        val status = if (MemberHelper.getInstance().memberCount > 0) 1 else 0 //0=>无数据， 1=>有数据
        return toJson(TreadmillData(deviceId, gymId,
                Mac.getMacAddress(BaseApplication.getInstance()), appVersion, "0", "0", "0",
                status.toString(), msyntime), TYPE_TREADMILL)
    }

    fun getInitRequest(): String? {
        return toJson(InitData(""), TYPE_INIT)
    }

    /**
     * 场馆绑定二维码获取
     */
    fun getBindQrcodeRequest(): String? {
        val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
        return toJson(GymBindQrcodeData(deviceId), TYPE_QRCODE_BIND)
    }

    /**
     * 场馆解绑二维码获取
     */
    fun getUnBindRequest(): String? {
        val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
        return toJson(GymUnBindQrcodeData(deviceId), TYPE_QRCODE_UNBIND)
    }


    fun getConfirmRequest(): String? {
        return toJson(ConfirmData(), TYPE_CONFIRM)
    }

    /**
     * 用户刷卡登录
     */
    fun getUserLoginRequest(braceletId: Long): String? {
        val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
        val time = DateUtils.currentDataSeconds().toString()
        val gymId = Preference.getBindUserGymId()
        return toJson(UserLogInOutData(braceletId, time, deviceId, gymId), TYPE_USERLOGIN)
    }

    /**
     * 用户刷卡退出
     */
    fun getUserLogoutRequest(braceletId: Long): String? {
        val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
        val time = DateUtils.currentDataSeconds().toString()
        val gymId = Preference.getBindUserGymId()

        val userLogout = UserLogInOutData(braceletId, time, deviceId, gymId)
        val result = mGson.toJson(TreadProtocolData(TYPE_USERLOGOUT, UUID.getMsgId(), VERSION, userLogout))

        /**更新最后退出时间*/
        val cache = FileUtils.load(ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
        if (!StringUtils.isEmpty(cache)) {
            FileUtils.store(result, ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
        }
        return toJson(result)
    }

    /**
     * 上报跑步后未上传成功的数据
     */
    fun getReportExerciseCacheDataRequest(cacheData: String): String? {
        return toJson(cacheData)
    }

    /**
     * period :获取跑步时间
     * distance : 单位时间内距离\
     * cal :消耗的总卡路里
     * type: 1=>快速启动， 2=>设定目标， 3=>预设课程
     * aim_type: 1=>设定运动时间， 2=>设定公里数，3=>设定卡路里，默认为0
     * achieve: 0=>设定目标未完成， 1=>设定目标完成，默认为0
     */
    fun getReportExerciseDataRequest(braceletId: Long, period: Int, distance: Float,
                                     cal: Float, type: Int, aimType: Int, aim: Float, achieve: Int): String? {
        val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
        val msgId = SerialPortUtil.getTreadInstance().cardNo + Date().time//UUID.getMsgId()
        val gymId = Preference.getBindUserGymId()
        val time = DateUtils.currentDataSeconds().toString()

        val exerciseData = ExerciseData(braceletId, gymId, deviceId,
                period.toString(), distance.toString(), cal.toString(),
                type.toString(), aimType.toString(), aim.toString(), achieve.toString(), time)

        val result = mGson.toJson(TreadProtocolData(TYPE_EXERCISE_DATA, msgId, VERSION, exerciseData))

        FileUtils.store(result, ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE + msgId)
        return toJson(result)
    }

    /**
     * 客户端发起请求会员列表命令
     */
    fun getRequestMembersCommandRequest(): String? {
        val lastMemberId = MemberHelper.getInstance().lastMemberId
        val gymId = Preference.getBindUserGymId()
        return toJson(MemberListData(lastMemberId, gymId), TYPE_MEMBER_LIST)
    }

    /**
     * 会员列状态回复
     */
    fun getMembersStateReplyCommandRequest(): String? {
        val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
        val gymId = Preference.getBindUserGymId()
        val msyntime = Preference.getMemberSynTimestamp()
        val status = if (MemberHelper.getInstance().memberCount > 0) 1 else 0 //0=>无数据， 1=>有数据

        return toJson(LocalMemberData(status, deviceId, gymId, msyntime), TYPE_LOCAL_MEMBER)
    }

    /**
     * 跑步机用户登录状态
     */
    fun reportedLogOutCache(func: (data: String) -> Unit) {
        File(ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE)
                .listFiles()?.map {
            var data = FileUtils.load(it.absolutePath)

            if (!StringUtils.isEmpty(data)) {
                try {
                    val logOutResult = mGson.fromJson(data, UserLogOutResult::class.java)
                    if (SerialPortUtil.getTreadInstance().userInfo != null
                            && logOutResult != null
                            && SerialPortUtil.getTreadInstance().userInfo.mBraceletId ==
                            logOutResult!!.data.braceletId) {
                        return
                    }
                } catch (e: Exception) {
                } finally {
                    if (data.contains(flag)) {
                        data = data.replace(flag, "")
                    }
                    func.invoke(data)
                }
            }
        }
    }

    /**
     * 用户跑步数据缓存
     */
    fun reportedExerciseCache(func: (data: String) -> Unit) {
        File(ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE)
                .listFiles()?.map {
            try {
                var data = FileUtils.load(it.absolutePath)
                if (!StringUtils.isEmpty(data) && data.contains(flag)) {
                    data = data.replace(flag, "")
                }
                func.invoke(toJson(data)!!)
            } catch (e: Exception) {
            }
        }
    }
}