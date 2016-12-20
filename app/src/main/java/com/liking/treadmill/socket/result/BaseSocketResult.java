package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class BaseSocketResult implements Serializable {

    /**
     * type : login
     * data : {"device_id":"84647269447bf4c8"}
     */

    @SerializedName("type")
    private String mType;
    @SerializedName("msg_id")
    private String msgId;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
