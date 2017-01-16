package com.liking.treadmill.treadcontroller;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;

import androidex.serialport.SerialPorManager;

/**
 * Created on 16/12/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class SerialPortUtil {
    private final static int PROTOCOL_HEAD_1 = 0xAA;
    private final static int PROTOCOL_HEAD_2 = 0x55;
    private final static int PROTOCOL_HEAD_CARDNO = 0x40;
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
    //    private final static int INDEX_IC_ID_STEP_COUNT = 17;
    private final static int INDEX_FAN_STATE = 21;
    private static TreadData sTreadData;

    public static class TreadData {
        private int mSafeLock;
        private int mOilPump;
        private int mPowerState;
        private int mCheck;
        private int mCurrentSpeed;
        private int mCurrentGrade;
        private int mMaxGrade;
        private int mErrorCode;
        private int mHeartRate;
        private int mPowerAd;
        private int mCheckGradeCountDown;
        private int mVersion;
        private int mIcId;
        private int mStepCount;
        private int mFanState = FanState.FAN_STATE_STOP;
        private float mDistance;
        private float mKCAL; //卡路里
        private int mTreadmillState;
        private int mCardIsValid;
        private int runTime = 0;
        private int mStepNumber;//步数

        private boolean isManager;//是否为管理员

        private boolean isVisitor;//是否为访客

        private UserInfo mUserInfo;

        private String mCardNo;

        public boolean isManager() {
            return isManager;
        }

        public void setManager(boolean manager) {
            isManager = manager;
        }

        public boolean isVisitor() {
            return isVisitor;
        }

        public void setVisitor(boolean visitor) {
            isVisitor = visitor;
        }

        public int isCardIsValid() {
            return mCardIsValid;
        }

        public void setCardIsValid(int cardIsValid) {
            mCardIsValid = cardIsValid;
        }

        public int getFanState() {
            return mFanState;
        }

        public void setFanState(byte fanState) {
            mFanState = fanState;
        }

        public int getSafeLock() {
            return mSafeLock;
        }

        public void setSafeLock(byte safeLock) {
            mSafeLock = safeLock;
        }

        public int getOilPump() {
            return mOilPump;
        }

        public void setOilPump(byte oilPump) {
            mOilPump = oilPump;
        }

        public int getPowerState() {
            return mPowerState;
        }

        public void setPowerState(byte powerState) {
            mPowerState = powerState;
        }

        public int getCheck() {
            return mCheck;
        }

        public void setCheck(byte check) {
            mCheck = check;
        }

        public int getStepNumber() {
            return mStepNumber;
        }

        public void setStepNumber(int stepNumber) {
            mStepNumber = stepNumber;
        }

        public int getCurrentSpeed() {
            LogUtils.d("data", "speed: " + (mCurrentSpeed & 0xFF));
            return mCurrentSpeed & 0xFF;
        }

        public void setCurrentSpeed(byte currentSpeed) {
            mCurrentSpeed = currentSpeed;
        }

        public int getCurrentGrade() {
            return mCurrentGrade;
        }

        public void setCurrentGrade(byte currentGrade) {
            mCurrentGrade = currentGrade;
        }

        public int getMaxGrade() {
            return mMaxGrade;
        }

        public void setMaxGrade(byte maxGrade) {
            mMaxGrade = maxGrade;
        }

        public int getErrorCode() {
            return mErrorCode;
        }

        public void setErrorCode(byte errorCode) {
            mErrorCode = errorCode;
        }

        public int getHeartRate() {
            return mHeartRate;
        }

        public void setHeartRate(byte heartRate) {
            mHeartRate = heartRate;
        }

        public int getPowerAd() {
            return mPowerAd;
        }

        public void setPowerAd(byte powerAd) {
            mPowerAd = powerAd;
        }

        public int getCheckGradeCountDown() {
            return mCheckGradeCountDown;
        }

        public void setCheckGradeCountDown(byte checkGradeCountDown) {
            mCheckGradeCountDown = checkGradeCountDown;
        }

        public int getVersion() {
            return mVersion;
        }

        public void setVersion(byte version) {
            mVersion = version;
        }

        public int getIcId() {
            return mIcId;
        }

        public void setIcId(byte icId) {
            mIcId = icId;
        }

        public int getStepCount() {
            return mStepCount;
        }

        public void setStepCount(byte stepCount) {
            mStepCount = stepCount;
        }

        public UserInfo getUserInfo() {
            return mUserInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            mUserInfo = userInfo;
        }

        /**
         * 计算距离增量
         * @return
         */
        public float measureDistanceIncrement() {
            float increment = (float) (getCurrentSpeed() / 36.0); //(Speed /10 * 1000 / 3600.0)
            return increment;
        }

        /**
         * 计算卡路里增量
         * @return
         */
        public float measureKcalIncrement() {
            float increment = (float) (0.0703 * (1 + getCurrentGrade() / 100.0 ) * measureDistanceIncrement());
            return increment;
        }

        /**
         * 单位时间内距离
         *
         * @return
         */
        public float getDistance() {
            return mDistance;
        }

        public void setDistance(float distance) {
            mDistance = distance;
        }

        public void setTreadmillState(int treadmillState) {
            mTreadmillState = treadmillState;
        }

        public int getTreadmillState() {
            return mTreadmillState;
        }

        /**
         * 跑步机是否正在运行
         *
         * @return
         */
        public boolean isRunning() {
            return mTreadmillState == BYTE_TREADMILL_RUNNING;
        }

        /**
         * 消耗的总卡路里
         *
         * @return
         */
        public float getKCAL() {
            return mKCAL;
        }

        public void setKCAL(float KCAL) {
            mKCAL = KCAL;
        }

        /**
         * 获取跑步时间
         *
         * @return
         */
        public int getRunTime() {
            return runTime;
        }

        /**
         * 设置跑步时间
         *
         * @param runTime
         */
        public void setRunTime(int runTime) {
            this.runTime = runTime;
        }

        public String getCardNo() {
            return mCardNo;
        }

        public void setCardNo(String cardNo) {
            mCardNo = cardNo;
        }

        /**
         * 清空上次跑步数据
         */
        public void reset() {
            mCurrentSpeed = 0;
            mCurrentGrade = 0;
            mDistance = 0;
            mKCAL = 0; //卡路里
            runTime = 0;
            mCardNo = "";
            mStepNumber = 0;
            mUserInfo = null;
            isManager = false;
            isVisitor = false;
            SerialPortUtil.setCardNoUnValid();//设置无效卡
        }

        public static class UserInfo {

            public int mRole;

            public String mUserName;

            public String mAvatar;

            public int mGender;

            public String mBraceletId;
        }

    }

    private static boolean checkSerialPortData(byte[] serialPortData) {
        if (serialPortData == null) {
            return false;
        }
        if (((byte) PROTOCOL_HEAD_1) == serialPortData[0] && ((byte) PROTOCOL_HEAD_2) == serialPortData[1]) {
            return true;
        }
        return false;
    }

    public static byte getKeyCodeFromSerialPort(byte[] serialPortData) {
        if (!checkSerialPortData(serialPortData)) {
            return LikingTreadKeyEvent.KEY_NONE;
        }
        return serialPortData[INDEX_KEY];
    }

    public static void updateTreadDataFromSerialPort(byte[] serialPortData) {
        if (!checkSerialPortData(serialPortData)) {
            return;
        }
        synchronized (getTreadInstance()) {
            getTreadInstance().setSafeLock(getIndexData(serialPortData, INDEX_SAFE_LOCK));
            getTreadInstance().setCheck(getIndexData(serialPortData, INDEX_CHECK));
            getTreadInstance().setCheckGradeCountDown(getIndexData(serialPortData, INDEX_CHECK_GRADE_COUNT_DOWN));
            getTreadInstance().setCurrentGrade(getIndexData(serialPortData, INDEX_CURRENT_GRADE));
            getTreadInstance().setCurrentSpeed(getIndexData(serialPortData, INDEX_CURRENT_SPEED));
            getTreadInstance().setErrorCode(getIndexData(serialPortData, INDEX_ERROR_CODE));
            getTreadInstance().setFanState(getIndexData(serialPortData, INDEX_FAN_STATE));
            getTreadInstance().setHeartRate(getIndexData(serialPortData, INDEX_HEART_RATE));
            getTreadInstance().setMaxGrade(getIndexData(serialPortData, INDEX_MAX_GRADE));
            getTreadInstance().setOilPump(getIndexData(serialPortData, INDEX_OIL_PUMP));
            getTreadInstance().setPowerAd(getIndexData(serialPortData, INDEX_POWER_AD));
            getTreadInstance().setPowerState(getIndexData(serialPortData, INDEX_POWER));
            getTreadInstance().setVersion(getIndexData(serialPortData, INDEX_VERSION));
            obtainCardNoOrStepNumber(serialPortData);
        }
    }

    public static TreadData getTreadInstance() {
        if (sTreadData == null) {
            sTreadData = new TreadData();
        }
        return sTreadData;
    }

    private static byte getIndexData(byte[] serialPortData, int index) {
        if (!checkSerialPortData(serialPortData)) {
            return BYTE_NONE;
        }
        return serialPortData[index];
    }

//    public static String byteToHexString(byte b) {
//        StringBuilder hexString = new StringBuilder("");
//        int v = b & 0xFF;
//        String hv = Integer.toHexString(v);
//        if (hv.length() < 2) {
//            hexString.append(0);
//        }
//        hexString.append(hv);
//        return hexString.toString();
//    }

    public final static class FanState {
        public final static int FAN_STATE_STOP = 0x00;
        public final static int FAN_STATE_LOW_SPEED = 0x01;
        public final static int FAN_STATE_HIGH_SPEED = 0x02;
    }

    public final static class SaveLock {
        public final static int SAVE_LOCK_CLOSE = 0x00;
        public final static int SAVE_LOCK_OPEN = 0x01;
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


    private final static int BYTE_NONE = 0x00;
    private final static int BTYE_CONTROL = (byte) 0xE2;

    public final static int BYTE_TREADMILL_STOP = 0x00;
    private final static int BYTE_TREADMILL_START = 0x01;
    private final static int BYTE_TREADMILL_RUNNING = 0x02;

    public final static int DEFAULT_SPEED = 0x0A;
    public final static int DEFAULT_GRADE = 0x01;

    private final static int BYTE_CARDNO_VALID = 0x01;
    private final static int BYTE_CARDNO_UNVALID = 0x00;

    /**
     * 设置速度,向串口发送数据
     *
     * @param speed
     */
    public static void setSpeedInRunning(int speed) {
        if (speed >= 0 && speed <= 200) {
            byte[] bytes = getControlBuffer();
            bytes[1] = (byte) (speed & 0xFF);
            bytes[6] = BYTE_TREADMILL_RUNNING;
            SerialPorManager.getInstance().sendMessage(bytes);
        }
    }


    /**
     * 设置坡度,向串口发送数据
     *
     * @param grade
     */
    public static void setGradeInRunning(int grade) {
        if (grade >= 0 && grade <= 25) {
            byte[] bytes = getControlBuffer();
            bytes[2] = (byte) grade;
            bytes[6] = BYTE_TREADMILL_RUNNING;
            SerialPorManager.getInstance().sendMessage(bytes);
        }
    }

    /**
     * 启动跑步机
     */
    public static void startTreadMill() {
        startTreadMill(DEFAULT_SPEED, DEFAULT_GRADE);
    }

    public static void startTreadMill(int speed, int grade) {
        byte[] bytes = getControlBuffer();
        bytes[1] = (byte) speed;
        bytes[2] = (byte) grade;
        bytes[6] = BYTE_TREADMILL_START;
        SerialPorManager.getInstance().sendMessage(bytes);
        sTreadData.setTreadmillState(BYTE_TREADMILL_RUNNING);
    }

    public static void restartTreadMill() {
        byte[] bytes = getControlBuffer();
        bytes[6] = BYTE_TREADMILL_RUNNING;
        SerialPorManager.getInstance().sendMessage(bytes);
        sTreadData.setTreadmillState(BYTE_TREADMILL_RUNNING);
    }

    /**
     * 暂停停止跑步机
     */
    public static void stopTreadMill() {
        byte[] bytes = getControlBuffer();
        bytes[1] = (byte) (0 & 0xFF);
        bytes[6] = BYTE_TREADMILL_STOP;
        SerialPorManager.getInstance().sendMessage(bytes);
        sTreadData.setTreadmillState(BYTE_TREADMILL_STOP);
    }

    /**
     * 是有效用户
     */
    public static void setCardNoValid() {
        byte[] bytes = getControlBuffer();
        bytes[8] = BYTE_CARDNO_VALID;
        SerialPorManager.getInstance().sendMessage(bytes);
    }

    /**
     * 无效用户
     */
    public static void setCardNoUnValid() {
        byte[] bytes = getControlBuffer();
        bytes[8] = BYTE_CARDNO_UNVALID;
        SerialPorManager.getInstance().sendMessage(bytes);
    }

    /**
     * 设置风扇
     */
    public static void setFanState(int fanstate) {
        byte[] bytes = getControlBuffer();
        bytes[9] = (byte) fanstate;
        SerialPorManager.getInstance().sendMessage(bytes);
    }

    private static byte[] getControlBuffer() {
        byte[] bytes = new byte[11];
        bytes[0] = BTYE_CONTROL;
        bytes[1] = (byte) sTreadData.getCurrentSpeed();
        bytes[2] = (byte) sTreadData.getCurrentGrade();
        bytes[3] = (byte) sTreadData.getCheck();
        bytes[4] = (byte) sTreadData.getOilPump();
        bytes[5] = BYTE_NONE;
        bytes[6] = (byte) sTreadData.getTreadmillState();
        bytes[7] = (byte) sTreadData.getIcId();
        bytes[8] = (byte) sTreadData.isCardIsValid();
        bytes[9] = (byte) sTreadData.getFanState();
        bytes[10] = BYTE_NONE;
        return bytes;
    }

    /**
     * 获取卡号或者步数
     * @param serialPortData
     */
    private static void obtainCardNoOrStepNumber(byte[] serialPortData) {
        if(checkSerialPortData(serialPortData)) {
            if (((byte) PROTOCOL_HEAD_CARDNO) == getKeyCodeFromSerialPort(serialPortData)) {//刷卡
                setValidCardNo(serialPortData);
            } else { //步数  其他情况下P14~P15为步数
                LogUtils.e("步数","14位:" + serialPortData[INDEX_KEY + 14] + "; 15位" + serialPortData[INDEX_KEY + 15]);
                int stepNumber = serialPortData[INDEX_KEY + 14] << 8 | serialPortData[INDEX_KEY + 15];
                sTreadData.setStepNumber(stepNumber);
            }
        }
    }

    /**
     * 获取卡号
     * @param serialPortData
     * @return
     */
    private static void setValidCardNo(byte[] serialPortData) {
        StringBuilder sb = new StringBuilder();
        String cardNo = "";
        try {
            sb.append(byteParseHex(serialPortData[INDEX_KEY + 17]));
            sb.append(byteParseHex(serialPortData[INDEX_KEY + 16]));
            sb.append(byteParseHex(serialPortData[INDEX_KEY + 15]));
            sb.append(byteParseHex(serialPortData[INDEX_KEY + 14]));
            cardNo = String.valueOf(Long.parseLong(sb.toString(), 16));
            if (!StringUtils.isEmpty(cardNo)) {
                sTreadData.setCardNo(cardNo);
            }
        } catch (Exception e) {
        }
    }

    public static String byteParseHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase();
    }
}
