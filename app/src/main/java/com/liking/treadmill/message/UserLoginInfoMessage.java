package com.liking.treadmill.message;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.liking.treadmill.socket.data.result.UserInfoResultData;

/**
 * Created on 2017/10/11
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class UserLoginInfoMessage extends BaseMessage {

    public UserInfoResultData mUserResult;
}
