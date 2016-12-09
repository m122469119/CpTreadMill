package com.aaron.android.thirdparty.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.aaron.android.thirdparty.R;
import com.aaron.android.thirdparty.pay.Pay;
import com.aaron.android.thirdparty.pay.alipay.utils.Result;
import com.alipay.sdk.app.PayTask;


/**
 * Created on 15/10/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class AliPay extends Pay<Object> {
    private static final int ALIPAY_FLAG = 1;
    private static final String RESULT_SUCCESS = "9000";
    private static final String RESULT_CONFIRMING = "8000";
    private static final String TAG = "alipay";
    private Activity mContext;
    private OnAliPayListener mOnAliPayListener;
    private String mPayOrderInfo;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ALIPAY_FLAG) {
                Result result = new Result((String) msg.obj);
                String resultStatus = result.resultStatus;
                switch (resultStatus) {
                    case RESULT_SUCCESS://成功
                        mOnAliPayListener.onSuccess();
                        break;
                    case RESULT_CONFIRMING://支付确认中
                        mOnAliPayListener.confirm();
                        break;
                    default://失败
                        mOnAliPayListener.onFailure(mContext.getString(R.string.pay_order_fail_message));
                        break;
                }
            }
        }
    };

    public AliPay(Activity context, OnAliPayListener aliPayListener) {
        mContext = context;
        mOnAliPayListener = aliPayListener == null ? new OnAliPayListenerImpl() : aliPayListener;
    }

    /**
     * 订单信息
     *
     * @param orderInfo String
     */
    public void setPayOrderInfo(String orderInfo) {
        mPayOrderInfo = orderInfo;
    }

    private class OnAliPayListenerImpl implements OnAliPayListener {

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(String errorMessage) {

        }

        @Override
        public void confirm() {

        }
    }

    @Override
    public void initPayApi() {

    }

    @Override
    public void doPay() {
        if (mPayOrderInfo == null && "".equals(mPayOrderInfo)) {
            mOnAliPayListener.onFailure(mContext.getString(R.string.pay_order_fail_message));
            return;
        }
        Thread payThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                mOnAliPayListener.onStart();
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口
                String result = alipay.pay(mPayOrderInfo, true);
                Message msg = new Message();
                msg.what = ALIPAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
        payThread.start();
    }
}
