package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;
import com.liking.treadmill.socket.result.data.BaseSocketData;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class VideoPlayWarnResult extends BaseSocketResult {
    /**
     * text : dddsfwdfwef
     */

    @SerializedName("data")
    private VedioPlayWarnData mData;

    public VedioPlayWarnData getData() {
        return mData;
    }

    public void setData(VedioPlayWarnData data) {
        mData = data;
    }

    public static class VedioPlayWarnData extends BaseSocketData {
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
