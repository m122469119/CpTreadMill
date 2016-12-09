package com.aaron.android.thirdparty.statistics;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.message.UmengRegistrar;

/**
 * Created on 15/10/23.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class UmengStatisticsUtils extends Statistics {
    /**
     * 获取集成测试设备DeviceInfo
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getRegistrationId(Context context) {
        return UmengRegistrar.getRegistrationId(context);
    }

    @Override
    public void initialize() {

    }
}
