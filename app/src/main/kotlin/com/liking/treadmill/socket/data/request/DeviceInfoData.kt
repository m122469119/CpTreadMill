package com.liking.treadmill.socket.data.request

import com.google.gson.annotations.SerializedName
import com.liking.treadmill.socket.data.BaseData

/**
 * Created on 2017/09/30
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class DeviceInfoData(@SerializedName("device_id") var deviceId: String,
                     @SerializedName("gateway_id") var gatewayId: String,
                     @SerializedName("device_name") var deviceName: String,
                     @SerializedName("control_num") var controlNum: Int,
                     @SerializedName("online_status") var onlineStatus: Int,
                     @SerializedName("battery_status") var batteryStatus: Int,
                     @SerializedName("device_status") var deviceStatus: Int) : BaseData()