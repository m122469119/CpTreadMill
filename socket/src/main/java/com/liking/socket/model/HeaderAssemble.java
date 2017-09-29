package com.liking.socket.model;

import com.liking.socket.Constant;
import com.liking.socket.model.message.MessageData;
import com.liking.socket.utils.AESUtils;
import com.liking.socket.utils.ResolverUtils;

import java.security.MessageDigest;

/**
 * Created by ttdevs
 * 2017-09-19 (Socket)
 * https://github.com/ttdevs
 */
public class HeaderAssemble implements IHeaderAssemble {

    @Override
    public byte[] assemble(MessageData msg) {
        byte cmd = msg.cmd();
        byte[] data = AESUtils.encode(msg.getData());
        if (null == data) {
            return null;
        }

        int size = ResolverUtils.INDEX_DATA;
        if (data.length > 0) {
            size += data.length;
        }
        byte[] result = new byte[size];
        byte[] length = ResolverUtils.int2ByteArray(size - ResolverUtils.INDEX_PROTOCOL_VERSION);
        System.arraycopy(length, 0,
                result,
                ResolverUtils.INDEX_PACKAGE_LENGTH,
                ResolverUtils.INDEX_PROTOCOL_VERSION);

        byte[] proVer = Constant.Protocol_Version.getBytes();
        System.arraycopy(proVer, 0, result,
                ResolverUtils.INDEX_PROTOCOL_VERSION,
                ResolverUtils.INDEX_APP_ID - ResolverUtils.INDEX_PROTOCOL_VERSION);

        byte[] appID = ResolverUtils.short2ByteArray(Constant.APP_ID);
        System.arraycopy(appID, 0, result,
                ResolverUtils.INDEX_APP_ID,
                ResolverUtils.INDEX_APP_VERSION - ResolverUtils.INDEX_APP_ID);

        System.arraycopy(Constant.APP_VERSION, 0, result,
                ResolverUtils.INDEX_APP_VERSION,
                ResolverUtils.INDEX_MESSAGE_ID - ResolverUtils.INDEX_APP_VERSION);

        long msgIDLong = msg.getMsgId();
        byte[] msgID = ResolverUtils.long2ByteArray(msgIDLong);
        System.arraycopy(msgID, 0, result,
                ResolverUtils.INDEX_MESSAGE_ID,
                ResolverUtils.INDEX_MESSAGE_ID_SRC - ResolverUtils.INDEX_MESSAGE_ID);

        byte[] srcMsgID = ResolverUtils.long2ByteArray(msg.getSrcMsgId());
        System.arraycopy(srcMsgID, 0, result,
                ResolverUtils.INDEX_MESSAGE_ID_SRC,
                ResolverUtils.INDEX_CMD - ResolverUtils.INDEX_MESSAGE_ID_SRC);

        result[ResolverUtils.INDEX_CMD] = cmd;

        try {
            byte[] sign = sign(msgIDLong, data);
            System.arraycopy(sign, 0, result,
                    ResolverUtils.INDEX_SIGN,
                    ResolverUtils.INDEX_DATA - ResolverUtils.INDEX_SIGN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != data && data.length > 0) {
            System.arraycopy(data, 0, result,
                    ResolverUtils.INDEX_DATA,
                    data.length);
        }
        return result;
    }

    /**
     * SHA1(MsgID+Data+AppKey)
     *
     * @param msgIDLong
     * @param data
     * @return
     * @throws Exception
     */
    private byte[] sign(long msgIDLong, byte[] data) throws Exception {
        byte[] msgID = ResolverUtils.long2ByteArray(msgIDLong);
        byte[] appKey = Constant.KEY_SIGN.getBytes();
        int size = msgID.length + data.length + appKey.length;

        byte[] result = new byte[size];
        System.arraycopy(msgID, 0, result, 0, msgID.length);
        System.arraycopy(data, 0, result, msgID.length, data.length);
        System.arraycopy(appKey, 0, result, msgID.length + data.length, appKey.length);

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(result);
        return digest.digest();
    }
}
