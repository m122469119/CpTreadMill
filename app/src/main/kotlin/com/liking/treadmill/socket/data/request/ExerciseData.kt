package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/08/10
 * desc: 跑步数据
 * @author: chenlei
 * @version:1.0
 */
class ExerciseData(@SerializedName("bracelet_id") var braceletId: Long,
                   @SerializedName("gym_id") var gymId: String,
                   @SerializedName("device_id") var deviceId: String,
                   @SerializedName("period") var period: String,
                   @SerializedName("distance") var distance: String,
                   @SerializedName("cal") var cal: String,
                   @SerializedName("type") var type: String,
                   @SerializedName("aim_type") var aimType: String,
                   @SerializedName("aim") var aim: String,
                   @SerializedName("achieve") var achieve: String,
                   @SerializedName("timestamp") var timestamp: String) :BaseData()