package com.liking.treadmill.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.liking.treadmill.R;
import com.liking.treadmill.fragment.AwaitActionFragment;
import com.liking.treadmill.fragment.UpdateFragment;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午5:51
 */

public class AwaitActionActivity extends LikingTreadmillBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchFragment(new AwaitActionFragment());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
