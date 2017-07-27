package liking.com.iqiyimedia.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseFragment;

import liking.com.iqiyimedia.R;

/**
 * Created on 2017/07/20
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class MediaFragment extends BaseFragment{

    private WebView webView = null;

    public static MediaFragment getInstance(Bundle bundle) {
        MediaFragment fragment = new MediaFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_media, null);
        webView = (WebView) view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        LogUtils.e("info", getArguments().getInt("tvId") + "");
        webView.loadData("<iframe src='http://open.iqiyi.com/developer/player_js/coopPlayerIndex.html?vid=77a4ba9809e102a2148796a15c4d964c&tvId=" + getArguments().getInt("tvId") + "&accessToken=2.f22860a2479ad60d8da7697274de9346&appKey=3955c3425820435e86d0f4cdfe56f5e7&appId=1368&height=100%&width=100%' frameborder='10' width='100%' height='100%' allowfullscreen='true'></iframe>", "text/html; charset=UTF-8", null);//这种写法可以正确解码
//        webView.loadData("<iframe src='http://open.iqiyi.com/developer/player_js/coopPlayerIndex.html?vid= " + IqiyiApiHelper.IQIYI_APIKEY + "&tvId= " + getArguments().getInt("tvId") + "&accessToken=2.f22860a2479ad60d8da7697274de9346&appKey=3955c3425820435e86d0f4cdfe56f5e7&appId=1368&height=100%&width=100%' frameborder='0' width='100%' height='100%' allowfullscreen='true'></iframe>", "text/html; charset=UTF-8", null);//这种写法可以正确解码

//        webView.loadUrl("http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=" + IqiyiApiHelper.IQIYI_APIKEY + "&tvId= " + getArguments().getInt("tvId") + "&coop=&cid=&bd=1&fullscreen=1");

//        webView.loadData("<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0\" width=\"680\" height=\"520\">\n" +
//                "  <param name=\"movie\" value=\"http://dispatcher.video.qiyi.com/disp/shareplayer.swf?vid=754159da4d6063c7eb8e99fb04020bab&tvId=479728600&coop=&cid=&bd=1\"/> \n" +
//                "  <param name=\"quality\" value=\"high\"/> \n" +
//                "  <param name=\"wmode\" value=\"transparent\"/> \n" +
//                "  <param value=\"true\" name=\"allowFullScreen\"/> \n" +
//                "  <embed src=\"http://dispatcher.video.qiyi.com/disp/shareplayer.swf?vid=754159da4d6063c7eb8e99fb04020bab&tvId=479728600&coop=&cid=&bd=1\" wmode=\"transparent\" quality=\"high\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\" width=\"680\" height=\"520\" allowfullscreen=\"true\"/>\n" +
//                "</object>", "text/html; charset=UTF-8", null);
        return view;
    }



}
