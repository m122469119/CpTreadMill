package com.liking.treadmill.treadcontroller;

/**
 * Created on 16/12/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class SerialPortReceiveUtil {
    private final static String PROTOCOL_HEAD_1 = "aa";
    private final static String PROTOCOL_HEAD_2 = "55";
    private final static String RECIVIE_HEAD = "fa";
    private final static int INDEX_KEY = 3;



    public static boolean checkSerialPortData(byte[] serialPortData) {
        if (serialPortData == null) {
            return false;
        }
        if (PROTOCOL_HEAD_1.equals(byteToHexString(serialPortData[0])) && PROTOCOL_HEAD_2.equals(byteToHexString(serialPortData[1]))) {
            return true;
        }
        return false;
    }

    public static String getKeyCodeFromSerialPort(byte[] serialPortData) {
        if (!checkSerialPortData(serialPortData)) {
            return LikingTreadKeyEvent.KEY_NONE;
        }
        return byteToHexString(serialPortData[INDEX_KEY]);
    }

    public static String byteToHexString(byte b) {
        StringBuilder hexString = new StringBuilder("");
        int v = b & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            hexString.append(0);
        }
        hexString.append(hv);
        return hexString.toString();
    }


}
