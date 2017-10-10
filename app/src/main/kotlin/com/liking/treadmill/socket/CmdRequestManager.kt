package com.liking.treadmill.socket

import android.os.Build
import com.aaron.android.framework.base.BaseApplication
import com.aaron.android.framework.utils.DeviceUtils
import com.liking.socket.Constant
import com.liking.treadmill.app.LikingThreadMillApplication
import com.liking.treadmill.socket.data.request.DeviceInfoData
import com.liking.treadmill.socket.data.request.TreadmillInfoData
import com.liking.treadmill.storge.Preference
import com.liking.treadmill.utils.Mac
import com.liking.treadmill.utils.MemberHelper

/**
 * Created on 2017/09/30
 * desc:
 * @author: chenlei
 * @version:1.0
 */
object CmdRequestManager {

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
    fun buildObtainQrcode(): CmdRequest  {
        return CmdRequest.Builder()
                .cmd(CmdConstant.CMD_OBTAIN_QRCODE)
                .socket(LikingThreadMillApplication.getSocket())
//                .data()
                .build()
    }

    /**
     * 上报设备信息
     */
    fun buildDeviceInfoRequest(): CmdRequest {
        return CmdRequest.Builder()
                .cmd(Constant.CMD_DEVICE_INFO)
                .socket(LikingThreadMillApplication.getSocket())
                .data(DeviceInfoData(
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
                .data(TreadmillInfoData(0, 0, 0,status, msyntime))
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

}