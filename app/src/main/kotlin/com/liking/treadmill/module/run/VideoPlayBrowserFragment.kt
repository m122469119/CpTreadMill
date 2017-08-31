package com.liking.treadmill.module.run

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.aaron.android.framework.base.ui.BaseFragment
import com.aaron.android.framework.utils.DisplayUtils
import com.liking.treadmill.BuildConfig
import com.liking.treadmill.R
import kotlinx.android.synthetic.main.layout_media_browser.*
import org.jetbrains.anko.backgroundResource
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.aaron.android.codelibrary.utils.LogUtils
import com.liking.treadmill.widget.IToast


/**
 * Created on 2017/08/23
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class VideoPlayBrowserFragment : BaseFragment() {

    private var mediaBrowserWebview: WebView? = null
//    private var h5url: String = ""
    private var category :String = ""

    private var marginTop : Int = 0

    private var margin1:Int = -166

    private var margin2:Int = -333

    private var handler: Handler = Handler()

    private var videoTypeRun =  Runnable {
        //清晰度设置
        mediaBrowserWebview?.loadUrl(
                "javascript: document.getElementsByClassName('c-videoType-item')[1].click()")
    }

    companion object {
        val H5URL_KEY = "h5url"
        val CATEGORY_KEY = "category"
        val tag = "VideoPlayBrowserFragment"
        fun newInstance(category :String, h5Url: String): VideoPlayBrowserFragment {
            val fragment = VideoPlayBrowserFragment()
            val args = android.os.Bundle()
            args.putString(CATEGORY_KEY, category)
            args.putString(H5URL_KEY, h5Url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_media_browser, null)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    fun loadUrl(h5url:String ) {
        LogUtils.e(TAG, "loadurl:"  + h5url)
        mediaBrowserWebview?.loadUrl(h5url)
    }

    fun initView() {
        mediaBrowserWebview = WebView(context.applicationContext)
        mediaBrowserWebview?.setBackgroundColor(android.R.color.transparent)
        media_browser_layout.backgroundResource = R.drawable.media_video_bg
        media_browser_layout.removeAllViews()
        media_browser_layout.addView(mediaBrowserWebview)

        var layoutParams = LinearLayout.LayoutParams(DisplayUtils.dp2px(1200), DisplayUtils.dp2px(675))
        media_browser_layout.layoutParams = layoutParams

        var layoutWebViewParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mediaBrowserWebview?.layoutParams = layoutWebViewParams
        mediaBrowserWebview?.removeJavascriptInterface("searchBoxJavaBridge_")
        // init webview settings
        mediaBrowserWebview?.settings?.javaScriptEnabled = true
        mediaBrowserWebview?.settings?.allowContentAccess = true
        mediaBrowserWebview?.settings?.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        mediaBrowserWebview?.settings?.useWideViewPort = true
        mediaBrowserWebview?.settings?.databaseEnabled = true
        mediaBrowserWebview?.settings?.domStorageEnabled = true
        mediaBrowserWebview?.settings?.loadWithOverviewMode = true

        mediaBrowserWebview?.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY // 设置无边框
        mediaBrowserWebview?.isHorizontalScrollBarEnabled = false//水平不显示
        mediaBrowserWebview?.isVerticalScrollBarEnabled = false //垂直不显示
        mediaBrowserWebview?.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    autoPlayVideo()
                }
            }
        })
        mediaBrowserWebview?.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                autoPlayVideo()
            }
        })
    }

    fun initData() {
        arguments.let {
//            h5url = it.getString(H5URL_KEY,"")
            category = it.getString(CATEGORY_KEY,"")
        }
    }

    override fun onDestroyView() {
        mediaBrowserWebview.let {
            media_browser_layout.removeAllViews()
            mediaBrowserWebview?.stopLoading()
            mediaBrowserWebview?.removeAllViews()
            mediaBrowserWebview?.destroy()
            mediaBrowserWebview = null
            releaseAllWebViewCallback()
        }
        super.onDestroyView()
        handler.removeCallbacks(videoTypeRun)
    }

    fun releaseAllWebViewCallback() {
        if (android.os.Build.VERSION.SDK_INT < 16) {
            try {
                var field = WebView::class.java.getDeclaredField("mWebViewCore")
                field = field.type.getDeclaredField("mBrowserFrame")
                field = field.type.getDeclaredField("sConfigCallback")
                field.isAccessible = true
                field.set(null, null)
            } catch (e: NoSuchFieldException) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            } catch (e: IllegalAccessException) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }

        } else {
            try {
                val sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback")
                if (sConfigCallback != null) {
                    sConfigCallback.isAccessible = true
                    sConfigCallback.set(null, null)
                }
            } catch (e: NoSuchFieldException) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            } catch (e: ClassNotFoundException) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            } catch (e: IllegalAccessException) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }

        }
    }

    fun autoPlayVideo() {
        IToast.show("autoPlayVideo")
        if("1".equals(category)) { //电影
            marginTop = margin1
        } else{
            marginTop = margin2
        }
        handler.removeCallbacks(videoTypeRun)
        handler.postDelayed(videoTypeRun, 2 * 60 * 1000 + 5000)
        mediaBrowserWebview?.loadUrl(
                "javascript:document.getElementsByTagName('body')[0].style.setProperty('margin-top', "
                        +"'${marginTop}px', 'important'); " +
                " document.getElementsByTagName('video')[0].play();")
    }

    fun pausePlayVideo() {
        mediaBrowserWebview?.loadUrl(
                        "javascript: document.getElementsByTagName('video')[0].pause();")
    }

    fun playVideo() {
        handler.removeCallbacks(videoTypeRun)
        handler.postDelayed(videoTypeRun, 2 * 60 * 1000 + 5000)
        mediaBrowserWebview?.loadUrl(
                "javascript: document.getElementsByTagName('video')[0].play();")
    }

}