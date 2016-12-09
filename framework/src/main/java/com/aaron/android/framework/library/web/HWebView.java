package com.aaron.android.framework.library.web;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aaron.android.framework.R;

/**
 * Created on 15/7/30.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class HWebView extends WebView {
    private Context mContext;
    private HWebProtocolHelper mWebProtocolHelper;
    public HWebView(Context context) {
        super(context);
        init(context);
    }

    public HWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void init(Context context) {
        mContext = context;
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setWebViewClient(new HWebViewClient());
        setWebChromeClient(new HWebChromeClient());
        addJavascriptInterface(new NativeMethod(getContext()), "NativeMethod");
    }

    class HWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean b = false;
            if (mWebProtocolHelper != null) {
                mWebProtocolHelper.setUrl(url);
                b = mWebProtocolHelper.verify();
            }
            if (!b) {
                /**处理内部链接跳转*/
                view.loadUrl(url);
            }
            return true;
        }
    }

    class HWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            /**使js中的alert调用android native系统弹框*/
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(message);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            result.confirm();
            return true;
        }
    }
    /**
     * 提供给Activity按返回键时WebView的处理操作
     * @param keyCode KeyCode
     * @param event KeyEvent
     * @return 是否被WebView处理返回键操作
     */
    public boolean doBackKeyAction(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && canGoBack()) {
            goBack();
            return true;
        }
        return false;
    }

    public HWebProtocolHelper getWebProtocolHelper() {
        return mWebProtocolHelper;
    }

    public void setWebProtocolHelper(HWebProtocolHelper webProtocolHelper) {
        mWebProtocolHelper = webProtocolHelper;
    }
}
