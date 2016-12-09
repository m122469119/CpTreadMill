package com.aaron.android.framework.base.widget.refresh;

/**
 * Created on 15/7/28.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public interface OnPullRefreshListener {

    /**
     * 下拉刷新
     */
    public void onPullDownRefresh();

    /**
     * 上拉刷新
     */
    public void onPullUpRefresh();
}
