package com.liking.treadmill.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:46
 */

public class LikingDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "LikingTreadmill.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LikingPersistenceContract.TreadmillMember.TABLE_NAME + " (" +
                    LikingPersistenceContract.TreadmillMember._ID + TEXT_TYPE + " PRIMARY KEY," +
                    LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_MEMBER_ID + TEXT_TYPE + COMMA_SEP +
                    LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_BRACELET_ID + TEXT_TYPE + COMMA_SEP +
                    LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_MEMBER_TYPE + INTEGER_TYPE  +
                    " )";

    public LikingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
