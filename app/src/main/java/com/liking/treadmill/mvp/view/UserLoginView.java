package com.liking.treadmill.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;

/**
 * 说明:用户刷卡登录
 * Author: chnelei
 * Time: 下午3:47
 */

public interface UserLoginView extends BaseNetworkLoadView {
    /**
     * 刷卡登录
     */
    void userLogin(String cardno);
    /**
     * 进入跑步界面
     */
    void launchRunFragment();
}
