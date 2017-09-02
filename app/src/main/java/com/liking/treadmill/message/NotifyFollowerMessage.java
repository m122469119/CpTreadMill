package com.liking.treadmill.message;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.liking.treadmill.socket.result.NotifyFollowerResult;

/**
 * Created on 2017/09/02
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class NotifyFollowerMessage extends BaseMessage {

    public NotifyFollowerResult.DataBean mDataBean;

    public NotifyFollowerMessage(NotifyFollowerResult.DataBean data) {
        this.mDataBean = data;
    }
}
