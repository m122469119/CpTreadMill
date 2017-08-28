package com.liking.treadmill.socket.result;

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

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private List<DefaultAdBean> home;
        private List<DefaultAdBean> login;
        private List<DefaultAdBean> quick_start;
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

            private String url;
            private int adv_id;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
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
