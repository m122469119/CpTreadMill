package com.aaron.android.thirdparty.pay;

import android.content.Context;
import android.content.Intent;

/**
 * Created on 16/8/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class Utils {

    /**
     * 判断是否有安装packageName的app
     * @param context 上下文
     * @param packageName 包名
     * @return true false
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName) != null;
    }
}
