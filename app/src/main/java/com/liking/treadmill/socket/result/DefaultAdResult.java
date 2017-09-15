package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;
import com.liking.treadmill.socket.data.BaseData;

import java.io.Serializable;
import java.util.List;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/8/28
 * @Version 1.0
 */
public class DefaultAdResult extends BaseSocketResult {


    /**
     * data : {"home":[{"url":"http: //testrun.likingfit.com/img/advertisement02.jpg","adv_id":1}],"login":[{"url":"http: //testrun.likingfit.com/img/advertisement02.jpg","adv_id":2}],"quick_start":[{"url":"http: //testrun.likingfit.com/img/advertisement02.jpg","adv_id":3}],"set_mode":[{"url":"http: //testrun.likingfit.com/img/advertisement02.jpg","adv_id":4}]}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        @SerializedName("home")
        private List<DefaultAdBean> home;
        @SerializedName("login")
        private List<DefaultAdBean> login;
        @SerializedName("quick_start")
        private List<DefaultAdBean> quick_start;
        @SerializedName("set_mode")
        private List<DefaultAdBean> set_mode;

        public List<DefaultAdBean> getHome() {
            return home;
        }

        public void setHome(List<DefaultAdBean> home) {
            this.home = home;
        }

        public List<DefaultAdBean> getLogin() {
            return login;
        }

        public void setLogin(List<DefaultAdBean> login) {
            this.login = login;
        }

        public List<DefaultAdBean> getQuick_start() {
            return quick_start;
        }

        public void setQuick_start(List<DefaultAdBean> quick_start) {
            this.quick_start = quick_start;
        }

        public List<DefaultAdBean> getSet_mode() {
            return set_mode;
        }

        public void setSet_mode(List<DefaultAdBean> set_mode) {
            this.set_mode = set_mode;
        }

        public static class DefaultAdBean implements Serializable {
            /**
             * url : http: //testrun.likingfit.com/img/advertisement02.jpg
             * adv_id : 1
             */
            @SerializedName("url")
            private String url;
            @SerializedName("staytime")
            private int staytime;//停留时间
            @SerializedName("interval")
            private int interval;//显示间隔
            @SerializedName("exhibition_id")
            private int exhibition_id;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getExhibition_id() {
                return exhibition_id;
            }

            public void setExhibition_id(int exhibition_id) {
                this.exhibition_id = exhibition_id;
            }

            public int getStaytime() {
                return staytime;
            }

            public void setStaytime(int staytime) {
                this.staytime = staytime;
            }

            public int getInterval() {
                return interval;
            }

            public void setInterval(int interval) {
                this.interval = interval;
            }
        }


    }
}
