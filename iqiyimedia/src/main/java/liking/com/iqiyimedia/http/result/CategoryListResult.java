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

public class CategoryListResult extends IqiyiResult {


    /**
     * total : 9
     * data : [{"categoryId":7,"categoryName":"娱乐","shortName":"娱乐"},{"categoryId":6,"categoryName":"综艺","shortName":"综艺"},{"categoryId":8,"categoryName":"游戏","shortName":"游戏"},{"categoryId":2,"categoryName":"电视剧","shortName":"电视剧"},{"categoryId":4,"categoryName":"动漫","shortName":"动漫"},{"categoryId":15,"categoryName":"儿童","shortName":"儿童"},{"categoryId":1,"categoryName":"电影","shortName":"电影"},{"categoryId":10,"categoryName":"片花","shortName":"片花"},{"categoryId":31,"categoryName":"脱口秀","shortName":"脱口秀"}]
     * pageNo : 1
     * pagesize : 100
     * foundNum : 9
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
         * categoryId : 7
         * categoryName : 娱乐
         * shortName : 娱乐
         */

        @SerializedName("categoryId")
        private int categoryId;
        @SerializedName("categoryName")
        private String categoryName;
        @SerializedName("shortName")
        private String shortName;

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }
    }
}
