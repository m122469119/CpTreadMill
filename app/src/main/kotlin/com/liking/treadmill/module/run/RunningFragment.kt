package com.liking.treadmill.module.run

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.liking.treadmill.R
import com.liking.treadmill.fragment.base.SerialPortFragment
import com.liking.treadmill.treadcontroller.SerialPortUtil
import com.liking.treadmill.widget.IToast
import kotlinx.android.synthetic.main.fragment_running.*
import kotlinx.android.synthetic.main.layout_run_content.view.*
import kotlinx.android.synthetic.main.layout_run_head.view.*
import kotlinx.android.synthetic.main.layout_run_way.view.*


/**
 * Created on 2017/08/16
 * desc:

 * @author: chenlei
 * *
 * @version:1.0
 */

class RunningFragment : SerialPortFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_running, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        layout_run_head.head_imageView.setOnClickListener {
            if(isInRunWayUI()) {
                hiddenRunWayUI()
            } else{
                showRunWayUI()
            }
        }
    }

    override fun handleTreadData(treadData: SerialPortUtil.TreadData?) {
        super.handleTreadData(treadData)
    }

    /**
     * 跑道显示隐藏
     */
    fun showRunWayUI() {
        //倒计时

        //else
        val y = layout_run_content.layout_run_way.translationY
        var runWayAnimatorShow = ObjectAnimator.ofFloat(layout_run_content.layout_run_way,
                "translationY", y, 0.0f)
                .setDuration(800)
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

    fun hiddenRunWayUI() {

        val y = layout_run_content.layout_run_way.translationY
        var runWayAnimatorHidden = ObjectAnimator.ofFloat(layout_run_content.layout_run_way,
                "translationY", y, layout_run_content.layout_run_way.height.toFloat())
                .setDuration(800)
        runWayAnimatorHidden.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                layout_run_content.layout_run_way.visibility = View.INVISIBLE
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        runWayAnimatorHidden .start()


    }

    /**
     * 是否显示的跑道
     */
    fun isInRunWayUI() : Boolean = isInUI(layout_run_content.layout_run_way.visibility)

    /**
     * 是否显示的视频分类
     */
    fun isInVideoCategoryUI() : Boolean = isInUI(layout_run_content.layout_run_video_category.visibility)

    /**
     * 是否显示的视频列表
     */
    fun isInVideoListUI() : Boolean = isInUI(layout_run_content.layout_run_video_list.visibility)

    fun isInUI(visible: Int) : Boolean = visible == View.VISIBLE

}
