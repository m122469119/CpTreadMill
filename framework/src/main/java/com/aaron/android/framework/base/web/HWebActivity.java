package com.aaron.android.framework.base.web;

import android.os.Bundle;
import android.view.KeyEvent;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.library.web.HWebView;

/**
 * Created on 15/10/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class HWebActivity extends AppBarActivity {
    protected HWebView mWebView;

    protected abstract void onCreate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new HWebView(this);
        setContentView(mWebView);
        onCreate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mWebView.doBackKeyAction(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}
