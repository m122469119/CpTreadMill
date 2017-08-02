package com.liking.treadmill.app;

import com.idescout.sql.SqlScoutServer;

/**
 * Created on 2017/08/02
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class DebugApplication extends LikingThreadMillApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SqlScoutServer.create(this, getPackageName());
    }
}
