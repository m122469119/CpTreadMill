package com.aaron.android.thirdparty.map.amap;

import android.content.Context;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.thirdparty.map.Location;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created on 16/1/18.
 *
 * @author aaron.huang
 * @version 1.0.0
 *          <p>
 *          高德地图定位
 */
public class AmapGDLocation extends Location<AMapLocation> {
    private static final String TAG = "Amap";
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;

    @Override
    public void initialize() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //初始化定位监听
        mLocationListener = new AmapGDLocationListener();
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(5000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void destroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationListener = null;
            mLocationOption = null;
        }
    }

    @Override
    public void start() {
        //启动定位
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            if (getLocationListener() != null) {
                getLocationListener().start();
            }
            mLocationClient.startLocation();
        }
    }

    @Override
    public void stop() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
    }

    public AmapGDLocation(Context context) {
        super(context);
    }

    private class AmapGDLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LogUtils.i(TAG, "amap location receive code : " + aMapLocation.getLocationType());
            if (getLocationListener() != null) {
                getLocationListener().end();
                getLocationListener().receive(aMapLocation);
            }
        }
    }
}
