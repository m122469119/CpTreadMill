package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

/**
 * 说明: APK更新
 * Author: chenlei
 * Time: 上午9:54
 */

public class ApkUpdateResult extends BaseSocketResult{

    /**
     * "lastest_version" : "v1.1",
     * "url" : "http://likingfit.com/run"
     */

    @SerializedName("data")
    private ApkUpdateResult.ApkUpdateData mApkUpdateData;

    public ApkUpdateData getApkUpdateData() {
        return mApkUpdateData;
    }

    public void setApkUpdateData(ApkUpdateData apkUpdateData) {
        mApkUpdateData = apkUpdateData;
    }

    public static class ApkUpdateData {
        @SerializedName("lastest_version")
        private String mVersion;

        @SerializedName("url")
        private String mUrl;

        public String getVersion() {
            return mVersion;
        }

        public void setVersion(String version) {
            mVersion = version;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String url) {
            mUrl = url;
        }
    }
}
