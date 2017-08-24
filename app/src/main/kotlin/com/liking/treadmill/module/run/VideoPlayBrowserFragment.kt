package com.liking.treadmill.module.run

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
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
import android.view.animation.ScaleAnimation
import android.webkit.WebChromeClient
import android.webkit.WebViewClient


/**
 * Created on 2017/08/23
 * desc:
 * @author: chenlei
 * @version:1.0
 */
class VideoPlayBrowserFragment : BaseFragment() {

    private var mediaBrowserWebview: WebView? = null
    private var h5url: String = "http://m.iqiyi.com/v_19rrolm54g.html?vfm=newvfm"

    companion object {
        val H5URL_KEY = "h5url"
        val tag = "VideoPlayBrowserFragment"
        fun newInstance(h5Url: String): VideoPlayBrowserFragment {
            val fragment = VideoPlayBrowserFragment()
            val args = android.os.Bundle()
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

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
        if (enter) {
            val scaleAnimationIn = ScaleAnimation(0f, 1f, 0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            scaleAnimationIn.duration = 800
            scaleAnimationIn.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    loadUrl()
                }
            })
            return scaleAnimationIn
        } else {
            val scaleAnimationOut = ScaleAnimation(1f, 0f, 1f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            scaleAnimationOut.duration = 800
            return scaleAnimationOut
        }
    }

    fun loadUrl() {
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
                    playVideo()
                }
            }
        })
        mediaBrowserWebview?.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                playVideo()
            }
        })
    }

    fun initData() {
        arguments.let {
            h5url = it.getString(H5URL_KEY,"")
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

    fun playVideo() {
        mediaBrowserWebview?.loadUrl("javascript:" + " document.getElementsByTagName('body')[0].style.setProperty('margin-top', '-116" +
                "px', 'important'); " +
                " var video = document.getElementsByTagName('video')[0]; video.play();")
    }

}