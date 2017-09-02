package com.liking.treadmill.socket.result;

import java.io.Serializable;
import java.util.List;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/8/28
 * @Version 1.0
 */
public class NewAdResult extends BaseSocketResult{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        List<NewAdBean> home;
        List<NewAdBean> login;
        List<NewAdBean> quick_start;
        List<NewAdBean> set_mode;

        public List<NewAdBean> getHome() {
            return home;
        }

        public void setHome(List<NewAdBean> home) {
            this.home = home;
        }

        public List<NewAdBean> getLogin() {
            return login;
        }

        public void setLogin(List<NewAdBean> login) {
            this.login = login;
        }

        public List<NewAdBean> getQuick_start() {
            return quick_start;
        }

        public void setQuick_start(List<NewAdBean> quick_start) {
            this.quick_start = quick_start;
        }

        public List<NewAdBean> getSet_mode() {
            return set_mode;
        }

        public void setSet_mode(List<NewAdBean> set_mode) {
            this.set_mode = set_mode;
        }

        public static class NewAdBean implements Serializable{

            /**
             * url : http://testrun.likingfit.com/img/advertisement02.jpg
             * endtime : 20170221
             * staytime : 10
             * adv_id : 1
             */

            private String url;
            private String endtime;
            private int staytime;
            private int adv_id;
            private int exhibition_id;


            public int getExhibition_id() {
                return exhibition_id;
            }

            public void setExhibition_id(int exhibition_id) {
                this.exhibition_id = exhibition_id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }

            public int getStaytime() {
                return staytime;
            }

            public void setStaytime(int staytime) {
                this.staytime = staytime;
            }

            public int getAdv_id() {
                return adv_id;
            }

            public void setAdv_id(int adv_id) {
                this.adv_id = adv_id;
            }
        }
    }

}
