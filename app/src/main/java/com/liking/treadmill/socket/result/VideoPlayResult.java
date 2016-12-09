package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 16/11/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class VideoPlayResult extends BaseSocketResult {

    /**
     * play_url : dddsfwdfwef
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
        @SerializedName("play_url")
        private String mPlayUrl;
        @SerializedName("text")
        private String videoName;
        @SerializedName("title")
        private String title;

        public String getPlayUrl() {
            return mPlayUrl;
        }

        public void setPlayUrl(String playUrl) {
            mPlayUrl = playUrl;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
