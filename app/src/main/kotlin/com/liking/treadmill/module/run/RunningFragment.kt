package com.liking.treadmill.module.run

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaron.android.framework.base.widget.refresh.StateView
import com.liking.treadmill.R
import com.liking.treadmill.R.id.layout_run_video_category_stateview
import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.fragment.base.SerialPortFragment
import com.liking.treadmill.treadcontroller.SerialPortUtil
import com.liking.treadmill.widget.IToast
import kotlinx.android.synthetic.main.fragment_running.*
import kotlinx.android.synthetic.main.layout_category_tablayout.view.*
import kotlinx.android.synthetic.main.layout_run_content.view.*
import kotlinx.android.synthetic.main.layout_run_head.view.*
import kotlinx.android.synthetic.main.layout_run_video_category.*
import kotlinx.android.synthetic.main.layout_run_video_category.view.*
import kotlinx.android.synthetic.main.layout_run_way.view.*
import liking.com.iqiyimedia.IqiyiContract
import liking.com.iqiyimedia.http.result.AlbumListResult
import liking.com.iqiyimedia.http.result.CategoryListResult
import liking.com.iqiyimedia.http.result.TopListResult
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

    var categoryTabIndex:Int = 0
    var animatorDuration:Long = 800

    lateinit var mIqiyiPresenter: IqiyiContract.Presenter

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
        initCategoryTabView()
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
        category_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.tag) {

                }
                IToast.show("s:" + tag)
                if(categoryTabIndex >= category_tab_layout.tabCount) {
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
            category_tab_layout.getTabAt(++categoryTabIndex)?.select()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIqiyiPresenter.detachView(this)
    }

    /**
     *
     */
    override fun handleTreadData(treadData: SerialPortUtil.TreadData?) {
        super.handleTreadData(treadData)
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
    fun getTabView(img:Int, name: String): View {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 加载失败
     */
    override fun showFailView(message: String?, failType:Int) {
        when(failType){
//            IqiyiContract.IQIYI_RESPONSE_FAIL_CATEGORYLIST ->
//                layout_run_content?.layout_run_video_category_stateview?.setState(StateView.State.FAILED)
            IqiyiContract.IQIYI_RESPONSE_FAIL_TOPLIST ->
                layout_run_content?.layout_run_video_list_stateview?.setState(StateView.State.FAILED)
        }
    }

    /**
     * 跑道隐藏
     */
    fun showRunWayUI() {
        //倒计时
        //else
        val y = layout_run_content.layout_run_way.translationY
        var runWayAnimatorShow = ObjectAnimator.ofFloat(layout_run_content.layout_run_way,
                "translationY", y, 0.0f)
                .setDuration(animatorDuration)
        runWayAnimatorShow.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                layout_run_content.layout_run_way.visibility = View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        runWayAnimatorShow.start()
    }

    /**
     * 跑道隐藏
     */
    fun hiddenRunWayUI() {
        val y = layout_run_content.layout_run_way.translationY
        var runWayAnimatorHidden = ObjectAnimator.ofFloat(layout_run_content.layout_run_way,
                "translationY", y, layout_run_content.layout_run_way.height.toFloat())
                .setDuration(animatorDuration)
        runWayAnimatorHidden.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                layout_run_content.layout_run_way.visibility = View.INVISIBLE
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        runWayAnimatorHidden.start()
    }

    /**
     * 显示频道分类
     */
    fun showVideoCategoryUI() {
        layout_run_content.layout_run_video_category_stateview.visibility = View.VISIBLE
//        if(layout_run_content.layout_run_video_category_stateview.currState == StateView.State.FAILED) {
//            loadCategoryList()
//        }
        ObjectAnimator.ofFloat(category_tab_layout, "scaleX", 0f, 1f)
                .setDuration(animatorDuration)
                .start()
        ObjectAnimator.ofFloat(category_tab_layout, "scaleY", 0f, 1f)
                .setDuration(animatorDuration)
                .start()
    }

    /**
     * 隐藏频道分类
     */
    fun hiddenVideoCategoryUI() {
        var  categoryTabAnimatorHidden = ObjectAnimator.ofFloat(category_tab_layout, "scaleX", 1f, 0f)
                .setDuration(animatorDuration)
        categoryTabAnimatorHidden.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                layout_run_content.layout_run_video_category_stateview.visibility = View.GONE
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        categoryTabAnimatorHidden.start()
        ObjectAnimator.ofFloat(category_tab_layout, "scaleY", 1f, 0f)
                .setDuration(animatorDuration)
                .start()
    }

    /**
     * 是否显示的跑道
     */
    fun isInRunWayUI(): Boolean = isInUI(layout_run_content.layout_run_way.visibility)

//    /**
//     * 是否显示的视频分类
//     */
//    fun isInVideoCategoryUI() : Boolean = isInUI(layout_run_content.layout_run_video_category.visibility)

//    /**
//     * 是否显示的视频列表
//     */
//    fun isInVideoListUI() : Boolean = isInUI(layout_run_content.layout_run_video_list.visibility)

    fun isInUI(visible: Int): Boolean = visible == View.VISIBLE

}
