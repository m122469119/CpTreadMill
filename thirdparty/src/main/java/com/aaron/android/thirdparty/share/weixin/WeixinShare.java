package com.aaron.android.thirdparty.share.weixin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.aaron.android.thirdparty.R;
import com.aaron.android.thirdparty.pay.Utils;
import com.aaron.android.thirdparty.pay.weixin.utils.WeixinPayConstants;
import com.aaron.android.thirdparty.share.Share;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * 微信分享
 * Created on 15/10/16.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class WeixinShare extends Share {

    public static final String COM_TENCENT_MM = "com.tencent.mm";
    private static final int THUMB_SIZE = 150;
    private IWXAPI mApi;
    private Context mContext;

    public WeixinShare(Context context) {
        mContext = context;
        initialize();
    }

    @Override
    public void initialize() {
        mApi = WXAPIFactory.createWXAPI(mContext, WeixinPayConstants.APP_ID);
    }

    public void shareWebPage(WeixinShareData.WebPageData webPageData) {
        if (!Utils.isAppInstalled(mContext, COM_TENCENT_MM)) {
            Toast.makeText(mContext, R.string.weixin_pay_no_install, Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webPageObject = new WXWebpageObject();
        webPageObject.webpageUrl = webPageData.getWebUrl();
        WXMediaMessage wxMediaMessage = new WXMediaMessage(webPageObject);
        wxMediaMessage.title = webPageData.getTitle();
        wxMediaMessage.thumbData = bmpToByteArray(webPageData.getIconResId(), true);
        wxMediaMessage.description = webPageData.getDescription();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(webPageData.getTransactionType());
        req.message = wxMediaMessage;
        req.scene = webPageData.getWeixinSceneType() == WeixinShareData.WeixinSceneType.FRIEND
                ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        mApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private byte[] bmpToByteArray(int imageResId, final boolean needRecycle) {
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            thumbBmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
