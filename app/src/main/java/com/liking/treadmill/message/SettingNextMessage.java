package com.liking.treadmill.message;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:26
 * version 1.0.0
 */
public class SettingNextMessage extends BaseMessage{
    private int next;

    public SettingNextMessage(int next) {
        this.next = next;
    }

    public int getNext() {
        return next;
    }
}
