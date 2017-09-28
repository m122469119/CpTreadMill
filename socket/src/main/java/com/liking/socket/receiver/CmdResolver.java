package com.liking.socket.receiver;

import com.liking.socket.SocketIO;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public abstract class CmdResolver<T> {

    /**
     * 解析的命令字
     *
     * @return
     */
    public abstract byte cmd();

    public abstract T callBack(byte[] data, SocketIO client);

    public String finish() {
        return "";
    }
}
