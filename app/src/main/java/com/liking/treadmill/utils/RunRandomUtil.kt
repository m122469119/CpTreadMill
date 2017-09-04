package com.liking.treadmill.utils


import android.os.Handler
import android.os.Message

/**
 * Created by aaa on 17/9/3.
 * 处理随机数工具类
 */

object RunRandomUtil {

    internal var lastCalculate = true
    internal var mAllNumber = 239183

    internal var mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            100001 -> {
                val num = randomRunNumber
                if (lastCalculate) {
                    lastCalculate = false
                    mAllNumber += num
                } else {
                    lastCalculate = true
                    mAllNumber -= num
                }
                setRandomNumber()
            }
        }
        false
    })

    /**
     * 获取1-100以内的随机数
     *
     * @return
     */
    private val randomRunNumber: Int
        get() = (1 + Math.random() * 100).toInt()

    fun setRandomNumber() {
        mHandler.postDelayed({
            val message = Message()
            message.what = 100001
            mHandler.sendMessage(message)
        }, (1000 * 10).toLong())
    }


}
