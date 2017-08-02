package com.liking.treadmill.activity;

import android.os.Bundle;

import com.idescout.sql.SqlScoutServer;

/**
 * Created on 2017/08/02
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class DebugHomeActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlScoutServer.create(this, getPackageName());
    }
}
