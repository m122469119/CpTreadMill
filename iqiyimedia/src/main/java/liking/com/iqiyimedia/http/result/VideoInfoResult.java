package liking.com.iqiyimedia.http.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2017/08/24
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */
public class VideoInfoResult extends IqiyiResult {

    /**
     * data : {"albumId":367710700,"albumQipuId":367710700,"qipuId":367710700,"tvId":367710700,"contentType":1,"playOrder":1,"tvYear":20150402,"beginTime":-1,"endTime":-1,"timeLength":5406,"videoStatus":1,"categoryId":1,"copyrightId":0,"tvName":"战狼","keyWord":"战狼","desc":"在南疆围捕贩毒分子的行动中，特种部队狙击手冷锋公然违抗上级的命令，开枪射杀伤害了战友的暴徒武吉。这一行动令冷锋遭到军方禁闭甚至强制退伍的处罚，不过各特种部队精英组成超级特种部队战狼中队的中队长龙小云，却十分欣赏这个敢作敢为且业务过硬的血性男儿，于是将其召入自己的麾下。在新近的一次演习中，冷锋凭借冷静的判断力从老首长处拔得一城，并且成功击退了突然出现的狼群。谁知在毫无准备的情况下，战狼遭到了一伙荷枪实弹分子的袭击。原来武吉的哥哥敏登是一个冷酷无情的国际通缉犯，他手下豢养了一大批身怀绝技的雇佣兵。为了给弟弟报仇，敏登派出雇佣兵千里迢迢奔着冷锋而来。","subTitle":"","domainName":"www","tvUniqId":"25c38c6cd64fe6de9c4b707ed7ca8f56","videoImage":"http://pic2.qiyipic.com/image/20170628/db/56/v_109159633_m_601_m9.jpg","videoUrl":"http://www.iqiyi.com/v_19rrnqxz7k.html?vfm=newvfm","updateTime":"2017-08-24 14:15:52","issueTime":"2015-05-20 20:31:38","html5PlayUrl":"http://m.iqiyi.com/v_19rrnqxz7k.html?vfm=newvfm","html5PlayUrlForBaidu":"","broadcastRate":"","androidApp":"","iosApp":"","swf":"http://dispatcher.video.iqiyi.com/disp/shareplayer.swf?vid=25c38c6cd64fe6de9c4b707ed7ca8f56&tvId=367710700&coop=test_coop&cid=test_cid01&bd=1","commonSwf":"http://dispatcher.video.iqiyi.com/common/shareplayer.html?vid=25c38c6cd64fe6de9c4b707ed7ca8f56&tvId=367710700&coop=test_coop&cid=test_cid01&aid=367710700&bd=1","is3D":0,"isPurchase":"0","payMark":0,"memberDownloadableOnly":0,"is1080p":1,"isDolby":1,"panorama":{"videoType":1,"viewAngleX":0,"viewAngleY":0,"zoomRate":1},"playControls":[{"platformId":6,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":18,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":5,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":10,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":17,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":7,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":3,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":14,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":19,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":15,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":2,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":22,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":11,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":20,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":12,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":8,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":1,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":13,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":4,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":9,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":16,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":21,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1}],"subsites":[]}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * albumId : 367710700
         * albumQipuId : 367710700
         * qipuId : 367710700
         * tvId : 367710700
         * contentType : 1
         * playOrder : 1
         * tvYear : 20150402
         * beginTime : -1
         * endTime : -1
         * timeLength : 5406
         * videoStatus : 1
         * categoryId : 1
         * copyrightId : 0
         * tvName : 战狼
         * keyWord : 战狼
         * desc : 在南疆围捕贩毒分子的行动中，特种部队狙击手冷锋公然违抗上级的命令，开枪射杀伤害了战友的暴徒武吉。这一行动令冷锋遭到军方禁闭甚至强制退伍的处罚，不过各特种部队精英组成超级特种部队战狼中队的中队长龙小云，却十分欣赏这个敢作敢为且业务过硬的血性男儿，于是将其召入自己的麾下。在新近的一次演习中，冷锋凭借冷静的判断力从老首长处拔得一城，并且成功击退了突然出现的狼群。谁知在毫无准备的情况下，战狼遭到了一伙荷枪实弹分子的袭击。原来武吉的哥哥敏登是一个冷酷无情的国际通缉犯，他手下豢养了一大批身怀绝技的雇佣兵。为了给弟弟报仇，敏登派出雇佣兵千里迢迢奔着冷锋而来。
         * subTitle :
         * domainName : www
         * tvUniqId : 25c38c6cd64fe6de9c4b707ed7ca8f56
         * videoImage : http://pic2.qiyipic.com/image/20170628/db/56/v_109159633_m_601_m9.jpg
         * videoUrl : http://www.iqiyi.com/v_19rrnqxz7k.html?vfm=newvfm
         * updateTime : 2017-08-24 14:15:52
         * issueTime : 2015-05-20 20:31:38
         * html5PlayUrl : http://m.iqiyi.com/v_19rrnqxz7k.html?vfm=newvfm
         * html5PlayUrlForBaidu :
         * broadcastRate :
         * androidApp :
         * iosApp :
         * swf : http://dispatcher.video.iqiyi.com/disp/shareplayer.swf?vid=25c38c6cd64fe6de9c4b707ed7ca8f56&tvId=367710700&coop=test_coop&cid=test_cid01&bd=1
         * commonSwf : http://dispatcher.video.iqiyi.com/common/shareplayer.html?vid=25c38c6cd64fe6de9c4b707ed7ca8f56&tvId=367710700&coop=test_coop&cid=test_cid01&aid=367710700&bd=1
         * is3D : 0
         * isPurchase : 0
         * payMark : 0
         * memberDownloadableOnly : 0
         * is1080p : 1
         * isDolby : 1
         * panorama : {"videoType":1,"viewAngleX":0,"viewAngleY":0,"zoomRate":1}
         * playControls : [{"platformId":6,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":18,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":5,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":10,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":17,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":7,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":3,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":14,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":19,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":15,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":2,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":22,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":11,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":20,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":12,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":8,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":1,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":13,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":4,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":9,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":16,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1},{"platformId":21,"downloadAllowed":1,"cooperationAllowed":1,"availableStatus":1}]
         * subsites : []
         */

        @SerializedName("albumId")
        private int albumId;
        @SerializedName("albumQipuId")
        private int albumQipuId;
        @SerializedName("qipuId")
        private int qipuId;
        @SerializedName("tvId")
        private int tvId;
        @SerializedName("contentType")
        private int contentType;
        @SerializedName("playOrder")
        private int playOrder;
        @SerializedName("tvYear")
        private int tvYear;
        @SerializedName("beginTime")
        private Long beginTime;
        @SerializedName("endTime")
        private Long endTime;
        @SerializedName("timeLength")
        private Long timeLength;
        @SerializedName("videoStatus")
        private int videoStatus;
        @SerializedName("categoryId")
        private int categoryId;
        @SerializedName("copyrightId")
        private int copyrightId;
        @SerializedName("tvName")
        private String tvName;
        @SerializedName("keyWord")
        private String keyWord;
        @SerializedName("desc")
        private String desc;
        @SerializedName("subTitle")
        private String subTitle;
        @SerializedName("domainName")
        private String domainName;
        @SerializedName("tvUniqId")
        private String tvUniqId;
        @SerializedName("videoImage")
        private String videoImage;
        @SerializedName("videoUrl")
        private String videoUrl;
        @SerializedName("updateTime")
        private String updateTime;
        @SerializedName("issueTime")
        private String issueTime;
        @SerializedName("html5PlayUrl")
        private String html5PlayUrl;
        @SerializedName("html5PlayUrlForBaidu")
        private String html5PlayUrlForBaidu;
        @SerializedName("broadcastRate")
        private String broadcastRate;
        @SerializedName("androidApp")
        private String androidApp;
        @SerializedName("iosApp")
        private String iosApp;
        @SerializedName("swf")
        private String swf;
        @SerializedName("commonSwf")
        private String commonSwf;
        @SerializedName("is3D")
        private int is3D;
        @SerializedName("isPurchase")
        private String isPurchase;
        @SerializedName("payMark")
        private int payMark;
        @SerializedName("memberDownloadableOnly")
        private int memberDownloadableOnly;
        @SerializedName("is1080p")
        private int is1080p;
        @SerializedName("isDolby")
        private int isDolby;
        @SerializedName("panorama")
        private PanoramaBean panorama;
        @SerializedName("playControls")
        private List<PlayControlsBean> playControls;
        @SerializedName("subsites")
        private List<?> subsites;

        public int getAlbumId() {
            return albumId;
        }

        public void setAlbumId(int albumId) {
            this.albumId = albumId;
        }

        public int getAlbumQipuId() {
            return albumQipuId;
        }

        public void setAlbumQipuId(int albumQipuId) {
            this.albumQipuId = albumQipuId;
        }

        public int getQipuId() {
            return qipuId;
        }

        public void setQipuId(int qipuId) {
            this.qipuId = qipuId;
        }

        public int getTvId() {
            return tvId;
        }

        public void setTvId(int tvId) {
            this.tvId = tvId;
        }

        public int getContentType() {
            return contentType;
        }

        public void setContentType(int contentType) {
            this.contentType = contentType;
        }

        public int getPlayOrder() {
            return playOrder;
        }

        public void setPlayOrder(int playOrder) {
            this.playOrder = playOrder;
        }

        public int getTvYear() {
            return tvYear;
        }

        public void setTvYear(int tvYear) {
            this.tvYear = tvYear;
        }

        public Long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Long beginTime) {
            this.beginTime = beginTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public Long getTimeLength() {
            return timeLength;
        }

        public void setTimeLength(Long timeLength) {
            this.timeLength = timeLength;
        }

        public int getVideoStatus() {
            return videoStatus;
        }

        public void setVideoStatus(int videoStatus) {
            this.videoStatus = videoStatus;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(int copyrightId) {
            this.copyrightId = copyrightId;
        }

        public String getTvName() {
            return tvName;
        }

        public void setTvName(String tvName) {
            this.tvName = tvName;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public String getTvUniqId() {
            return tvUniqId;
        }

        public void setTvUniqId(String tvUniqId) {
            this.tvUniqId = tvUniqId;
        }

        public String getVideoImage() {
            return videoImage;
        }

        public void setVideoImage(String videoImage) {
            this.videoImage = videoImage;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getIssueTime() {
            return issueTime;
        }

        public void setIssueTime(String issueTime) {
            this.issueTime = issueTime;
        }

        public String getHtml5PlayUrl() {
            return html5PlayUrl;
        }

        public void setHtml5PlayUrl(String html5PlayUrl) {
            this.html5PlayUrl = html5PlayUrl;
        }

        public String getHtml5PlayUrlForBaidu() {
            return html5PlayUrlForBaidu;
        }

        public void setHtml5PlayUrlForBaidu(String html5PlayUrlForBaidu) {
            this.html5PlayUrlForBaidu = html5PlayUrlForBaidu;
        }

        public String getBroadcastRate() {
            return broadcastRate;
        }

        public void setBroadcastRate(String broadcastRate) {
            this.broadcastRate = broadcastRate;
        }

        public String getAndroidApp() {
            return androidApp;
        }

        public void setAndroidApp(String androidApp) {
            this.androidApp = androidApp;
        }

        public String getIosApp() {
            return iosApp;
        }

        public void setIosApp(String iosApp) {
            this.iosApp = iosApp;
        }

        public String getSwf() {
            return swf;
        }

        public void setSwf(String swf) {
            this.swf = swf;
        }

        public String getCommonSwf() {
            return commonSwf;
        }

        public void setCommonSwf(String commonSwf) {
            this.commonSwf = commonSwf;
        }

        public int getIs3D() {
            return is3D;
        }

        public void setIs3D(int is3D) {
            this.is3D = is3D;
        }

        public String getIsPurchase() {
            return isPurchase;
        }

        public void setIsPurchase(String isPurchase) {
            this.isPurchase = isPurchase;
        }

        public int getPayMark() {
            return payMark;
        }

        public void setPayMark(int payMark) {
            this.payMark = payMark;
        }

        public int getMemberDownloadableOnly() {
            return memberDownloadableOnly;
        }

        public void setMemberDownloadableOnly(int memberDownloadableOnly) {
            this.memberDownloadableOnly = memberDownloadableOnly;
        }

        public int getIs1080p() {
            return is1080p;
        }

        public void setIs1080p(int is1080p) {
            this.is1080p = is1080p;
        }

        public int getIsDolby() {
            return isDolby;
        }

        public void setIsDolby(int isDolby) {
            this.isDolby = isDolby;
        }

        public PanoramaBean getPanorama() {
            return panorama;
        }

        public void setPanorama(PanoramaBean panorama) {
            this.panorama = panorama;
        }

        public List<PlayControlsBean> getPlayControls() {
            return playControls;
        }

        public void setPlayControls(List<PlayControlsBean> playControls) {
            this.playControls = playControls;
        }

        public List<?> getSubsites() {
            return subsites;
        }

        public void setSubsites(List<?> subsites) {
            this.subsites = subsites;
        }

        public static class PanoramaBean {
            /**
             * videoType : 1
             * viewAngleX : 0
             * viewAngleY : 0
             * zoomRate : 1
             */

            @SerializedName("videoType")
            private int videoType;
            @SerializedName("viewAngleX")
            private int viewAngleX;
            @SerializedName("viewAngleY")
            private int viewAngleY;
            @SerializedName("zoomRate")
            private int zoomRate;

            public int getVideoType() {
                return videoType;
            }

            public void setVideoType(int videoType) {
                this.videoType = videoType;
            }

            public int getViewAngleX() {
                return viewAngleX;
            }

            public void setViewAngleX(int viewAngleX) {
                this.viewAngleX = viewAngleX;
            }

            public int getViewAngleY() {
                return viewAngleY;
            }

            public void setViewAngleY(int viewAngleY) {
                this.viewAngleY = viewAngleY;
            }

            public int getZoomRate() {
                return zoomRate;
            }

            public void setZoomRate(int zoomRate) {
                this.zoomRate = zoomRate;
            }
        }

        public static class PlayControlsBean {
            /**
             * platformId : 6
             * downloadAllowed : 1
             * cooperationAllowed : 1
             * availableStatus : 1
             */

            @SerializedName("platformId")
            private int platformId;
            @SerializedName("downloadAllowed")
            private int downloadAllowed;
            @SerializedName("cooperationAllowed")
            private int cooperationAllowed;
            @SerializedName("availableStatus")
            private int availableStatus;

            public int getPlatformId() {
                return platformId;
            }

            public void setPlatformId(int platformId) {
                this.platformId = platformId;
            }

            public int getDownloadAllowed() {
                return downloadAllowed;
            }

            public void setDownloadAllowed(int downloadAllowed) {
                this.downloadAllowed = downloadAllowed;
            }

            public int getCooperationAllowed() {
                return cooperationAllowed;
            }

            public void setCooperationAllowed(int cooperationAllowed) {
                this.cooperationAllowed = cooperationAllowed;
            }

            public int getAvailableStatus() {
                return availableStatus;
            }

            public void setAvailableStatus(int availableStatus) {
                this.availableStatus = availableStatus;
            }
        }
    }
}
