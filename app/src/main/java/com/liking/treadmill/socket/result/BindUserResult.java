package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午8:19
 * version 1.0.0
 */

public class BindUserResult extends BaseSocketResult{


    /**
     * version : v1.0
     * data : {"err_code":0,"err_msg":"success","gym_id":1221}
     */

    @SerializedName("version")
    private String version;
    @SerializedName("data")
    private BindUserData data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BindUserData getData() {
        return data;
    }

    public void setData(BindUserData data) {
        this.data = data;
    }

    public static class BindUserData {
        /**
         * err_code : 0
         * err_msg : success
         * gym_id : 1221
         */

        @SerializedName("err_code")
        private int errCode;
        @SerializedName("err_msg")
        private String errMsg;
        @SerializedName("gym_id")
        private String gymId;

        public int getErrCode() {
            return errCode;
        }

        public void setErrCode(int errCode) {
            this.errCode = errCode;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public String getGymId() {
            return gymId;
        }

        public void setGymId(String gymId) {
            this.gymId = gymId;
        }
    }
}
