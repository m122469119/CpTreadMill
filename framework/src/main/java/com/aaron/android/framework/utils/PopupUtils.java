package com.aaron.android.framework.utils;

import android.widget.Toast;

import com.aaron.android.framework.base.BaseApplication;

/**
 * Created on 2014/12/16 11:18.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class PopupUtils {

    /**
     * 显示Toast
     * @param msg 内容
     */
    public static void showToast(String msg) {
        Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示Toast
     * @param resId 资源ID
     */
    public static void showToast(int resId) {
        Toast.makeText(BaseApplication.getInstance(), ResourceUtils.getString(resId), Toast.LENGTH_LONG).show();
    }
}
