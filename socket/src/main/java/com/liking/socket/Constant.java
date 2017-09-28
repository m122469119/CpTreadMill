package com.liking.socket;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public class Constant {

    public static final String KEY_SIGN = "keykeykeykeykey2"; // sign sha1(MsgID+Data+AppKey)
    public static final String KEY_DATA = "keykeykeykeykey1"; // aes256

    public static final String Protocol_Version = "V1.0";
    public static final String APP_VERSION = "1.0.0"; // x.y.z
    public static final short APP_ID = 10001;

    public static final int DEFAULT_RECONNECT = 3 * 1000; // 错误重连间隔
    public static final int DEFAULT_RETRY = 3 * 1000; // 错误重试间隔
    public static final int DEFAULT_PING_PONG = 60 * 1000; // 心跳间隔 60s

    public static final int SENDER_QUEUE_SIZE = 20;
    public static final int HEADER_SIZE = 4;

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static final byte CMD_PING_PONG = 0x64;
    public static final byte CMD_DEVICE_INFO = 0x65;
    public static final byte CMD_FEEDBACK = 0x6F;
    public static final byte CMD_QR_CODE_REQUEST = (byte) 0xA0;
    public static final byte CMD_QR_CODE_RESPONSE = (byte) 0x69;
}
