package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/08/10
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class MemberListData(@SerializedName("id") var id: String,
                     @SerializedName("gym_id") var gymId: String) : BaseData()