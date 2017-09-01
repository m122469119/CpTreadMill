package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/09/01
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class MarathonRankData(@SerializedName("bracelet_id") var bracelet_id: String,
                       @SerializedName("marathon_id") var marathon_id: String,
                       @SerializedName("use_time") var use_time: String,
                       @SerializedName("distance") var distance: String,
                       @SerializedName("cal") var cal: String) : BaseData()