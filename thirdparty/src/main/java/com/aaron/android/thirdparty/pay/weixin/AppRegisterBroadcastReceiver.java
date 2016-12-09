package com.aaron.android.thirdparty.pay.weixin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aaron.android.thirdparty.pay.weixin.utils.WeixinPayConstants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegisterBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        /**微信支付注册app*/
		api.registerApp(WeixinPayConstants.APP_ID);
	}
}
