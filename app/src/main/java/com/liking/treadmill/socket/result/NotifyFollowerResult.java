package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/09/01
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class NotifyFollowerResult extends BaseSocketResult {

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
         * name : name
         * gender : 1
         */

        @SerializedName("name")
        private String name;
        @SerializedName("gender")
        private int gender;
        @SerializedName("avatar")
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }
    }
}
