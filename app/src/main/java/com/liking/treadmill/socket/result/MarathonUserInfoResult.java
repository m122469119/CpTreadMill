package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2017/09/01
 * desc: 马拉松用户信息
 *
 * @author: chenlei
 * @version:1.0
 */

public class MarathonUserInfoResult extends BaseSocketResult {

    /**
     *  type : user_marathon
     * msg_id : 0
     * data : {"first":false,"last_data":{"bracelet_id":123,"use_time":2134,"distance":100,"cal":100}}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * first : false
         * last_data : {"bracelet_id":123,"use_time":2134,"distance":100,"cal":100}
         */

        @SerializedName("first")
        private boolean first;
        @SerializedName("last_data")
        private LastDataBean lastData;

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public LastDataBean getLastData() {
            return lastData;
        }

        public void setLastData(LastDataBean lastData) {
            this.lastData = lastData;
        }

        public static class LastDataBean {
            /**
             * bracelet_id : 123
             * use_time : 2134
             * distance : 100
             * cal : 100
             */

            @SerializedName("bracelet_id")
            private String braceletId;
            @SerializedName("use_time")
            private Long useTime;
            @SerializedName("end_time")
            private Long endTime;
            @SerializedName("distance")
            private int distance;
            @SerializedName("cal")
            private Float cal;

            public String getBraceletId() {
                return braceletId;
            }

            public void setBraceletId(String braceletId) {
                this.braceletId = braceletId;
            }

            public Long getUseTime() {
                return useTime;
            }

            public void setUseTime(Long useTime) {
                this.useTime = useTime;
            }

            public Long getEndTime() {
                return endTime;
            }

            public void setEndTime(Long endTime) {
                this.endTime = endTime;
            }

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public Float getCal() {
                return cal;
            }

            public void setCal(Float cal) {
                this.cal = cal;
            }
        }
    }
}
