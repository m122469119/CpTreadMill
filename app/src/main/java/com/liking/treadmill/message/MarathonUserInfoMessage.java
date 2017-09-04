package com.liking.treadmill.message;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.liking.treadmill.socket.result.MarathonUserInfoResult;

/**
 * Created on 2017/09/03
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class MarathonUserInfoMessage extends BaseMessage {

    public MarathonUserInfoResult.DataBean mDataBean;

    public MarathonUserInfoMessage(MarathonUserInfoResult.DataBean data) {
        this.mDataBean = data;
    }
}
