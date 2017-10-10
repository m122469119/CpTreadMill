package com.liking.socket.model.message;

import com.liking.socket.Constant;
import com.liking.socket.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ttdevs
 * 2017-09-21 (Socket)
 * https://github.com/ttdevs
 */
public class PingPongMsg extends MessageData {
    public static final String DEVICE_ID = "device_id";

    private int mRetryTime = Constant.DEFAULT_MSG_RETRY_TIME;

    public PingPongMsg() {

    }

    @Override
    public boolean canRetry() {
        return mRetryTime > 0;
    }

    @Override
    public void retry() {
        mRetryTime--;
    }

    public void reset() {
        mRetryTime = Constant.DEFAULT_MSG_RETRY_TIME;
    }

    @Override
    public byte cmd() {
        return Constant.CMD_PING_PONG;
    }

    @Override
    public byte[] getData() {
        JSONObject json = new JSONObject();
        try {
            json.put(DEVICE_ID, SystemUtils.getDeviceID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString().getBytes();
    }
}
