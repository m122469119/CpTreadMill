package com.liking.socket.model.message;

import com.liking.socket.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ttdevs
 * 2017-09-21 (Socket)
 * https://github.com/ttdevs
 */
public class QRCodeRequestMsg extends MessageData {
    private String mDeviceId;
    private int mDeviceType;
    private int mQRCodeType;

    public QRCodeRequestMsg(String deviceId, int deviceType, int qrcodeType) {
        mDeviceId = deviceId;
        mDeviceType = deviceType;
        mQRCodeType = qrcodeType;
    }

    @Override
    public byte cmd() {
        return Constant.CMD_PING_PONG;
    }

    @Override
    public byte[] getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("device_id", mDeviceId);
            object.put("device_type", mDeviceType);
            object.put("qrcode_type", mQRCodeType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString().getBytes();
    }
}
