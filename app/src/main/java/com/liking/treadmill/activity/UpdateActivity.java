package com.liking.treadmill.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.aaron.android.framework.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.liking.treadmill.R;
import com.liking.treadmill.fragment.UpdateFragment;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午5:51
 */

public class UpdateActivity extends LikingTreadmillBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update);
        launchFragment(new UpdateFragment());
    }

}
