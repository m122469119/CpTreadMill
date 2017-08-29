package com.liking.treadmill.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.TextView;
import butterknife.BindView;
import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.adapter.BannerPagerAdapter;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.db.entity.AdvEntity;
import com.liking.treadmill.db.service.AdvService;
import com.liking.treadmill.fragment.base.SerialPortFragment;
import com.liking.treadmill.message.AdvRefreshMessage;
import com.liking.treadmill.message.GymUnBindSuccessMessage;
import com.liking.treadmill.message.UpdateAppMessage;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.ApkUpdateHelper;
import com.liking.treadmill.utils.TimeUtils;
import com.liking.treadmill.widget.IToast;

import butterknife.ButterKnife;
import com.liking.treadmill.widget.autoviewpager.InfiniteViewPager;
import com.liking.treadmill.widget.autoviewpager.indicator.IconPageIndicator;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 16/12/15.
 */

public class AwaitActionFragment extends SerialPortFragment {

    public static final long AUTO_SCROLL = 8 * 1000;

    @BindView(R.id.viewpager_await)
    InfiniteViewPager mViewpager;
    @BindView(R.id.indicator_await)
    IconPageIndicator mIndicator;
    BannerPagerAdapter mBannerPagerAdapter;
    @BindView(R.id.text_please)
    TextView mPleaseView;

    List<String> mBannerList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_awaitaction, container, false);
        ButterKnife.bind(this, mRootView);
        if (SerialPortUtil.getTreadInstance().getUserInfo() != null) {
            final HomeActivity homeActivity = (HomeActivity) getActivity();
            if (homeActivity.isLogin) {
                homeActivity.userLogout(SerialPortUtil.getTreadInstance().getUserInfo().mBraceletId);
                homeActivity.isLogin = false;
            }
            SerialPortUtil.getTreadInstance().resetUserInfo();
        }
        //本地检测更新
        if (ApkUpdateHelper.isApkUpdate()
                && EnvironmentUtils.Network.isNetWorkAvailable()
                && Preference.getAppDownloadFailCount() < ThreadMillConstant.THREADMILL_UPDATE_FAIL_COUNT) {
            EventBus.getDefault().post(new UpdateAppMessage());
        }
        initViews();
        return mRootView;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        final HomeActivity homeActivity = (HomeActivity) getActivity();
        if (keyCode == LikingTreadKeyEvent.KEY_CARD) {//刷卡
            if (homeActivity.mUserLoginPresenter != null) {
                homeActivity.mUserLoginPresenter.userLogin();
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_MODE_MODE) {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
//            startActivity(intent);
        } else if (keyCode == LikingTreadKeyEvent.KEY_PGR_PGR_SPEED_REDUCE) {
            View customView = getLayoutInflater(null).inflate(R.layout.layout_visit_validate, null, false);
            final EditText inputPasswordEditText = (EditText) customView.findViewById(R.id.visit_password_editText);
            new HBaseDialog.Builder(homeActivity).setCustomView(customView).
                    setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if ("123456".equals(inputPasswordEditText.getText().toString())) {
                                homeActivity.launchFragment(new StartFragment());
                            } else {
                                PopupUtils.showToast("密码不正确");
                            }
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            }).show();

        } else if (keyCode == LikingTreadKeyEvent.KEY_PGR_START) {
            if (Preference.isVisitorMode()) {//访客模式
                SerialPortUtil.TreadData.UserInfo userInfo = SerialPortUtil.getTreadInstance().getUserInfo();
                if (userInfo == null) {
                    userInfo = new SerialPortUtil.TreadData.UserInfo();
                    userInfo.mUserName = "LikingFans";
                    userInfo.mGender = 1;
                    userInfo.mAvatar = "";
                    userInfo.isVisitor = true;
                }
                SerialPortUtil.getTreadInstance().setUserInfo(userInfo);
                ((HomeActivity) getActivity()).launchFragment(new RunFragment());
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SPEED_PLUS_SPEED_REDUCE) {
            String gymId = Preference.getBindUserGymId();
            if (!StringUtils.isEmpty(gymId) && "-1".equals(gymId)) {
                Preference.setUnBindRest();
                EventBus.getDefault().post(new GymUnBindSuccessMessage());
                IToast.show("解绑中...");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
        mViewpager.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "------onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "------onStop()");
        mViewpager.stopAutoScroll();
    }

    private void initViews() {
        mBannerPagerAdapter = new BannerPagerAdapter(getActivity());
        mViewpager.setAdapter(mBannerPagerAdapter);
        mViewpager.setAutoScrollTime(AUTO_SCROLL);
        mIndicator.setViewPager(mViewpager);
        mViewpager.startAutoScroll();
        mPleaseView.setText(Html.fromHtml("<font color=\"#85878e\">请</font><font color=\"#34c86c\">在下方面板上刷手环处刷手环</font><font color=\"#85878e\">开启跑步机</font>"));
        initBanner();
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(AdvRefreshMessage message) {
        initBanner();
    }


    public void initBanner() {
        String yyyyMMdd = DateUtils.formatDate("yyyyMMdd", new Date());
        yyyyMMdd = "20160212";
        AdvService.getInstance().findAdvByTypeAndEndTime(AdvEntity.TYPE_HOME, AdvEntity.NOT_DEFAULT, yyyyMMdd, new AdvService.CallBack<List<AdvEntity>>() {
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
                    AdvService.getInstance().findAdvByType(AdvEntity.TYPE_HOME, AdvEntity.NOT_DEFAULT, new AdvService.CallBack<List<AdvEntity>>() {
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

    private void setAdapterData(final List<String> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBannerPagerAdapter.setData(list);
                mBannerPagerAdapter.notifyDataSetChanged();
                mViewpager.startAutoScroll();

            }
        });
    }

}
