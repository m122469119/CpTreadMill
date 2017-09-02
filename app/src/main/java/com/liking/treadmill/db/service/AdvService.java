package com.liking.treadmill.db.service;


import android.telecom.Call;
import com.aaron.android.framework.base.BaseApplication;
import com.liking.treadmill.db.AdvLocalDataSource;
import com.liking.treadmill.db.entity.AdvEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/8/29
 * @Version 1.0
 */
public class AdvService {

    ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    AdvLocalDataSource mLocalDataSource;

    private AdvService() {
        mLocalDataSource = new AdvLocalDataSource(BaseApplication.getInstance());
    }

    public static AdvService getInstance() {
        return AdvServiceFactory.instance();
    }

    private static class AdvServiceFactory {
        private static AdvService advService = new AdvService();

        public static AdvService instance() {
            return advService;
        }
    }

    public void findAdvByType(final String type, final int isDefault, final CallBack<List<AdvEntity>> callBack) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<AdvEntity> advByType = mLocalDataSource.findAdvByType(type, isDefault);
                callBack.onBack(advByType);
            }
        });
    }

    public void findAdvByTypeAndEndTime(final String type, final int isDefault, final String endTime, final CallBack<List<AdvEntity>> callBack) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<AdvEntity> advByTypeAndEndTime = mLocalDataSource.findAdvByTypeAndEndTime(type, endTime, isDefault);
                callBack.onBack(advByTypeAndEndTime);
            }
        });
    }

    public void insertAdvOne(final AdvEntity advEntity, final CallBack<Boolean> callBack) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callBack.onBack(mLocalDataSource.insertAdvOne(advEntity));
            }
        });
    }

    public void insertAdvList(final List<AdvEntity> advEntities, final CallBack<Boolean> callBack) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                boolean b = mLocalDataSource.insertAdvList(advEntities);
                callBack.onBack(b);
            }
        });
    }

    public void deleteAll(final CallBack<Boolean> callBack) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callBack.onBack(mLocalDataSource.deleteAll());
            }
        });
    }

    public void deleteAdvByTime(final String endTime, final CallBack<Boolean> callBack) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callBack.onBack(mLocalDataSource.deleteAdvByTime(endTime));
            }
        });
    }

    public void deleteAdvByIsDefault(final int type, final CallBack<Boolean> callBack){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callBack.onBack(mLocalDataSource.deleteAdvByIsDefault(type));
            }
        });
    }


    public interface CallBack<T> {
        void onBack(T t);
    }
}
