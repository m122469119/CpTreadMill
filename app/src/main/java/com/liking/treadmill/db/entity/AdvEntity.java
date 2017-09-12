package com.liking.treadmill.db.entity;

import java.io.Serializable;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/8/28
 * @Version 1.0
 */
public class AdvEntity implements Serializable{
    public static final String TYPE_HOME = "home";
    public static final String TYPE_LOGIN = "login";
    public static final String TYPE_QUICK_START = "quick_start";
    public static final String TYPE_SET_MODE = "set_mode";

    public static final int DEFAULT = 1;
    public static final int NOT_DEFAULT = 0;


    private Long adv_id; //主键
    private String url;
    private String type;
    private String endtime;
    private int staytime;
    private int interval;//显示间隔
    private Long exhibition_id; //主键
    private int isDefault;

    public AdvEntity() {
    }

    public AdvEntity(Long adv_id, String url, String type, String endtime, int staytime, int intervaltime, Long exhibition_id, int isDefault) {
        this.adv_id = adv_id;
        this.url = url;
        this.type = type;
        this.endtime = endtime;
        this.staytime = staytime;
        this.interval = intervaltime;
        this.exhibition_id = exhibition_id;
        this.isDefault = isDefault;
    }

    public Long getExhibition_id() {
        return exhibition_id;
    }

    public void setExhibition_id(Long exhibition_id) {
        this.exhibition_id = exhibition_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Long getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(Long adv_id) {
        this.adv_id = adv_id;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
