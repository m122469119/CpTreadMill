package com.aaron.android.thirdparty.map;

/**
 * 定位回调
 * Created on 15/10/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 *
 * @param <T> 回调数据泛型
 */
public interface LocationListener<T> {
    void receive(T object);

    void start();

    void end();
}
