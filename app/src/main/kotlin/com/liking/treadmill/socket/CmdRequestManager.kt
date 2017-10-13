package com.liking.treadmill.socket

import com.aaron.android.codelibrary.utils.DateUtils
import com.aaron.android.codelibrary.utils.FileUtils
import com.aaron.android.codelibrary.utils.StringUtils
import com.liking.socket.Constant
import com.liking.socket.SocketIO
import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.socket.data.BaseData
import com.liking.treadmill.socket.data.request.*

/**
 * Created on 2017/09/30
 * desc:
 * @author: chenlei
 * @version:1.0
 */
object CmdRequestManager {

    var TAG: String = CmdRequestManager::class.java.simpleName

    var socket: SocketIO? = null

    fun init(s :SocketIO) {
        this.socket = s
    }

    /**
     * 时间戳
     */
    fun buildTimeStampRequest(): CmdRequest {
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TIMESTAMP)
                .socket(socket)
                .build()
    }

    /**
     * 获取二维码
     */
    fun buildObtainQrcode(type: Int): CmdRequest {
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_OBTAIN_QRCODE)
                .socket(socket)
                .data(ObtainQrcodeRequestData(type))
                .build()
    }

    /**
     * 上报设备信息
     */
    fun buildDeviceInfoRequest(deviceId: String, gatewayId: String, deviceName:String): CmdRequest {
        return CmdRequest.Builder()
                .cmd(Constant.CMD_DEVICE_INFO)
                .socket(socket)
                .data(DeviceRequestData(deviceId, gatewayId, deviceName,
                        0, 1, 1, 1))
                .build()
    }

    /**
     * 上报跑步机基础信息
     */
    fun buildTreadmillRequest(status: Int, time:Long): CmdRequest {
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TREADMILL)
                .socket(socket)
                .data(TreadmillRequestData(0, 0, 0, status, time))
                .build()
    }

    /**
     * 更新
     */
    fun buildUpdateRequest(): CmdRequest {
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_UPDATE)
                .socket(socket)
                .build()
    }

    /**
     * 用户刷卡登录
     */
    fun buildUserLoginRequest(gymId: Int, time:Long, deviceId: String, braceletId: Long): CmdRequest {

        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TREADMILL_LOGIN)
                .data(UserLogInOutRequestData(gymId, time, deviceId, braceletId))
                .socket(socket)
                .build()
    }

    /**
     * 用户刷卡退出
     */
    fun buildUserLogOutRequest(gymId: Int, time:Long, deviceId: String, braceletId: Long): CmdRequest {
        val logOut = UserLogInOutRequestData(gymId, time, deviceId, braceletId)
        /**更新最后退出时间*/
        val cache = FileUtils.load(
                ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
        if (!StringUtils.isEmpty(cache)) {
            FileUtils.store(MessageHandlerHelper.toJson(logOut),
                    ThreadMillConstant.THREADMILL_PATH_STORAGE_LOGINOUT_CACHE + braceletId)
        }
        return buildLogOutRequest(logOut)
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

        return buildUserExerciseRequest(exerciseData)
    }

    fun buildLogOutRequest(data : BaseData) : CmdRequest{
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_TTREADMILL_LOGOUT)
                .data(data)
                .socket(socket)
                .build()
    }

    fun buildUserExerciseRequest(data: BaseData) : CmdRequest {
        return CmdRequest.Builder()
                .socket(socket)
                .cmd(CmdConstant.CMD_REPORT_DATA)
                .data(data)
                .build()
    }

}