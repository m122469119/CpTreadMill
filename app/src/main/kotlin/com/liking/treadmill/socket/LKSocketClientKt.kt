package com.liking.treadmill.socket

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import com.aaron.android.codelibrary.utils.LogUtils
import java.io.IOException
import java.io.InputStream
import java.net.Socket
import java.util.*


/**
 * Created on 2017/5/22
 * Created by sanfen
 *
 * @version 1.0.0
 */
class LKSocketClientKt private constructor(private val mHost: String,
                                           private val mPort: Int,
                                           private var mIsReconnect: Boolean,
                                           private val mHeartEntity: HeartEntity<Any>?,
                                           private var mCheckIsResult: Long) {

    private val TAG = "LKSocketClient"
    private var mSocket: Socket? = null
    private var mCallback: LKSocketCallback = object : LKSocketCallback {
        override fun onOpen() {
            Log.e(TAG, "open")
        }

        override fun onMessage(inputStream: InputStream): Boolean {
            return true
        }

        override fun onReconnect(count: Int) {
            Log.e(TAG, "onReconnect: $count")
        }

        override fun onClose() {
            Log.e(TAG, "onClose")
        }

        override fun onRelease() {
            Log.e(TAG, "onRelease")
        }
    }

    private var mSendTime = 0L
    private var mCount = 0

    private var mReadThread: ReadThread? = null

    private val HEART_BEAT_MESSAGE = 1
    private val RE_CONNECT_MESSAGE = 2
    private val SEND_MSG = 3

    private var mSendHandlerThread: HandlerThread? = null
    private var mHandler: Handler? = null

    private var mIsStart = false

    @Volatile private var mIsReconnecting = false

    private var mLastPoneTime: Long = 0
    private var mReConnectRuleListener: ReConnectRuleListener? = null


    fun setCallback(mCallback: LKSocketCallback?) {
        if (mCallback != null)
            this.mCallback = mCallback
    }

    fun setReConnectRuleListener(mReConnectRuleListener: ReConnectRuleListener) {
        this.mReConnectRuleListener = mReConnectRuleListener
    }

    fun sendMessage(json: String?) {
        return sendMessage(listOf(json!!.toByteArray()))
    }


    fun sendMessage(json: List<ByteArray>?) {
        if (!mIsStart) {
            return
        }
        val msg = mHandler?.obtainMessage(SEND_MSG) ?: return
        msg.obj = json
        mHandler?.sendMessage(msg)
    }


    fun start() {
        if (mIsStart) {
            return
        }
        mIsStart = true
        mSendHandlerThread = HandlerThread("socket:${UUID.randomUUID()}")
        mSendHandlerThread!!.start()
        mHandler = object : Handler(mSendHandlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == HEART_BEAT_MESSAGE) { //心跳包
                    if (System.currentTimeMillis() - mSendTime >= msg.arg1) {
                        val isSuccess = sendMsg(msg.obj as List<ByteArray>)
                        if (!isSuccess) {
                            reConnectSocket()
                        } else {
                            mCount += 1
                        }
                    }
                    sendHeartMessageDelayed()
                } else if (msg.what == RE_CONNECT_MESSAGE) { // 重连
                    reConnectSocket()
                } else if (msg.what == SEND_MSG) {
                    val sendMessage = msg.obj as List<ByteArray>
                    sendMsg(sendMessage)
                }
            }
        }
        CheckIsStopThread().start()
        InitSocketThread().start()
    }

    private fun sendMsg(list: List<ByteArray>): Boolean {
        if (null == mSocket) {
            return false
        }
        try {
            if (!mSocket!!.isClosed && !mSocket!!.isOutputShutdown) {
                val os = mSocket!!.getOutputStream()
                for (byteArray in list) {
                    os.write(byteArray)
                }
                os.flush()
                mSendTime = System.currentTimeMillis()//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
            } else {
                return false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun release() {
        this.mIsReconnect = false
        mIsStart = false
        mHandler?.removeCallbacksAndMessages(null)
        mSendHandlerThread?.quit()
        mCallback.onRelease()
    }

    private var mReCount = 0 //重连次数
    private var mSleepTime: Long = 0 //重连间隔时间
    private val MAX_ALARM_SLEEP_TIME = 20 * 1000 // 最大间隔时间
    private val MID_ALARM_COUNT = 5   //前5次是5秒重连一次，之后是20秒重连一次
    private val MAX_ALARM_COUNT = 20  // 报警次数
    private val SLEEP_TIME_DIFF: Long = 1000  // 每次添加1s
    private val MAX_SLEEP_TIME = (5 * 1000).toLong() //重试次数

    private fun sendReConnectMessage() {
        if (mIsReconnecting || mHandler == null) {
            return
        }
        mIsReconnecting = true
        mHandler?.removeCallbacksAndMessages(null)
        val reconnect = getReconnect()
        mCallback.onReconnect(mReCount)
        val message = mHandler?.obtainMessage(RE_CONNECT_MESSAGE)
        mHandler?.sendMessageDelayed(message, reconnect)
    }


    private fun getReconnect(): Long {
        if (mReConnectRuleListener != null) {
            return mReConnectRuleListener!!.reConnectRule(mReCount)
        }
        if (mSleepTime < MAX_SLEEP_TIME) {
            mSleepTime += SLEEP_TIME_DIFF
        }
        mReCount++
        if (mReCount in (MID_ALARM_COUNT + 1)..(MAX_ALARM_COUNT - 1)) {
            mSleepTime = MAX_ALARM_SLEEP_TIME.toLong()
        } else if (mReCount > MAX_ALARM_COUNT) {
            mSleepTime = MAX_ALARM_SLEEP_TIME.toLong()
        }
        return mSleepTime
    }


    private fun reConnectSocket() {
        mHandler?.removeCallbacksAndMessages(null)
        mReadThread?.release()
        if (!mIsReconnect) {
            return
        }
        InitSocketThread().start()
    }

    private inner class InitSocketThread : Thread() {
        override fun run() {
            super.run()
            initSocket()
        }
    }

    fun createHeartEntity(): List<ByteArray> {
        val msg: Any = mHeartEntity!!.createHeartEntity()!!
        if (msg is List<*>) {
            if (msg.size > 0) {
                if (msg[0] !is ByteArray) {
                    return listOf(msg.toString().toByteArray())
                }
            }
            return msg as List<ByteArray>
        } else {
            return listOf(msg.toString().toByteArray())
        }
    }

    private fun sendHeartMessageDelayed() {
        val message = mHandler!!.obtainMessage(HEART_BEAT_MESSAGE)
        message.obj = createHeartEntity()
        if (mCount == 0) {
            mHandler?.sendMessageDelayed(message, 1000)
        } else {
            val heartTime = mHeartEntity!!.createHeartTime()
            message.arg1 = heartTime.toInt()
            mHandler?.sendMessageDelayed(message, heartTime)
        }
    }

    private fun initSocket() {//初始化Socket
        try {
            mSocket = Socket(mHost, mPort)
            mReadThread = ReadThread(mSocket!!)
            mReadThread!!.start()
            mCount = 0
            mSendTime = 0
            mCallback.onOpen()
            if (mHeartEntity != null) {
                sendHeartMessageDelayed()//初始化成功后，就准备发送心跳
            }
            mIsReconnecting = false
        } catch (e: IOException) {
            mIsReconnecting = false
            e.printStackTrace()
            sendReConnectMessage()
        }
    }

    /**
     * 检查是否有数据返回， 如果长时间没有数据，则表示断开连接
     */
    private inner class CheckIsStopThread : Thread() {
        internal var currentTime: Long = 0
        override fun run() {
            while (mIsReconnect)
                try {
                    Thread.sleep(mCheckIsResult)
                    if (currentTime == mLastPoneTime) {
                        sendReConnectMessage()
                    }
                    currentTime = mLastPoneTime
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
        }
    }


    /**
     * 接收数据的线程
     */
    private inner class ReadThread internal constructor(private var socket: Socket) : Thread() {
        internal fun release() {
            try {
                if (!socket.isClosed) {
                    socket.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun run() {
            super.run()
            try {
                val input = socket.getInputStream()
                while (!socket.isClosed
                        && !socket.isInputShutdown
                        && socket.isConnected
                        && mIsReconnect) {
                    mSleepTime = 0
                    mReCount = 0
                    mLastPoneTime = System.currentTimeMillis()
                    if (!mCallback.onMessage(input)) {
                        return
                    }
                }
            } catch (e: IOException) {
                LogUtils.e(TAG, "cmd" + e.toString())
                e.printStackTrace()
            } finally {
                mCallback.onClose()
                if (mIsReconnect)
                    sendReConnectMessage()
            }
        }
    }

    class LkSocketClientBuilder {
        private var mHost = ""
        private var mPort = 0
        private var mIsReconnect = true
        private var mHeartEntity: HeartEntity<Any>? = null
        private var mCheckIsResult = (3 * 60 * 1000).toLong()


        fun setHost(host: String): LkSocketClientBuilder {
            this.mHost = host
            return this
        }

        fun setPort(port: Int): LkSocketClientBuilder {
            this.mPort = port
            return this
        }

        fun setIsReconnect(isReconnect: Boolean): LkSocketClientBuilder {
            this.mIsReconnect = isReconnect
            return this
        }

        fun setHeartEntity(entity: HeartEntity<Any>): LkSocketClientBuilder {
            this.mHeartEntity = entity
            return this
        }

        fun setCheckIsResult(checkIsResult: Long): LkSocketClientBuilder {
            this.mCheckIsResult = checkIsResult
            return this
        }

        fun build(): LKSocketClientKt {
            return LKSocketClientKt(mHost, mPort, mIsReconnect, mHeartEntity, mCheckIsResult)
        }
    }


    interface HeartEntity<out T> {
        fun createHeartEntity(): T?
        //可以设计成每次心跳的时间不同，防止客户端同时发心跳
        fun createHeartTime(): Long
    }

    interface LKSocketCallback {
        fun onOpen()
        fun onMessage(inputStream: InputStream): Boolean
        fun onReconnect(count: Int)
        fun onClose()
        fun onRelease()
    }

    interface ReConnectRuleListener {
        //复写重连机制
        fun reConnectRule(count: Int): Long
    }
}