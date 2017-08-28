package com.liking.treadmill.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.liking.treadmill.db.entity.AdvEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/8/28
 * @Version 1.0
 */
public class AdvLocalDataSource {
    private static final String TAG = "AdvLocalDataSource";


    private DatabaseManager mDatabaseManager;

    private static final String FIND_ADV_BY_TYPE = "SELECT * FROM " + LikingPersistenceContract.TreadmillAdv.TABLE_NAME
            + " WHERE " + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_TYPE  + "=?";



    public AdvLocalDataSource(Context context) {
        mDatabaseManager = DatabaseManager.getInstance(new LikingDbHelper(context));
    }

    public List<AdvEntity> findAdvByType(String type){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        Cursor c = db.rawQuery(FIND_ADV_BY_TYPE, new String[]{type});
        List<AdvEntity> advEntityList = new ArrayList<>();
        while (c.moveToNext()){
            AdvEntity advEntity = loadAdvEntity(c);
            if (advEntity != null) {
                advEntityList.add(advEntity);
            }
        }
        return advEntityList;
    }


    public boolean deleteAll() {
        boolean isSuccess = false;
        SQLiteDatabase db = null;
        try {
            db = mDatabaseManager.getWritableDatabase();
            db.delete(LikingPersistenceContract.TreadmillAdv.TABLE_NAME, null, null);
            isSuccess = true;
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        } finally {
            try {
                if (db != null) {
                    mDatabaseManager.closeDatabase();
                }
            } catch (Exception c) {
            }
        }
        return isSuccess;
    }


    public AdvEntity loadAdvEntity(Cursor c){
        Long adv_id = c.getLong(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_ID));
        String type = c.getString(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_TYPE));
        String end_time = c.getString(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_END_TIME));
        int stay_time = c.getInt(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_STAY_TIME));
        String url = c.getString(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_URL));
        return new AdvEntity(url, type, end_time, stay_time, adv_id);
    }

}
