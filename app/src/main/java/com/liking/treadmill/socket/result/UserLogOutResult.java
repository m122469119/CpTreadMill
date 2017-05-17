package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/05/17
 * desc: 用户退出返回
 *
 * @author: chenlei
 * @version:1.0
 */

public class UserLogOutResult extends BaseSocketResult {

    /**
     * msg_id : 0
     * data : {"bracelet_id":1233445}
     */

    @SerializedName("data")
    private LogOut data;

    public LogOut getData() {
        return data;
    }

    public void setData(LogOut data) {
        this.data = data;
    }

    public static class LogOut {
        /**
         * bracelet_id : 1233445
         * gym_id : 1233
         */

        @SerializedName("bracelet_id")
        private String braceletId;

        public String getBraceletId() {
            return braceletId;
        }

        public void setBraceletId(String braceletId) {
            this.braceletId = braceletId;
        }

    }
}
