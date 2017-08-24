package liking.com.iqiyimedia.http.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2017/07/25
 * desc: 排行榜
 *
 * @author: chenlei
 * @version:1.0
 */

public class TopListResult extends IqiyiResult {

    /**
     * total : 150
     * pageNo : 1
     * pagesize : 150
     * foundNum : 94
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
         * albumId : 264740100
         * qipuId : 264740100
         * ipLimit : 1
         * categoryId : 1
         * sets : 1
         * purchaseType : 0
         * isPurchase : 0
         * payMark : 0
         * copyrightId : 13397
         * tvYear : 20120817
         * albumStatus : 1
         * contentType : 1
         * isSD : 0
         * is3D : 0
         * sourceId : 0
         * isDubo : 0
         * isCoopAllowed : 0
         * downloadAllowed : 1
         * sourceQipuId : 0
         * playcnt : 23089163
         * timeLength : 6143
         * ownerAlbumId : -1
         * albumName : 敢死队2
         * desc : 再次经历了一番炮火密集的血雨腥风，巴尼、圣诞、贡纳、比利、收费公路和凯撒这几个敢死队的铁哥们正享受大战之后的难得惬意。可是好景不长，某晚教堂找上门来，迫令他前往阿尔巴尼亚噶扎克山区找一架被击落飞机上的保险箱，并确保箱子不会落入敌人之手，教堂还委派麦琪·张与这群硬汉同行。看似简单的任务实则充满凶险，比利被对手残忍杀害。这已不再是一项任务，而是一场关乎战士们友情与荣誉的战争。
         * startDate : 2017-06-20 08:00:00
         * endDate : 2020-03-31 08:00:00
         * focus : 李连杰斯坦森火力全开
         * albumUrl : http://www.iqiyi.com/v_19rrho97jc.html?vfm=newvfm
         * html5Url : http://m.iqiyi.com/v_19rrho97jc.html?vfm=newvfm
         * html5PlayUrl : http://m.iqiyi.com/v_19rrho97jc.html?vfm=newvfm
         * picUrl : http://pic5.qiyipic.com/image/20170622/14/9c/v_62647401_m_601_m4.jpg
         * posterPicUrl : http://pic4.qiyipic.com/image/20170622/79/eb/v_62647401_m_600_m2.jpg
         * leafctgs : 美国 动作 1080P 英语 军事 暴力 当代 冒险 街道 好莱坞 片库
         * producer : 巴兹尔·伊万尼克,艾威·勒纳,丹尼·伦纳
         * credits : 西尔维斯特·史泰龙 杰森·斯坦森 布鲁斯·威利斯 西蒙·韦斯特
         * source :
         * companyName : 霍尔果斯捷成华视网聚文化传媒有限公司
         * keyword : 敢死队
         * subTitle :
         * score : 7.9
         * actor : 西尔维斯特·史泰龙 杰森·斯坦森 布鲁斯·威利斯
         * director : 西蒙·韦斯特
         * area : 美国
         * albumType : 动作|冒险
         * playUrl : http://www.iqiyi.com/v_19rrho97jc.html?vfm=newvfm
         * createdTime : 2017-06-23 20:01:07
         * sysPlatform : PHONE,PAD,PC,PC_APP,TV,PHONE_WEB_IQIYI,PAD_WEB_IQIYI
         * alias : 浴血任务2
         * tvIds : [264740100]
         * tvQipuIds : [264740100]
         * threeCtgs : [{"name":"美国","subType":1,"subCtgName":"地区","id":2},{"name":"动作","subType":2,"subCtgName":"类型","id":11},{"name":"1080P","subType":7,"subCtgName":"规格","id":999},{"name":"英语","subType":0,"subCtgName":"配音语种","id":20004},{"name":"军事","subType":3,"subCtgName":"题材","id":23682},{"name":"暴力","subType":4,"subCtgName":"风格","id":23741},{"name":"当代","subType":6,"subCtgName":"时代","id":23782},{"name":"冒险","subType":2,"subCtgName":"类型","id":27355},{"name":"街道","subType":5,"subCtgName":"地点场景","id":27826},{"name":"好莱坞","subType":3,"subCtgName":"流派","id":29735},{"name":"片库","subType":7,"subCtgName":"规格","id":30088}]
         * creditList : [{"name":"西尔维斯特·史泰龙","roleName":"Barney Ross","type":8},{"name":"杰森·斯坦森","roleName":"Lee Christmas","type":8},{"name":"布鲁斯·威利斯","roleName":"Church","type":8},{"name":"尚格·云顿","roleName":"Vilain","type":11},{"name":"李连杰","roleName":"Yin Yang","type":11},{"name":"杜夫·龙格尔","roleName":"Gunnar Jensen","type":11},{"name":"查克·诺瑞斯","roleName":"Booker","type":11},{"name":"阿诺·施瓦辛格","roleName":"Trench","type":11},{"name":"泰瑞·克鲁斯","roleName":"Hale Caesar","type":11},{"name":"兰迪·库卓","roleName":"Toll Road","type":11},{"name":"利亚姆·海姆斯沃斯","roleName":"Bill The Kid","type":11},{"name":"斯科特·阿特金斯","roleName":"Hector","type":11},{"name":"余男","roleName":"Maggie","type":11},{"name":"阿曼达·奥慕斯","roleName":"Pilar","type":11},{"name":"查瑞丝玛·卡朋特","roleName":"Lacy","type":11},{"name":"诺瓦克·德约科维奇","roleName":"Himself (uncredited)","type":11}]
         * upDown : {"score":7.9,"voters":3729,"up":3016,"albumQipuId":264740100,"down":713}
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
        private String playcnt;
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
        private List<String> tvIds;
        @SerializedName("tvQipuIds")
        private List<String> tvQipuIds;
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

        public String getPlaycnt() {
            return playcnt;
        }

        public void setPlaycnt(String playcnt) {
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

        public List<String> getTvIds() {
            return tvIds;
        }

        public void setTvIds(List<String> tvIds) {
            this.tvIds = tvIds;
        }

        public List<String> getTvQipuIds() {
            return tvQipuIds;
        }

        public void setTvQipuIds(List<String> tvQipuIds) {
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
             * score : 7.9
             * voters : 3729
             * up : 3016
             * albumQipuId : 264740100
             * down : 713
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
             * name : 美国
             * subType : 1
             * subCtgName : 地区
             * id : 2
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
             * name : 西尔维斯特·史泰龙
             * roleName : Barney Ross
             * type : 8
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
