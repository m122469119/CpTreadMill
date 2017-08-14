package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/08/09
 * desc:

 * @author: chenlei
 * *
 * @version:1.0
 */

class RequestDataWrapper(@SerializedName("data") val data: String,
                         @SerializedName("app_version") val appVersion: String,
                         @SerializedName("version") val version: String) : BaseData()