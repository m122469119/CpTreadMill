package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/08/10
 * desc: 跑步数据
 * @author: chenlei
 * @version:1.0
 */
class ExerciseInfoData(@SerializedName("bracelet_id") var braceletId: Long,
                   @SerializedName("period") var period: Int,
                   @SerializedName("distance") var distance: Float,
                   @SerializedName("cal") var cal: Float,
                   @SerializedName("type") var type: Int,
                   @SerializedName("aim_type") var aimType: Int,
                   @SerializedName("aim") var aim: Float,
                   @SerializedName("achieve") var achieve: Int,
                   @SerializedName("run_time") var runTime: Long) :BaseData()