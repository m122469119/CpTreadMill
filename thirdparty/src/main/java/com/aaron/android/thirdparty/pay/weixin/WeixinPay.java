package com.aaron.android.thirdparty.pay.weixin;

import android.content.Context;
import android.widget.Toast;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.SecurityUtils;
import com.aaron.android.thirdparty.R;
import com.aaron.android.thirdparty.pay.Pay;
import com.aaron.android.thirdparty.pay.Utils;
import com.aaron.android.thirdparty.pay.weixin.utils.WeixinPayConstants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created on 15/9/29.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class WeixinPay extends Pay<IWXAPI> {

    private final static String TAG = "WeixinPay";
    public static final String PACKAGENAME_WEIXIN = "com.tencent.mm";
    private Context mContext;
    private String mPrePayId;
    private WeixinPayListener mWeixinPayListener; //微信支付回调

    public WeixinPay(Context context, WeixinPayListener weixinPayListener) {
        mContext = context;
        mWeixinPayListener = weixinPayListener != null ? weixinPayListener : new WeixinPayListener.DefaultWeixinPayListenerImpl();
        setPayApi(WXAPIFactory.createWXAPI(mContext, null));
        initPayApi();
    }


    @Override
    public void initPayApi() {
        getPayApi().registerApp(WeixinPayConstants.APP_ID);
    }

    @Override
    public void doPay() {
        if (!Utils.isAppInstalled(mContext, PACKAGENAME_WEIXIN)) {
            Toast.makeText(mContext, R.string.weixin_pay_no_install, Toast.LENGTH_SHORT).show();
            return;
        }
        sendPayReq();
    }

    public void setPrePayId(String prePayId) {
        mPrePayId = prePayId;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 生成调起微信支付参数
     * @return
     */
    private PayReq genPayReq() {
        PayReq payReq = new PayReq();
        payReq.appId = WeixinPayConstants.APP_ID;
        payReq.partnerId = WeixinPayConstants.PARTNER_ID;
        payReq.prepayId = mPrePayId;
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = genNonceStr();
        LogUtils.d(TAG, "nonceStr: " + payReq.nonceStr);
        payReq.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<>();
        signParams.add(new BasicNameValuePair("appid", payReq.appId));
        signParams.add(new BasicNameValuePair("noncestr", payReq.nonceStr));
        signParams.add(new BasicNameValuePair("package", payReq.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", payReq.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", payReq.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", payReq.timeStamp));
        payReq.sign = genSign(signParams);
        return payReq;
    }

    /**
     * 调起微信支付
     */
    private void sendPayReq() {
        LogUtils.i(TAG, "weixin sendPayReq");
        PayReq payReq = genPayReq();
        getPayApi().registerApp(WeixinPayConstants.APP_ID);
        getPayApi().sendReq(payReq);
        mWeixinPayListener.onSuccess();
    }

    /**
     * 构造参数列表
     *
     * @param params
     * @return
     */
    private String genSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < params.size(); i++) {
            String value = params.get(i).getValue();
            if (value == null || value.equals("")) {
                continue;
            }
            LogUtils.d(TAG, "key: " +params.get(i).getName() + " value: " + params.get(i).getValue());
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(WeixinPayConstants.SIGN_KEY);
        LogUtils.d(TAG, "sign-stringA: " + sb.toString());
        String sign = SecurityUtils.MD5.get32MD5String(sb.toString()).toUpperCase();
        LogUtils.d(TAG, "sign: " + sign);
        return sign;
    }

    /**
     * 建议 traceid 字段包含用户信息及订单信息，方便后续对订单状态的查询和跟踪
     */
    private String getTraceId() {
        return "crestxu_" + genTimeStamp();
    }

    private String genNonceStr() {
        Random random = new Random();
        return SecurityUtils.MD5.get32MD5String(String.valueOf(random.nextInt(10000)));
    }

}
