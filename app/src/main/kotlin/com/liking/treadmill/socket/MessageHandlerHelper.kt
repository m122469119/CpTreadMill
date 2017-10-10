package com.liking.treadmill.socket

import android.app.AlarmManager
import android.content.Context
import android.os.SystemClock
import com.aaron.android.codelibrary.utils.LogUtils
import com.aaron.android.framework.base.BaseApplication
import com.google.gson.GsonBuilder
import com.liking.treadmill.socket.data.BaseData
import com.liking.treadmill.socket.data.result.TimeStampData

/**
 * Created on 2017/09/29
 * desc:
 * @author: chenlei
 * @version:1.0
 */
object MessageHandlerHelper {

    var TAG: String = MessageHandlerHelper::class.java.javaClass.simpleName

    val mGson = GsonBuilder().disableHtmlEscaping().create()

    private fun <T> fromJson(result: String, clazz: Class<T>): T? {
        try {
            return mGson.fromJson(result, clazz)
        } catch (e: Exception) {
            return null
        }
    }

    fun toJson(data: BaseData?) : String {
        try {
            if(data == null) return ""
            return mGson.toJson(data)
        } catch (e:Exception) {
            return ""
        }
    }

    /**
     * 消息返回
     */
    fun handlerReceive(cmd: Byte, result: String) {
        when (cmd) {
            //时间戳
            CmdConstant.CMD_TIMESTAMP -> {
                fromJson(result, TimeStampData::class.java).let {
                    if (it != null) {
                        setSystemTime(it.timestamp)
                    }
                }
            }

            //更新通知
            CmdConstant.CMD_UPDATE -> {
//                val updateResult = mGson.fromJson(result, ApkUpdateResult::class.java)
//                val updateData = updateResult.apkUpdateData
//                Preference.setServerVersion(updateData?.version)
//                Preference.setServerVersionUrl(updateData?.url)
//                Preference.setApkMd5(updateData?.md5)
//                Preference.setApkSize(updateData?.size)
//                //需要更新并且跑步机没有运行
//                if (ApkUpdateHelper.isApkUpdate() && !SerialPortUtil.getTreadInstance().isRunning) {
//                    postEvent(UpdateAppMessage())
//                }
//                Preference.setAppDownloadFailCount(0)
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