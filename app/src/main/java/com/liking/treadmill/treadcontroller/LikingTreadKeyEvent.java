package com.liking.treadmill.treadcontroller;

/**
 * Created on 16/12/13.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingTreadKeyEvent {

    public final static String KEY_NONE = "00"; //无按键
    public final static String KEY_START = "01"; //开始
    public final static String KEY_STOP = "02"; //停止
    public final static String KEY_PAUSE = "03"; //暂停
    public final static String KEY_RESET = "04"; //复位
    public final static String KEY_MODE = "05"; //模式
    public final static String KEY_PROGRAM = "06"; //程序
    public final static String KEY_GRADE_PLUS = "07"; //坡度+
    public final static String KEY_GRADE_REDUCE = "08"; //坡度-
    public final static String KEY_SPEED_PLUS = "09"; //速度+
    public final static String KEY_SPEED_REDUCE = "10"; //速度-
    public final static String KEY_ROTATE_GRADE_PLUS = "11"; //坡度+（旋转轮）
    public final static String KEY_ROTATE_GRADE_REDUCE = "12"; //坡度-（旋转轮）
    public final static String KEY_ROTATE_SPEED_PLUS = "13"; //坡度-（旋转轮）
    public final static String KEY_ROTATE_SPEED_REDUCE = "14";//速度-（旋转轮）
    public final static String KEY_GRADE_3 = "15";//坡度3
    public final static String KEY_GRADE_6 = "16";//坡度6
    public final static String KEY_GRADE_9 = "17";//坡度9
    public final static String KEY_GRADE_12 = "18";//坡度9
    public final static String KEY_GRADE_15 = "19";//坡度15
    public final static String KEY_SPEED_3 = "20";//坡度15
    public final static String KEY_SPEED_6 = "21";//速度6
    public final static String KEY_SPEED_9 = "22";//速度6
    public final static String KEY_SPEED_12 = "23";//速度12
    public final static String KEY_SPEED_15 = "24";//速度12
    public final static String KEY_GRADE_CONFIRM = "25";//坡度确认（旋转轮）
    public final static String KEY_SPEED_CONFIRM = "26";//坡度确认（旋转轮）
    public final static String KEY_LAST = "27";//上一曲/视频/图片
    public final static String KEY_NEXT = "28";//上一曲/视频/图片
    public final static String KEY_PLAY_PAUSE_MEDIA = "29";//播放/暂停当前节目
    public final static String KEY_STOP_MEDIA = "30";//停止当前节目
    public final static String KEY_MULTIMEDIA = "31";//多媒体
    public final static String KEY_VOL_PLUS = "32";//音量+
    public final static String KEY_VOL_REDUCE = "33";//音量-
    public final static String KEY_AVIN = "34";//外部视频输入（AVin）
    public final static String KEY_AWAKEN = "35"; //休眠（AWAKEN）
    public final static String KEY_COOLDOWN = "36"; //休眠（AWAKEN）
    public final static String KEY_FAN = "37"; //风扇（FAN）
    public final static String KEY_RETURN = "38"; //返回（RETURN）
    public final static String KEY_SET = "39";//参数设置（SET）
    public final static String KEY_CARD = "40";//刷卡

    /**
     * 以下是产品定义的组合键
     */
    public final static String KEY_PGR_PGR_SPEED_REDUCE = "50"; //PGR+ PGR+SPEED-
    public final static String KEY_SPEED_PLUS_SPEED_REDUCE = "51"; //PGR+ SPEED++ SPEED-
    public final static String KEY_PGR_SPEED_COOLDOWN = "52"; //PGR+ SPEED++ COOLDWON
    public final static String KEY_PGR_PAUSE_STOP = "53"; //PGR+ PAUSE+ STOP
    public final static String KEY_PGR_START = "54"; //PGR+ START
    public final static String KEY_MODE_MODE = "55"; //MODE+ MODE
    public final static String KEY_INCLINE_INCLINE_REDUCE_INCLINE_PLUS = "56"; //INCLINE++INCLINE-+ INCLINE+
    public final static String KEY_SPEED_SPEED_REDUCE_SPEED_PLUS = "57"; //SPEED++ SPEED-+ SPEED+
    public final static String KEY_VOL_VOL_REDUCE_VOL_PLUS = "58"; //VOL+ +  VOL- +  VOL+

}
