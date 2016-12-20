package com.liking.treadmill.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.message.LoginUserInfoMessage;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;

import butterknife.ButterKnife;

/**
 * Created on 16/12/15.
 *
 */

public class AwaitActionFragment extends SerialPortFragment {

    private View mRootView;

    private static final String KEY_CARDNO_VALUE = "cardno";

    private  HomeActivity homeActivity = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_awaitaction, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_CARD) {//刷卡跳转
            String cardNo = SerialPortUtil.getTreadInstance().getCardNo();
            LogUtils.e(TAG," onTreadKeyDown :" + LikingTreadKeyEvent.KEY_CARD + ";cardNo" + cardNo);
            if(!StringUtils.isEmpty(cardNo)) {
//                Bundle bundle = new Bundle();
//                bundle.putString(KEY_CARDNO_VALUE, cardNo);
                  homeActivity = (HomeActivity)getActivity();
                try {
                    homeActivity.iBackService.userLogin(cardNo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onEvent(LoginUserInfoMessage loginUserInfoMessage) {
        if(loginUserInfoMessage.errcode != 0) {
            IToast.show(loginUserInfoMessage.errmsg);
            SerialPortUtil.setCardNoUnValid();
        } else {
            SerialPortUtil.setCardNoValid();
            homeActivity.launchFragment(new RunFragment());
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

}
