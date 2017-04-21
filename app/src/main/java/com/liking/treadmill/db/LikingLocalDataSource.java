package com.liking.treadmill.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liking.treadmill.db.entity.Member;
import java.util.List;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:46
 */

public class LikingLocalDataSource {

    private DatabaseManager mDatabaseManager;

    public LikingLocalDataSource(Context context) {
        mDatabaseManager = DatabaseManager.getInstance(new LikingDbHelper(context));
    }

    /**
     * 查询最后成员的MemberId
     * @return
     */
    public String queryLastMemberId() {
        SQLiteDatabase db = null;
        Member member = null;
        String defaultMemberId = "0";
        try {
            db = mDatabaseManager.getReadableDatabase();
            Cursor c = db.rawQuery("select * from " + LikingPersistenceContract.TreadmillMember.TABLE_NAME, null);
            if(c != null && c.getCount() > 0 && c.moveToLast()) {
                member = loadMember(c);
            }
            if(member != null) {
                defaultMemberId = member.getMemberId();
            }
        }catch (Exception e) {
        } finally {
            try {
                if(db != null) {
                    mDatabaseManager.closeDatabase();
                }
            }catch (Exception e){}
        }
        return defaultMemberId;
    }

    /**
     * 查询是否存会员
     *  @param braceletId
     */
    public Member queryMemberInfo(String braceletId) {
        Member member = null;
        SQLiteDatabase db = null;
        try {
            db = mDatabaseManager.getReadableDatabase();
            String[] projection = {
                    LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_MEMBER_ID,
                    LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_BRACELET_ID,
                    LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_MEMBER_TYPE
            };

            String selection = LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_BRACELET_ID + " = ?";
            String[] selectionArgs = {braceletId};

            Cursor c = db.query(
                    LikingPersistenceContract.TreadmillMember.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            member = loadMemberDatas(db, c);
        }catch (Exception e) {
        } finally {
            try {
                if(db != null) {
                    mDatabaseManager.closeDatabase();
                }
            }catch (Exception e){}
        }
        return member;
    }

    /**
     *
     * @param db
     * @param c
     */
    private Member loadMemberDatas(SQLiteDatabase db, Cursor c) {
        Member member = null;
        try {
            if (c != null && c.getCount() > 0 && c.moveToNext()) {
                member = loadMember(c);
            }
        }catch (Exception e) {
        } finally {
            if(c != null) {
                c.close();
            }
        }
        return member;
    }

    private Member loadMember(Cursor c) {
        Member member = new Member();
        String memberId = c.getString(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_MEMBER_ID));
        String braceletId = c.getString(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_BRACELET_ID));
        int memberType = c.getInt(c.getColumnIndexOrThrow(LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_MEMBER_TYPE));
        member.setMemberId(memberId);
        member.setBraceletId(braceletId);
        member.setMemberType(memberType);
        return member;
    }

    /**
     * 添加、删除会员
     *
     * @param members
     */
    public void updateMemberList(List<Member> members) {
        SQLiteDatabase db = null;
        try {
            db = mDatabaseManager.getWritableDatabase();
            db.beginTransaction();

            for (Member data : members) {
                if(Member.BRACELET_OPERATE_NEW == data.getBraceletOperate()) { //新增
                    ContentValues values = new ContentValues();
                    values.put(LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_MEMBER_ID, data.getMemberId());
                    values.put(LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_BRACELET_ID, data.getBraceletId());
                    values.put(LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_MEMBER_TYPE, data.getMemberType());

                    db.insert(LikingPersistenceContract.TreadmillMember.TABLE_NAME, null, values);
                } else if(Member.BRACELET_OPERATE_DELETE == data.getBraceletOperate()) { //删除
                    String selection = LikingPersistenceContract.TreadmillMember.COLUMN_NAME_TREADMILL_BRACELET_ID + " = ?";
                    String[] selectionArgs = {data.getBraceletId()};

                    db.delete(LikingPersistenceContract.TreadmillMember.TABLE_NAME, selection, selectionArgs);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }catch (Exception e) {

        } finally {
            try {
                if(db != null) {
                    mDatabaseManager.closeDatabase();
                }
            }catch (Exception e){}
        }
    }

    /**
     * 删除所有场馆会员
     */
    public void deleteAllMembers() {
        SQLiteDatabase db = null;
        try {
            db = mDatabaseManager.getWritableDatabase();
            db.delete(LikingPersistenceContract.TreadmillMember.TABLE_NAME, null, null);
        }catch (Exception e) {
        }finally {
            try {
                if(db != null) {
                    mDatabaseManager.closeDatabase();
                }
            }catch (Exception c) {}
        }
    }

    public interface LoadDatasCallback {

        void onDatasLoaded(List<Member> datas);

        void onDataNotAvailable();
    }

    public interface GetDataCallback {

        void onTaskLoaded(Member task);

        void onDataNotAvailable();
    }
}
