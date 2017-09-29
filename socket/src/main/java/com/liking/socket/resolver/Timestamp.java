package com.liking.socket.resolver;

import com.liking.socket.Constant;
import com.liking.socket.SocketIO;
import com.liking.socket.receiver.CmdResolver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ttdevs
 * 2017-09-29 (Socket)
 * https://github.com/ttdevs
 */
public class Timestamp extends CmdResolver<String> {
    @Override
    public byte cmd() {
        return Constant.CMD_TIMESTAMP;
    }

    @Override
    public String callBack(String data, SocketIO client) {
        JSONObject timestamp = null;
        try {
            timestamp = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timestamp.toString();
    }
}
