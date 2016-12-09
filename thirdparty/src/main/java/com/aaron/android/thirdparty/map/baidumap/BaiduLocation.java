package com.aaron.android.thirdparty.map.baidumap;

import android.content.Context;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.thirdparty.map.Location;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created on 15/10/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 *
 * 百度地图定位
 */
public class BaiduLocation extends Location<BDLocation> {
    private static final String TAG = "baiduMap";
    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener = new MyBaiduLocationListener();

    public BaiduLocation(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        mLocationClient = new LocationClient(getContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(mBDLocationListener);

        LocationClientOption option = new LocationClientOption();
        // 设置定位模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 返回的定位结果是百度经纬度,(百度加密经纬度坐标)
        option.setCoorType("bd09ll");
        // 是否反编译地理
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void destroy() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
            mLocationClient.stop();
            mBDLocationListener = null;
            mLocationClient = null;
        }
    }

    @Override
    public void start() {
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            if (getLocationListener() != null) {
                getLocationListener().start();
            }
            mLocationClient.start();
        }
    }

    @Override
    public void stop() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    private class MyBaiduLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            LogUtils.i(TAG, "baidu location receive code : " + bdLocation.getLocType());
            if (getLocationListener() != null) {
                getLocationListener().end();
                getLocationListener().receive(bdLocation);
            }
        }
    }

    public boolean isLocationSuccess(BDLocation bdLocation) {
        if (bdLocation == null) {
            return false;
        }
        int locationType = bdLocation.getLocType();
        return locationType == BDLocation.TypeGpsLocation
                || locationType == BDLocation.TypeNetWorkLocation
                || locationType == BDLocation.TypeCacheLocation
                || locationType == BDLocation.TypeOffLineLocation;

    }

}
