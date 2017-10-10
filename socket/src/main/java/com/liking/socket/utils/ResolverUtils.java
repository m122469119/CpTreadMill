package com.liking.socket.utils;

import com.liking.socket.Constant;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by ttdevs
 * 2017-09-10 (SocketDemo)
 * https://github.com/ttdevs
 */
public class ResolverUtils {
    public static final int INDEX_PACKAGE_LENGTH = 0;
    public static final int INDEX_PROTOCOL_VERSION = 4;
    public static final int INDEX_APP_ID = 8;
    public static final int INDEX_APP_VERSION = 10;
    public static final int INDEX_MESSAGE_ID = 13;
    public static final int INDEX_CMD = 21;
    public static final int INDEX_SIGN = 22;
    public static final int INDEX_DATA = 42;

    private static final int LENGTH_HEADER = INDEX_DATA;

    public static final byte[] short2ByteArray(short value) {
        return new byte[]{(byte) (value >>> 8), (byte) value};
    }

    public static final byte[] int2ByteArray(int value) { // TODO: 2017/9/29  
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static final byte[] long2ByteArray(long value) { // TODO: 2017/9/29
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    /**
     * 解析2个字节成short
     *
     * @param data 数据源
     * @param from 从from向后2个字节
     * @return
     */
    public static short parseShort(byte[] data, int from) {
        return (short) ((data[from] & 0xFF) << 8 | (data[from + 1] & 0xFF));
    }

    /**
     * 解析4个字节成int
     *
     * @param data 数据源
     * @param from 从from向后4个字节
     * @return
     */
    public static int parseInt(byte[] data, int from) {
        return (data[from] & 0xFF) << 24 |
                (data[from + 1] & 0xFF) << 16 |
                (data[from + 2] & 0xFF) << 8 |
                (data[from + 3] & 0xFF);
    }

    public static long parseLong(byte[] data, int from) { // TODO: 2017/9/29  
        ByteBuffer LONG_BUFFER = ByteBuffer.allocate(8); 
        LONG_BUFFER.put(data, from, 8);
        LONG_BUFFER.flip(); //need flip
        return LONG_BUFFER.getLong();
    }

    /**
     * 数据包的大小 <br/>
     * 目前协议：开始的4个字节
     *
     * @param data 数据源
     * @return
     */
    public static int parsePkgSize(byte[] data) {
        return parseInt(data, INDEX_PACKAGE_LENGTH);
    }

    /**
     * 协议版本 <br/>
     * 目前协议：4个字节，如：V1.0
     *
     * @param data 数据源
     * @return V1.0
     */
    public static String parseProtocolVersion(byte[] data) {
        int size = INDEX_APP_ID - INDEX_PROTOCOL_VERSION;
        byte[] buffer = new byte[size];
        System.arraycopy(data, INDEX_PROTOCOL_VERSION, buffer, 0, size);
        return new String(buffer, Constant.DEFAULT_CHARSET);
    }

    /**
     * App的ID，short类型
     *
     * @param data 数据源
     * @return
     */
    public static short parseAppID(byte[] data) {
        return parseShort(data, INDEX_APP_ID);
    }

    /**
     * App的版本，客户端提供。版本格式按x.y.z提供，此值为xyz
     *
     * @param data 数据源
     * @return
     */
    public static String parseAppVersion(byte[] data) {
        int size = INDEX_MESSAGE_ID - INDEX_APP_VERSION;
        byte[] buffer = new byte[size];
        System.arraycopy(data, INDEX_APP_VERSION, buffer, 0, size);
        return new String(buffer, Constant.DEFAULT_CHARSET);
    }

    /**
     * 客户端自己维护的MessageID
     *
     * @param data 数据源
     * @return
     */
    public static long parseMessageId(byte[] data) {
        return parseLong(data, INDEX_MESSAGE_ID);
    }

    /**
     * 命令字，1字节
     *
     * @param data 数据源
     * @return
     */
    public static byte parseCmd(byte[] data) {
        return data[INDEX_CMD];
    }

    /**
     * 签名信息：<br/>
     * sha1(拼接(升序(cmd,msgID,data)))
     *
     * @param data 数据源
     * @return
     */
    public static byte[] parseSign(byte[] data) {
        int size = INDEX_DATA - INDEX_SIGN; // TODO: 2017/9/29 Why not System.arraycopy
        return Arrays.copyOfRange(data, INDEX_SIGN, INDEX_SIGN + size);
    }

    /**
     * 获取数据部分
     *
     * @param data
     * @param size
     * @return
     */
    public static String parseData(byte[] data, int size) {
        int dataSize = size - INDEX_DATA;
        byte[] buffer = new byte[dataSize];
        System.arraycopy(data, INDEX_DATA, buffer, 0, dataSize);
        return new String(buffer, StandardCharsets.UTF_8);
    }

    public static int getHeaderLength() {
        return LENGTH_HEADER;
    }
}
