package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:42
 * version 1.0.0
 */

public class SettingConfigResult extends  BaseSocketResult{


    /**
     * index_refresh : 1
     * screen_duration : 300
     */

    @SerializedName("data")
    private ConfigData mData;

    public ConfigData getData() {
        return mData;
    }

    public void setData(ConfigData data) {
        mData = data;
    }

    public static class ConfigData {
        @SerializedName("index_refresh")
        private int mIndexRefresh;
        @SerializedName("screen_duration")
        private int mScreenDuration;

        public int getIndexRefresh() {
            return mIndexRefresh;
        }

        public void setIndexRefresh(int indexRefresh) {
            mIndexRefresh = indexRefresh;
        }

        public int getScreenDuration() {
            return mScreenDuration;
        }

        public void setScreenDuration(int screenDuration) {
            mScreenDuration = screenDuration;
        }
    }
}
