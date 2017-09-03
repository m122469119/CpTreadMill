package com.liking.treadmill.message;

import com.liking.treadmill.socket.result.MarathonRankHotResult;

/**
 * Created on 2017/09/03
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class MarathonRankHotMessage {

    public MarathonRankHotResult.DataBean mDataBean;

    public MarathonRankHotMessage(MarathonRankHotResult.DataBean dataBean) {
            this.mDataBean = dataBean;
    }
}
