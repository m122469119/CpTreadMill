package com.liking.treadmill.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import com.aaron.android.codelibrary.utils.LogUtils
import com.aaron.android.framework.utils.EnvironmentUtils
import com.liking.treadmill.test.IBackService
import com.liking.treadmill.treadcontroller.SerialPortUtil
import java.io.InputStream

/**
 * Created on 2017/5/22
 * Created by sanfen
 *
 * @version 1.0.0
 */
class LKSocketServiceKt : Service() {
    private val TAG = "LKSocketService"
    private val HEART_BEAT_RATE = (2 * 60 * 1000).toLong()
    private val HOST = if (EnvironmentUtils.Config.isDebugMode()) "120.24.177.134" else "112.74.27.162"
    private val PORT = 8192

    private var mCount = 0
    private lateinit var mLocalBroadcastManager: LocalBroadcastManager
    private lateinit var mLKSocketClient: LKSocketClientKt

    private val flag = "\\r\\n"
    private val splitflag = "\\\\r\\\\n"
    private val flaglength = flag.length//4
    private val resultHandlerBuilder = StringBuilder()

    override fun onCreate() {
        super.onCreate()
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        mLKSocketClient = LKSocketClientKt
                .LkSocketClientBuilder()
                .setHost(HOST)
                .setPort(PORT)
                .setHeartEntity(object : LKSocketClientKt.HeartEntity<String> {
                    override fun createHeartEntity(): String? {
                        return LKProtocolsHelperKt.getPingRequest()
                    }

                    override fun createHeartTime(): Long {
                        return HEART_BEAT_RATE
                    }
                })
                .setIsReconnect(true)
                .build()

        mLKSocketClient.setCallback(object : LKSocketClientKt.LKSocketCallback {
            override fun onMessage(inputStream: InputStream): Boolean {
                val buffer = ByteArray(4 * 1024)
                val len = inputStream.read(buffer)
                if (len == -1) {
                    return false
                }

                val json = String(buffer, 0, len)
                handlerResult(json)
                return true
            }

            override fun onOpen() {
                mIBackService?.reportDevices()
                LKProtocolsHelperKt.reportedLogOutCache {
                    data -> mIBackService.reportExerciseCacheData(data)
                }
                LKProtocolsHelperKt.reportedExerciseCache {
                    data -> mIBackService.reportExerciseCacheData(data)
                }

            }


            override fun onReconnect(count: Int) {
                mCount = 0
                resultHandlerBuilder.setLength(0)
            }

            override fun onClose() {

            }

            override fun onRelease() {

            }

        })
        mLKSocketClient.start()
    }

    fun handlerResult(json: String) {

        resultHandlerBuilder.append(json)

        val flagLastIndex = resultHandlerBuilder.lastIndexOf(flag)

        if (flagIsLast(flagLastIndex, resultHandlerBuilder.length, flaglength)) {
            val results = resultHandlerBuilder.toString().trim()
            resultHandlerBuilder.setLength(0)
            if (results.isNotEmpty()) {
                handlerFullResult(results)
            }
        } else if (flagLastIndex > -1) {
            val partStart = flagLastIndex + flaglength
            //不完整部分
            val imperfectData = resultHandlerBuilder.substring(partStart)
            //去除不完整部分
            resultHandlerBuilder.replace(partStart, resultHandlerBuilder.length, "")
            //处理完整部分
            handlerFullResult(resultHandlerBuilder.toString())
            resultHandlerBuilder.setLength(0)
            resultHandlerBuilder.append(imperfectData)
        }
    }

    /**
     * 判断是否 以 flag 结尾
     * @param flagLastPoi
     * @param dataLen
     * @param flagLen
     * @return
     */
    fun flagIsLast(flagLastPoi: Int, dataLen: Int, flagLen: Int): Boolean {
        return flagLastPoi > -1 && dataLen == flagLastPoi + flagLen
    }

    fun handlerFullResult(rs: String) {
        val rlist = rs.split(splitflag.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (result in rlist) {
            try {
                val resultSuccess = LKProtocolsHelperKt.isResultSuccess(result)
                if (resultSuccess != null) {
                    mCount += 1
                    LogUtils.i(TAG, "result:" + resultSuccess)
                    LKProtocolsHelperKt.handlerSocketReceive(resultSuccess)
//                    val intent = Intent("com.liking.threadmill.socket")
//                    intent.putExtra("message", resultSuccess)
//                    intent.putExtra("mCount", mCount)
//                    mLocalBroadcastManager.sendBroadcast(intent)
                }
            }catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private val mIBackService = object : IBackService.Stub() {

        override fun rebind() {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getRebindRequest())
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getReportDevicesRequest())
        }

        override fun init() {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getInitRequest())
        }

        override fun bind() {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getBindQrcodeRequest())
        }

        override fun unBind() {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getUnBindRequest())
        }

        override fun confirm() {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getConfirmRequest())
        }

        override fun reportDevices() {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getReportDevicesRequest())
        }

        override fun userLogin(cardno: String) {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getUserLoginRequest(cardno.toLong()))
        }

        override fun userLogOut(cardno: String) {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getUserLogoutRequest(cardno.toLong()))
        }

        override fun reportExerciseCacheData(data: String) {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getReportExerciseCacheDataRequest(data))
        }

        override fun reportExerciseData(type: Int, aimType: Int, aim: Float, achieve: Int) {
            var bracelet_id = SerialPortUtil.getTreadInstance().cardNo
            var period = SerialPortUtil.getTreadInstance().runTime
            var distance = SerialPortUtil.getTreadInstance().distance
            var cal = SerialPortUtil.getTreadInstance().kcal
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getReportExerciseDataRequest(
                    bracelet_id.toLong(), period, distance, cal, type, aimType, aim, achieve
            ))
        }

        override fun requestMembersCommand() {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getRequestMembersCommandRequest())
        }

        override fun membersStateReplyCommand() {
            mLKSocketClient.sendMessage(LKProtocolsHelperKt.getMembersStateReplyCommandRequest())
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mIBackService
    }

    override fun onDestroy() {
        super.onDestroy()
        mLKSocketClient.release()
    }
}