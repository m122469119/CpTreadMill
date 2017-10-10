package com.liking.socket.model.message;

import com.liking.socket.Constant;
import com.liking.socket.utils.SnowflakeIdWorker;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public abstract class MessageData {

    private static SnowflakeIdWorker mIDWorker = new SnowflakeIdWorker(0, 0);

    /**
     * 消息ID，facebook算法
     */
    private long mMsgId;

    public long getMsgId() {
        if (mMsgId == 0) {
            mMsgId = mIDWorker.nextId();
        }
        return mMsgId;
    }

    /**
     * 重试次数
     */
    private int mRetryTime = Constant.DEFAULT_MSG_RETRY_TIME;

    /**
     * 是否还可以重试
     *
     * @return
     */
    public boolean canRetry() {
        return mRetryTime > 0;
    }

    public void retry() {
        mRetryTime--;
    }

    private byte[] mData;

    /**
     * 命令字
     *
     * @return
     */
    public abstract byte cmd();

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
}
