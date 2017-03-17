package com.aaron.android.framework.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.aaron.android.codelibrary.utils.SecurityUtils;
import com.aaron.android.codelibrary.utils.StringUtils;

/**
 * Created on 15/7/23.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class DeviceUtils {

    /**
     * 获取Android设备唯一标识
     * @param context 上下文资源
     * @return Android设备唯一标识
     */
    public static String getDeviceInfo(Context context) {
        try{
            TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String m_szImei = TelephonyMgr.getDeviceId();
            String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                    Build.BOARD.length()%10 +
                    Build.BRAND.length()%10 +
                    Build.CPU_ABI.length()%10 +
                    Build.DEVICE.length()%10 +
                    Build.DISPLAY.length()%10 +
                    Build.HOST.length()%10 +
                    Build.ID.length()%10 +
                    Build.MANUFACTURER.length()%10 +
                    Build.MODEL.length()%10 +
                    Build.PRODUCT.length()%10 +
                    Build.TAGS.length()%10 +
                    Build.TYPE.length()%10 +
                    Build.USER.length()%10 ; //13 digits
            String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String m_szLongID = m_szImei + m_szDevIDShort
                    + m_szAndroidID;
            return SecurityUtils.MD5.get32MD5String(m_szLongID);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
