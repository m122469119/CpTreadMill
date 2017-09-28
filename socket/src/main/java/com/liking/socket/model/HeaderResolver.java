package com.liking.socket.model;

import com.liking.socket.utils.ResolverUtils;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public class HeaderResolver extends IHeaderResolver {
    private int mPkgSize;
    private String mProtocolVer;
    private short mAppID;
    private String mAppVersion;
    private long mMsgID;
    private byte[] mSign;
    private byte mCmd;

    @Override
    public int headerLength() {
        return ResolverUtils.getHeaderLength();
    }

    @Override
    public void resolver(byte[] buffer) {
        mPkgSize = ResolverUtils.parsePkgSize(buffer) ;
        mProtocolVer = ResolverUtils.parseProtocolVersion(buffer);
        mAppID = ResolverUtils.parseAppID(buffer);
        mAppVersion = ResolverUtils.parseAppVersion(buffer);
        mMsgID = ResolverUtils.parseMessageId(buffer);
        mSign = ResolverUtils.parseSign(buffer);
        mCmd = ResolverUtils.parseCmd(buffer);
    }

    @Override
    public int bodyLength() {
        return mPkgSize - ResolverUtils.INDEX_DATA + ResolverUtils.INDEX_PROTOCOL_VERSION;
    }

    public int getPkgSize() {
        return mPkgSize;
    }

    public String getProtocolVersion() {
        return mProtocolVer;
    }

    public int getAppID() {
        return mAppID;
    }

    public String getAppVersion() {
        // TODO: 2017/9/19
        return mAppVersion;
    }

    public long getMsgID() {
        return mMsgID;
    }

    public byte[] getSign() {
        return mSign;
    }

    @Override
    public byte getCmd() {
        return mCmd;
    }
}
