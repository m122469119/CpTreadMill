package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class QrcodeResult extends BaseSocketResult {


    /**
     * code_url : http://testroom.likingfit.com/codes/20161116/QRCODE582c306fa127b.png
     */

    @SerializedName("data")
    private QrcodeData mQrcodeData;

    public QrcodeData getQrcodeData() {
        return mQrcodeData;
    }

    public void setQrcodeData(QrcodeData qrcodeData) {
        mQrcodeData = qrcodeData;
    }

    public static class QrcodeData {
        @SerializedName("code_url")
        private String mCodeUrl;

        public String getCodeUrl() {
            return mCodeUrl;
        }

        public void setCodeUrl(String codeUrl) {
            mCodeUrl = codeUrl;
        }
    }
}
