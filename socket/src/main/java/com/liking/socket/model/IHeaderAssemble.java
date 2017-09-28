package com.liking.socket.model;

import com.liking.socket.model.message.MessageData;

/**
 * Created by ttdevs
 * 2017-09-19 (Socket)
 * https://github.com/ttdevs
 */
public interface IHeaderAssemble {
    /**
     * 根据MessageData，构造Header
     *
     * @param data
     * @return
     */
    byte[] assemble(MessageData data);
}
