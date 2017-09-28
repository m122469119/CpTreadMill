package com.liking.socket.model.message;

import com.liking.socket.Constant;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public abstract class MessageData {

    /**
     * 命令字
     */
    private byte mCmd;

    /**
     * 消息ID，facebook算法
     */
    private int mMsgId;
    /**
     * 重试次数
     */
    private int mRetryTime = Constant.DEFAULT_MSG_RETRY_TIME;

    public boolean canRetry() {
        return mRetryTime > 0;
    }

    public void retry() {
        mRetryTime--;
    }

    private String mKey;

    public void setKey(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    private byte[] mData;

    public abstract byte cmd();

    /**
     * 是否需要反馈
     *
     * @return true 需要反馈
     */
    public boolean needFeedback() {
        return false;
    }

    public final void setData(String data) {
        if (null == data) {
            throw new IllegalStateException("Data String not be null.");
        }

        setData(data.getBytes(Constant.DEFAULT_CHARSET));
    }

    public final void setData(byte[] data) {
        if (null == data) {
            throw new IllegalStateException("Data not be null.");
        }

        mData = data;
    }

    public byte[] getData() {
        // TODO: 2017/9/19 构建Header
        return mData;
    }

    /**
     * 不需要反馈，不回调 <br/>
     * 需要反馈，回调
     *
     * @param isSuccess 是否发送成功
     * @param message   成功：data
     *                  失败：失败信息
     */
    public void callBack(boolean isSuccess, String message) {

    }
}
