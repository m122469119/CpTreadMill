package com.liking.treadmill.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/12/27.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class StartFragment extends SerialPortFragment {
    @BindView(R.id.user_name_TextView)
    TextView mUserNameTextView;
    @BindView(R.id.head_imageView)
    HImageView mHeadImageView;
//    @BindView(R.id.quick_start)
//    ImageView mQuickStart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_start, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
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
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            SerialPortUtil.setCardNoUnValid();//设置无效卡
            SerialPortUtil.getTreadInstance().reset();//清空数据
            ((HomeActivity) getActivity()).setTitle("");
            ((HomeActivity) getActivity()).launchFragment(new AwaitActionFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_START) {
            ((HomeActivity) getActivity()).launchFragment(new RunFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_CARD) {
            cardLogin();
        } else if (keyCode == LikingTreadKeyEvent.KEY_PROGRAM) {
            showSettingUI();
        } else if (keyCode == LikingTreadKeyEvent.KEY_SET) {//参数设置
            ((HomeActivity) getActivity()).launchFragment(new GoalSettingFragment());
        }
    }

    @Override
    public void handleTreadData(SerialPortUtil.TreadData treadData) {
        super.handleTreadData(treadData);
    }

    private void showSettingUI() {
        ((HomeActivity) getActivity()).setTitle("系统设置");
        ((HomeActivity) getActivity()).launchFragment(new SettingFragment());
    }

    /**
     * 刷卡登录
     */
    private void cardLogin() {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.setTitle("");
        if (homeActivity.mUserLoginPresenter != null) {
            homeActivity.mUserLoginPresenter.userLogin();
        }
    }
}
