package com.liking.treadmill.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;

/**
 * Created on 16/12/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingTreadmillBaseActivity extends AppBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }
}
