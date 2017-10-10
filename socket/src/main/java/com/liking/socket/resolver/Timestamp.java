package com.liking.socket.resolver;

import android.os.SystemClock;

import com.liking.socket.Constant;
import com.liking.socket.SocketIO;
import com.liking.socket.receiver.CmdResolver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置系统时间
 *
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
        try {
            JSONObject json = new JSONObject(data);
            long timestamp = json.optLong("timestamp");
            SystemClock.setCurrentTimeMillis(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
