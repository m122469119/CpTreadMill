package com.liking.treadmill.socket.internal

import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.socket.CmdRequestManager
import com.liking.treadmill.socket.ThreadMillServiceApi
import com.liking.treadmill.treadcontroller.SerialPortUtil

/**
 * Created on 2017/09/28
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class ThreadMillServiceApiImpl : ThreadMillServiceApi {

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
        CmdRequestManager.buildTreadmillRequest().send()
    }

    /**
     * 登录
     */
    override fun userLogin(cardNo: String) {
        CmdRequestManager.buildUserLoginRequest(cardNo.toLong())
    }

    /**
     * 退出
     */
    override fun userLogOut(cardNo: String) {
        CmdRequestManager.buildUserLogOutRequest(cardNo.toLong())
    }

    /**
     * 上报训练数据
     */
    override fun reportExerciseData(type: Int, aimType: Int, aim: Float, achieve: Int) {
        var braceletId = SerialPortUtil.getTreadInstance().cardNo
        var period = SerialPortUtil.getTreadInstance().runTime
        var distance = SerialPortUtil.getTreadInstance().distance
        var cal = SerialPortUtil.getTreadInstance().kcal
        CmdRequestManager.buildUserExerciseRequest(braceletId.toLong(), period, distance, cal,
                type, aimType, aim, achieve)
    }
}