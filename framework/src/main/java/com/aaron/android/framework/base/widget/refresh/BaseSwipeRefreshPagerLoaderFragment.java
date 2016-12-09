package com.aaron.android.framework.base.widget.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.R;

/**
 * Created on 16/8/24.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class BaseSwipeRefreshPagerLoaderFragment extends BasePagerLoaderFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View mContentView;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void updateRefreshViewState(boolean refresh) {
        if (mRefreshLayout == null) {
            return;
        }
        if (refresh) {
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    public void setRefreshColor(int... colorResId) {
        mRefreshLayout.setColorSchemeResources(colorResId);
    }

    @Override
     protected void setupRefreshLayout(View view, LayoutInflater inflater) {
        View swipeRefreshLayout = inflater.inflate(R.layout.view_swipe_refresh, null, false);
        mRefreshLayout = (SwipeRefreshLayout) swipeRefreshLayout.findViewById(R.id.swipelayout_pager_load);
        mRefreshLayout.setOnRefreshListener(this);
        setRefreshColor(R.color.default_swipe_refresh_color);
        if (view == null) {
            throw new IllegalArgumentException("refresh view content must not be null!");
        }
        if (mContentView != null) {
            mRefreshLayout.removeView(mContentView);
        }
        mContentView = view;
        ViewGroup.LayoutParams contentLayoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRefreshLayout.addView(mContentView, contentLayoutParams);
        getStateView().setSuccessView(swipeRefreshLayout);
    }

    protected void setBackground(int colorResId) {
        mRefreshLayout.setBackgroundResource(colorResId);
    }

    @Override
    public void setPullMode(PullMode pullMode) {
        super.setPullMode(pullMode);
        mRefreshLayout.setEnabled(!(pullMode == PullMode.PULL_NONE));
    }

    @Override
    public void onRefresh() {
        if (isLoading()) {
            return;
        }
        setRefreshViewPull(true);
        loadHomePage();
    }
}
