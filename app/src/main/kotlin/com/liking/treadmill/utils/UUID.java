package com.liking.treadmill.utils;

import com.aaron.android.codelibrary.utils.SecurityUtils;

/**
 * Created on 2017/5/5
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class UUID {
    public static String getMsgId() {
        String s = java.util.UUID.randomUUID().toString();
        return SecurityUtils.MD5.get32MD5String(s);
    }

}