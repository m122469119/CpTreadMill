package com.liking.treadmill.socket

import android.app.AlarmManager
import android.content.Context
import android.os.*
import com.aaron.android.codelibrary.utils.DateUtils
import com.aaron.android.codelibrary.utils.FileUtils
import com.aaron.android.codelibrary.utils.LogUtils
import com.aaron.android.codelibrary.utils.StringUtils
import com.aaron.android.framework.base.BaseApplication
import com.aaron.android.framework.utils.DeviceUtils
import com.google.gson.GsonBuilder
import com.liking.socket.Constant
import com.liking.treadmill.app.LikingThreadMillApplication
import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.message.*
import com.liking.treadmill.socket.data.BaseData
import com.liking.treadmill.socket.data.request.ExerciseInfoResultData
import com.liking.treadmill.socket.data.request.QrcodeInfoResultData
import com.liking.treadmill.socket.data.request.UserLogInOutRequestData
import com.liking.treadmill.socket.data.result.*
import com.liking.treadmill.storge.Preference
import com.liking.treadmill.treadcontroller.SerialPortUtil
import com.liking.treadmill.utils.ApkUpdateHelper
import com.liking.treadmill.utils.MemberHelper
import de.greenrobot.event.EventBus

/**
 * Created on 2017/09/29
 * desc:
 * @author: chenlei
 * @version:1.0
 */
object MessageHandlerHelper {

    var TAG: String = MessageHandlerHelper::class.java.simpleName

    val HANDLE_WHAT_ACTION_MSG = 10

    val HANDLE_WHAT_ACTION_CONNECT = 11

    private var messageHandlerThread: HandlerThread = HandlerThread("MessageHandlerThread")
    var messageHandler: Handler

    init {
        messageHandlerThread.start()
        messageHandler = object : Handler(messageHandlerThread.looper) {
            override fun handleMessage(msg: Message?) {
                if (msg != null) {
                    when (msg.what) {
                        HANDLE_WHAT_ACTION_MSG -> try {
                            val bundle = msg.obj as Bundle
                            if (bundle != null) {
                                val cmd = bundle.getByte(Constant.KEY_MSG_CMD)
                                val body = bundle.getString(Constant.KEY_MSG_DATA)
                                handlerReceive(cmd, body!!)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        HANDLE_WHAT_ACTION_CONNECT -> try {
                            CmdRequestManager.reportedAllUserExerciseRequest()
                            CmdRequestManager.reportedLogOutRequest()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    val mGson = GsonBuilder().disableHtmlEscaping().create()

    fun <T> fromJson(result: String, clazz: Class<T>): T? {
        try {
            return mGson.fromJson(result, clazz)
        } catch (e: Exception) {
            return null
        }
    }

    fun toJson(data: BaseData?): String {
        try {
            if (data == null) return ""
            return mGson.toJson(data)
        } catch (e: Exception) {
            return ""
        }
    }

    val mainHandler = Handler(Looper.getMainLooper())

    fun postEvent(event: Any) {
        mainHandler.post {
            EventBus.getDefault().post(event)
        }
    }

    /**
     * 消息返回
     */
    fun handlerReceive(cmd: Byte, result: String) {
        LogUtils.e(TAG, String.format("%02X %s", cmd, result))
        when (cmd) {

        //时间戳
            CmdConstant.CMD_TIMESTAMP -> {
                fromJson(result, TimeStampResultData::class.java).let {
                    if (it != null) {
                        setSystemTime(it.timestamp)
                    }
                }
            }

        //获取绑定、解绑二维码
            CmdConstant.CMD_OBTAIN_QRCODE -> {
                fromJson(result, QrcodeInfoResultData::class.java).let {
                    if (it != null) {
                        Preference.setQcodeUrl(it.url)
                        postEvent(QrCodeMessage())
                    }
                }
            }

        //更新通知
            CmdConstant.CMD_UPDATE -> {
                fromJson(result, ApkUpdateInfoResultData::class.java).let {
                    if (it != null) {
                        Preference.setServerVersion(it.version)
                        Preference.setServerVersionUrl(it.url)
                        Preference.setApkMd5(it.md5)
                        Preference.setApkSize(it.size)
                        //需要更新并且跑步机没有运行
                        if (ApkUpdateHelper.isApkUpdate() && !SerialPortUtil.getTreadInstance().isRunning) {
                            postEvent(UpdateAppMessage())
                        }
                        Preference.setAppDownloadFailCount(0)
                    }
                }
            }

        //刷卡登录
            CmdConstant.CMD_TREADMILL_LOGIN -> {
                fromJson(result, UserInfoResultData::class.java).let {
                    if (it != null) {
                        val message = UserLoginInfoMessage()
                        val userResult = it
                        val user = userResult.user

                        user?.let {
                            message.mUserResult = userResult
                            //缓存一条登录状态
                            val deviceId = DeviceUtils.getDeviceInfo(BaseApplication.getInstance())
                            val time = DateUtils.currentDataSeconds()
                            val braceletId = user.braceletId
                            val gymId = Preference.getBindUserGymId()

                            val logOut = UserLogInOutRequestData(gymId.toInt(), time, deviceId, braceletId.toLong())
                            val json = toJson(logOut)
                            if (json.isNotEmpty()) {
                                FileUtils.store(json, ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
                            }
                            postEvent(message)
                        }
                    }
                }
            }

        //刷卡退出
            CmdConstant.CMD_TTREADMILL_LOGOUT -> {
                fromJson(result, BraceletInfoResultData::class.java).let {
                    if (it != null) {
                        val braceletId = it.braceletId
                        FileUtils.delete(ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
                    }
                }
            }

        //训练数据返回
            CmdConstant.CMD_REPORT_DATA -> {
                fromJson(result, ExerciseInfoResultData::class.java).let {
                    if (it != null) {
                        val dataId = it.braceletId.toString().plus(it.runTime)
                        LogUtils.e(TAG, "file delete:" + dataId)
                        FileUtils.delete(ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE + dataId)
                    }
                }
            }

        //场馆绑定、解绑返回
            CmdConstant.CMD_GYM_BIND -> {
                fromJson(result, BindGymInfoResultData::class.java).let {
                    if (it != null) {
                        if (it.type == ThreadMillConstant.TYPE_GYM_BIND) {
                            val gymId = Preference.getBindUserGymId()
                            if (StringUtils.isEmpty(gymId) || "0" == gymId) {
                                Preference.setIsStartingUp(false)
                                Preference.setBindUserGymId(it.gymId.toString())
                                Preference.setBindUserGymName(it.gymName)
                                LKProtocolsHelperKt.postEvent(GymBindSuccessMessage())
                            }
                        } else if (it.type == ThreadMillConstant.TYPE_GYM_UNBIND) {
                            Preference.setUnBindRest()
                            postEvent(GymUnBindSuccessMessage())
                        }
                    }
                }
            }

        //广告下发


        //上传log
            CmdConstant.CMD_LOG -> {
                LikingThreadMillApplication.mLKAppSocketLogQueue.put("aaron", "REPORT_LOG, 上报log操作")
                LikingThreadMillApplication.mLKSocketLogQueue.putOnce()
                LikingThreadMillApplication.mLKAppSocketLogQueue.putOnce()
            }

        //场馆会员清除
            CmdConstant.CMD_CLEAR_USER_DATA -> {
                MemberHelper.getInstance().deleteMembersFromLocal { result ->
                    if (result) {
                        LogUtils.d(TAG, "删除成功")
                    } else {
                        LogUtils.d(TAG, "删除失败")
                    }
                    LKProtocolsHelperKt.postEvent(MembersDeleteMessage())
                }
            }

        //下发服务列表
            CmdConstant.CMD_SERVICE_LIST -> {

            }
        }
    }

    /**
     * 系统同步服务器时间
     */
    fun setSystemTime(time: Long) {
        val mAlarmManager = BaseApplication.getInstance().getSystemService(Context.ALARM_SERVICE)
                as AlarmManager
        mAlarmManager.setTimeZone("GMT+08:00")
        val ct = SystemClock.setCurrentTimeMillis(time * 1000)
        LogUtils.d(TAG, if (ct) "同步成功" else "同步失败")
    }
}