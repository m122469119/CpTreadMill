package com.liking.treadmill.utils

import android.content.Context
import android.net.wifi.WifiManager

/**
 * Created on 2017/7/13
 * Created by sanfen
 *
 * @version 1.0.0
 */
object Mac {
    fun getMacAddress(context: Context): String {
        try {
            var mac = ""
            // 获取wifi管理器
            val wifiMng = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfor = wifiMng.connectionInfo
            mac = wifiInfor.macAddress
            return mac
        } catch (e: Exception) {
            return ""
        }
    }
}