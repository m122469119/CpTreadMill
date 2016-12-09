package com.aaron.android.framework.library.http.helper;

import com.aaron.android.codelibrary.http.result.Result;

/**
 * Created on 15/7/4.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class VerifyResultUtils {
    /**
     * 检查是否成功，若不成功弹出错误信息
     * @param result BaseResult
     * @return boolean
     */
    public static boolean checkResultSuccess(Result result) {
        if (result == null || !result.isSuccess()) {
            return false;
        }
        return true;
    }

}
