package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/08/10
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class TreadmillData(@SerializedName("device_id") var deviceId: String ,
                    @SerializedName("gym_id") var gymId: String ,
                    @SerializedName("mac") var mac: String ,
                    @SerializedName("app_version") var appVersion: String ,
                    @SerializedName("total_distance") var totalDistance: String ,
                    @SerializedName("total_time") var totalTime: String ,
                    @SerializedName("power_times") var powerTimes: String ,
                    @SerializedName("member_status") var memberStatus: String,
                    @SerializedName("member_time") var memberTime: Long) : BaseData()