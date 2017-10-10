package com.liking.socket.model;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public interface IHeaderResolver {

    /**
     * 解析自定义协议头
     *
     * @param data
     * @return
     */
    void resolver(byte[] data);

    /**
     * 获取自定义协议头的长度
     *
     * @return 除body之外的所有长度
     */
    int headerLength();


    /**
     * 获取body部分的长度，这个在resolver()中解析得到
     *
     * @return
     */
    int bodyLength();

    /**
     * 获取命令字
     *
     * @return
     */
    byte getCmd();

    /**
     * 服务器返回：服务器消息ID
     *
     * @return
     */
    long getMsgID();
}
