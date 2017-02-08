package com.liking.treadmill.socket.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午10:43
 */

public class AdvertisementResult extends BaseSocketResult {

    @SerializedName("data")
    private AdvUrlResource mUrlResources;

    public AdvUrlResource getUrlResources() {
        return mUrlResources;
    }

    public void setUrlResources(AdvUrlResource urlResources) {
        mUrlResources = urlResources;
    }

    public static class AdvUrlResource {

        @SerializedName("url")
        private List<Resource> mResources;

        public List<Resource> getResources() {
            return mResources;
        }

        public void setResources(List<Resource> resources) {
            mResources = resources;
        }

        public static class Resource {
            @SerializedName("url")
            private String url;

            @SerializedName("endtime")
            private String endtime;

            private String localPath;

            private boolean isOpen = true;

            public boolean isOpen() {
                return isOpen;
            }

            public void setOpen(boolean open) {
                isOpen = open;
            }

            public String getLocalPath() {
                return localPath;
            }

            public void setLocalPath(String localPath) {
                this.localPath = localPath;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }
        }
    }
}
