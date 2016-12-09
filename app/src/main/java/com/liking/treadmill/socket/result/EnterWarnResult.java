package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class EnterWarnResult extends BaseSocketResult {

    /**
     * text : dddsfwdfwef
     */

    @SerializedName("data")
    private DataData mData;

    public DataData getData() {
        return mData;
    }

    public void setData(DataData data) {
        mData = data;
    }

    public static class DataData {
        @SerializedName("text")
        private String mText;

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }
    }
}
