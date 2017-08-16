package com.liking.treadmill.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.library.thread.TaskScheduler;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.db.entity.Member;
import com.liking.treadmill.message.LoginUserInfoMessage;
import com.liking.treadmill.mvp.view.UserLoginView;
import com.liking.treadmill.socket.result.UserInfoResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.utils.MemberHelper;
import com.liking.treadmill.widget.IToast;

import java.util.List;

/**
 * 说明: 用户刷卡登录
 * Author: chenlei
 * Time: 下午3:46
 */

public class UserLoginPresenter extends BasePresenter<UserLoginView> {

    private static long lastTime;
    private final static int SPACE_TIME = 1000;

    private Member member = null;

    public UserLoginPresenter(Context context, UserLoginView mainView) {
        super(context, mainView);
    }

    /**
     * 用户刷卡登录
     */
    public void userLogin() {

        final String cardNo = SerialPortUtil.getTreadInstance().getCardNo();
        if(!StringUtils.isEmpty(cardNo) && !isRepeatReadCard()) {//不为空并且1s内只允许发起一次刷卡请求
            if(EnvironmentUtils.Network.isNetWorkAvailable()) {
                if(mView != null) {
                    mView.userLogin(cardNo);
                }
            } else {
                //无网
                if(!StringUtils.isEmpty(Preference.getBindUserGymId()) && MemberHelper.getInstance().getMembersFromLocal() != null) {//已绑定场馆

                    TaskScheduler.execute(new Runnable() {
                        @Override
                        public void run() {
                            member = MemberHelper.getInstance().getMembersFromLocal().queryMemberInfo(cardNo);
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            if(member != null) {
                                launchRunFragment(getDefaultUserInfo(member.getMemberType(), cardNo));
                            } else {
                                //缓存中查询(未缓存到数据库)
                                List<Member> mMemberCache = MemberHelper.getInstance().getMembersFromMemory();
                                if(mMemberCache != null && !mMemberCache.isEmpty()) {
                                    for (Member m:mMemberCache) {
                                        if(cardNo.equals(m.getBraceletId())) {
                                            launchRunFragment(getDefaultUserInfo(member.getMemberType(), cardNo));
                                            return;
                                        }
                                    }
                                    IToast.show(ResourceUtils.getString(R.string.network_no_connection));
                                } else {
                                    IToast.show(ResourceUtils.getString(R.string.network_no_connection));
                                }
                            }
                        }
                    });

                } else { //场馆未绑定
                    IToast.show(ResourceUtils.getString(R.string.network_no_connection));
                }
            }
        }
    }

    /**
     * 设置默认的用户信息
     * @param role
     * @param braceletId
     * @return
     */
    private UserInfoResult.UserData.UserInfoData getDefaultUserInfo(int role, String braceletId) {
        UserInfoResult.UserData.UserInfoData userDefaultInfo = new UserInfoResult.UserData.UserInfoData();
        userDefaultInfo.setRole(role); //1管理员 2普通会员
        userDefaultInfo.setUserName("LikingFans");
        userDefaultInfo.setAvatar("");
        userDefaultInfo.setGender(1);
        userDefaultInfo.setBraceletId(braceletId);
        return userDefaultInfo;
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
            launchRunFragment(userResult);
        } else {
            IToast.show(mUserData.getErrmsg());
            userLoginFail();
        }
    }

    public void userLoginFail() {
        SerialPortUtil.getTreadInstance().reset();
        if(mView != null) {
            mView.userLoginFail();
        }
    }

    private void launchRunFragment(UserInfoResult.UserData.UserInfoData userResult) {
        SerialPortUtil.TreadData.UserInfo userInfo = new SerialPortUtil.TreadData.UserInfo();
        userInfo.mRole = userResult.getRole();
        userInfo.mUserName = userResult.getUserName();
        userInfo.mAvatar = userResult.getAvatar();
        userInfo.mGender = userResult.getGender();
        userInfo.mBraceletId = userResult.getBraceletId();
        userInfo.isManager = userInfo.mRole == 1 ? true : false;//是否为管理员

        SerialPortUtil.getTreadInstance().setCardNo(userInfo.mBraceletId);
        SerialPortUtil.getTreadInstance().setUserInfo(userInfo);
        SerialPortUtil.setCardNoValid();

        if(mView != null) {
            mView.launchStartFragment();
        }
    }

}
