package liking.com.iqiyimedia.http.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2017/07/22
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class AlbumListResult extends IqiyiResult{


    /**
     * total : 265203
     * data : [{"albumId":736923000,"qipuId":736923000,"ipLimit":0,"categoryId":7,"sets":1,"purchaseType":0,"isPurchase":0,"payMark":0,"copyrightId":8465,"tvYear":20170724,"albumStatus":1,"contentType":1,"isSD":0,"is3D":0,"sourceId":0,"isDubo":0,"isCoopAllowed":1,"downloadAllowed":1,"sourceQipuId":0,"playcnt":3728,"timeLength":46,"ownerAlbumId":-1,"albumName":"顽童瘦子挺《嘻哈》热狗：觉得没资格大可不看","desc":"顽童瘦子挺《嘻哈》热狗：觉得没资格大可不看","startDate":"2016-08-01 08:00:00","endDate":"2017-07-31 08:00:00","focus":"觉得没资格大可不看","albumUrl":"http://www.iqiyi.com/v_19rr7nomc4.html?vfm=newvfm","html5Url":"http://m.iqiyi.com/v_19rr7nomc4.html?vfm=newvfm","html5PlayUrl":"http://m.iqiyi.com/v_19rr7nomc4.html?vfm=newvfm","picUrl":"http://pic5.qiyipic.com/image/20170724/c8/dd/v_112913021_m_601.jpg","posterPicUrl":"http://pic5.qiyipic.com/image/20170724/c8/dd/v_112913021_m_601.jpg","leafctgs":"热点 八卦 内地 新闻 综艺 国语","producer":"","credits":" ","source":"","companyName":"东星（天津）视讯科技有限公司","keyword":"中国有嘻哈","subTitle":"","score":"9.4","actor":"","director":"","area":"内地","albumType":"综艺","playUrl":"http://www.iqiyi.com/v_19rr7nomc4.html?vfm=newvfm","createdTime":"2017-07-24 10:27:28","sysPlatform":"PHONE,PAD,PC,PC_APP,TV,PHONE_WEB_IQIYI,PAD_WEB_IQIYI","alias":"","tvIds":[736923000],"tvQipuIds":[736923000],"threeCtgs":[{"name":"热点","subType":2,"subCtgName":"内容类型","id":170},{"name":"八卦","subType":2,"subCtgName":"内容类型","id":175},{"name":"内地","subType":3,"subCtgName":"地区","id":184},{"name":"新闻","subType":4,"subCtgName":"节目分类","id":189},{"name":"综艺","subType":5,"subCtgName":"类型","id":1082},{"name":"国语","subType":0,"subCtgName":"配音语种","id":20131}],"creditList":[{"name":"热狗","roleName":"","type":21},{"name":"顽童MJ116","roleName":"","type":21},{"name":"张震岳","roleName":"","type":21},{"name":"潘玮柏","roleName":"","type":21},{"name":"吴亦凡","roleName":"","type":21}],"upDown":{"score":9.4,"voters":0,"up":0,"albumQipuId":736923000,"down":0},"season":0,"edition":"","subsites":[],"swf":""}]
     * pageNo : 1
     * pagesize : 100
     * foundNum : 100
     */

    @SerializedName("total")
    private int total;
    @SerializedName("pageNo")
    private int pageNo;
    @SerializedName("pagesize")
    private int pagesize;
    @SerializedName("foundNum")
    private int foundNum;
    @SerializedName("data")
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getFoundNum() {
        return foundNum;
    }

    public void setFoundNum(int foundNum) {
        this.foundNum = foundNum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * albumId : 736923000
         * qipuId : 736923000
         * ipLimit : 0
         * categoryId : 7
         * sets : 1
         * purchaseType : 0
         * isPurchase : 0
         * payMark : 0
         * copyrightId : 8465
         * tvYear : 20170724
         * albumStatus : 1
         * contentType : 1
         * isSD : 0
         * is3D : 0
         * sourceId : 0
         * isDubo : 0
         * isCoopAllowed : 1
         * downloadAllowed : 1
         * sourceQipuId : 0
         * playcnt : 3728
         * timeLength : 46
         * ownerAlbumId : -1
         * albumName : 顽童瘦子挺《嘻哈》热狗：觉得没资格大可不看
         * desc : 顽童瘦子挺《嘻哈》热狗：觉得没资格大可不看
         * startDate : 2016-08-01 08:00:00
         * endDate : 2017-07-31 08:00:00
         * focus : 觉得没资格大可不看
         * albumUrl : http://www.iqiyi.com/v_19rr7nomc4.html?vfm=newvfm
         * html5Url : http://m.iqiyi.com/v_19rr7nomc4.html?vfm=newvfm
         * html5PlayUrl : http://m.iqiyi.com/v_19rr7nomc4.html?vfm=newvfm
         * picUrl : http://pic5.qiyipic.com/image/20170724/c8/dd/v_112913021_m_601.jpg
         * posterPicUrl : http://pic5.qiyipic.com/image/20170724/c8/dd/v_112913021_m_601.jpg
         * leafctgs : 热点 八卦 内地 新闻 综艺 国语
         * producer :
         * credits :
         * source :
         * companyName : 东星（天津）视讯科技有限公司
         * keyword : 中国有嘻哈
         * subTitle :
         * score : 9.4
         * actor :
         * director :
         * area : 内地
         * albumType : 综艺
         * playUrl : http://www.iqiyi.com/v_19rr7nomc4.html?vfm=newvfm
         * createdTime : 2017-07-24 10:27:28
         * sysPlatform : PHONE,PAD,PC,PC_APP,TV,PHONE_WEB_IQIYI,PAD_WEB_IQIYI
         * alias :
         * tvIds : [736923000]
         * tvQipuIds : [736923000]
         * threeCtgs : [{"name":"热点","subType":2,"subCtgName":"内容类型","id":170},{"name":"八卦","subType":2,"subCtgName":"内容类型","id":175},{"name":"内地","subType":3,"subCtgName":"地区","id":184},{"name":"新闻","subType":4,"subCtgName":"节目分类","id":189},{"name":"综艺","subType":5,"subCtgName":"类型","id":1082},{"name":"国语","subType":0,"subCtgName":"配音语种","id":20131}]
         * creditList : [{"name":"热狗","roleName":"","type":21},{"name":"顽童MJ116","roleName":"","type":21},{"name":"张震岳","roleName":"","type":21},{"name":"潘玮柏","roleName":"","type":21},{"name":"吴亦凡","roleName":"","type":21}]
         * upDown : {"score":9.4,"voters":0,"up":0,"albumQipuId":736923000,"down":0}
         * season : 0
         * edition :
         * subsites : []
         * swf :
         */

        @SerializedName("albumId")
        private int albumId;
        @SerializedName("qipuId")
        private int qipuId;
        @SerializedName("ipLimit")
        private int ipLimit;
        @SerializedName("categoryId")
        private int categoryId;
        @SerializedName("sets")
        private int sets;
        @SerializedName("purchaseType")
        private int purchaseType;
        @SerializedName("isPurchase")
        private int isPurchase;
        @SerializedName("payMark")
        private int payMark;
        @SerializedName("copyrightId")
        private int copyrightId;
        @SerializedName("tvYear")
        private int tvYear;
        @SerializedName("albumStatus")
        private int albumStatus;
        @SerializedName("contentType")
        private int contentType;
        @SerializedName("isSD")
        private int isSD;
        @SerializedName("is3D")
        private int is3D;
        @SerializedName("sourceId")
        private int sourceId;
        @SerializedName("isDubo")
        private int isDubo;
        @SerializedName("isCoopAllowed")
        private int isCoopAllowed;
        @SerializedName("downloadAllowed")
        private int downloadAllowed;
        @SerializedName("sourceQipuId")
        private int sourceQipuId;
        @SerializedName("playcnt")
        private int playcnt;
        @SerializedName("timeLength")
        private int timeLength;
        @SerializedName("ownerAlbumId")
        private int ownerAlbumId;
        @SerializedName("albumName")
        private String albumName;
        @SerializedName("desc")
        private String desc;
        @SerializedName("startDate")
        private String startDate;
        @SerializedName("endDate")
        private String endDate;
        @SerializedName("focus")
        private String focus;
        @SerializedName("albumUrl")
        private String albumUrl;
        @SerializedName("html5Url")
        private String html5Url;
        @SerializedName("html5PlayUrl")
        private String html5PlayUrl;
        @SerializedName("picUrl")
        private String picUrl;
        @SerializedName("posterPicUrl")
        private String posterPicUrl;
        @SerializedName("leafctgs")
        private String leafctgs;
        @SerializedName("producer")
        private String producer;
        @SerializedName("credits")
        private String credits;
        @SerializedName("source")
        private String source;
        @SerializedName("companyName")
        private String companyName;
        @SerializedName("keyword")
        private String keyword;
        @SerializedName("subTitle")
        private String subTitle;
        @SerializedName("score")
        private String score;
        @SerializedName("actor")
        private String actor;
        @SerializedName("director")
        private String director;
        @SerializedName("area")
        private String area;
        @SerializedName("albumType")
        private String albumType;
        @SerializedName("playUrl")
        private String playUrl;
        @SerializedName("createdTime")
        private String createdTime;
        @SerializedName("sysPlatform")
        private String sysPlatform;
        @SerializedName("alias")
        private String alias;
        @SerializedName("upDown")
        private UpDownBean upDown;
        @SerializedName("season")
        private int season;
        @SerializedName("edition")
        private String edition;
        @SerializedName("swf")
        private String swf;
        @SerializedName("tvIds")
        private List<Integer> tvIds;
        @SerializedName("tvQipuIds")
        private List<Integer> tvQipuIds;
        @SerializedName("threeCtgs")
        private List<ThreeCtgsBean> threeCtgs;
        @SerializedName("creditList")
        private List<CreditListBean> creditList;
        @SerializedName("subsites")
        private List<?> subsites;

        public int getAlbumId() {
            return albumId;
        }

        public void setAlbumId(int albumId) {
            this.albumId = albumId;
        }

        public int getQipuId() {
            return qipuId;
        }

        public void setQipuId(int qipuId) {
            this.qipuId = qipuId;
        }

        public int getIpLimit() {
            return ipLimit;
        }

        public void setIpLimit(int ipLimit) {
            this.ipLimit = ipLimit;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getSets() {
            return sets;
        }

        public void setSets(int sets) {
            this.sets = sets;
        }

        public int getPurchaseType() {
            return purchaseType;
        }

        public void setPurchaseType(int purchaseType) {
            this.purchaseType = purchaseType;
        }

        public int getIsPurchase() {
            return isPurchase;
        }

        public void setIsPurchase(int isPurchase) {
            this.isPurchase = isPurchase;
        }

        public int getPayMark() {
            return payMark;
        }

        public void setPayMark(int payMark) {
            this.payMark = payMark;
        }

        public int getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(int copyrightId) {
            this.copyrightId = copyrightId;
        }

        public int getTvYear() {
            return tvYear;
        }

        public void setTvYear(int tvYear) {
            this.tvYear = tvYear;
        }

        public int getAlbumStatus() {
            return albumStatus;
        }

        public void setAlbumStatus(int albumStatus) {
            this.albumStatus = albumStatus;
        }

        public int getContentType() {
            return contentType;
        }

        public void setContentType(int contentType) {
            this.contentType = contentType;
        }

        public int getIsSD() {
            return isSD;
        }

        public void setIsSD(int isSD) {
            this.isSD = isSD;
        }

        public int getIs3D() {
            return is3D;
        }

        public void setIs3D(int is3D) {
            this.is3D = is3D;
        }

        public int getSourceId() {
            return sourceId;
        }

        public void setSourceId(int sourceId) {
            this.sourceId = sourceId;
        }

        public int getIsDubo() {
            return isDubo;
        }

        public void setIsDubo(int isDubo) {
            this.isDubo = isDubo;
        }

        public int getIsCoopAllowed() {
            return isCoopAllowed;
        }

        public void setIsCoopAllowed(int isCoopAllowed) {
            this.isCoopAllowed = isCoopAllowed;
        }

        public int getDownloadAllowed() {
            return downloadAllowed;
        }

        public void setDownloadAllowed(int downloadAllowed) {
            this.downloadAllowed = downloadAllowed;
        }

        public int getSourceQipuId() {
            return sourceQipuId;
        }

        public void setSourceQipuId(int sourceQipuId) {
            this.sourceQipuId = sourceQipuId;
        }

        public int getPlaycnt() {
            return playcnt;
        }

        public void setPlaycnt(int playcnt) {
            this.playcnt = playcnt;
        }

        public int getTimeLength() {
            return timeLength;
        }

        public void setTimeLength(int timeLength) {
            this.timeLength = timeLength;
        }

        public int getOwnerAlbumId() {
            return ownerAlbumId;
        }

        public void setOwnerAlbumId(int ownerAlbumId) {
            this.ownerAlbumId = ownerAlbumId;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }

        public String getAlbumUrl() {
            return albumUrl;
        }

        public void setAlbumUrl(String albumUrl) {
            this.albumUrl = albumUrl;
        }

        public String getHtml5Url() {
            return html5Url;
        }

        public void setHtml5Url(String html5Url) {
            this.html5Url = html5Url;
        }

        public String getHtml5PlayUrl() {
            return html5PlayUrl;
        }

        public void setHtml5PlayUrl(String html5PlayUrl) {
            this.html5PlayUrl = html5PlayUrl;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getPosterPicUrl() {
            return posterPicUrl;
        }

        public void setPosterPicUrl(String posterPicUrl) {
            this.posterPicUrl = posterPicUrl;
        }

        public String getLeafctgs() {
            return leafctgs;
        }

        public void setLeafctgs(String leafctgs) {
            this.leafctgs = leafctgs;
        }

        public String getProducer() {
            return producer;
        }

        public void setProducer(String producer) {
            this.producer = producer;
        }

        public String getCredits() {
            return credits;
        }

        public void setCredits(String credits) {
            this.credits = credits;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAlbumType() {
            return albumType;
        }

        public void setAlbumType(String albumType) {
            this.albumType = albumType;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getSysPlatform() {
            return sysPlatform;
        }

        public void setSysPlatform(String sysPlatform) {
            this.sysPlatform = sysPlatform;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public UpDownBean getUpDown() {
            return upDown;
        }

        public void setUpDown(UpDownBean upDown) {
            this.upDown = upDown;
        }

        public int getSeason() {
            return season;
        }

        public void setSeason(int season) {
            this.season = season;
        }

        public String getEdition() {
            return edition;
        }

        public void setEdition(String edition) {
            this.edition = edition;
        }

        public String getSwf() {
            return swf;
        }

        public void setSwf(String swf) {
            this.swf = swf;
        }

        public List<Integer> getTvIds() {
            return tvIds;
        }

        public void setTvIds(List<Integer> tvIds) {
            this.tvIds = tvIds;
        }

        public List<Integer> getTvQipuIds() {
            return tvQipuIds;
        }

        public void setTvQipuIds(List<Integer> tvQipuIds) {
            this.tvQipuIds = tvQipuIds;
        }

        public List<ThreeCtgsBean> getThreeCtgs() {
            return threeCtgs;
        }

        public void setThreeCtgs(List<ThreeCtgsBean> threeCtgs) {
            this.threeCtgs = threeCtgs;
        }

        public List<CreditListBean> getCreditList() {
            return creditList;
        }

        public void setCreditList(List<CreditListBean> creditList) {
            this.creditList = creditList;
        }

        public List<?> getSubsites() {
            return subsites;
        }

        public void setSubsites(List<?> subsites) {
            this.subsites = subsites;
        }

        public static class UpDownBean {
            /**
             * score : 9.4
             * voters : 0
             * up : 0
             * albumQipuId : 736923000
             * down : 0
             */

            @SerializedName("score")
            private double score;
            @SerializedName("voters")
            private int voters;
            @SerializedName("up")
            private int up;
            @SerializedName("albumQipuId")
            private int albumQipuId;
            @SerializedName("down")
            private int down;

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }

            public int getVoters() {
                return voters;
            }

            public void setVoters(int voters) {
                this.voters = voters;
            }

            public int getUp() {
                return up;
            }

            public void setUp(int up) {
                this.up = up;
            }

            public int getAlbumQipuId() {
                return albumQipuId;
            }

            public void setAlbumQipuId(int albumQipuId) {
                this.albumQipuId = albumQipuId;
            }

            public int getDown() {
                return down;
            }

            public void setDown(int down) {
                this.down = down;
            }
        }

        public static class ThreeCtgsBean {
            /**
             * name : 热点
             * subType : 2
             * subCtgName : 内容类型
             * id : 170
             */

            @SerializedName("name")
            private String name;
            @SerializedName("subType")
            private int subType;
            @SerializedName("subCtgName")
            private String subCtgName;
            @SerializedName("id")
            private int id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSubType() {
                return subType;
            }

            public void setSubType(int subType) {
                this.subType = subType;
            }

            public String getSubCtgName() {
                return subCtgName;
            }

            public void setSubCtgName(String subCtgName) {
                this.subCtgName = subCtgName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class CreditListBean {
            /**
             * name : 热狗
             * roleName :
             * type : 21
             */

            @SerializedName("name")
            private String name;
            @SerializedName("roleName")
            private String roleName;
            @SerializedName("type")
            private int type;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
