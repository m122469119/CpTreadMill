package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/08/10
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class TreadmillInfoData(@SerializedName("total_distance") var totalDistance: Int ,
                    @SerializedName("total_time") var totalTime: Int ,
                    @SerializedName("power_times") var powerTimes: Int ,
                    @SerializedName("member_status") var memberStatus: Int,
                    @SerializedName("member_time") var memberTime: Long) : BaseData()