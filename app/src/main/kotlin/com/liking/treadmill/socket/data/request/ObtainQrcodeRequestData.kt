package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/10/10
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class ObtainQrcodeRequestData(@SerializedName("qrcode_type") var qrcodeType: Int) : BaseData()