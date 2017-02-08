package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * 说明: 下发服务器时间返回
 * Author: chenlei
 * Time: 上午9:56
 */

public class ServiceTimeResult extends BaseSocketResult {

    @SerializedName("data")
    private ServiceTime mServiceTime;

    public ServiceTime getServiceTime() {
        return mServiceTime;
    }

    public void setServiceTime(ServiceTime serviceTime) {
        mServiceTime = serviceTime;
    }

    public static class ServiceTime {

        @SerializedName("time")
        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
