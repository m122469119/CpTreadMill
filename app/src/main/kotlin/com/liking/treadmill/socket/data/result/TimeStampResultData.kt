package com.liking.treadmill.socket.data.result

import com.google.gson.annotations.SerializedName

/**
 * Created on 2017/09/29
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class TimeStampResultData(@SerializedName("device_id") var deviceId: String,
                          @SerializedName("timestamp") var timestamp: Long) : ResultData()