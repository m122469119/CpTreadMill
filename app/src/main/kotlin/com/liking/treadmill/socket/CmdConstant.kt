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

    /*获取会员列表*/
    val CMD_OBTAIN_MEMBER_LIST: Byte = 0x67

    /*清空本地会员数据*/
    val CMD_CLEAR_USER_DATA: Byte = 0x6D

    /*下发服务列表*/
    val CMD_SERVICE_LIST: Byte = 0x6D

    /*获取二维码*/
    val CMD_OBTAIN_QRCODE: Byte = 0x68

    /*上传Log命令*/
    val CMD_LOG: Byte = 0x69

    /*更新*/
    val CMD_UPDATE: Byte = 0x6A

    /*跑步机场馆绑定状态*/
    val CMD_GYM_BIND: Byte = 0x71

    /*跑步机基础信息*/
    val CMD_TREADMILL: Byte = 0xa2.toByte()

    /*上报锻炼数据*/
    val CMD_REPORT_DATA: Byte = 0xa3.toByte()

    /*手环登录*/
    val CMD_TREADMILL_LOGIN: Byte = 0xa0.toByte()

    /*手环退出*/
    val CMD_TTREADMILL_LOGOUT: Byte = 0xa1.toByte()
}