package com.liking.treadmill.socket

import android.os.Build
import com.aaron.android.codelibrary.utils.DateUtils
import com.aaron.android.codelibrary.utils.FileUtils
import com.aaron.android.codelibrary.utils.LogUtils
import com.aaron.android.codelibrary.utils.StringUtils
import com.aaron.android.framework.base.BaseApplication
import com.aaron.android.framework.utils.DeviceUtils
import com.liking.socket.Constant
import com.liking.treadmill.app.LikingThreadMillApplication
import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.socket.data.BaseData
import com.liking.treadmill.socket.data.request.*
import com.liking.treadmill.socket.result.UserLogOutResult
import com.liking.treadmill.storge.Preference
import com.liking.treadmill.treadcontroller.SerialPortUtil
import com.liking.treadmill.utils.Mac
import com.liking.treadmill.utils.MemberHelper
import java.io.File

/**
 * Created on 2017/09/30
 * desc:
 * @author: chenlei
 * @version:1.0
 */
object CmdRequestManager {

    var TAG: String = CmdRequestManager::class.java.simpleName

    /**
     * 时间戳
     */
    fun buildTimeStampRequest(): CmdRequest {
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TIMESTAMP)
                .socket(LikingThreadMillApplication.getSocket())
                .build()
    }

    /**
     * 获取二维码
     */
    fun buildObtainQrcode(type: Int): CmdRequest {
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_OBTAIN_QRCODE)
                .socket(LikingThreadMillApplication.getSocket())
                .data(ObtainQrcodeRequestData(type))
                .build()
    }

    /**
     * 上报设备信息
     */
    fun buildDeviceInfoRequest(): CmdRequest {
        return CmdRequest.Builder()
                .cmd(Constant.CMD_DEVICE_INFO)
                .socket(LikingThreadMillApplication.getSocket())
                .data(DeviceRequestData(
                        DeviceUtils.getDeviceInfo(LikingThreadMillApplication.getInstance()),
                        Mac.getMacAddress(BaseApplication.getInstance()),
                        Build.MODEL.plus("|").plus(Build.VERSION.RELEASE),
                        0, 1, 1, 1))
                .build()
    }

    /**
     * 上报跑步机基础信息
     */
    fun buildTreadmillRequest(): CmdRequest {
        //本地会员列表状态
        val status = if (MemberHelper.getInstance().memberCount > 0) 1 else 0 //0=>无数据， 1=>有数据
        //会员列表最后同步时间
        val msyntime = Preference.getMemberSynTimestamp()
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TREADMILL)
                .socket(LikingThreadMillApplication.getSocket())
                .data(TreadmillRequestData(0, 0, 0, status, msyntime))
                .build()
    }

    /**
     * 更新
     */
    fun buildUpdateRequest(): CmdRequest {
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_UPDATE)
                .socket(LikingThreadMillApplication.getSocket())
                .build()
    }

    /**
     * 用户刷卡登录
     */
    fun buildUserLoginRequest(braceletId: Long): CmdRequest {
        var gymId = Preference.getBindUserGymId()
        if (gymId.isNullOrEmpty()) {
            gymId = "0"
        }
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TREADMILL_LOGIN)
                .data(UserLogInOutRequestData(
                        gymId.toInt(),
                        DateUtils.currentDataSeconds(),
                        DeviceUtils.getDeviceInfo(LikingThreadMillApplication.getInstance()),
                        braceletId
                ))
                .socket(LikingThreadMillApplication.getSocket())
                .build()
    }

    /**
     * 用户刷卡退出
     */
    fun buildUserLogOutRequest(braceletId: Long): CmdRequest {
        var gymId = Preference.getBindUserGymId()
        if (gymId.isNullOrEmpty()) {
            gymId = "0"
        }
        var logOut = UserLogInOutRequestData(
                gymId.toInt(),
                DateUtils.currentDataSeconds(),
                DeviceUtils.getDeviceInfo(LikingThreadMillApplication.getInstance()),
                braceletId
        )
        /**更新最后退出时间*/
        val cache = FileUtils.load(
                ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
        if (!StringUtils.isEmpty(cache)) {
            FileUtils.store(MessageHandlerHelper.toJson(logOut),
                    ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
        }
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TTREADMILL_LOGOUT)
                .data(logOut)
                .socket(LikingThreadMillApplication.getSocket())
                .build()
    }

    /**
     * 上报用户训练数据
     * period :获取跑步时间
     * distance : 单位时间内距离
     * cal :消耗的总卡路里
     * type: 1=>快速启动， 2=>设定目标， 3=>预设课程
     * aim_type: 1=>设定运动时间， 2=>设定公里数，3=>设定卡路里，默认为0
     * achieve: 0=>设定目标未完成， 1=>设定目标完成，默认为0
     */
    fun buildUserExerciseRequest(braceletId: Long, period: Int, distance: Float,
                                 cal: Float, type: Int, aimType: Int, aim: Float, achieve: Int): CmdRequest {
        val time = DateUtils.currentDataSeconds()
        val dataId = braceletId.toString().plus(time)
        val exerciseData = ExerciseInfoRequestData(braceletId, period, distance,
                                                    cal, type, aimType, aim, achieve, time)
        val request = MessageHandlerHelper.toJson(exerciseData)

        FileUtils.store(request, ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE + dataId)

        return CmdRequest.Builder()
                .socket(LikingThreadMillApplication.getSocket())
                .cmd(CmdConstant.CMD_REPORT_DATA)
                .data(exerciseData)
                .build()
    }

    /**
     * 上报未提交成功---用户训练数据
     */
    fun reportedAllUserExerciseRequest() {
        File(ThreadMillConstant.THREADMILL_PATH_STORAGE_DATA_CACHE)
                .listFiles()?.forEach {
                try {
                    val data = FileUtils.load(it.absolutePath)
                    if (!StringUtils.isEmpty(data)) {
                        LogUtils.e(LKProtocolsHelperKt.TAG, "用户跑步数据缓存:".plus(data).plus("-").plus(it.name))
                        val reqData = MessageHandlerHelper.fromJson(data, ExerciseInfoRequestData::class.java)
                        if (reqData != null) {
                            buildUserExerciseRequest(reqData).send()
                        }
                    } else {
                        LogUtils.e(TAG, "用户跑步数据缓存:null-".plus(it.name))
                        it.delete()
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        LogUtils.e(TAG, "用户跑步数据缓存:error-".plus(e.message))
                        it.delete()
                    }catch (e: Exception){}
                }
        }
    }

    /**
     * 上报未提交成功---用户登出
     */
    fun reportedLogOutRequest() {
        File(ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE)
                .listFiles()?.forEach {
            try {
                var data = FileUtils.load(it.absolutePath)
                if (!StringUtils.isEmpty(data)) {
                    //如果在登录状态return
                    val logOut = MessageHandlerHelper.fromJson(data, UserLogInOutRequestData::class.java)
                    if (SerialPortUtil.getTreadInstance().userInfo != null
                            && logOut != null
                            && SerialPortUtil.getTreadInstance().userInfo.mBraceletId ==
                            logOut.braceletId.toString()) {
                        return
                    }

                    LogUtils.e(LKProtocolsHelperKt.TAG, "跑步机用户登录状态:" + data)
                    buildLogOutRequest(logOut!!).send()
                } else {
                    LogUtils.e(LKProtocolsHelperKt.TAG, "跑步机用户登录状态:null")
                    it.delete()
                }
            }catch (e: Exception) {
                e.printStackTrace()
                try {
                    LogUtils.e(TAG, "登录:error-".plus(e.message))
                    it.delete()
                }catch (e: Exception){}
            }
        }
    }

    private fun buildLogOutRequest(data : BaseData) : CmdRequest{
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TTREADMILL_LOGOUT)
                .data(data)
                .socket(LikingThreadMillApplication.getSocket())
                .build()
    }

    private fun buildUserExerciseRequest(data: BaseData) : CmdRequest {
        return CmdRequest.Builder()
                .socket(LikingThreadMillApplication.getSocket())
                .cmd(CmdConstant.CMD_REPORT_DATA)
                .data(data)
                .build()
    }

}