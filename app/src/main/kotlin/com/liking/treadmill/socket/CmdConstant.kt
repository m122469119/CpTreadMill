package com.liking.treadmill.socket

/**
 * Created on 2017/09/29
 * desc:
 * @author: chenlei
 * @version:1.0
 */
object CmdConstant {

    val CMD_NORMAL: Byte = 0x00

    /*时间戳*/
    val CMD_TIMESTAMP: Byte = 0x66

    /*获取二维码*/
    val CMD_OBTAIN_QRCODE: Byte = 0x68

    /*更新*/
    val CMD_UPDATE: Byte = 0x6A

    /*跑步机基础信息*/
    val CMD_TREADMILL: Byte = 0xa2.toByte()

    /*上报锻炼数据*/
    val CMD_REPORT_DATA: Byte = 0xa3.toByte()

    /*手环登录*/
    val CMD_TREADMILL_LOGIN: Byte = 0xa0.toByte()

    /*手环退出*/
    val CMD_TTREADMILL_LOGOUT: Byte = 0xa1.toByte()
}