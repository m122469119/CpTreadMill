package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/08/10
 * desc: 跑步数据 返回
 * @author: chenlei
 * @version:1.0
 */
class ExerciseInfoResultData(@SerializedName("BraceletId") var braceletId: Long,
                              @SerializedName("RunTime") var runTime: Long) :BaseData()