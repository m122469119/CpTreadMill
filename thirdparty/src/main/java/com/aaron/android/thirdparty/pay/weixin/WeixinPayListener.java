package com.aaron.android.thirdparty.pay.weixin;

import com.aaron.android.thirdparty.pay.PayListener;

/**
 * Created on 15/10/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public interface WeixinPayListener extends PayListener {

     class DefaultWeixinPayListenerImpl implements WeixinPayListener {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(String errorMessage) {

        }
    }
}
