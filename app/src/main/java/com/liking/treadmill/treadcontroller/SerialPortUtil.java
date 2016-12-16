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
    private static TreadData sTreadData;

    public static class TreadData {
        private String mSafeLock = "";
        private String mOilPump = "";
        private String mPowerState = "";
        private String mCheck = "";
        private String mCurrentSpeed = "";
        private String mCurrentGrade = "";
        private String mMaxGrade = "";
        private String mErrorCode = "";
        private String mHeartRate = "";
        private String mPowerAd = "";
        private String mCheckGradeCountDown = "";
        private String mVersion = "";
        private String mIcId = "";
        private String mStepCount = "";
        private String mFanState = "";
        private float mDistance;
        private float mKCAL; //卡路里

        public String getSafeLock() {
            return mSafeLock;
        }

        public void setSafeLock(String safeLock) {
            mSafeLock = safeLock;
        }

        public String getOilPump() {
            return mOilPump;
        }

        public void setOilPump(String oilPump) {
            mOilPump = oilPump;
        }

        public String getPowerState() {
            return mPowerState;
        }

        public void setPowerState(String powerState) {
            mPowerState = powerState;
        }

        public String getCheck() {
            return mCheck;
        }

        public void setCheck(String check) {
            mCheck = check;
        }

        public String getCurrentSpeed() {
            return mCurrentSpeed;
        }

        public void setCurrentSpeed(String currentSpeed) {
            mCurrentSpeed = currentSpeed;
        }

        public String getCurrentGrade() {
            return mCurrentGrade;
        }

        public void setCurrentGrade(String currentGrade) {
            mCurrentGrade = currentGrade;
        }

        public String getMaxGrade() {
            return mMaxGrade;
        }

        public void setMaxGrade(String maxGrade) {
            mMaxGrade = maxGrade;
        }

        public String getErrorCode() {
            return mErrorCode;
        }

        public void setErrorCode(String errorCode) {
            mErrorCode = errorCode;
        }

        public String getHeartRate() {
            return mHeartRate;
        }

        public void setHeartRate(String heartRate) {
            mHeartRate = heartRate;
        }

        public String getPowerAd() {
            return mPowerAd;
        }

        public void setPowerAd(String powerAd) {
            mPowerAd = powerAd;
        }

        public String getCheckGradeCountDown() {
            return mCheckGradeCountDown;
        }

        public void setCheckGradeCountDown(String checkGradeCountDown) {
            mCheckGradeCountDown = checkGradeCountDown;
        }

        public String getVersion() {
            return mVersion;
        }

        public void setVersion(String version) {
            mVersion = version;
        }

        public String getIcId() {
            return mIcId;
        }

        public void setIcId(String icId) {
            mIcId = icId;
        }

        public String getStepCount() {
            return mStepCount;
        }

        public void setStepCount(String stepCount) {
            mStepCount = stepCount;
        }

        public String getFanState() {
            return mFanState;
        }

        public void setFanState(String fanState) {
            mFanState = fanState;
        }

        public float getDistance() {
            mDistance = (float) (Float.parseFloat(mCurrentSpeed) / 36.0);
            return mDistance;
        }

        public String getKCAL() {
            mKCAL = mKCAL + (float) (0.0703 * (1 + Float.parseFloat(getCurrentGrade()) / 100) * getDistance());
            return String.valueOf(mKCAL);
        }

        public void reset() {
            mSafeLock = "";
            mOilPump = "";
            mPowerState = "";
            mCheck = "";
            mCurrentSpeed = "";
            mCurrentGrade = "";
            mMaxGrade = "";
            mErrorCode = "";
            mHeartRate = "";
            mPowerAd = "";
            mCheckGradeCountDown = "";
            mVersion = "";
            mIcId = "";
            mStepCount = "";
            mFanState = "";
            mDistance = 0;
            mKCAL = 0; //卡路里
        }
    }
    private static boolean checkSerialPortData(byte[] serialPortData) {
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

    public static TreadData getTreadData(byte[] serialPortData) {
        if (sTreadData == null) {
            sTreadData = new TreadData();
        }
        if (!checkSerialPortData(serialPortData)) {
            return null;
        }
        sTreadData.setSafeLock(getIndexData(serialPortData, INDEX_SAFE_LOCK));
        sTreadData.setCheck(getIndexData(serialPortData, INDEX_CHECK));
        sTreadData.setCheckGradeCountDown(getIndexData(serialPortData, INDEX_CHECK_GRADE_COUNT_DOWN));
//        treadData.setCurrentGrade(getIndexData(serialPortData, INDEX_CURRENT_GRADE));
        sTreadData.setCurrentGrade("5.5");
//        treadData.setCurrentSpeed(getIndexData(serialPortData, INDEX_CURRENT_SPEED));
        sTreadData.setCurrentSpeed("6");
        sTreadData.setErrorCode(getIndexData(serialPortData, INDEX_ERROR_CODE));
        sTreadData.setFanState(getIndexData(serialPortData, INDEX_FAN_STATE));
        sTreadData.setHeartRate(getIndexData(serialPortData, INDEX_HEART_RATE));
        sTreadData.setMaxGrade(getIndexData(serialPortData, INDEX_MAX_GRADE));
        sTreadData.setOilPump(getIndexData(serialPortData, INDEX_OIL_PUMP));
        sTreadData.setPowerAd(getIndexData(serialPortData, INDEX_POWER_AD));
        sTreadData.setPowerState(getIndexData(serialPortData, INDEX_POWER));
        sTreadData.setVersion(getIndexData(serialPortData, INDEX_VERSION));
        return sTreadData;
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
        public final static String SAVE_LOCK_CLOSE = "00";
        public final static String SAVE_LOCK_OPEN = "01";
    }

    public final static class OilPump {
        public final static String OIL_PUMP_PLUS_NONE = "00";
        public final static String OIL_PUMP_PLUSING = "01";
    }

    public final static class PowerState {
        public final static String POWER_STATE_NORMAL = "00";
        public final static String POWER_STATE_BATTERY = "01";
    }

    public final static class Check {
        public final static String NO_CHECK = "00";
        public final static String CHECKING = "01";
    }

    enum ErrorCode {
        NONE("00", "无故障"),
        ACCELERATE_OVER_CURRENT("02", "加速过电流"),
        SLOW_DOWN_OVER_CURRENT("03", "减速过电流"),
        CONSTANT_SPEED_OVER_CURRENT("04", "恒速过电流"),
        ACCELERATE_OVERVOLTAGE("05", "加速过电压"),
        SLOW_DOWN_OVERVOLTAGE("06", "减速过电压"),
        CONSTANT_SPEED_OVERVOLTAGE("07", "恒速过电压"),
        UNDER_VOLTAGE("09", "欠压"),
        INVERTER_OVERLOAD("10", "变频器过载"),
        MOTOR_OVERLOAD("11", "电机过载"),
        INPUT_PHASE("12", "输入缺相"),
        OUTPUT_PHASE("13", "输出缺相"),
        MODULE_OVERHEATING("14", "模块过热"),
        EXTERNAL_FAULT("15", "外部故障"),
        ABNORMAL_COMMUNICATION("16", "通信异常"),
        ABNORMAL_CONTACTOR("17", "接触器异常"),
        CURRENT_DETECT_ABNORMAL("18", "电流检测异常"),
        MACHINE_SELF_LEARNING_FAULT("19", "电机自学习故障"),
        ENCODER_PGCARD_ANOMALIES("20", "编码器/PG卡异常"),
        ABNORMAL_PARAMETERS_SPEAKING_READING_WRITING("21", "参数读写异常"),
        INVERTER_HARDWARE_FAILURE("22", "变频器硬件故障"),
        MOTOR_SHORT_CIRCUIT("23", "电机对地短路"),
        RUN_TIME("26", "运行时间到达"),
        USER_DEFINED_FAILURE_1("27", "用户自定义故障1"),
        USER_DEFINED_FAILURE_2("28", "用户自定义故障2"),
        POWER_ON_TIME("29", "上电时间到达"),
        OFF_LOAD("30", "掉载"),
        RUNTIME_PID_FEEDBACK_MISSING("31", "运行时PID反馈丢失"),
        RAPID_CURRENT_LIMIT_FAILURE("40", "快速限流是故障"),
        SPEED_DEVIATION_TOO_LARGE("42", "速度偏差过大"),
        MOTOR_SPEED_TOO_LARGE("43", "电机超速过大"),
        BEYOND_LIMIT_TEST_RUN("44", "超出限定试跑距离");


        private final String mMessage;
        private final String mCode;

        ErrorCode(String code, String message) {
            mCode = code;
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }

        public String getCode() {
            return mCode;
        }
    }


}
