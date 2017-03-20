package com.liking.treadmill.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.PopupUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;

import butterknife.ButterKnife;

/**
 * Created on 16/12/15.
 */

public class AwaitActionFragment extends SerialPortFragment {

    private View mRootView;

    private static final String KEY_CARDNO_VALUE = "cardno";

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
        final HomeActivity homeActivity = (HomeActivity) getActivity();
        if (keyCode == LikingTreadKeyEvent.KEY_CARD) {//刷卡
            if (homeActivity.mUserLoginPresenter != null) {
                homeActivity.mUserLoginPresenter.userLogin();
            }
        } else if (keyCode == LikingTreadKeyEvent.KEY_SET) {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
//            startActivity(intent);
        } else if (keyCode == LikingTreadKeyEvent.KEY_PGR_PGR_SPEED_REDUCE) {
            View customView =  getLayoutInflater(null).inflate(R.layout.layout_visit_validate, null, false);
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

        } else if(keyCode == LikingTreadKeyEvent.KEY_PGR_START) {
            if(Preference.isVisitorMode()) {//访客模式
                SerialPortUtil.TreadData.UserInfo userInfo = SerialPortUtil.getTreadInstance().getUserInfo();
                if(userInfo == null) {
                    userInfo = new SerialPortUtil.TreadData.UserInfo();
                    userInfo.mUserName = "LikingFans";
                    userInfo.mGender = 1;
                    userInfo.mAvatar = "";
                    userInfo.isVisitor = true;
                }
                SerialPortUtil.getTreadInstance().setUserInfo(userInfo);
                ((HomeActivity)getActivity()).launchFragment(new RunFragment());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
        initViews();
        if(SerialPortUtil.getTreadInstance().getUserInfo() != null) {
            SerialPortUtil.getTreadInstance().resetUserInfo();
        }
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
