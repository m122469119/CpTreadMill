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

    public static final String THREADMILL_PATH_STORAGE_DATA_CACHE = DiskStorageManager.getInstance().getFilePath() + "data/";

    public static final String GOALSETTING_RUNTIME = "goalsettingruntime";
    public static final String GOALSETTING_KILOMETRE = "goalsettingkilometre";
    public static final String GOALSETTING_KCAL = "goalsettingkcal";
}
