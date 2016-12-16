package com.liking.treadmill.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.mvp.presenter.AppUpdatePresenter;
import com.liking.treadmill.mvp.view.AppUpdateView;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.widget.ColorfulRingProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author chenlei
 */

public class UpdateFragment extends SerialPortFragment implements AppUpdateView {

    private View mRootView;
    @BindView(R.id.update_layout)
    FrameLayout mUpdateLayout;
    @BindView(R.id.update_hint)
    TextView updateHint;
    @BindView(R.id.update_progress)
    TextView updateProgress;
    @BindView(R.id.update_colorfulring_progress)
    ColorfulRingProgressView mProgressView;

    private AppUpdatePresenter mAppUpdatePresenter = null;

    private static final long DELAYED_TIME_EXIT = 2000;

    private static final long DELAYED_TIME_ANIMATION= 1500;

    private Handler apkUpdateHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_update, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {
        //Tost:正在更新中,请稍后操作....
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
        initViews();
        initData();
    }

    private void initData() {
        if(mAppUpdatePresenter == null) {
            mAppUpdatePresenter = new AppUpdatePresenter(getActivity(),this);
        }
        if(mAppUpdatePresenter.checkTreadMillState()){//检查跑步机使用状态
            exitUpdateView();
        }
        if(mAppUpdatePresenter.checkAppVersion()){
            //转圈效果
            startProgressAnimation();
        }
    }

    private void startProgressAnimation() {
        mProgressView.setProgressColor(ResourceUtils.getColor(R.color.colorfulringprogress_pre_bgcolor),
                ResourceUtils.getColor(R.color.colorfulringprogress_pre_fgcolorstart),
                ResourceUtils.getColor(R.color.colorfulringprogress_pre_fgcolorend));
        mProgressView.setPercent(58);
        ObjectAnimator anim = ObjectAnimator.ofFloat(mProgressView,"rotation",0f,1080f);
        anim.setInterpolator(new LinearInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                updateHint.setText("正在检查更新");
                updateProgress.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateHint.setText("正在安装更新");
                updateProgress.setVisibility(View.VISIBLE);
                mProgressView.setProgressColor(ResourceUtils.getColor(R.color.colorfulringprogress_bgcolor),
                        ResourceUtils.getColor(R.color.colorfulringprogress_fgcolorstart),
                        ResourceUtils.getColor(R.color.colorfulringprogress_fgcolorend));
                mProgressView.setPercent(0);
                Preference.setServerVersion("1.0.1");
                Preference.setServerVersionUrl("http://down.360safe.com/360PermRoot/PermRoot.apk");
                mAppUpdatePresenter.startDownloadApk();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(2500);
        anim.start();
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
    }

    private void initViews() {
        int height = DisplayUtils.getHeightPixels();
        int mProgressHeight = height / 2;
        mUpdateLayout.getLayoutParams().width = mProgressHeight;
        mUpdateLayout.getLayoutParams().height = mProgressHeight;
    }

    @Override
    public void startUpdateView() {
    }

    @Override
    public void updateProgressView(float percent) {
        updateHint.setText("正在安装更新");
        updateProgress.setVisibility(View.VISIBLE);
        updateProgress.setText(percent + "%");
        mProgressView.setPercent(percent);
    }

    @Override
    public void updateDownloadCompleteView() {
        updateHint.setText("已完成下载,更新中...");
        updateProgress.setVisibility(View.GONE);
    }

    @Override
    public void updateFailView() {
        updateHint.setText("更新失败!");
        updateProgress.setVisibility(View.GONE);
        apkUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exitUpdateView();
            }
        }, DELAYED_TIME_EXIT);
    }

    @Override
    public void exitUpdateView() {
        this.getActivity().finish();
    }

    @Override
    public void handleNetworkFailure() {

    }
}
