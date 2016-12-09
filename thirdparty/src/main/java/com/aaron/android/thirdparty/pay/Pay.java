package com.aaron.android.thirdparty.pay;

/**
 * Created on 15/9/29.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class Pay<T> {
    private T mPayApi;
    public abstract void initPayApi();

    public abstract void doPay();

    public void setPayApi(T payApi) {
        mPayApi = payApi;
    }

    public T getPayApi() {
        return mPayApi;
    }
}
