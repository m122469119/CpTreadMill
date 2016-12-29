package com.liking.treadmill.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.message.LoginUserInfoMessage;
import com.liking.treadmill.mvp.view.UserLoginView;
import com.liking.treadmill.socket.result.UserInfoResult;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;

/**
 * 说明: 用户刷卡登录
 * Author: chenlei
 * Time: 下午3:46
 */

public class UserLoginPresenter extends BasePresenter<UserLoginView> {

    private static long lastTime;
    private final static int SPACE_TIME = 1000;

    public UserLoginPresenter(Context context, UserLoginView mainView) {
        super(context, mainView);
    }

    /**
     * 用户刷卡登录
     */
    public void userLogin() {
        if(EnvironmentUtils.Network.isNetWorkAvailable()) {
            String cardNo = SerialPortUtil.getTreadInstance().getCardNo();
            if(!StringUtils.isEmpty(cardNo) && !isRepeatReadCard()) {//不为空并且1s内只允许发起一次刷卡请求

                if(mView != null) {
                    mView.userLogin(cardNo);
                }
            }
        } else {
            IToast.show(ResourceUtils.getString(R.string.network_no_connection));
        }
    }

    /**
     * 防止短时间内多次读卡或者过滤串口多次返回
     * @return
     */
    private boolean isRepeatReadCard() {
        long currentTime = System.currentTimeMillis();
        boolean repeat;
        if (currentTime - lastTime > SPACE_TIME) {
            repeat = false;
        } else {
            repeat = true;
        }
        lastTime = currentTime;
        return repeat;
    }

    /**
     * 刷卡成功后处理
     * @param message
     */
    public void userLoginResult(LoginUserInfoMessage message) {
        UserInfoResult.UserData mUserData = message.mUserData;
        if(mUserData.getErrcode() == 0) {
            UserInfoResult.UserData.UserInfoData userResult = message.mUserData.getUserInfoData();
//            if(!mCurrCardNo.equals(userResult.getBraceletId())) { //返回的卡号与当前卡号不一致
//            } else {
//            }
            SerialPortUtil.TreadData.UserInfo userInfo = new SerialPortUtil.TreadData.UserInfo();
            userInfo.mUserName = userResult.getUserName();
            userInfo.mAvatar = userResult.getAvatar();
            userInfo.mGender = userResult.getGender();
            userInfo.mBraceletId = userResult.getBraceletId();
            SerialPortUtil.getTreadInstance().setCardNo(userInfo.mBraceletId);
            SerialPortUtil.getTreadInstance().setUserInfo(userInfo);
            SerialPortUtil.setCardNoValid();
            if(mView != null) {
                mView.launchRunFragment();
            }
        } else {
            IToast.show(mUserData.getErrmsg());
            userLoginFail();

        }
//        mCurrCardNo = "";
    }

    public void userLoginFail() {
        SerialPortUtil.setCardNoUnValid();
        SerialPortUtil.getTreadInstance().reset();
        if(mView != null) {
            mView.userLoginFail();
        }
    }

}
