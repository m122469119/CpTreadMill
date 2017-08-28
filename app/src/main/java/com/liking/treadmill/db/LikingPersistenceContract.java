package com.liking.treadmill.db;

import android.provider.BaseColumns;

public final class LikingPersistenceContract {

    private LikingPersistenceContract() {}

    public static abstract class TreadmillMember implements BaseColumns {
        public static final String TABLE_NAME = "treadmillmember";
        public static final String COLUMN_NAME_TREADMILL_MEMBER_ID = "memberid";//会员Id
        public static final String COLUMN_NAME_TREADMILL_BRACELET_ID = "braceletid";//会员手环
        public static final String COLUMN_NAME_TREADMILL_MEMBER_TYPE = "membertype";//会员类型
    }

    public static abstract class TreadmillAdv implements BaseColumns {
        public static final String TABLE_NAME = "treadmill_adv";
        public static final String COLUMN_NAME_TREADMILL_ADV_ID = "adv_id";  //adv_id
        public static final String COLUMN_NAME_TREADMILL_ADV_TYPE = "type";  //类型（home, login, quick_start, set_mode）
        public static final String COLUMN_NAME_TREADMILL_ADV_URL = "url"; //img type
        public static final String COLUMN_NAME_TREADMILL_ADV_END_TIME = "end_time"; // 失效时间
        public static final String COLUMN_NAME_TREADMILL_ADV_STAY_TIME = "stay_time";  // 显示时间
    }
}
