package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;
import com.liking.treadmill.socket.result.data.BaseSocketData;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class CheckConfirmResult extends BaseSocketResult {

    /**
     * token : 4e22d9b9d228e1904c5936d9310bc426
     */

    @SerializedName("data")
    private CheckConfirmData mData;

    public CheckConfirmData getData() {
        return mData;
    }

    public void setData(CheckConfirmData data) {
        mData = data;
    }

    public static class CheckConfirmData extends BaseSocketData {
        @SerializedName("token")
        private String mToken;

        public String getToken() {
            return mToken;
        }

        public void setToken(String token) {
            mToken = token;
        }
    }
}
