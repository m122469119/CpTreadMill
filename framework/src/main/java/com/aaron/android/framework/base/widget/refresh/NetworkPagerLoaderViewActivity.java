package com.aaron.android.framework.base.widget.refresh;

import android.content.Context;
import android.os.Bundle;

import com.aaron.android.framework.R;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;

/**
 * Created on 16/2/23.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class NetworkPagerLoaderViewActivity<T extends BasePagerLoaderFragment> extends AppBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_pagerloader);
        launchFragment(R.id.layout_network_pager_loader_content, createNetworkPagerLoaderFragment(this));
    }

    public abstract T createNetworkPagerLoaderFragment(Context context);
}
