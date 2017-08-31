package com.liking.treadmill.module.run

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.RemoteException
import android.support.design.widget.TabLayout
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.aaron.android.codelibrary.utils.DateUtils
import com.aaron.android.codelibrary.utils.LogUtils
import com.aaron.android.codelibrary.utils.StringUtils
import com.aaron.android.framework.base.widget.refresh.StateView
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton
import com.aaron.android.framework.utils.DisplayUtils
import com.aaron.android.framework.utils.ResourceUtils
import com.liking.treadmill.R
import com.liking.treadmill.activity.HomeActivity
import com.liking.treadmill.adapter.IqiyiVideoEpisodesAdapter
import com.liking.treadmill.adapter.IqiyiVideoListAdapter
import com.liking.treadmill.app.ThreadMillConstant
import com.liking.treadmill.db.entity.AdvEntity
import com.liking.treadmill.db.service.AdvService
import com.liking.treadmill.fragment.AwaitActionFragment
import com.liking.treadmill.fragment.GoalSettingFragment
import com.liking.treadmill.fragment.SettingFragment
import com.liking.treadmill.fragment.StartFragment
import com.liking.treadmill.fragment.base.SerialPortFragment
import com.liking.treadmill.message.AdvRefreshMessage
import com.liking.treadmill.message.ToolBarTimeMessage
import com.liking.treadmill.storge.Preference
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent
import com.liking.treadmill.treadcontroller.SerialPortUtil
import com.liking.treadmill.treadcontroller.SerialPortUtil.*
import com.liking.treadmill.utils.RunTimeUtil
import com.liking.treadmill.utils.TypefaceHelper
import com.liking.treadmill.utils.countdownutils.CountDownListener
import com.liking.treadmill.utils.countdownutils.LikingCountDownHelper
import com.liking.treadmill.widget.IToast
import com.liking.treadmill.widget.ScrollSpeedLinearLayoutManger
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.activity_run.*
import kotlinx.android.synthetic.main.fragment_running.*
import kotlinx.android.synthetic.main.layout_category_tablayout.view.*
import kotlinx.android.synthetic.main.layout_pause.*
import kotlinx.android.synthetic.main.layout_prepare.*
import kotlinx.android.synthetic.main.layout_run.*
import kotlinx.android.synthetic.main.layout_run_bottom.*
import kotlinx.android.synthetic.main.layout_run_bottom.view.*
import kotlinx.android.synthetic.main.layout_run_content.*
import kotlinx.android.synthetic.main.layout_run_content.view.*
import kotlinx.android.synthetic.main.layout_run_finish.view.*
import kotlinx.android.synthetic.main.layout_run_head.view.*
import kotlinx.android.synthetic.main.layout_run_video_category.*
import kotlinx.android.synthetic.main.layout_run_video_list.*
import kotlinx.android.synthetic.main.layout_run_way.view.*
import kotlinx.android.synthetic.main.run_info_cell.view.*
import liking.com.iqiyimedia.IqiyiContract
import liking.com.iqiyimedia.http.result.AlbumListResult
import liking.com.iqiyimedia.http.result.CategoryListResult
import liking.com.iqiyimedia.http.result.TopListResult
import liking.com.iqiyimedia.http.result.VideoInfoResult
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.layoutInflater
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created on 2017/08/16
 * desc:

 * @author: chenlei
 * *
 * @version:1.0
 */

class RunningFragment : SerialPortFragment(), IqiyiContract.IqiyiView {

    val RUNING_START_MODE_KEY: String = "MEDIA_STAR" //影音方式启动

    private val RUN_COUNTDOWN_TIME_INTERVAL: Long = 1000 //倒计时间隔
    private val RUN_COUNTDOWN_TIME_PREPARE = 5 * 1000 //跑步倒计时
    private val RUN_COUNTDOWN_TIME_PAUSE = Preference.getStandbyTime() * 1000 //跑步暂停倒计时

    private val UNIT_KM: String = "Km"
    private val UNIT_KMH: String = "Km/h"
    private val UNIT_KCL: String = "Kcl"
    private val UNIT_MIN: String = "min"

    private lateinit var prepareAnimation: Animation
    private lateinit var mCountDownHelper: LikingCountDownHelper

    private var categoryTabIndex: Int = 0
    private var animatorDuration: Long = 800

    private lateinit var mIqiyiPresenter: IqiyiContract.Presenter
    private var videoListAdapter: IqiyiVideoListAdapter? = null
    private var videoEpisodeAdapter: IqiyiVideoEpisodesAdapter? = null
    private var videoTotal: Int = 1
    private var videoSelectedPosition: Int = 0
    private var videoEpisodeSelectedPosition: Int = 0

    private var mKcal = 0f //瞬时热量
    private var mHeartRate = 0 //瞬时心跳
    private var mSpeed = 0 //瞬时速度
    private var mGrade = 0 //瞬时坡度

    private var mTotalHeartRate = 0 //总心率
    private var mHeartChangeCount = 0//心里变化次数(用于平均心率计算)
    private var mTotalKmDistance: Float = 0.0f //已跑总距离
    private var mTotalRunTime: Int = 0//已跑总时间 /min
    private var mTotalKcal: Float = 0.0f//消耗总卡路里

    @Volatile private var isPause = false //跑步机结束
    @Volatile private var isFinish = false //跑步机结束

    /**目标设置start**/
    private var isTargetCmp = false
    private var maxTotalTime: Float = 0f  //系统设置的最长跑步时间 /min
    private var totalTime: Float = 0f  //目标设置的总时间 /min
    private var totalKilometre: Float = 0f//目标设置的总距离
    private var totalKcal: Float = 0f//目标设置的总卡路里
    private var totalTarget = ""
    private var THREADMILL_MODE_SELECT = ThreadMillConstant.THREADMILL_MODE_SELECT_QUICK_START//启动方式
    private var GOAL_TYPE = 0//设定目标时的类型
    private var GOAL_VALUE = 0f
    private var ACHIEVE_TYPE = 0//设定目标时完成情况
    /**目标设置end**/

    private val MESSAGE_RETAIN = 0x10
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MESSAGE_RETAIN) {
                /**
                 * 跑步过程中计数,主要计算总距离和卡路里计数，以及验证目标设置
                 */
                getTreadInstance().runTime = getTreadInstance().runTime + (RUNNING_CALCULATE_INTERVAL_SECOND / 1000)
                val mDistanceIncrement = getTreadInstance().measureDistanceIncrement()
                val mKcalIncrement = getTreadInstance().measureKcalIncrement()
                val distanceCalculate = getTreadInstance().distance + mDistanceIncrement
                val kcalCalculate = getTreadInstance().kcal + mKcalIncrement

                getTreadInstance().distance = distanceCalculate
                getTreadInstance().kcal = kcalCalculate

                val distance = getTreadInstance().distance
                if (distance > 0.0f) {
                    mTotalKmDistance = getKmDistance(distance)
                }
                setupRunContentCell(cell_distance, StringUtils.getDecimalString(mTotalKmDistance, 2).plus(UNIT_KM))
                val runTime = getTreadInstance().runTime
                val time = RunTimeUtil.secToTime(runTime)
                if (runTime > 0) {
                    mTotalRunTime = runTime
                    setupRunContentCell(cell_time_used, time)
                }
                val kcal = getTreadInstance().kcal
                if (kcal > 0.0f) {
                    mTotalKcal = kcal
                }
                checkRunResult(mTotalRunTime.toFloat(), mTotalKmDistance, mTotalKcal)
//                LogUtils.d("dddd", "distance: " + getTreadInstance().distance + " kcal: " + mTotalKcal)
                if (!isPause && !isFinish) {
                    sendEmptyMessageDelayed(MESSAGE_RETAIN, RUNNING_CALCULATE_INTERVAL_SECOND.toLong())
                }

                showAdvertisement(mTotalRunTime, mTotalKmDistance, mTotalKcal)
            }
        }
    }

    private val RUNNING_CALCULATE_INTERVAL_SECOND = 1000

    private var isMediaStart: Boolean = false
    private var isRunning: Boolean = false
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd hh:mm")
    private val mAdvEntities = ArrayList<AdvEntity>() //广告资源
    private var mAdvPosition: Int = 0

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
        initCountDowns()
        initView()
        initData()
    }

    /**
     * 初始化计时器
     */
    fun initCountDowns() {
        mCountDownHelper = LikingCountDownHelper()
    }

    /**
     * 跑步预备
     */
    fun startRunPrepareUI() {
        layout_run_prepare.visibility = View.VISIBLE
        startCountDown(RUN_COUNTDOWN_TIME_PREPARE.toLong(),
                RUN_COUNTDOWN_TIME_INTERVAL,
                object : CountDownListener {
                    override fun onTick(millisUntilFinished: Long) {
                        var value: String
                        val second = ((millisUntilFinished - 1000) / 1000).toInt()
                        value = second.toString()
                        if (second == 0) value = "GO"
                        prepare_count_down_TextView.text = value
                        prepareAnimation.reset()
                        prepareAnimation.fillAfter = true
                        prepareAnimation.repeatCount = 1
                        prepare_count_down_TextView.startAnimation(prepareAnimation)
                    }

                    override fun onFinish() {
                        if (!isFinish) {
                            //跑步动画
                            layout_run_content.layout_run_way.run_way_view.startRun()
                            this@RunningFragment.startRun()
                        }
                        layout_run_prepare.visibility = View.GONE
                    }
                })
    }

    /**
     * 跑步暂停
     */
    fun startRunPauseUI() {
        layout_run_pause.visibility = View.VISIBLE
        isPause = true
        handler.removeMessages(MESSAGE_RETAIN)
        startCountDown(RUN_COUNTDOWN_TIME_PAUSE.toLong(),
                RUN_COUNTDOWN_TIME_INTERVAL,
                object : CountDownListener {
                    override fun onTick(millisUntilFinished: Long) {
                        count_down_TextView.text = (millisUntilFinished / 1000).toString().plus("s")
                    }

                    override fun onFinish() {
                        finishExercise()
                    }
                })
    }

    fun initView() {
        initRunInfoView()
        initIqiyiMediaView()

        //会员以及场馆信息显示
        try {
            layout_run_head.user_name_TextView.text = getTreadInstance().userInfo.mUserName
            HImageLoaderSingleton.getInstance().loadImage(layout_run_head.head_imageView,
                    SerialPortUtil.getTreadInstance().userInfo.mAvatar)
            layout_run_head.text_gym_name.text = Preference.getBindUserGymName()
        } catch (e: Exception) {
        }
    }

    /**
     * 跑步数据显示初始化
     */
    fun initRunInfoView() {
        setupRunInfoCell(layout_run_bottom.cell_speed, "Speed", "0".plus(UNIT_KMH)) //速度
        setTypeFace(layout_run_bottom.cell_speed)
        setupRunInfoCell(layout_run_bottom.cell_distance, "Distance", "0".plus(UNIT_KM))//距离
        setTypeFace(layout_run_bottom.cell_distance)
        setupRunInfoCell(layout_run_bottom.cell_calories, "Calories", "0.0".plus(UNIT_KCL))//卡路里
        setTypeFace(layout_run_bottom.cell_calories)
        setupRunInfoCell(layout_run_bottom.cell_time_used, "Time used", "00:00:00") //使用时间
        setTypeFace(layout_run_bottom.cell_time_used)
        setupRunInfoCell(layout_run_bottom.cell_incline, "Incline", "0") //坡度
        setTypeFace(layout_run_bottom.cell_incline)
        setupRunInfoCell(layout_run_bottom.cell_bmp, "BMP", "0")//心率
        setTypeFace(layout_run_bottom.cell_bmp)
    }

    fun setupRunInfoCell(view: View, title: String, content: String) {
        view.info_title_textView.text = title
        setupRunContentCell(view, content)
    }

    fun setupRunContentCell(view: View, content: String) {
        view?.info_content_textView.text = content
    }

    /**
     * 设置字体
     */
    fun setTypeFace(v: View) {
        TypefaceHelper.setImpactFont(context, v.info_content_textView)
    }

    /**
     * 初始化视频相关view
     */
    fun initIqiyiMediaView() {
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

        if (arguments != null) {
            isMediaStart = arguments.getBoolean(RUNING_START_MODE_KEY, false)
        }
        //预备动画
        prepareAnimation = AnimationUtils.loadAnimation(context, R.anim.count_down_exit)

        initGoalSettingData()
        initAdvertisementData()
    }

    /**
     * 初始化目标设置
     */
    fun initGoalSettingData() {
        maxTotalTime = Preference.getMotionParamMaxRunTime().toFloat()
        val bundle = arguments
        if (bundle != null) {
            totalTime = bundle.getFloat(ThreadMillConstant.GOALSETTING_RUNTIME, totalTime)
            totalKilometre = bundle.getFloat(ThreadMillConstant.GOALSETTING_KILOMETRE, 0f)
            totalKcal = bundle.getFloat(ThreadMillConstant.GOALSETTING_KCAL, 0f)
            if (totalTime > 0) {
                GOAL_TYPE = 1
                GOAL_VALUE = totalTime
                totalTarget = totalTime.toInt().toString().plus(UNIT_MIN)
                setGoalSttingValue(totalTarget)
            } else if (totalKilometre > 0) {
                GOAL_TYPE = 2
                GOAL_VALUE = totalKilometre
                totalTarget = StringUtils.getDecimalString(totalKilometre, 1).plus(UNIT_KM)
                setGoalSttingValue(totalTarget)
            } else if (totalKcal > 0) {
                GOAL_TYPE = 3
                GOAL_VALUE = totalKcal
                totalTarget = totalKcal.toInt().toString().plus(UNIT_KCL)
                setGoalSttingValue(totalTarget)
            }
        }
    }

    fun setGoalSttingValue(value: String) {
        val homeActivity = activity as HomeActivity
        THREADMILL_MODE_SELECT = ThreadMillConstant.THREADMILL_MODE_SELECT_GOAL_SETTING
        if (value.isNotEmpty()) {
            homeActivity.setTitle("目标:".plus(value))
        }
    }

    override fun onResume() {
        super.onResume()
        if (isMediaStart) {
            layout_run_content.layout_run_way.visibility = View.INVISIBLE
            layout_run_bottom.layout_run_bottom_content_layout.visibility = View.INVISIBLE
            layout_run_content.layout_run_way.translationY =
                    layout_run_content.layout_run_way.height +
                            DisplayUtils.dp2px(ResourceUtils.getDimen(R.dimen.run_bottom_height)).toFloat()
            showVideoCategoryUI()
            mediaStartActiveMonitor()
        } else {
            startRunPrepareUI()
        }
        //添加延迟验证
        verificationIsRun()
    }

    /**
     * 媒体按键打开跑步页面  20s计时，无操作退出（选择视频过程）
     */
    fun mediaStartActiveMonitor() {
        LogUtils.e(TAG, "-----mediaStartActiveMonitor---")
        startActiveMonitor(22)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        mIqiyiPresenter.detachView(this)
        cancleCountDown()
        if (isEventTarget) {
            EventBus.getDefault().unregister(this)
        }
        layout_run_content.layout_run_way.run_way_view.stopRun()
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
            }
        })
    }

    /**
     * 选中视频Item进入播放或者选集
     * @direction
     */
    fun selectIqiyiVideoItem(direction: Boolean) {
        if (isInVideoListUI()) {
            val lastPosition = videoSelectedPosition
            if (direction) {
                if (videoSelectedPosition < videoListAdapter!!.dataList.size) {
                    ++videoSelectedPosition
                } else {
                    videoSelectedPosition = videoListAdapter!!.dataList.size - 1
                }
            } else {
                if (videoSelectedPosition > 0) {
                    --videoSelectedPosition
                } else {
                    videoSelectedPosition = 0
                }
            }
            selectedVideo(video_list_recyclerView, lastPosition, videoSelectedPosition)
        } else if (isInVideoEpisodeListUI()) {
            val lastPosition = videoEpisodeSelectedPosition

            if (direction) {
                if (videoEpisodeSelectedPosition < videoEpisodeAdapter!!.dataList.size) {
                    ++videoEpisodeSelectedPosition
                } else {
                    videoEpisodeSelectedPosition = videoEpisodeAdapter!!.dataList.size - 1
                }
            } else {
                if (videoEpisodeSelectedPosition > 0) {
                    --videoEpisodeSelectedPosition
                } else {
                    videoEpisodeSelectedPosition = 0
                }
            }
            selectedVideo(video_list_recyclerView_episode, lastPosition, videoEpisodeSelectedPosition)
        }
    }

    /**
     * 选择video
     */
    fun selectedVideo(recyclerView: RecyclerView, lastPosition: Int, nextPosition: Int) {
        recyclerView.smoothScrollToPosition(nextPosition)
        recyclerView.postDelayed({
            val nextItem = recyclerView.findViewHolderForAdapterPosition(nextPosition)
            val lastItem = recyclerView.findViewHolderForAdapterPosition(lastPosition)
            if (nextItem != null) {
                if (lastItem != null) {
                    lastItem.itemView.isSelected = false
                }
                nextItem.itemView.isSelected = true
            }
        }, 100)
    }

    /**
     * 进入视频播放
     */
    fun getIntoIqiyiVideo(position: Int) {
        if (isInVideoListUI()) {
            if (position < videoListAdapter!!.dataList.size) {
                videoListAdapter!!.dataList[position].let {
                    var videoName = it.albumName
                    IToast.show(videoName)
                    it.tvQipuIds.let {
                        if (it.size == 1) {
                            layout_run_content?.layout_run_video_list_stateview
                                    ?.findViewById(R.id.layout_root)?.visibility = View.VISIBLE
                            mIqiyiPresenter.getVideoInfo(this, it[0])
                        } else if (it.size > 1) { //剧集视频,显示剧集
                            videoEpisodeSelectedPosition = 0
                            video_list_recyclerView?.visibility = View.INVISIBLE
                            video_list_recyclerView_episode?.visibility = View.VISIBLE
                            videoEpisodeAdapter = IqiyiVideoEpisodesAdapter(videoName, context)
                            videoEpisodeAdapter!!.addData(it)
                            video_list_recyclerView_episode?.adapter = videoEpisodeAdapter
                            //默认选中第一项
                            selectedVideo(video_list_recyclerView_episode, 0, 0)
                        } else {
                            IToast.show("视频播放失败!")
                        }
                    }
                }
            }
        } else if (isInVideoEpisodeListUI()) {
            if (position < videoEpisodeAdapter!!.dataList.size) {
                videoEpisodeAdapter!!.dataList[position].let {
                    layout_run_content?.layout_run_video_list_stateview
                            ?.findViewById(R.id.layout_root)
                            ?.visibility = View.VISIBLE
                    mIqiyiPresenter.getVideoInfo(this, it.toString())
                }
            }
        }
    }

    /**
     * 切换频道
     */
    fun categoryNext() {
        //切换频道
        if (categoryTabIndex < category_tab_layout.tabCount - 1) {
            ++categoryTabIndex
        } else {
            categoryTabIndex = category_tab_layout.tabCount - 1
        }
        category_tab_layout.getTabAt(categoryTabIndex)?.select()
    }

    fun categoryLast() {
        //切换频道
        if (categoryTabIndex > 0) {
            --categoryTabIndex
        } else {
            categoryTabIndex = 0
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
        video_list_recyclerView?.scrollToPosition(0) //每次加载视频列表时回到顶部
        videoSelectedPosition = 0
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
                    videoListAdapter = IqiyiVideoListAdapter(context)
                    video_list_recyclerView.adapter = videoListAdapter
                    videoListAdapter!!.addData(result?.data?.filter { 0 == it.isPurchase })
//                    videoListAdapter.notifyDataSetChanged()
                    //默认选中第一项
                    if (it.size > 0) {
                        selectedVideo(video_list_recyclerView, 0, 0)
                    }
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
                    openVideoPlayBrowserUI(it.categoryId.toString(), it.html5PlayUrl)
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
        hiddenTranslationYUI(layout_run_content.layout_run_way, 0.0f, animatorDuration)
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
    fun openVideoPlayBrowserUI(category: String, h5url: String) {
        stopActiveMonitor() //关闭计时
        //hidden列表向下移
        setAllParentsClip(layout_run_content.layout_run_video_list_stateview, false)
        hiddenTranslationYUI(layout_run_content.layout_run_video_list_stateview,
                DisplayUtils.dp2px(ResourceUtils.getDimen(R.dimen.run_bottom_height)).toFloat(),
                animatorDuration)

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
                setAllParentsClip(layout_run_content.layout_run_video_list_stateview, true)

                var videoPlayUI = childFragmentManager
                        .findFragmentById(R.id.layout_run_video_play)
                if (videoPlayUI == null) {
                    videoPlayUI = VideoPlayBrowserFragment.newInstance(category, h5url)
                    childFragmentManager
                            .beginTransaction()
                            .replace(R.id.layout_run_video_play, videoPlayUI)
                            .commitAllowingStateLoss()
                }
                if (!isInVideoPlayUI()) {
                    showTranslationYUI(run_bottom_layout_bg, animatorDuration)
                    layout_run_video_play.visibility = View.VISIBLE

                    var obj = ObjectAnimator.ofFloat(layout_run_video_play, "scaleX", 0f, 1f)
                            .setDuration(animatorDuration)
                    obj.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationEnd(animation: Animator) {
                            (videoPlayUI as VideoPlayBrowserFragment).loadUrl(h5url)
                        }

                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                    obj.start()
                    ObjectAnimator.ofFloat(layout_run_video_play, "scaleY", 0f, 1f)
                            .setDuration(animatorDuration)
                            .start()
                } else {
                    (videoPlayUI as VideoPlayBrowserFragment).loadUrl(h5url)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
        obj.start()
    }

    fun setAllParentsClip(v: View, enabled: Boolean) {
        var v = v
        while (v.parent != null && v.parent is ViewGroup) {
            val viewGroup = v.parent as ViewGroup
            viewGroup.clipChildren = enabled
            viewGroup.clipToPadding = enabled
            v = viewGroup
        }
    }

    /**
     * 关闭播放页面
     */
    fun closeVideoPlayBrowserUI() {
        layout_run_video_play.visibility = View.GONE
        hiddenBottomLayoutBg()//TranslationYUI(run_bottom_layout_bg, animatorDuration)
        //show 播放列表向上移
        showTranslationYUI(layout_run_content.layout_run_video_list_stateview, animatorDuration)
        //show head left
        ObjectAnimator.ofFloat(
                layout_run_head.layout_run_head_left,
                "translationX",
                layout_run_head.layout_run_head_left.translationX,
                0.0f)
                .setDuration(animatorDuration)
                .start()
        //show head right
        ObjectAnimator.ofFloat(
                layout_run_head.layout_run_head_right,
                "translationX",
                layout_run_head.layout_run_head_right.translationX, 0.0f)
                .setDuration(animatorDuration)
    }

    /**
     * Y位移动画显示
     */
    fun hiddenTranslationYUI(view: View, offset: Float, duration: Long) {
        val y = view.translationY
        var translationYAnimatorHidden = ObjectAnimator.ofFloat(view,
                "translationY", y, view.height.toFloat() + offset)
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
     *隐藏底部背景
     */
    fun hiddenBottomLayoutBg() {
        hiddenTranslationYUI(run_bottom_layout_bg, 0.0f, animatorDuration)
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

    fun isInVideoPlayUI(): Boolean = isInUI(layout_run_video_play.visibility)

    /**
     * 是否在准备页
     */
    fun isInPrepareUI(): Boolean = isInUI(layout_run_prepare.visibility)

    /**
     * 是否在暂停页
     */
    fun isInPauseUI(): Boolean = isInUI(layout_run_pause.visibility)

    /**
     * 是否在结束页
     */
    fun isInFinishUI(): Boolean = isInUI(layout_run_finish.visibility)

    /**
     * 开始倒计时
     * @param m
     * *
     * @param i
     */
    private fun startCountDown(m: Long, i: Long, countDownListener: CountDownListener) {
        mCountDownHelper.let {
            mCountDownHelper.reset(m, i)
            mCountDownHelper.setCountDownListener(countDownListener)
            mCountDownHelper.start()
        }
    }

    /**
     * 取消倒计时
     */
    fun cancleCountDown() {
        mCountDownHelper.let {
            mCountDownHelper.cancel()
        }
    }

    fun isInUI(visible: Int): Boolean = visible == View.VISIBLE

    fun startRun() {
        startTreadMill(DEFAULT_SPEED, DEFAULT_GRADE)
    }

    /**
     * 系统设置页面
     */
    private fun showSettingUI() {
        (activity as HomeActivity).launchFragment(SettingFragment())
    }

    /**
     * 刷卡登录
     */
    private fun cardLogin() {
        val homeActivity = activity as HomeActivity
        homeActivity.setTitle("")
        if (homeActivity.mUserLoginPresenter != null) {
            val cardNo = getTreadInstance().cardNo
            if (getTreadInstance().userInfo != null
                    && !StringUtils.isEmpty(cardNo)
                    && cardNo != getTreadInstance().userInfo.mBraceletId) {
                //Logout  //刷卡切换
                if (homeActivity.isLogin) {
                    homeActivity.userLogout(getTreadInstance().userInfo.mBraceletId)
                    homeActivity.isLogin = false
                }
                //Login
                homeActivity.mUserLoginPresenter.userLogin()
            } else {
                homeActivity.launchFragment(StartFragment())
            }
        }
    }

    /**
     * 退出
     */
    private fun exitRunUI() {
        resetTreadmill()
        (activity as HomeActivity).setTitle("")
        (activity as HomeActivity).launchFragment(AwaitActionFragment())
    }

    /**
     * 跳转目标设置
     */
    fun setUpGoalSetting() {
        //目标设置
        if (!isRunning) {
            (activity as HomeActivity).launchFragment(GoalSettingFragment())
        }
    }

    /**
     * 跑步机按键回调
     */
    override fun onTreadKeyDown(keyCode: Int, event: LikingTreadKeyEvent) {
        super.onTreadKeyDown(keyCode, event)
        if (isInPrepareUI()) {

        } else if (isInPauseUI()) { //暂停页面

            if (isRunning) {
                if (keyCode == LikingTreadKeyEvent.KEY_START.toInt()) {
                    startTreadMill(DEFAULT_SPEED, mGrade)
                } else if (keyCode == LikingTreadKeyEvent.KEY_STOP.toInt()) {
                    finishExercise()
                }
            }

        } else if (isInFinishUI()) { //结束页面

            if (keyCode == LikingTreadKeyEvent.KEY_RETURN.toInt()) {
                exitRunUI()
            } else if (keyCode == LikingTreadKeyEvent.KEY_CARD.toInt()) {
                cardLogin()
            } else if (keyCode == LikingTreadKeyEvent.KEY_PROGRAM.toInt()) {
                val userInfo = getTreadInstance().userInfo
                if (userInfo != null && userInfo.isManager) {
                    showSettingUI()
                }
            }

        } else if (isInRunWayUI()) { //正在跑步界面

            setUpRun(keyCode, true)

        } else if (isInVideoCategoryUI()) { //视频分类页

            if (keyCode == LikingTreadKeyEvent.KEY_LAST.toInt()) {
                if (isMediaStart) { //刷新计时
                    mediaStartActiveMonitor()
                }
                categoryLast()
            } else if (keyCode == LikingTreadKeyEvent.KEY_NEXT.toInt()) {
                if (isMediaStart) { //刷新计时
                    mediaStartActiveMonitor()
                }
                categoryNext()
            } else if (keyCode == LikingTreadKeyEvent.KEY_PLAY_PAUSE_MEDIA.toInt()) {
                if (isMediaStart) { //刷新计时
                    mediaStartActiveMonitor()
                }
                hiddenVideoCategoryUI()
                loadVideoList()
            } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN.toInt()) {

                if (isRunning) {
                    hiddenVideoCategoryUI()
                    showRunWayUI()
                } else {
                    exitRunUI()
                }

            } else if (keyCode == LikingTreadKeyEvent.KEY_START.toInt()) {

                if (!isRunning) {
                    hiddenVideoCategoryUI()
                    showRunWayUI()
                    layout_run_bottom.layout_run_bottom_content_layout.visibility = View.VISIBLE
                    startRunPrepareUI()
                }

            } else if (keyCode == LikingTreadKeyEvent.KEY_SET.toInt()) {
                setUpGoalSetting()
            }

        } else if (isInVideosUI()) { //视频页（视频列表、视频剧集列表）

            if (keyCode == LikingTreadKeyEvent.KEY_START.toInt()) {
                if (!isRunning) {
                    hiddenVideoListUI()
                    showRunWayUI()
                    layout_run_bottom.layout_run_bottom_content_layout.visibility = View.VISIBLE
                    startRunPrepareUI()
                }
                return
            } else if (keyCode == LikingTreadKeyEvent.KEY_SET.toInt()) {
                setUpGoalSetting()
                return
            }

            if (isInVideoListUI()) {
                //如果加载中 return
                if (layout_run_content.layout_run_video_list_stateview.currState == StateView.State.LOADING) return

                if (keyCode == LikingTreadKeyEvent.KEY_LAST.toInt()) {
                    if (isMediaStart) { //刷新计时
                        mediaStartActiveMonitor()
                    }
                    selectIqiyiVideoItem(false)
                } else if (keyCode == LikingTreadKeyEvent.KEY_NEXT.toInt()) {
                    if (isMediaStart) { //刷新计时
                        mediaStartActiveMonitor()
                    }
                    selectIqiyiVideoItem(true)
                } else if (keyCode == LikingTreadKeyEvent.KEY_PLAY_PAUSE_MEDIA.toInt()) {
                    if (isMediaStart) { //刷新计时
                        mediaStartActiveMonitor()
                    }
                    getIntoIqiyiVideo(videoSelectedPosition)
                } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN.toInt()) {
                    if (isMediaStart) { //刷新计时
                        mediaStartActiveMonitor()
                    }
                    hiddenVideoListUI()
                    showVideoCategoryUI()
                } else {
                    setUpRun(keyCode, false)
                }
            } else if (isInVideoEpisodeListUI()) {
                if (keyCode == LikingTreadKeyEvent.KEY_LAST.toInt()) {
                    if (isMediaStart) { //刷新计时
                        mediaStartActiveMonitor()
                    }
                    selectIqiyiVideoItem(false)
                } else if (keyCode == LikingTreadKeyEvent.KEY_NEXT.toInt()) {
                    if (isMediaStart) { //刷新计时
                        mediaStartActiveMonitor()
                    }
                    selectIqiyiVideoItem(true)
                } else if (keyCode == LikingTreadKeyEvent.KEY_PLAY_PAUSE_MEDIA.toInt()) {
                    if (isMediaStart) { //刷新计时
                        mediaStartActiveMonitor()
                    }
                    getIntoIqiyiVideo(videoEpisodeSelectedPosition)
                } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN.toInt()) {
                    if (isMediaStart) { //刷新计时
                        mediaStartActiveMonitor()
                    }
                    video_list_recyclerView_episode.visibility = View.GONE
                    video_list_recyclerView.visibility = View.VISIBLE
                } else {
                    setUpRun(keyCode, false)
                }
            }

        } else if (isInVideoPlayUI()) { //视频播放

            if (keyCode == LikingTreadKeyEvent.KEY_START.toInt()) {
                if (!isRunning) {
                    layout_run_bottom.layout_run_bottom_content_layout.visibility = View.VISIBLE
                    startRunPrepareUI()
                }
                return
            }

            if (keyCode == LikingTreadKeyEvent.KEY_LAST.toInt()) {
                videoPlayBrowserUIAction {
                    selectIqiyiVideoItem(false)
                    if (isInVideoListUI()) {
                        getIntoIqiyiVideo(videoSelectedPosition)
                    } else if (isInVideoEpisodeListUI()) {
                        getIntoIqiyiVideo(videoEpisodeSelectedPosition)
                    }
                }
            } else if (keyCode == LikingTreadKeyEvent.KEY_NEXT.toInt()) {
                videoPlayBrowserUIAction {
                    selectIqiyiVideoItem(true)
                    if (isInVideoListUI()) {
                        getIntoIqiyiVideo(videoSelectedPosition)
                    } else if (isInVideoEpisodeListUI()) {
                        getIntoIqiyiVideo(videoEpisodeSelectedPosition)
                    }
                }
            } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN.toInt()) {
                videoPlayBrowserUIAction {
                    childFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
                }
                closeVideoPlayBrowserUI()
            } else if (keyCode == LikingTreadKeyEvent.KEY_STOP_MEDIA.toInt()) {
                videoPlayBrowserUIAction {
                    it.pausePlayVideo()
                }
            } else if (keyCode == LikingTreadKeyEvent.KEY_PLAY_PAUSE_MEDIA.toInt()) {
                videoPlayBrowserUIAction {
                    it.playVideo()
                }
            } else if (keyCode == LikingTreadKeyEvent.KEY_SET.toInt()) {
                //目标设置
                setUpGoalSetting()
            } else {
                setUpRun(keyCode, false)
            }
        }
    }

    /**
     * 跑步机设置速度坡度，暂停，停止
     */
    fun setUpRun(keyCode: Int, isInRunWayUI: Boolean) {

        if (keyCode == LikingTreadKeyEvent.KEY_PAUSE.toInt()) {
            pauseTreadmill()
        } else if (keyCode == LikingTreadKeyEvent.KEY_STOP.toInt()) {
            finishExercise()
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_PLUS.toInt()) {
            setSpeed(getTreadInstance().currentSpeed + 1)
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_REDUCE.toInt()) {
            setSpeed(getTreadInstance().currentSpeed - 1)
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_PLUS.toInt()) {
            setGrade(getTreadInstance().currentGrade + 1)
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_REDUCE.toInt()) {
            setGrade(getTreadInstance().currentGrade - 1)
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_3.toInt()) {
            setSpeed(30)
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_6.toInt()) {
            setSpeed(60)
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_9.toInt()) {
            setSpeed(90)
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_12.toInt()) {
            setSpeed(120)
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_15.toInt()) {
            setSpeed(150)
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_3.toInt()) {
            setGrade(3)
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_6.toInt()) {
            setGrade(6)
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_9.toInt()) {
            setGrade(9)
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_12.toInt()) {
            setGrade(12)
        } else if (keyCode == LikingTreadKeyEvent.KEY_GRADE_15.toInt()) {
            setGrade(15)
        } else if (keyCode == LikingTreadKeyEvent.KEY_HAND_SHANK_SPEED_PLUS.toInt()) {
            setSpeed(getTreadInstance().currentSpeed + 10)
        } else if (keyCode == LikingTreadKeyEvent.KEY_HAND_SHANK_SPEED_REDUCE.toInt()) {
            setSpeed(getTreadInstance().currentSpeed - 10)
        } else if (keyCode == LikingTreadKeyEvent.KEY_HAND_SHANK_GRADE_PLUS.toInt()) {
            setGrade(getTreadInstance().currentGrade + 1)
        } else if (keyCode == LikingTreadKeyEvent.KEY_HAND_SHANK_GRADE_REDUCE.toInt()) {
            setGrade(getTreadInstance().currentGrade - 1)
        } else if (keyCode == LikingTreadKeyEvent.KEY_MULTIMEDIA.toInt() && isInRunWayUI) {
            hiddenRunWayUI()
            showVideoCategoryUI()
        }
    }

    /**
     * 视频播放页
     */
    fun videoPlayBrowserUIAction(f: (ui: VideoPlayBrowserFragment) -> Unit) {
        childFragmentManager.findFragmentById(R.id.layout_run_video_play)
                .let {
                    if (it != null) {
                        f.invoke(it as VideoPlayBrowserFragment)
                    }
                }
    }

    /**
     * 设置跑步机速度
     * @param speed
     */
    private fun setSpeed(speed: Int) {
        if (speed in 1..150) {
            mSpeed = speed
            setSpeedInRunning(mSpeed)
            showToast("速度", StringUtils.getDecimalString(mSpeed / 10.0f, 1))
        }
    }

    /**
     * 设置跑步机速度
     * @param grade
     */
    private fun setGrade(grade: Int) {
        if (grade in 1..15) {
            mGrade = grade
            setGradeInRunning(mGrade)
            showToast("坡度", mGrade.toString())
        }
    }

    /**
     * 开始跑步
     */
    fun startTreadMill(speed: Int, grade: Int) {
        stopActiveMonitor()//关闭计时
        SerialPortUtil.startTreadMill(speed, grade)
        layout_run_pause.visibility = View.GONE
        layout_run_content.visibility = View.VISIBLE
        layout_run_finish.visibility = View.GONE
        isRunning = true
        isFinish = false
        isPause = false
        handler.sendEmptyMessageDelayed(MESSAGE_RETAIN, RUNNING_CALCULATE_INTERVAL_SECOND.toLong())
    }

    /**
     * 暂停跑步机
     */
    private fun pauseTreadmill() {
        startRunPauseUI()
        isRunning = false
        stopTreadMill()//暂停命令
        layout_run_pause.visibility = View.VISIBLE
        layout_run_content.layout_run_way.run_way_view.stopRun()
    }

    /**
     * 重置跑步机设置
     */
    fun resetTreadmill() {
        getTreadInstance().reset()//清空数据
    }

    /**
     *  跑步机相关
     */
    override fun handleTreadData(treadData: SerialPortUtil.TreadData?) {
        super.handleTreadData(treadData)
        if (isFinish) return
        if (treadData?.safeLock == SaveLock.SAVE_LOCK_OPEN) {
            finishExercise()
        } else if (treadData?.safeLock == SaveLock.SAVE_LOCK_CLOSE) {
//            LogUtils.e(TAG, "SAVE_LOCK_CLOSE")
        }
        //坡度设置
        val grade = treadData?.currentGrade
        if (mGrade != grade && grade != null && grade != 0) {
            mGrade = grade
            setupRunContentCell(layout_run_bottom.cell_incline, mGrade.toString())
        }
        //心率
        val heartRate = treadData?.heartRate
        if (mHeartRate != heartRate && heartRate != null) {
            mHeartRate = heartRate
            if (mHeartRate != 0) {
                mTotalHeartRate += mHeartRate
                mHeartChangeCount++
            }
            setupRunContentCell(layout_run_bottom.cell_bmp, heartRate.toString())
        }
        //速度
        val speed = treadData?.currentSpeed
        if (mSpeed != speed && speed != null) {
            mSpeed = speed
            setupRunContentCell(layout_run_bottom.cell_speed, StringUtils.getDecimalString((mSpeed / 10.0).toFloat(), 2))
        }
        //卡路里
        val kcal = treadData?.kcal
        if (mKcal != kcal && kcal != null) {
            mKcal = kcal
            setupRunContentCell(layout_run_bottom.cell_calories, StringUtils.getDecimalString(mKcal, 1))
        }
        //设置跑道速度
        setSpeedBack(mSpeed)
    }

    /**
     * 设置跑道速度
     */
    fun setSpeedBack(speed: Int) {
        val speed = speed.toFloat() / 10.0f
        if (speed > 0f && speed <= 5f) {//慢走
//            layout_run_way.run_way_view.
        } else if (speed > 5f && speed <= 6.5f) {//快走

        } else if (speed > 6.5f && speed <= 9f) {//慢跑

        } else if (speed > 9f) {//疾跑
        }
    }

    /**
     * 结束跑步
     */
    fun finishExercise() {
        isRunning = false
        isFinish = true
        handler.removeCallbacksAndMessages(null)
        videoPlayBrowserUIAction {
            childFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
        }
        hiddenBottomLayoutBg()
        layout_run_content.visibility = View.GONE
        layout_run_video_play.visibility = View.GONE
        layout_run_finish.visibility = View.VISIBLE
        statisticsRunData()
        //清空数据
        getTreadInstance().reset()
        stopTreadMill()
        //10s后无操作退出
        startActiveMonitor(12)
    }

    /***
     * 跑步结束统计 距离 、用时、平均坡度、平均速度、消耗热量，平均心率
     */
    fun statisticsRunData() {
        val treadData = getTreadInstance()
        //用时
        val runTime = treadData.runTime
        if (runTime != 0) {
            val userTime = RunTimeUtil.secToTime(runTime)
            setupRunContentCell(cell_time_used, userTime)
        }
        val totalDistance = treadData.distance//米
        val totalDistanceKm = getKmDistance(totalDistance)
        //总距离
        if (totalDistanceKm > 0.0f) {
            setupRunContentCell(cell_distance, StringUtils.getDecimalString(totalDistanceKm, 2).plus(UNIT_KM))
        }
        //平均速度
        if (totalDistance > 0.0f) {
            val h = (treadData.runTime / 3600.0).toFloat()
            val avergageSpeed = totalDistanceKm / h
            setupRunInfoCell(layout_run_bottom.cell_speed,
                    "Average Speed",
                    StringUtils.getDecimalString(avergageSpeed, 2).plus(UNIT_KMH)) //平均速度
        }
        //消耗热量
        val kcal = treadData.kcal
        if (kcal > 0.0f) {
            setupRunContentCell(layout_run_bottom.cell_calories, StringUtils.getDecimalString(kcal, 2))
        }
        //平均心率
        var mAverageHeartRate = 0
        if (mTotalHeartRate != 0) {
            mAverageHeartRate = mTotalHeartRate / mHeartChangeCount
        }
        setupRunInfoCell(layout_run_bottom.cell_bmp,
                "Average BPM",
                mAverageHeartRate.toString()) //平均心率

        //安全锁打开时,会清除所有数据
        if (runTime == 0 || totalDistanceKm == 0.0f || kcal == 0.0f) {
            showRunResult(mTotalRunTime.toFloat(), mTotalKmDistance, mTotalKcal)
        } else {
            showRunResult(treadData.runTime.toFloat(), totalDistanceKm, treadData.kcal)
        }
        val userInfo = treadData.userInfo
        if (userInfo != null && !userInfo.isVisitor) {//非访客模式
            try {
                //上传锻炼数据
                (activity as HomeActivity)
                        .iBackService
                        .reportExerciseData(THREADMILL_MODE_SELECT, GOAL_TYPE, GOAL_VALUE, ACHIEVE_TYPE)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    fun getKmDistance(distance: Float): Float {
        return (distance / 1000.0).toFloat()
    }

    /**
     * 验证跑步结果
     * @param time
     * @param distanceKm
     * @param kcal
     */
    fun showRunResult(time: Float, distanceKm: Float, kcal: Float) {
        if (totalTime > 0) {
            showfinishedView(time / (totalTime * 60))
        } else if (totalKilometre > 0) {
            showfinishedView(distanceKm / totalKilometre)
        } else if (totalKcal > 0) {
            showfinishedView(kcal / totalKcal)
        } else {
            showfinishedView(-1f)
        }
    }

    /**
     * 显示未完成界面(目标设置情况下进入)
     * @param percentage
     */
    fun showfinishedView(percentage: Float) {
        if (percentage == -1f) {
            layout_run_finish.this_run_finish_prompt.text = ResourceUtils.getString(R.string.this_run_finish)
        } else if (percentage < 1) {
            val percent = Math.round(percentage * 100)
            layout_run_finish.run_finish_ImageView.visibility = View.GONE
            layout_run_finish.run_progress_layout.visibility = View.VISIBLE
            layout_run_finish.colorfulring_progress.percent = percent.toFloat()
            layout_run_finish.run_progress.text = percent.toString().plus("%")
            layout_run_finish.this_run_finish_prompt.text = ResourceUtils.getString(R.string.run_result_unfinished_txt_hint)
        } else {
            ACHIEVE_TYPE = 1
            layout_run_finish.run_progress_layout.visibility = View.GONE
            layout_run_finish.run_finish_ImageView.visibility = View.VISIBLE
            layout_run_finish.this_run_finish_prompt.text = ResourceUtils.getString(R.string.this_run_attainment_target)
        }
    }

    fun showToast(n: String, value: String) {
        IToast.show(String.format(ResourceUtils.getString(R.string.run_thread_set_txt), n, value))
    }

    /**
     * 目标设置：验证跑步结果
     */
    fun checkRunResult(time: Float, distance: Float, kcal: Float) {

        if (time >= maxTotalTime * 60) { //超过跑步的最长时间
            finishExercise()
        }

        if (totalTime > 0 && time >= totalTime * 60
                || totalKilometre > 0 && distance >= totalKilometre
                || totalKcal > 0 && kcal >= totalKcal) {
            if (!isTargetCmp) {
                isTargetCmp = true
                IToast.show(ResourceUtils.getString(R.string.this_run_attainment_target))
            }
        }
        //在锻炼期即将达成目标时，当前画面上方出现toast消息进行提示
        if (totalTime * 60 > 300 && totalTime * 60 - time == 300f) {
            IToast.show(String.format(ResourceUtils.getString(R.string.run_attainment_target_upcoming), "继续5分钟"))
        } else if (totalKilometre > 0.5 && totalKilometre - distance < 0.51 && totalKilometre - distance > 0.49) {
            IToast.show(String.format(ResourceUtils.getString(R.string.run_attainment_target_upcoming), "跑步0.5公里"))
        } else if (totalKcal > 50 && (totalKcal - kcal).toInt() == 50) {
            IToast.show(String.format(ResourceUtils.getString(R.string.run_attainment_target_upcoming), "消耗50卡路里"))
        }
        //如果为访客模式 超过五分钟结束跑步
        val userInfo = getTreadInstance().userInfo
        if (userInfo != null && userInfo.isVisitor) {
            if (time >= 5 * 60) {
                finishExercise()
            }
        }
    }

    override fun isEventTarget(): Boolean {
        return true
    }

    /**
     * 时间刷新
     * @param message
     */
    fun onEvent(message: ToolBarTimeMessage) {
        layout_run_head?.text_time?.text = getTime()
    }

    fun getTime(): String {
        return dateFormat.format(Date())
    }

    /**
     * 超时验证（避免跑步机沦为电视机）
     * 如果用户在刷手环后10分钟内（超时机制仍然存在）未进行跑步操作
     * 8分钟时弹框提示：您目前位于跑步机，快快跑起来吧~（5s自动消失）
     * 9分钟时弹框提示：您必须马上跑起来，排队的小伙伴正在等待，1分钟后若您未进入跑步状态，本次使用将结束（5s自动消失）
     * 10分钟时弹框提示：本次使用结束，谢谢使用（5s自动消失）后跳转至待机页面
     */
    fun verificationIsRun() {
        handler.postDelayed({
            verificationTost(ResourceUtils.getString(R.string.run_timeout_1), false)
        }, 8 * 60 * 1000)
        handler.postDelayed({
            verificationTost(ResourceUtils.getString(R.string.run_timeout_2), false)
        }, 9 * 60 * 1000)
        handler.postDelayed({
            verificationTost(ResourceUtils.getString(R.string.run_timeout_3), true)
        }, 10 * 60 * 1000)
    }

    fun verificationTost(hint: String, isExit: Boolean) {
        if (!isRunning && !isPause && !isFinish) { //没有跑步过程并且不在暂停状态,不在结束页面
            IToast.showLong(hint)
            if (isExit) {
                exitRunUI()
            }
        }
    }

    /**
     * 加载对应广告
     */
    fun initAdvertisementData() {
        var yyyyMMdd = DateUtils.formatDate("yyyyMMdd", Date())
        yyyyMMdd = "20160212"

        when (THREADMILL_MODE_SELECT) {
            ThreadMillConstant.THREADMILL_MODE_SELECT_QUICK_START -> {

                AdvService.getInstance()
                        .findAdvByTypeAndEndTime(AdvEntity.TYPE_QUICK_START, AdvEntity.NOT_DEFAULT, yyyyMMdd) {
                            advEntities ->
                            if (advEntities != null && advEntities.size > 0) {
                                mAdvEntities.addAll(advEntities)
                            } else {
                                //设置为默认的图片
                                AdvService.getInstance()
                                        .findAdvByType(AdvEntity.TYPE_QUICK_START, AdvEntity.DEFAULT) {
                                            advEntities ->
                                            if (advEntities != null && advEntities.size > 0) {
                                                mAdvEntities.addAll(advEntities)
                                            }
                                        }
                            }
                        }
            }

            ThreadMillConstant.THREADMILL_MODE_SELECT_GOAL_SETTING -> {

                AdvService.getInstance()
                        .findAdvByTypeAndEndTime(AdvEntity.TYPE_SET_MODE, AdvEntity.NOT_DEFAULT, yyyyMMdd) {
                            advEntities ->
                            if (advEntities != null && advEntities.size > 0) {
                                mAdvEntities.addAll(advEntities)

                            } else {
                                //设置为默认的图片
                                AdvService.getInstance().findAdvByType(AdvEntity.TYPE_SET_MODE, AdvEntity.DEFAULT) {
                                    advEntities ->
                                    if (advEntities != null && advEntities.size > 0) {
                                        mAdvEntities.addAll(advEntities)
                                    }
                                }
                            }
                        }
            }
        }
    }

    /**
     * 显示广告
     * QUICKSTART模式下以时间维度触发
     * 切换广告
     * 广告最多4张最1张
     * 广告存在时间20s
     * SET模式下以距离维度触发
     * 按照用户的目标进行切分，以每四分之为一个触发点推送广告
     * 广告最多4张最1张
     * 广告存在时间20s
     */
    fun showAdvertisement(time: Int, distanceKm: Float, kcal: Float) {

        LogUtils.e(TAG, "已跑时间：$time; 距离 ：$distanceKm; 卡路里：$kcal")

        when (THREADMILL_MODE_SELECT) {
            ThreadMillConstant.THREADMILL_MODE_SELECT_QUICK_START -> {
                val t = time / 60 //分钟
                if (t > 0 && time % 60 == 0) {
                    showAdv(t % 5 == 0)//每5分钟切换一次广告
                }
            }
            ThreadMillConstant.THREADMILL_MODE_SELECT_GOAL_SETTING -> {
                if(totalTime > 0) {
                    val t = time / 60
                    if (t > 0 && time % 60 == 0) {
                        val section = totalTime.toInt() / 4
                        showAdv(t % section == 0)
                    }
                } else if (totalKilometre > 0) {
                    val distance = (distanceKm * 1000).toInt()
                    val section = totalKilometre.toInt() / 4
                    if(distance > 0) {
                        showAdv(distance % section == 0)
                    }
                } else if (totalKcal > 0) {
                    val section = totalKcal.toInt() / 4
                    if(kcal.toInt() > 0) {
                        showAdv(kcal.toInt() % section == 0)
                    }
                }
            }
        }
    }

    fun showAdv(show: Boolean) {
        LogUtils.e(TAG, "是否显示广告：" + show)
        if (show) {
            if (mAdvEntities.size > 0) {
                layout_run_way.imageview_adv.visibility = View.VISIBLE
                HImageLoaderSingleton.getInstance()
                        .loadImage(layout_run_way.imageview_adv,
                                mAdvEntities[mAdvPosition].url)
                ++mAdvPosition
                if (mAdvPosition >= mAdvEntities.size) {
                    mAdvPosition = 0
                }
                //广告存在时间20s
                handler.postDelayed({
                    layout_run_way.imageview_adv.visibility = View.INVISIBLE
                }, 20 * 1000)
            }
        }
    }

    fun onEvent(message: AdvRefreshMessage) {
        initAdvertisementData()
    }

}
