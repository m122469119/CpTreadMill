package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.result.ResultData

/**
 * Created on 2017/10/10
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class QrcodeInfoResultData(@SerializedName("qrcode_url") var url: String,
                           @SerializedName("qrcode_type") var type: Int) : ResultData()