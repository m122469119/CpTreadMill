package com.liking.treadmill.socket.internal

import com.aaron.android.codelibrary.utils.DateUtils
import com.aaron.android.codelibrary.utils.FileUtils
import com.aaron.android.codelibrary.utils.LogUtils
import com.aaron.android.codelibrary.utils.StringUtils
import com.aaron.android.framework.utils.DeviceUtils
import com.liking.treadmill.app.LikingThreadMillApplication
import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.socket.CmdRequestManager
import com.liking.treadmill.socket.LKProtocolsHelperKt
import com.liking.treadmill.socket.MessageHandlerHelper
import com.liking.treadmill.socket.ThreadMillServiceApi
import com.liking.treadmill.socket.data.request.ExerciseInfoRequestData
import com.liking.treadmill.socket.data.request.UserLogInOutRequestData
import com.liking.treadmill.storge.Preference
import com.liking.treadmill.treadcontroller.SerialPortUtil
import com.liking.treadmill.utils.MemberHelper
import java.io.File

/**
 * Created on 2017/09/28
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class ThreadMillServiceApiImpl : ThreadMillServiceApi {

    override fun initialization() {
        CmdRequestManager.init(LikingThreadMillApplication.getSocket())
    }

    /**
     * 更新
     */
    override fun checkUpdates() {
        CmdRequestManager.buildUpdateRequest().send()
    }

    /**
     * 绑定
     */
    override fun bind() {
        CmdRequestManager.buildObtainQrcode(ThreadMillConstant.TYPE_GYM_BIND).send()
    }

    /**
     * 解绑
     */
    override fun unBind() {
        CmdRequestManager.buildObtainQrcode(ThreadMillConstant.TYPE_GYM_UNBIND).send()
    }

    /**
     * 上报设备信息
     */
    override fun reportDevices() {
        //本地会员列表状态
        val status = if (MemberHelper.getInstance().memberCount > 0) 1 else 0 //0=>无数据， 1=>有数据
        //会员列表最后同步时间
        val msyntime = Preference.getMemberSynTimestamp()
        CmdRequestManager.buildTreadmillRequest(status, msyntime).send()
    }

    /**
     * 登录
     */
    override fun userLogin(cardNo: String) {
        var gymId = Preference.getBindUserGymId()
        if (gymId.isNullOrEmpty()) {
            gymId = "0"
        }
        CmdRequestManager.buildUserLoginRequest(
                gymId.toInt(),
                DateUtils.currentDataSeconds(),
                DeviceUtils.getDeviceInfo(LikingThreadMillApplication.getInstance()),
                cardNo.toLong()).send()
    }

    /**
     * 退出
     */
    override fun userLogOut(cardNo: String) {
        var gymId = Preference.getBindUserGymId()
        if (gymId.isNullOrEmpty()) {
            gymId = "0"
        }
        CmdRequestManager.buildUserLogOutRequest(
                gymId.toInt(),
                DateUtils.currentDataSeconds(),
                DeviceUtils.getDeviceInfo(LikingThreadMillApplication.getInstance()),
                cardNo.toLong()).send()
    }

    /**
     * 上报训练数据
     */
    override fun reportExerciseData(type: Int, aimType: Int, aim: Float, achieve: Int) {
        val braceletId = SerialPortUtil.getTreadInstance().cardNo
        val period = SerialPortUtil.getTreadInstance().runTime
        val distance = SerialPortUtil.getTreadInstance().distance
        val cal = SerialPortUtil.getTreadInstance().kcal
        CmdRequestManager.buildUserExerciseRequest(braceletId.toLong(), period, distance, cal,
                type, aimType, aim, achieve).send()
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
                        CmdRequestManager.buildUserExerciseRequest(reqData).send()
                    }
                } else {
                    LogUtils.e(CmdRequestManager.TAG, "用户跑步数据缓存:null-".plus(it.name))
                    it.delete()
                }
            }catch (e: Exception) {
                e.printStackTrace()
                try {
                    LogUtils.e(CmdRequestManager.TAG, "用户跑步数据缓存:error-".plus(e.message))
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
                val data = FileUtils.load(it.absolutePath)
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
                    if(logOut != null) {
                        CmdRequestManager.buildLogOutRequest(logOut).send()
                    }
                } else {
                    LogUtils.e(LKProtocolsHelperKt.TAG, "跑步机用户登录状态:null")
                    it.delete()
                }
            }catch (e: Exception) {
                e.printStackTrace()
                try {
                    LogUtils.e(CmdRequestManager.TAG, "登录:error-".plus(e.message))
                    it.delete()
                }catch (e: Exception){}
            }
        }
    }
}