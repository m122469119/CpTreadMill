package com.liking.socket.receiver;

import com.liking.socket.SocketIO;

import java.io.Serializable;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public abstract class CmdResolver<T> implements Serializable {

    /**
     * 解析的命令字
     *
     * @return
     */
    public abstract byte cmd();

    public abstract T callBack(String data, SocketIO client);
}
