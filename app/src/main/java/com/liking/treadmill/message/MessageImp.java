package com.liking.treadmill.message;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/8/29
 * @Version 1.0
 */
public class MessageImp extends BaseMessage {
    public int what;
    public int arg1;
    public int arg2;
    public String msg1;
    public String msg2;
    public Object obj1;
    public Object obj2;

    public MessageImp() {
    }

    public MessageImp(int what) {
        this.what = what;
    }
}
