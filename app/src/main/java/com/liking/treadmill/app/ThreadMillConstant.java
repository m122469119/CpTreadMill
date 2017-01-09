package com.liking.treadmill.app;

import com.aaron.android.framework.library.storage.DiskStorageManager;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午10:38
 */

public class ThreadMillConstant {

    //1=>快速启动， 2=>设定目标， 3=>预设课程
    public static final int THREADMILL_MODE_SELECT_QUICK_START = 1;//快速启动
    public static final int THREADMILL_MODE_SELECT_GOAL_SETTING = 2;//目标设置

    //跑步数据缓存目录
    public static final String THREADMILL_PATH_STORAGE_DATA_CACHE = DiskStorageManager.getInstance().getFilePath() + "data/";

    public static final String GOALSETTING_RUNTIME = "goalsettingruntime";
    public static final String GOALSETTING_KILOMETRE = "goalsettingkilometre";
    public static final String GOALSETTING_KCAL = "goalsettingkcal";

    public static final String THREADMILL_SYSTEMSETTING = "systemsetting";

    //目标设置默认目标时间 (30min)
    public static final int GOALSETTING_DEFAULT_RUNNING_TIME = 30;
    //目标设置最小目标时间
    public static final int GOALSETTING_MIN_RUNNING_TIME = 1;

    //目标设置默认目标公里
    public static final int GOALSETTING_DEFAULT_KILOMETRE = 5;
    //目标设置最小目标公里
    public static final float GOALSETTING_MIN_KILOMETRE = 0.1f;
    //目标设置最大目标公里
    public static final float GOALSETTING_MAX_KILOMETRE = 50.0f;

    //目标设置默认目标卡路里
    public static final int GOALSETTING_DEFAULT_KCAL = 100;
    //目标设置最小目标卡路里
    public static final int GOALSETTING_MIN_KCAL = 10;
    //目标设置最大目标卡路里
    public static final int GOALSETTING_MAX_KCAL = 5000;

    //默认最大跑步时间 (240min)
    public static final int THREADMILL_DEFAULT_RUNNING_TIME = 240;
    //最大跑步时间上限 (360min)
    public static final int THREADMILL_MAX_RUNNING_TIME = 360;
    //最小跑步时间
    public static final int THREADMILL_MIN_RUNNING_TIME = 120;

    //默认待机时间 (120s)
    public static final int THREADMILL_DEFAULT_SYSTEM_STANBYTIME = 1 * 60;
}
