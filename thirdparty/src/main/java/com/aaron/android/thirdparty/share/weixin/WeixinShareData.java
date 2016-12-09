package com.aaron.android.thirdparty.share.weixin;

/**
 * 微信分享数据(包括分享内容类型(如文本|图片|app|网页))
 * Created on 15/10/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class WeixinShareData {
    public static final String TRANSACTION_WEBPAGE = "webpage";
    //标题
    private String mTitle;
    //描述
    private String mDescription;
    //类型
    private String mTransactionType; //example:"webpage"

    private WeixinSceneType mWeixinSceneType;
    private int mIconResId;

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int iconResId) {
        mIconResId = iconResId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTransactionType() {
        return mTransactionType;
    }

    public void setTransactionType(String transactionType) {
        mTransactionType = transactionType;
    }

    public WeixinSceneType getWeixinSceneType() {
        return mWeixinSceneType;
    }

    public void setWeixinSceneType(WeixinSceneType weixinSceneType) {
        mWeixinSceneType = weixinSceneType;
    }

    /**
     * 分享WebPage数据
     */
    public static class WebPageData extends WeixinShareData {
        private String mWebUrl;

        public WebPageData() {
            setTransactionType(TRANSACTION_WEBPAGE);
        }

        public String getWebUrl() {
            return mWebUrl;
        }

        public void setWebUrl(String webUrl) {
            mWebUrl = webUrl;
        }
    }

    /**
     * 分享到微信平台类型:微信好友|微信朋友圈
     */
    public enum WeixinSceneType {
        //分享给好友
        FRIEND,
        //分享到朋友圈
        CIRCLE
    }
}
