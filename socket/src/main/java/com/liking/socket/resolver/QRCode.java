package com.liking.socket.resolver;

import com.liking.socket.Constant;
import com.liking.socket.SocketIO;
import com.liking.socket.receiver.CmdResolver;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public class QRCode extends CmdResolver<String> {

    @Override
    public byte cmd() {
        return Constant.CMD_QR_CODE_RESPONSE;
    }

    @Override
    public String callBack(String data, SocketIO sender) {
        System.out.println(">>>>>>>>>>>>" + data);
        return data;
    }
}
