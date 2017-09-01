package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/09/01
 * desc: 获取当前用户马拉松状态
 * @author: chenlei
 * @version:1.0
 */
class UserMarathonInfoData(@SerializedName("bracelet_id") var bracelet_id: String,
                           @SerializedName("marathon_id") var marathon_id: String,
                           @SerializedName("gym_id") var gym_id: String,
                           @SerializedName("device_id") var device_id: String) : BaseData()