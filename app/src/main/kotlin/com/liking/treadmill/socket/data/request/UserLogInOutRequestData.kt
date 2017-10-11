package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/10/11
 * desc: 用户登录、登出请求
 * @author: chenlei
 * @version:1.0
 */
class UserLogInOutRequestData(@SerializedName("gym_id") var gymId: Int,
                              @SerializedName("timestamp") var timestamp: Long,
                              @SerializedName("device_id") var deviceId: String,
                              @SerializedName("bracelet_id") var braceletId: Long) : BaseData()