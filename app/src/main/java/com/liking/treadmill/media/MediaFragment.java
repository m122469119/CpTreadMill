package com.liking.treadmill.media;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.liking.treadmill.R;
import com.liking.treadmill.fragment.base.SerialPortFragment;

/**
 * Created on 2017/07/20
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class MediaFragment extends SerialPortFragment{

    private WebView webView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_media_browser, null);
        webView = (WebView) view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        webView.loadData("<iframe src='http://open.iqiyi.com/developer/player_js/coopPlayerIndex.html?vid=77a4ba9809e102a2148796a15c4d964c&tvId=485218900&accessToken=2.f22860a2479ad60d8da7697274de9346&appKey=3955c3425820435e86d0f4cdfe56f5e7&appId=1368&height=100%&width=100%' frameborder='0' width='100%' height='100%' allowfullscreen='true'></iframe>", "text/html; charset=UTF-8", null);//这种写法可以正确解码
        return view;
    }



}
