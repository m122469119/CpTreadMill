package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/08/10
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class UserLogInOutData(@SerializedName("bracelet_id") var braceletId: Long,
                       @SerializedName("timestamp") var timestamp: String,
                       @SerializedName("device_id") var deviceId: String,
                       @SerializedName("gym_id") var gymId: String) : BaseData()