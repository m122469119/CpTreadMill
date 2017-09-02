package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/09/01
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class NotifyUserResult extends BaseSocketResult {

    /**
     * version : v1.0
     * msg_id : 0
     * data : {"name":"name","gender":1,"encounter":0}
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
         * name : name
         * gender : 1
         * encounter : 0
         */

        @SerializedName("name")
        private String name;
        @SerializedName("gender")
        private int gender;
        @SerializedName("encounter")
        private int encounter;
        @SerializedName("type")
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public int getEncounter() {
            return encounter;
        }

        public void setEncounter(int encounter) {
            this.encounter = encounter;
        }
    }
}
