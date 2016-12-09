package com.aaron.android.thirdparty.pay;

/**
 * Created on 15/10/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public interface PayListener {
    /**
     * 启动支付回调
     */
    void onStart();

    /**
     * 支付成功回调
     */
    void onSuccess();

    /**
     * 支付失败回调
     * @param errorMessage 失败错误信息
     */
    void onFailure(String errorMessage);
}
