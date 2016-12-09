package com.aaron.android.framework.library.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 15/7/23.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class RequestParams {
    private Map<String, Object> mParamsMap = new HashMap<>();

    public RequestParams() {
        super();
    }

    public RequestParams(Map<String, Object> params) {
        if (params != null) {
            mParamsMap = params;
        }
    }

    /**
     * 添加请求参数
     * @param key 键
     * @param value 值
     */
    public RequestParams append(String key, Object value) {
        mParamsMap.put(key, value);
        return this;
    }

    /**
     *
     * @return 获取请求参数集合
     */
    public Map<String, Object> getParams() {
        return mParamsMap;
    }

}
