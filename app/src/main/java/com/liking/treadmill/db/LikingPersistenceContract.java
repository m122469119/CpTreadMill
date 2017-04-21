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
}
