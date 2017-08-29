package com.liking.treadmill.message;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/8/29
 * @Version 1.0
 */
public class AdvResultMessage extends MessageImp{
    public static final int ADV_NEW = 100;
    public static final int ADV_DEFAULT = 101;

    public AdvResultMessage(int what) {
        super(what);
    }
}
