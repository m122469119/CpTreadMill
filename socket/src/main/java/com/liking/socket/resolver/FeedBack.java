package com.liking.socket.resolver;

import com.liking.socket.Constant;
import com.liking.socket.SocketIO;
import com.liking.socket.receiver.CmdResolver;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ttdevs
 * 2017-09-26 (Socket)
 * https://github.com/ttdevs
 */
public class FeedBack extends CmdResolver<String> {
    private String mMsgID;

    @Override
    public byte cmd() {
        return Constant.CMD_FEEDBACK;
    }

    @Override
    public String callBack(byte[] data, SocketIO client) {
        // {"msg_id":"2072072300","device_id":"2072072300"}
        String result = new String(data, Constant.DEFAULT_CHARSET);
        JSONObject feedback = null;
        try {
            feedback = new JSONObject(result);
            mMsgID = feedback.optString("msg_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMsgID;
    }

    @Override
    public String finish() {
        return mMsgID;
    }
}
