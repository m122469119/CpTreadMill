package com.aaron.android.codelibrary.http;

import com.aaron.android.codelibrary.http.result.Result;

/**
 * Created on 16/11/24.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public interface Request<T extends Result> {
    /**
     * 异步请求
     *
     * @param requestCallback 请求回调
     */
    void execute(RequestCallback<T> requestCallback);

    /**
     * 同步请求
     *
     * @return 网络数据
     */
    T execute();
}
