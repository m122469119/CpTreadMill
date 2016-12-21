package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.message.LoginUserInfoMessage;
import com.liking.treadmill.mvp.presenter.UserLoginPresenter;
import com.liking.treadmill.mvp.view.UserLoginView;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;

import butterknife.ButterKnife;

/**
 * Created on 16/12/15.
 *
 */

public class AwaitActionFragment extends SerialPortFragment implements UserLoginView {

    private View mRootView;

    private static final String KEY_CARDNO_VALUE = "cardno";

    private  HomeActivity homeActivity = null;

    private UserLoginPresenter mUserLoginPresenter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_awaitaction, container, false);
        ButterKnife.bind(this, mRootView);
        if(mUserLoginPresenter == null) {
            mUserLoginPresenter = new UserLoginPresenter(getActivity(), this);
        }
        return mRootView;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_CARD) {//刷卡
            if(mUserLoginPresenter != null) {
                mUserLoginPresenter.userLogin();
            }
        }
    }

    public void onEvent(LoginUserInfoMessage loginUserInfoMessage) {
        if(mUserLoginPresenter != null) {
            mUserLoginPresenter.userLoginResult(loginUserInfoMessage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
        initViews();
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

    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }


    @Override
    public void userLogin(String cardno) {
        homeActivity = (HomeActivity)getActivity();
        try {
            homeActivity.iBackService.userLogin(cardno);
        } catch (RemoteException e) {
            e.printStackTrace();
            IToast.show(ResourceUtils.getString(R.string.read_card_error));
        }
    }

    @Override
    public void launchRunFragment() {
        homeActivity.launchFragment(new RunFragment());
    }

    @Override
    public void handleNetworkFailure() {

    }
}
