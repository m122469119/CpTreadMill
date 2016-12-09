package com.aaron.android.framework.library.web;

/**
 * Created on 15/11/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class HWebProtocol {
    /**
     * 校验webView url是否符合规定的网络协议
     * @param url url
     * @return 符合与服务器预定要的网络协议
     */
    public abstract boolean verify(String url);

}
