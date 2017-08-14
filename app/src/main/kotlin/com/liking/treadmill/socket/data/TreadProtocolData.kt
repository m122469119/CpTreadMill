package com.liking.treadmill.socket.data

import com.google.gson.annotations.SerializedName

/**
 * Created on 2017/08/09
 * desc:

 * @author: chenlei
 * *
 * @version:1.0
 */

data class TreadProtocolData<T>(@SerializedName("type") var type: String,
                                @SerializedName("msg_id") var msg_id: String?,
                                @SerializedName("version") var version: String,
                                @SerializedName("data") var t: T?) : BaseData()