package com.liking.treadmill.socket.data.result

import com.google.gson.annotations.SerializedName

/**
 * Created on 2017/10/11
 * desc: 场馆绑定返回
 *  {"gym_id":1,"gym_name":"复兴","type":1}
 *  type 1 绑定 2 解绑
 * @author: chenlei
 * @version:1.0
 */
class BindGymInfoResultData(
        @SerializedName("gym_id") var gymId: Int,
        @SerializedName("gym_name") var gymName: String,
        @SerializedName("type") var type: Int) : ResultData()