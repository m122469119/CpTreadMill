package com.liking.treadmill.utils;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.thread.TaskScheduler;
import com.liking.treadmill.app.LikingThreadMillApplication;
import com.liking.treadmill.db.MemberLocalDataSource;
import com.liking.treadmill.db.entity.Member;
import com.liking.treadmill.storge.Preference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/04/24
 * desc: 成员场馆工具类
 *
 * @author: chenlei
 * @version:1.0
 */

public class MemberUtils {

    private List<Member> mMemberListCache = new ArrayList<>();//成员列表内存缓存

    private MemberLocalDataSource mDataSource = null; //成员列表数据库缓存

    private MemberUtils(){
        mDataSource = new MemberLocalDataSource(LikingThreadMillApplication.getInstance());
    }

    private static class MemberDataHelper {
        public static MemberUtils INSTANCE = new MemberUtils();
    }

    public static MemberUtils getInstance() {
        return MemberDataHelper.INSTANCE;
    }

    public List<Member> getMembersFromMemory() {
       return mMemberListCache;
    }

    public MemberLocalDataSource getMembersFromLocal() {
        return mDataSource;
    }

    /**
     * 获取最后成员的MemberID
     * @return
     */
    public String getLastMemberId() {
        int cacheSize = mMemberListCache.size();
        if(cacheSize > 0) {
            Member member = mMemberListCache.get(cacheSize - 1);
            if(member != null && !StringUtils.isEmpty(member.getMemberId())) {
                return member.getMemberId();
            }
        }
        return Preference.getLastMemberId();
    }

    /**
     * 更新内存场馆会员列表
     */
    public void updateMembersFromMemory(Member m) {
        if(!mMemberListCache.contains(m)) {
            mMemberListCache.add(m);
        }
    }

    /**
     * 更新本地场馆会员列表
     */
    public void updateMembersFromLocal() {
        TaskScheduler.execute(new Runnable() { // 往数据库添加数据
            @Override
            public void run() {  //child thread
                if (!mMemberListCache.isEmpty() && mDataSource != null) {
                    LogUtils.e("sql", "addAllMembersForSql");
                    mDataSource.updateMemberList(mMemberListCache);
                }
            }
        }, new Runnable() {
            @Override
            public void run() {  //main thread
                int cacheSize = mMemberListCache.size();
                if (cacheSize > 0) {
                    Member member = mMemberListCache.get(cacheSize - 1);
                    Preference.setLastMemberId(member.getMemberId());
                }
                LogUtils.e("sql", "clearAllMembersForMemory");
                mMemberListCache.clear();
            }
        });
    }

    /**
     * 删除本地场馆会员列表
     */
    public void deleteMembersFromLocal() {
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() { //child thread
                if (mDataSource != null) {
                    mDataSource.deleteAllMembers();
                }
            }
        }, new Runnable() {
            @Override
            public void run() { //main thread
                Preference.setLastMemberId("0");
            }
        });
    }
}
