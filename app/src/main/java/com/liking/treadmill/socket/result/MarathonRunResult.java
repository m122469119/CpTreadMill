package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2017/09/01
 * desc: 马拉松活动
 *
 * @author: chenlei
 * @version:1.0
 */

public class MarathonRunResult extends BaseSocketResult {


    @SerializedName("data")
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * marathon_name : 马拉松15公里跑
         * distance : 1000
         * start_date : 20170902
         * end_date : 20170904
         * limit_time : 7200
         * max_speed : 9
         * min_speed : 1
         */

        @SerializedName("id")
        private String id;
        @SerializedName("marathon_name")
        private String marathonName;
        @SerializedName("distance")
        private String distance;
        @SerializedName("start_date")
        private String startDate;
        @SerializedName("end_date")
        private String endDate;
        @SerializedName("limit_time")
        private String limitTime;
        @SerializedName("max_speed")
        private String maxSpeed;
        @SerializedName("min_speed")
        private String minSpeed;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMarathonName() {
            return marathonName;
        }

        public void setMarathonName(String marathonName) {
            this.marathonName = marathonName;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getLimitTime() {
            return limitTime;
        }

        public void setLimitTime(String limitTime) {
            this.limitTime = limitTime;
        }

        public String getMaxSpeed() {
            return maxSpeed;
        }

        public void setMaxSpeed(String maxSpeed) {
            this.maxSpeed = maxSpeed;
        }

        public String getMinSpeed() {
            return minSpeed;
        }

        public void setMinSpeed(String minSpeed) {
            this.minSpeed = minSpeed;
        }
    }
}
