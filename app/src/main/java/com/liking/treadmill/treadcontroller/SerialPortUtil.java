package com.liking.treadmill.treadcontroller;

import com.aaron.android.codelibrary.utils.ConstantUtils;

/**
 * Created on 16/12/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class SerialPortUtil {
    private final static String PROTOCOL_HEAD_1 = "aa";
    private final static String PROTOCOL_HEAD_2 = "55";
    private final static String RECIVIE_HEAD = "fa";
    private final static int INDEX_KEY = 3;
    private final static int INDEX_SAFE_LOCK = 4;
    private final static int INDEX_OIL_PUMP = 5;
    private final static int INDEX_POWER = 6;
    private final static int INDEX_CHECK = 7;
    private final static int INDEX_CURRENT_SPEED = 8;
    private final static int INDEX_CURRENT_GRADE = 9;
    private final static int INDEX_MAX_GRADE = 10;
    private final static int INDEX_ERROR_CODE = 11;
    private final static int INDEX_HEART_RATE = 12;
    private final static int INDEX_SAVE = 13;
    private final static int INDEX_POWER_AD = 14;
    private final static int INDEX_CHECK_GRADE_COUNT_DOWN = 15;
    private final static int INDEX_VERSION = 16;
    private final static int INDEX_IC_ID_STEP_COUNT = 17;
    private final static int INDEX_FAN_STATE = 21;


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

    public static String getFanState(byte[] serialPortData) {
        return getIndexData(serialPortData, INDEX_FAN_STATE);
    }

    public static String getSaveLock(byte[] serialPortData) {
        return getIndexData(serialPortData, INDEX_SAFE_LOCK);
    }

    public static String getOilPump(byte[] serialPortData) {
        return getIndexData(serialPortData, INDEX_OIL_PUMP);
    }

    private static String getIndexData(byte[] serialPortData, int index, String defaultStr) {
        if (!checkSerialPortData(serialPortData)) {
            return defaultStr;
        }
        return byteToHexString(serialPortData[index]);
    }

    private static String getIndexData(byte[] serialPortData, int index) {
        return getIndexData(serialPortData, index, ConstantUtils.BLANK_STRING);
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

    public final static class FanState {
        public final static String FAN_STATE_STOP = "00";
        public final static String FAN_STATE_LOW_SPEED = "01";
        public final static String FAN_STATE_HIGH_SPEED = "02";
    }

    public final static class SaveLock {

    }

}
