package com.aaron.android.thirdparty.map;

import android.content.Context;

/**
 * Created on 15/10/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class Location<T> {

    private Context mContext;
    private LocationListener<T> mLocationListener;

    public Location(Context context) {
        mContext = context;
        initialize();
    }

    /**
     * 获取上下文资源
     * @return Context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 地图定位Api初始化操作
     */
    public abstract void initialize();

    /**
     * 销毁定位相关对象操作
     */
    public abstract void destroy();

    /**
     * 启动定位
     */
    public abstract void start();

    /**
     * 停止定位
     */
    public abstract void stop();

    /**
     * 获取定位监听器
     * @return LocationListener
     */
    public LocationListener<T> getLocationListener() {
        return mLocationListener;
    }

    /**
     * 设置定位监听器,处理接收到的位置信息
     * @param locationListener
     */
    public void setLocationListener(LocationListener<T> locationListener) {
        mLocationListener = locationListener;
    }
}
