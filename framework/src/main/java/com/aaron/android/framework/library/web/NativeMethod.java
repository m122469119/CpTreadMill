package com.aaron.android.framework.library.web;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.aaron.android.framework.utils.PhoneUtils;

/**
 * Created on 16/7/23.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class NativeMethod {

    private Context mContext;

    public NativeMethod(Context context) {
        mContext = context;
    }

    /**
     * 拨打电话
     */
    @JavascriptInterface
    public void phoneCall(String number) {
        PhoneUtils.phoneCall(mContext, number);
    }

    /**
     * 跳转拨号界面
     */
    @JavascriptInterface
    public void skipPhoneDial(String number) {
        PhoneUtils.skipPhoneDial(mContext, number);
    }

}
