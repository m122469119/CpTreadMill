package com.liking.treadmill.treadcontroller;

/**
 * Created on 16/12/13.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingTreadKeyEvent {

    public final static byte KEY_NONE = 0x00; //无按键
    public final static byte KEY_START = 0x01; //开始
    public final static byte KEY_STOP = 0x02; //停止
    public final static byte KEY_PAUSE = 0x03; //暂停
    public final static byte KEY_RESET = 0x04; //复位
    public final static byte KEY_MODE = 0x05; //模式
    public final static byte KEY_PROGRAM = 0x06; //程序
    public final static byte KEY_GRADE_PLUS = 0x07; //坡度+
    public final static byte KEY_GRADE_REDUCE = 0x08; //坡度-
    public final static byte KEY_SPEED_PLUS = 0x09; //速度+
    public final static byte KEY_SPEED_REDUCE = 0x10; //速度-
    public final static byte KEY_ROTATE_GRADE_PLUS = 0x11; //坡度+（旋转轮）
    public final static byte KEY_ROTATE_GRADE_REDUCE = 0x12; //坡度-（旋转轮）
    public final static byte KEY_ROTATE_SPEED_PLUS = 0x13; //坡度-（旋转轮）
    public final static byte KEY_ROTATE_SPEED_REDUCE = 0x14;//速度-（旋转轮）
    public final static byte KEY_GRADE_3 = 0x15;//坡度3
    public final static byte KEY_GRADE_6 = 0x16;//坡度6
    public final static byte KEY_GRADE_9 = 0x17;//坡度9
    public final static byte KEY_GRADE_12 = 0x18;//坡度9
    public final static byte KEY_GRADE_15 = 0x19;//坡度15
    public final static byte KEY_SPEED_3 = 0x20;//坡度15
    public final static byte KEY_SPEED_6 = 0x21;//速度6
    public final static byte KEY_SPEED_9 = 0x22;//速度6
    public final static byte KEY_SPEED_12 = 0x23;//速度12
    public final static byte KEY_SPEED_15 = 0x24;//速度12
    public final static byte KEY_GRADE_CONFIRM = 0x25;//坡度确认（旋转轮）
    public final static byte KEY_SPEED_CONFIRM = 0x26;//坡度确认（旋转轮）
    public final static byte KEY_LAST = 0x27;//上一曲/视频/图片
    public final static byte KEY_NEXT = 0x28;//上一曲/视频/图片
    public final static byte KEY_PLAY_PAUSE_MEDIA = 0x29;//播放/暂停当前节目
    public final static byte KEY_STOP_MEDIA = 0x30;//停止当前节目
    public final static byte KEY_MULTIMEDIA = 0x31;//多媒体
    public final static byte KEY_VOL_PLUS = 0x32;//音量+
    public final static byte KEY_VOL_REDUCE = 0x33;//音量-
    public final static byte KEY_AVIN = 0x34;//外部视频输入（AVin）
    public final static byte KEY_AWAKEN = 0x35; //休眠（AWAKEN）
    public final static byte KEY_COOLDOWN = 0x36; //cooldown（AWAKEN）
    public final static byte KEY_FAN = 0x37; //风扇（FAN）
    public final static byte KEY_RETURN = 0x38; //返回（RETURN）
    public final static byte KEY_SET = 0x39;//参数设置（SET）
    public final static byte KEY_CARD = 0x40;//刷卡
    public final static byte KEY_HAND_SHANK_SPEED_PLUS = 0x41;//手柄速度+
    public final static byte KEY_HAND_SHANK_SPEED_REDUCE = 0x42;//手柄速度-
    public final static byte KEY_HAND_SHANK_GRADE_PLUS = 0x43;//手柄坡度+
    public final static byte KEY_HAND_SHANK_GRADE_REDUCE = 0x44;//手柄坡度-
    public final static byte KEY_MODE_MODE = 0x55;//音量页面

    /**
     * 以下是产品定义的组合键
     */
    public final static byte KEY_PGR_PGR_SPEED_REDUCE = 0x50; //PGR+ PGR+SPEED-
    public final static byte KEY_SPEED_PLUS_SPEED_REDUCE = 0x51; //PGR+ SPEED++ SPEED-
    public final static byte KEY_PGR_SPEED_COOLDOWN = 0x52; //PGR+ SPEED++ COOLDWON //升降自检(坡度)
    public final static byte KEY_PGR_PAUSE_STOP = 0x53; //PGR+ PAUSE+ STOP
    public final static byte KEY_PGR_START = 0x54; //PGR+ START
//    public final static String KEY_MODE_MODE = "55"; //MODE+ MODE
    public final static String KEY_INCLINE_INCLINE_REDUCE_INCLINE_PLUS = "56"; //INCLINE++INCLINE-+ INCLINE+
    public final static String KEY_SPEED_SPEED_REDUCE_SPEED_PLUS = "57"; //SPEED++ SPEED-+ SPEED+
    public final static byte KEY_VOL_VOL_REDUCE_VOL_PLUS = 0x58; //VOL+ +  VOL- +  VOL+

}
