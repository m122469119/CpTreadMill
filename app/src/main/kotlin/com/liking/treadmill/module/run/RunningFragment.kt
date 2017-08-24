package com.liking.treadmill.module.run

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaron.android.codelibrary.utils.StringUtils
import com.aaron.android.framework.base.widget.refresh.StateView
import com.aaron.android.framework.utils.DisplayUtils
import com.aaron.android.framework.utils.ResourceUtils
import com.liking.treadmill.R
import com.liking.treadmill.R.id.layout_run_video_category_stateview
import com.liking.treadmill.adapter.IqiyiVideoEpisodesAdapter
import com.liking.treadmill.adapter.IqiyiVideoListAdapter
import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.fragment.base.SerialPortFragment
import com.liking.treadmill.treadcontroller.SerialPortUtil
import com.liking.treadmill.widget.IToast
import com.liking.treadmill.widget.ScrollSpeedLinearLayoutManger
import kotlinx.android.synthetic.main.fragment_running.*
import kotlinx.android.synthetic.main.layout_category_tablayout.view.*
import kotlinx.android.synthetic.main.layout_run_bottom.*
import kotlinx.android.synthetic.main.layout_run_content.view.*
import kotlinx.android.synthetic.main.layout_run_head.view.*
import kotlinx.android.synthetic.main.layout_run_video_category.*
import kotlinx.android.synthetic.main.layout_run_video_category.view.*
import kotlinx.android.synthetic.main.layout_run_video_list.*
import kotlinx.android.synthetic.main.layout_run_way.view.*
import liking.com.iqiyimedia.IqiyiContract
import liking.com.iqiyimedia.http.result.AlbumListResult
import liking.com.iqiyimedia.http.result.CategoryListResult
import liking.com.iqiyimedia.http.result.TopListResult
import liking.com.iqiyimedia.http.result.VideoInfoResult
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.layoutInflater


/**
 * Created on 2017/08/16
 * desc:

 * @author: chenlei
 * *
 * @version:1.0
 */

class RunningFragment : SerialPortFragment(), IqiyiContract.IqiyiView {

    private var categoryTabIndex: Int = 0
    private var animatorDuration: Long = 800

    private lateinit var mIqiyiPresenter: IqiyiContract.Presenter
    private lateinit var videoListAdapter: IqiyiVideoListAdapter
    private lateinit var videoEpisodeAdapter: IqiyiVideoEpisodesAdapter
    private var videoTotal: Int = 1
    private var videoSelectedPosition: Int = 0
    private var videoEpisodeSelectedPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_running, container, false)
        setPresenter()
        return rootView
    }

    private fun setPresenter() {
        mIqiyiPresenter = IqiyiContract.Presenter(context, this)
        mIqiyiPresenter.attachView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    fun initView() {
        initCategoryTabView()

        //视频列表
        video_list_recyclerView.setHasFixedSize(true)
        video_list_recyclerView.layoutManager = ScrollSpeedLinearLayoutManger(context)
        video_list_recyclerView_episode.setHasFixedSize(true)
        video_list_recyclerView_episode.layoutManager = ScrollSpeedLinearLayoutManger(context)

        //频道切换提示
        val builderCategory = SpannableStringBuilder(ResourceUtils.getString(R.string.category_hint_txt))
        builderCategory.setSpan(
                ForegroundColorSpan(ResourceUtils.getColor(R.color.c27C454)), 1, 9,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        category_change_hint.text = builderCategory

        //视频切换提示
        val builderVideolist = SpannableStringBuilder(ResourceUtils.getString(R.string.videolist_hint_txt))
        builderVideolist.setSpan(
                ForegroundColorSpan(ResourceUtils.getColor(R.color.c27C454)), 1, 9,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        video_list_change_hint.text = builderVideolist

    }

    fun initData() {
        //视频列表
        videoListAdapter = IqiyiVideoListAdapter(context)
        video_list_recyclerView.adapter = videoListAdapter
    }

    /**
     * 加载视频分类
     */
    fun initCategoryTabView() {
//        if (mIqiyiPresenter != null) {
//            mIqiyiPresenter.getCategoryList(this)
//            layout_run_content.layout_run_video_category_stateview.setState(StateView.State.LOADING)
//        }
        ThreadMillConstant.CATEGORYRESOURCE.map {
            category_tab_layout.addTab(
                    category_tab_layout
                            .newTab()
                            .setTag(it.key)
                            .setCustomView(getTabView(it.value.categoryRes, it.value.categoryName)))
        }

        category_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                IToast.show("s:" + tab?.tag + ";count:" + category_tab_layout.tabCount
                        + ";index:" + categoryTabIndex + ";select:" + category_tab_layout.selectedTabPosition)
                if (categoryTabIndex >= category_tab_layout.tabCount - 1) {
                    categoryTabIndex = 0
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        layout_run_head.head_imageView.setOnClickListener {
            if (isInRunWayUI()) {
                hiddenRunWayUI()
                //显示catatory
                showVideoCategoryUI()
            } else {
                showRunWayUI()
                hiddenVideoCategoryUI()
            }
        }

        layout_run_head.user_name_TextView.setOnClickListener {

            if (isInVideosUI()) {
                if(isInVideoListUI()) {
                    getIntoIqiyiVideo(videoSelectedPosition)
                } else if(isInVideoEpisodeListUI()) {
                    getIntoIqiyiVideo(videoEpisodeSelectedPosition)
                }

            } else {
                changeCategoryTab()
            }
        }

        layout_run_head.text_gym_name.setOnClickListener {

            if (isInVideoCategoryUI()) {
                hiddenVideoCategoryUI()
                loadVideoList()
            }
        }
        layout_run_head.text_time.setOnClickListener {
            if (isInVideosUI()) {
                selectIqiyiVideoItem(true)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIqiyiPresenter.detachView(this)
    }

    /**
     * 选中视频Item进入播放或者选集
     * @direction
     */
    fun selectIqiyiVideoItem(direction: Boolean) {
        if(isInVideoListUI()) {
            val lastPosition = videoSelectedPosition
            ++videoSelectedPosition
            if(videoSelectedPosition >= videoListAdapter.dataList.size) {
                videoSelectedPosition = videoListAdapter.dataList.size - 1
            }
            selectedVideo(video_list_recyclerView, lastPosition, videoSelectedPosition)
        } else if(isInVideoEpisodeListUI()) {
            val lastPosition = videoEpisodeSelectedPosition
            ++videoEpisodeSelectedPosition
            if(videoEpisodeSelectedPosition >= videoEpisodeAdapter.dataList.size) {
                videoEpisodeSelectedPosition = videoEpisodeAdapter.dataList.size - 1
            }
            selectedVideo(video_list_recyclerView_episode, lastPosition, videoEpisodeSelectedPosition)
        }
    }

    /**
     * 选择video
     */
    fun selectedVideo(recyclerView: RecyclerView, lastPosition:Int, newPosition: Int) {
        recyclerView.smoothScrollToPosition(newPosition)
        recyclerView.postDelayed({
            if (recyclerView.findViewHolderForAdapterPosition(newPosition) != null) {
                recyclerView.findViewHolderForAdapterPosition(lastPosition).itemView.isSelected = false
                recyclerView.findViewHolderForAdapterPosition(newPosition).itemView.isSelected = true
            }
        }, 50)
    }

    /**
     * 进入视频播放
     */
    fun getIntoIqiyiVideo(position: Int) {
        if(isInVideoListUI()) {
            if (position < videoListAdapter.dataList.size) {
                videoListAdapter.dataList[position].let {
                    var videoName = it.albumName
                    IToast.show(videoName)
                    it.tvQipuIds.let {
                        if (it.size == 1) {
                            layout_run_content?.layout_run_video_list_stateview
                                    ?.findViewById(R.id.layout_root)?.visibility = View.VISIBLE
                            mIqiyiPresenter.getVideoInfo(this, it[0])
                        } else if (it.size > 1) { //剧集视频,显示剧集
                            video_list_recyclerView?.visibility = View.INVISIBLE
                            video_list_recyclerView_episode?.visibility = View.VISIBLE
                            videoEpisodeAdapter = IqiyiVideoEpisodesAdapter(videoName, context)
                            videoEpisodeAdapter.addData(it)
                            video_list_recyclerView_episode?.adapter = videoEpisodeAdapter
                        } else {
                            IToast.show("视频播放失败!")
                        }
                    }
                }
            }
        } else if(isInVideoEpisodeListUI()) {
            if (position < videoEpisodeAdapter.dataList.size) {
                videoEpisodeAdapter.dataList[position].let {
                    layout_run_content?.layout_run_video_list_stateview
                            ?.findViewById(R.id.layout_root)?.visibility = View.VISIBLE
                    mIqiyiPresenter.getVideoInfo(this, it.toString())
                }
            }
        }
    }

    /**
     * 切换频道
     */
    fun changeCategoryTab() {
        //切换频道
        if (category_tab_layout.selectedTabPosition < category_tab_layout.tabCount - 1) {
            ++categoryTabIndex
        }
        category_tab_layout.getTabAt(categoryTabIndex)?.select()
    }

    /**
     * 选中分类加载排行榜视频列表
     */
    fun loadVideoList() {
        showVideolistUI()
        layout_run_content.layout_run_video_list_stateview.setState(StateView.State.LOADING)
        loadIqiyiVideoTopList(category_tab_layout.getTabAt(category_tab_layout.selectedTabPosition)?.tag.toString())
    }

    /**
     * 加载爱奇艺排行榜视频
     */
    fun loadIqiyiVideoTopList(categoryId: String) {
        mIqiyiPresenter.getTopList(this, categoryId)
    }

    /**
     * 视频频道分类返回
     */
    override fun setCategoryListView(categoryResult: CategoryListResult?) {
//        layout_run_content.layout_run_video_category_stateview.setState(StateView.State.SUCCESS)
//        categoryResult.let {
//            it?.data?.map {
//                it.categoryId
//                category_tab_layout.addTab(
//                        category_tab_layout.newTab().setCustomView(getTabView(it.categoryName)))
//            }
//        }
    }

    /**
     * 分类tab
     */
    fun getTabView(img: Int, name: String): View {
        val inflate = context.layoutInflater.inflate(R.layout.layout_category_tablayout, null)
        inflate.tab_category_img.imageResource = img
        inflate.tab_category_txt.text = name
        return inflate
    }

    /**
     * 视频资源列表
     */
    override fun setAlbumListView(result: AlbumListResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 排行榜列表
     */
    override fun setTopListView(result: TopListResult?) {
        layout_run_content?.layout_run_video_list_stateview?.setState(StateView.State.SUCCESS)
        result.let {
            if (it != null) {
                videoTotal = it.total
                it.data.let {
                    //免费视频
                    videoListAdapter.addData(result?.data?.filter { 0 == it.isPurchase })
                    videoListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * 视频详细信息
     */
    override fun setVideoInfoView(result: VideoInfoResult?) {
        layout_run_content?.layout_run_video_list_stateview?.setState(StateView.State.SUCCESS)
        result.let {
            it?.data.let {
                if (it != null && !StringUtils.isEmpty(it.html5PlayUrl)) {
                    openVideoPlayBrowserUI(it.html5PlayUrl)
                } else {
                    IToast.show("视频播放失败!")
                }
            }
        }
    }

    /**
     * 加载失败
     */
    override fun showFailView(message: String?, failType: Int) {
        when (failType) {
            IqiyiContract.IQIYI_RESPONSE_FAIL_TOPLIST -> {
                layout_run_content?.layout_run_video_list_stateview?.setState(StateView.State.FAILED)
            }
            IqiyiContract.IQIYI_RESPONSE_FAIL_VIDEOINFO -> {
                layout_run_content?.layout_run_video_list_stateview
                        ?.findViewById(R.id.layout_root)?.visibility = View.GONE
                IToast.show("视频加载失败!")
            }


        }
    }

    /**
     * 跑道显示
     */
    fun showRunWayUI() {
        showTranslationYUI(layout_run_content.layout_run_way, animatorDuration)
    }

    /**
     * 跑道隐藏
     */
    fun hiddenRunWayUI() {
        hiddenTranslationYUI(layout_run_content.layout_run_way, animatorDuration)
    }

    /**
     * 显示频道分类
     */
    fun showVideoCategoryUI() {
        showScaleUI(layout_run_content.layout_run_video_category_stateview)
    }

    /**
     * 隐藏频道分类
     */
    fun hiddenVideoCategoryUI() {
        hiddenScaleUI(layout_run_content.layout_run_video_category_stateview)
    }

    /**
     * 显示视频列表
     */
    fun showVideolistUI() {
        showScaleUI(layout_run_content.layout_run_video_list_stateview)
    }

    /**
     * 隐藏视频列表
     */
    fun hiddenVideoListUI() {
        hiddenScaleUI(layout_run_content.layout_run_video_list_stateview)
    }

    /**
     * 打开播放页面
     */
    fun openVideoPlayBrowserUI(h5url: String) {
        //hidden列表向下移
        hiddenTranslationYUI(layout_run_content.layout_run_video_list_stateview, animatorDuration)

        //hidden head left
        ObjectAnimator.ofFloat(
                layout_run_head.layout_run_head_left,
                "translationX",
                layout_run_head.layout_run_head_left.translationX,
                -layout_run_head.layout_run_head_left.width.toFloat())
                .setDuration(animatorDuration)
                .start()

        //hidden head right
        var obj = ObjectAnimator.ofFloat(
                layout_run_head.layout_run_head_right,
                "translationX",
                layout_run_head.layout_run_head_right.translationX,
                layout_run_head.layout_run_head_right.width.toFloat())
                .setDuration(animatorDuration)
        obj.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                layout_run_video_play.visibility = View.VISIBLE
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.layout_run_video_play, VideoPlayBrowserFragment.newInstance(h5url))
                        .commitAllowingStateLoss()
                showTranslationYUI(run_bottom_layout_bg, animatorDuration)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
        obj.start()
    }

    /**
     * 关闭播放页面
     */
    fun closeVideoPlayBrowserUI() {

        //        layout_run_video_play.visibility = View.GONE
        hiddenTranslationYUI(run_bottom_layout_bg, animatorDuration)
        //show 播放列表向上移
        showTranslationYUI(layout_run_content.layout_run_video_list_stateview, animatorDuration)
        //show head left
        //show head right
    }

    /**
     * Y位移动画显示
     */
    fun hiddenTranslationYUI(view: View, duration: Long) {
        val y = view.translationY
        var translationYAnimatorHidden = ObjectAnimator.ofFloat(view,
                "translationY", y, view.height.toFloat())
                .setDuration(duration)
        translationYAnimatorHidden.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        translationYAnimatorHidden.start()
    }

    /**
     * Y位移动画隐藏
     */
    fun showTranslationYUI(view: View, duration: Long) {
        view.visibility = View.VISIBLE
        val y = view.translationY
        ObjectAnimator.ofFloat(view,
                "translationY", y, 0.0f)
                .setDuration(duration)
                .start()
    }

    /**
     * 缩放动画显示
     */
    fun showScaleUI(view: View) {
        view.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
                .setDuration(animatorDuration)
                .start()
        ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
                .setDuration(animatorDuration)
                .start()
    }

    /**
     * 缩放动画隐藏
     */
    fun hiddenScaleUI(view: View) {
        var animatorHidden = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
                .setDuration(animatorDuration)
        animatorHidden.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animatorHidden.start()
        ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
                .setDuration(animatorDuration)
                .start()
    }

    /**
     * 是否在跑道页
     */
    fun isInRunWayUI(): Boolean = isInUI(layout_run_content.layout_run_way.visibility)

    /**
     * 是否在视频分类页
     */
    fun isInVideoCategoryUI(): Boolean = isInUI(layout_run_content.layout_run_video_category_stateview.visibility)

    /**
     * 是否在视频展示页
     */
    fun isInVideosUI(): Boolean = isInUI(layout_run_content.layout_run_video_list_stateview.visibility)

    /**
     * 是否在视频列表页
     */
    fun isInVideoListUI(): Boolean = isInUI(video_list_recyclerView.visibility)

    /**
     * 是否在视频剧集列表页
     */
    fun isInVideoEpisodeListUI(): Boolean = isInUI(video_list_recyclerView_episode.visibility)

    /**
     *  跑步机
     */
    override fun handleTreadData(treadData: SerialPortUtil.TreadData?) {
        super.handleTreadData(treadData)
    }

    fun isInUI(visible: Int): Boolean = visible == View.VISIBLE

}
