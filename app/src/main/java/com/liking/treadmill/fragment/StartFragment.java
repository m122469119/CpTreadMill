package com.liking.treadmill.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.adapter.BannerPagerAdapter;
import com.liking.treadmill.db.entity.AdvEntity;
import com.liking.treadmill.db.service.AdvService;
import com.liking.treadmill.fragment.base.SerialPortFragment;
import com.liking.treadmill.message.AdvRefreshMessage;
import com.liking.treadmill.module.run.RunningFragment;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.liking.treadmill.widget.autoviewpager.InfiniteViewPager;
import com.liking.treadmill.widget.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 16/12/27.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class StartFragment extends SerialPortFragment {

    public static final long AUTO_SCROLL = 8 * 1000;

    @BindView(R.id.user_name_TextView)
    TextView mUserNameTextView;
    @BindView(R.id.head_imageView)
    HImageView mHeadImageView;


    @BindView(R.id.text_quickstart)
    TextView mQuickstartTextView;
    @BindView(R.id.text_set)
    TextView mSetTextView;
    @BindView(R.id.image_quick)
    ImageView mQuickImageView;
    @BindView(R.id.image_set)
    ImageView mSetImageView;


    @BindView(R.id.viewpager_start)
    InfiniteViewPager mViewpager;
    @BindView(R.id.indicator_start)
    IconPageIndicator mIndicator;
    BannerPagerAdapter mBannerPagerAdapter;

    @BindView(R.id.text_please)
    TextView mPleaseView;

    AnimatorSet mQuickAnimator, mSetAnimator;

    List<String> mBannerList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_start, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        initAnimation();
        return rootView;
    }

    private void initView() {
        mBannerPagerAdapter= new BannerPagerAdapter(getActivity());
        mViewpager.setAdapter(mBannerPagerAdapter);
        mIndicator.setViewPager(mViewpager);
        mViewpager.startAutoScroll();
        mPleaseView.setText(Html.fromHtml("<font color=\"#AAACAF\">请使用下方面板上的相应按钮</font><br></br><font color=\"#34c86c\">快速开始</font><font color=\"#AAACAF\">或</font><font color=\"#34c86c\">设定目标</font>"));
        initBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SerialPortUtil.getTreadInstance().getUserInfo() != null) {
            ////0女 1男
            if(SerialPortUtil.getTreadInstance().getUserInfo().mGender == 0) {
                mUserNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_girl, 0);
            } else {
                mUserNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_boy, 0);
            }
            mUserNameTextView.setText(SerialPortUtil.getTreadInstance().getUserInfo().mUserName);
            HImageLoaderSingleton.getInstance().loadImage(mHeadImageView, SerialPortUtil.getTreadInstance().getUserInfo().mAvatar);
        }
        startActiveMonitor();
        mQuickAnimator.start();
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            SerialPortUtil.getTreadInstance().reset();//清空数据
            ((HomeActivity) getActivity()).setTitle("");
            ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_START) {
            if(StringUtils.isEmpty(Preference.getBindUserGymId())) {//未绑定场馆
                startActiveMonitor();
                IToast.show("场馆未绑定，请联系管理员!");
                return;
            }
            ((HomeActivity) getActivity()).launchFragment(new RunningFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_CARD) {
            startActiveMonitor();
            cardLogin();
        } else if (keyCode == LikingTreadKeyEvent.KEY_PROGRAM) {
            startActiveMonitor();
            SerialPortUtil.TreadData.UserInfo userInfo = SerialPortUtil.getTreadInstance().getUserInfo();
            if(userInfo != null && userInfo.isManager) {
                showSettingUI();
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SET) {//参数设置
            if(StringUtils.isEmpty(Preference.getBindUserGymId())) {//未绑定场馆
                startActiveMonitor();
                IToast.show("场馆未绑定，请联系管理员!");
                return;
            }
            ((HomeActivity) getActivity()).launchFragment(new GoalSettingFragment());
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
    }

    private void showSettingUI() {
        ((HomeActivity) getActivity()).launchFullFragment(new SettingFragment());
    }

    /**
     * 刷卡登录
     */
    private void cardLogin() {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.setTitle("");

        final String cardNo = SerialPortUtil.getTreadInstance().getCardNo();

        if(SerialPortUtil.getTreadInstance().getUserInfo() != null
                && !StringUtils.isEmpty(cardNo)
                && !cardNo.equals(SerialPortUtil.getTreadInstance().getUserInfo().mBraceletId)) {

            //Logout
            if(homeActivity.isLogin) {
                homeActivity.userLogout(SerialPortUtil.getTreadInstance().getUserInfo().mBraceletId);
                homeActivity.isLogin = false;
            }

            //Login
            if (homeActivity.mUserLoginPresenter != null) {
                homeActivity.mUserLoginPresenter.userLogin();
            }
        }
    }


    /**
     * 动画
     */
    private void initAnimation() {
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mQuickstartTextView, "scaleX", 1F, 1.5F, 1F);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mQuickstartTextView, "scaleY", 1F, 1.5F, 1F);
        ObjectAnimator imageX = ObjectAnimator.ofFloat(mQuickImageView, "scaleX", 1F, 1.5F, 1F);
        ObjectAnimator imageY = ObjectAnimator.ofFloat(mQuickImageView, "scaleY", 1F, 1.5F, 1F);
        mQuickAnimator = new AnimatorSet();
        mQuickAnimator.setDuration(2000);
        mQuickAnimator.playTogether(objectAnimatorX, objectAnimatorY, imageX, imageY);

        ObjectAnimator setAnimatorX = ObjectAnimator.ofFloat(mSetTextView, "scaleX", 1F, 1.5F, 1F);
        ObjectAnimator setAnimatorY = ObjectAnimator.ofFloat(mSetTextView, "scaleY", 1F, 1.5F, 1F);
        ObjectAnimator setImageAnimatorX = ObjectAnimator.ofFloat(mSetImageView, "scaleX", 1F, 1.5F, 1F);
        ObjectAnimator setImageAnimatorY = ObjectAnimator.ofFloat(mSetImageView, "scaleY", 1F, 1.5F, 1F);
        mSetAnimator = new AnimatorSet();
        mSetAnimator.setDuration(2000);
        mSetAnimator.playTogether(setAnimatorX, setAnimatorY, setImageAnimatorX, setImageAnimatorY);

        mQuickAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mSetAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mSetAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mQuickAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(AdvRefreshMessage message){
        initBanner();
    }

    private void setAdapterData(final List<String> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBannerPagerAdapter.setData(list);
                mBannerPagerAdapter.notifyDataSetChanged();
                mIndicator.notifyDataSetChanged();
                mViewpager.startAutoScroll();

            }
        });
    }


    public void initBanner() {
        String yyyyMMdd = DateUtils.formatDate("yyyyMMdd", new Date());
        yyyyMMdd = "20160212";
        AdvService.getInstance().findAdvByTypeAndEndTime(AdvEntity.TYPE_QUICK_START, AdvEntity.NOT_DEFAULT, yyyyMMdd, new AdvService.CallBack<List<AdvEntity>>() {
            @Override
            public void onBack(List<AdvEntity> advEntities) {
                mBannerList.clear();
                if (advEntities != null && advEntities.size() > 0) {
                    for (AdvEntity entity : advEntities) {
                        mBannerList.add(entity.getUrl());
                    }
                    setAdapterData(mBannerList);
                } else {
                    //设置为默认的图片
                    AdvService.getInstance().findAdvByType(AdvEntity.TYPE_QUICK_START, AdvEntity.DEFAULT, new AdvService.CallBack<List<AdvEntity>>() {
                        @Override
                        public void onBack(List<AdvEntity> advEntities) {
                            if (advEntities != null && advEntities.size() > 0) {
                                for (AdvEntity entity : advEntities) {
                                    mBannerList.add(entity.getUrl());
                                }
                                setAdapterData(mBannerList);
                            }
                        }
                    });
                }
            }
        });
    }

}
