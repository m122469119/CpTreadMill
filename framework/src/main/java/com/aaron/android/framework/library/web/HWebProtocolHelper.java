package com.aaron.android.framework.library.web;

import com.aaron.android.codelibrary.utils.StringUtils;

/**
 * Created on 15/11/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class HWebProtocolHelper {
    private String mUrl;
    private HWebProtocol mHWebProtocol;

    public HWebProtocolHelper(HWebProtocol hWebProtocol) {
        mHWebProtocol = hWebProtocol;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public boolean verify() {
        if (StringUtils.isEmpty(mUrl) || mHWebProtocol == null) {
            return false;
        }
        return mHWebProtocol.verify(mUrl);
    }

}
