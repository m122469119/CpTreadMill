package com.liking.treadmill.app;

import android.support.v4.util.ArrayMap;

import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.liking.treadmill.R;
import com.liking.treadmill.entity.CategoryEntity;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午10:38
 */

public class ThreadMillConstant {

    public static ArrayMap<Integer, CategoryEntity> CATEGORYRESOURCE = new ArrayMap<>();

    static {
        //电影
        CATEGORYRESOURCE.put(1, new CategoryEntity(R.drawable.tab_category_movie_select, "电影"));
        //动漫
        CATEGORYRESOURCE.put(4, new CategoryEntity(R.drawable.tab_category_anime_select, "动漫"));
        //电视剧
        CATEGORYRESOURCE.put(2, new CategoryEntity(R.drawable.tab_category_tv_select, "电视剧"));
        //综艺
        CATEGORYRESOURCE.put(6, new CategoryEntity(R.drawable.tab_category_variety_select, "综艺"));
        //脱口秀
        CATEGORYRESOURCE.put(31, new CategoryEntity(R.drawable.tab_category_talkshow_select, "脱口秀"));
        //娱乐
        CATEGORYRESOURCE.put(7, new CategoryEntity(R.drawable.tab_category_entertainment_select, "娱乐"));
        //体育
        CATEGORYRESOURCE.put(17, new CategoryEntity(R.drawable.tab_category_sports_select, "体育"));
        //财经
        CATEGORYRESOURCE.put(24, new CategoryEntity(R.drawable.tab_category_finance_select, "财经"));
        //军事
        CATEGORYRESOURCE.put(28, new CategoryEntity(R.drawable.tab_category_military_select, "财经"));
        //科技
        CATEGORYRESOURCE.put(30, new CategoryEntity(R.drawable.tab_category_science_select, "财经"));
        //汽车
        CATEGORYRESOURCE.put(26, new CategoryEntity(R.drawable.tab_category_automobile_select, "财经"));

    }

    /**
     * 下载失败，允许更新的次数
     */
    public static final int THREADMILL_UPDATE_FAIL_COUNT = 5;

    /**
     * U盘目录
     */
    private static String USB_FILE_PATH = "/mnt/usb_storage";

    public static String USB_FILE_PATH_APK = USB_FILE_PATH + "/lk/LikingTreadMill.apk";

    //1=>快速启动， 2=>设定目标， 3=>预设课程
    public static final int THREADMILL_MODE_SELECT_QUICK_START = 1;//快速启动
    public static final int THREADMILL_MODE_SELECT_GOAL_SETTING = 2;//目标设置
    public static final int THREADMILL_MODE_SELECT_MARATHON = 3;//马拉松

    //跑步数据缓存目录
    public static final String THREADMILL_PATH_STORAGE_DATA_CACHE = DiskStorageManager.getInstance().getFilePath() + "data/";
    //用户登录状态缓存
    public static final String THREADMILL_PATH_STORAGE_LOGINOUT_CACHE = DiskStorageManager.getInstance().getFilePath() + "loginout/";

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
