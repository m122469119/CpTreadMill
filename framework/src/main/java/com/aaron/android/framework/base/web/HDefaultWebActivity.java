package com.aaron.android.framework.base.web;

import android.content.Context;
import android.content.Intent;

import com.aaron.android.codelibrary.utils.StringUtils;

/**
 * Created on 15/10/14.
 *
 * @author aaron.huang
 * @version 1.0.0
 *
 * 需要传递url和title参数的默认的WebActivity,是抽象HWebActivity类的一种默认实现
 */
public class HDefaultWebActivity extends HWebActivity {
    public final static String EXTRA_URL = "url";
    public final static String EXTRA_TITLE = "title";
    @Override
    protected void onCreate() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_URL);
        String title = intent.getStringExtra(EXTRA_TITLE);
        if (!StringUtils.isEmpty(title)) {
            setTitle(title);
        }
        if (!StringUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    public static void launch(Context context, String url, String title) {
        Intent intent = new Intent(context,
                HDefaultWebActivity.class);
        intent.putExtra(HDefaultWebActivity.EXTRA_URL, url);
        intent.putExtra(HDefaultWebActivity.EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    public static void launch(Context context, String url, String title, int flags) {
        Intent intent = new Intent(context,
                HDefaultWebActivity.class);
        intent.setFlags(flags);
        intent.putExtra(HDefaultWebActivity.EXTRA_URL, url);
        intent.putExtra(HDefaultWebActivity.EXTRA_TITLE, title);
        context.startActivity(intent);
    }
}
