package com.liking.treadmill.socket.result.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class QrcodeData extends BaseSocketData {

    /**
     * code_url : http://testroom.likingfit.com/codes/20161116/QRCODE582c2c9fa343d.png
     */

    @SerializedName("code_url")
    private String mCodeUrl;

    public String getCodeUrl() {
        return mCodeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        mCodeUrl = codeUrl;
    }
}
