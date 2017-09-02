package com.liking.treadmill.db;

import android.content.ContentValues;
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
    private static final String FIND_ADV_BY_ONE = "SELECT * FROM "
            + LikingPersistenceContract.TreadmillAdv.TABLE_NAME
            + " WHERE " + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_ID + "=?";

    private static final String FIND_ADV_BY_TYPE_AND_NEW = "SELECT * FROM "
            + LikingPersistenceContract.TreadmillAdv.TABLE_NAME
            + " WHERE " + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_TYPE + "=?"
            + " AND " + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_IS_DEFAULT + "=?";

    private static final String FIND_ADV_BY_TYPE_AND_TYPE_NEW = "SELECT * FROM "
            + LikingPersistenceContract.TreadmillAdv.TABLE_NAME
            + " WHERE " + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_TYPE + "=?"
            + " AND " + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_END_TIME + ">=?"
            + " AND " + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_IS_DEFAULT + "=?";

    private static final String INSERT_INTO_ADV_ONE = "INSERT INTO "
            + LikingPersistenceContract.TreadmillAdv.TABLE_NAME
            + " (" + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_TYPE + ", "
            + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_STAY_TIME + ", "
            + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_END_TIME + ", "
            + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_URL + ", "
            + LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_ID + ") "
            + " VALUE (?, ?, ?, ?, ?)";

    public AdvLocalDataSource(Context context) {
        mDatabaseManager = DatabaseManager.getInstance(new LikingDbHelper(context));
    }

    public List<AdvEntity> findAdvByType(String type, int isDefault) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        List<AdvEntity> advEntityList = new ArrayList<>();
        try {
            Cursor c = db.rawQuery(FIND_ADV_BY_TYPE_AND_NEW, new String[]{type,
                    String.valueOf(isDefault)});
            while (c.moveToNext()) {
                AdvEntity advEntity = loadAdvEntity(c);
                if (advEntity != null) {
                    advEntityList.add(advEntity);
                }
            }
        } finally {
            mDatabaseManager.closeDatabase();
        }
        return advEntityList;
    }


    public AdvEntity findAdvByOne(Long adv_id) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        AdvEntity advEntity = null;
        try {
            Cursor c = db.rawQuery(FIND_ADV_BY_ONE, new String[]{String.valueOf(adv_id)});
            if(c.moveToNext()) {
                advEntity = loadAdvEntity(c);
            }
        } finally {
          //  mDatabaseManager.closeDatabase();
        }
        return advEntity;
    }


    public List<AdvEntity> findAdvByTypeAndEndTime(String type, String endTime, int isDefault) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        List<AdvEntity> advEntityList = new ArrayList<>();
        try {
            LogUtils.e("sql", FIND_ADV_BY_TYPE_AND_TYPE_NEW + ";"+ type + ";" + endTime +";" +String.valueOf(isDefault));
            Cursor c = db.rawQuery(FIND_ADV_BY_TYPE_AND_TYPE_NEW, new String[]{type,
                    endTime,
                    String.valueOf(isDefault)});
            while (c.moveToNext()) {
                AdvEntity advEntity = loadAdvEntity(c);
                if (advEntity != null) {
                    advEntityList.add(advEntity);
                }
            }
            LogUtils.e("sql", "result:" + advEntityList.size());

        } finally {
            mDatabaseManager.closeDatabase();
        }
        return advEntityList;
    }

    public boolean insertAdvOne(AdvEntity advEntity) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        try {
            insertAdv(db, advEntity);
        } catch (Exception e) {
            return false;
        } finally {
            mDatabaseManager.closeDatabase();
        }
        return true;
    }

    public boolean insertAdvList(List<AdvEntity> advEntities) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        //    db.beginTransaction();
        try {
            for (AdvEntity entity : advEntities) {
                insertAdv(db, entity);
            }
            //  db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtils.e("insertAdvList", e.getMessage());
            return false;
        } finally {
            //   db.endTransaction();
            mDatabaseManager.closeDatabase();
        }
        LogUtils.e("insertAdvList", " success ");
        return true;
    }

    private void insertAdv(SQLiteDatabase db, AdvEntity entity) {
        ContentValues values = new ContentValues();
        values.put(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_TYPE, entity.getType());
        values.put(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_STAY_TIME, entity.getStaytime());
        values.put(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_END_TIME, entity.getEndtime());
        values.put(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_URL, entity.getUrl());
        values.put(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_ID, entity.getAdv_id());
        values.put(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_IS_DEFAULT, entity.getIsDefault());
        AdvEntity advByOne = findAdvByOne(entity.getAdv_id());
        if (advByOne != null) {
            LogUtils.e("insertAdvList", "advByOne != null");
            String selection = LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_ID + " = ?";
            String[] selectionArgs = {String.valueOf(entity.getAdv_id())};
            db.update(LikingPersistenceContract.TreadmillAdv.TABLE_NAME, values, selection, selectionArgs);
        } else {
            LogUtils.e("insertAdvList", "advByOne == null");
            db.insert(LikingPersistenceContract.TreadmillAdv.TABLE_NAME, null, values);
        }
    }


    public boolean deleteAll() {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        try {
            db.delete(LikingPersistenceContract.TreadmillAdv.TABLE_NAME, null, null);
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
            return false;
        } finally {
            mDatabaseManager.closeDatabase();
        }
        return true;
    }

    public boolean deleteAdvByTime(String endTime) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        try {
            db.delete(LikingPersistenceContract.TreadmillAdv.TABLE_NAME,
                    LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_END_TIME + ">?",
                    new String[]{endTime});
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
            return false;
        } finally {
            mDatabaseManager.closeDatabase();
        }
        return true;
    }

    public boolean deleteAdvByIsDefault(int isDefault){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        try {
            db.delete(LikingPersistenceContract.TreadmillAdv.TABLE_NAME,
                    LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_IS_DEFAULT + ">?",
                    new String[]{String.valueOf(isDefault)});
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
            return false;
        } finally {
            mDatabaseManager.closeDatabase();
        }
        return true;
    }


    public AdvEntity loadAdvEntity(Cursor c) {

        try {
            Long adv_id = c.getLong(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_ID));
            String type = c.getString(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_TYPE));
            String end_time = c.getString(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_END_TIME));
            int stay_time = c.getInt(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_STAY_TIME));
            String url = c.getString(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_URL));
            int isDefault = c.getInt(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillAdv.COLUMN_NAME_TREADMILL_ADV_IS_DEFAULT));

            if ("".equals(url) || url == null) {
                return null;
            }
            return new AdvEntity(url, type, end_time, stay_time, adv_id, isDefault);
        }catch (Exception e){
            LogUtils.e("info", e.getMessage());
            return  null;
        }
    }


}
