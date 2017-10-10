package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/10/10
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class ObtainQrcodeData(@SerializedName("device_id") var deviceId: String,
                       @SerializedName("device_type") var deviceType: Int,
                       @SerializedName("qrcode_type") var qrcodeType: Int,
                       @SerializedName("qrcode_url") var qrcodeUrl: Int) : BaseData()