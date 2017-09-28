package com.liking.socket.resolver;

import com.liking.socket.Constant;
import com.liking.socket.SocketIO;
import com.liking.socket.model.message.PingPongMsg;
import com.liking.socket.receiver.CmdResolver;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public class PingPong extends CmdResolver<String> {

    @Override
    public byte cmd() {
        return Constant.CMD_PING_PONG;
    }

    @Override
    public String callBack(String data,  SocketIO client) {
        System.out.println("*******************" + data);
        // sendResponse(sender); // TODO: 2017/9/26
        return data;
    }

    /**
     * 发Pong
     *
     * @param sender
     */
    private void sendResponse(SocketIO sender) {
        sender.send(new PingPongMsg());
    }
}
