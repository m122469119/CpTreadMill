package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2017/09/01
 * desc: 马拉松排行榜
 *
 * @author: chenlei
 * @version:1.0
 */

public class MarathonRankHotResult extends BaseSocketResult {
    /**
     * msg_id : 0
     * data : {"list":[{"bracelet_id":134,"marathon_id":1,"distance":123,"use_time":12,"username":"liking","gender":1,"avatar ":"http: //testimg.324.png"}],"count":1,"my_num":1,"front":[{"bracelet_id":134,"marathon_id":1,"distance":123,"use_time":12,"username":"liking","gender":1,"avatar":"http: //testimg.324.png"}]}
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
         * list : [{"bracelet_id":134,"marathon_id":1,"distance":123,"use_time":12,"username":"liking","gender":1,"avatar ":"http: //testimg.324.png"}]
         * count : 1
         * my_num : 1
         * front : [{"bracelet_id":134,"marathon_id":1,"distance":123,"use_time":12,"username":"liking","gender":1,"avatar":"http: //testimg.324.png"}]
         */

        @SerializedName("count")
        private int count;
        @SerializedName("my_num")
        private int myNum;
        @SerializedName("list")
        private List<ListBean> list;
        @SerializedName("front")
        private List<FrontBean> front;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getMyNum() {
            return myNum;
        }

        public void setMyNum(int myNum) {
            this.myNum = myNum;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<FrontBean> getFront() {
            return front;
        }

        public void setFront(List<FrontBean> front) {
            this.front = front;
        }

        public static class ListBean {
            /**
             * bracelet_id : 134
             * marathon_id : 1
             * distance : 123
             * use_time : 12
             * username : liking
             * gender : 1
             * avatar  : http: //testimg.324.png
             */

            @SerializedName("bracelet_id")
            private String braceletId;
            @SerializedName("marathon_id")
            private String marathonId;
            @SerializedName("distance")
            private String distance;
            @SerializedName("use_time")
            private int useTime;
            @SerializedName("username")
            private String username;
            @SerializedName("gender")
            private int gender;
            @SerializedName("avatar")
            private String avatar;
            private String ranking_number;

            public String getBraceletId() {
                return braceletId;
            }

            public void setBraceletId(String braceletId) {
                this.braceletId = braceletId;
            }

            public String getMarathonId() {
                return marathonId;
            }

            public void setMarathonId(String marathonId) {
                this.marathonId = marathonId;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public int getUseTime() {
                return useTime;
            }

            public void setUseTime(int useTime) {
                this.useTime = useTime;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getRanking_number() {
                return ranking_number;
            }

            public void setRanking_number(String ranking_number) {
                this.ranking_number = ranking_number;
            }
        }

        public static class FrontBean {
            /**
             * bracelet_id : 134
             * marathon_id : 1
             * distance : 123
             * use_time : 12
             * username : liking
             * gender : 1
             * avatar : http: //testimg.324.png
             */

            @SerializedName("bracelet_id")
            private String braceletId;
            @SerializedName("marathon_id")
            private String marathonId;
            @SerializedName("distance")
            private String distance;
            @SerializedName("use_time")
            private String useTime;
            @SerializedName("username")
            private String username;
            @SerializedName("gender")
            private int gender;
            @SerializedName("avatar")
            private String avatar;

            public String getBraceletId() {
                return braceletId;
            }

            public void setBraceletId(String braceletId) {
                this.braceletId = braceletId;
            }

            public String getMarathonId() {
                return marathonId;
            }

            public void setMarathonId(String marathonId) {
                this.marathonId = marathonId;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getUseTime() {
                return useTime;
            }

            public void setUseTime(String useTime) {
                this.useTime = useTime;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
