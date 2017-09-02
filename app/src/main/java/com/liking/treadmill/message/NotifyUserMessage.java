package com.liking.treadmill.message;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.liking.treadmill.socket.result.NotifyUserResult;

/**
 * Created on 2017/09/02
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class NotifyUserMessage extends BaseMessage {

    public NotifyUserResult.DataBean mDataBean;

    public NotifyUserMessage(NotifyUserResult.DataBean data) {
        this.mDataBean = data;
    }
}
