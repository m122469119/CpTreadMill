package com.aaron.android.framework.base.widget.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.R;
import com.aaron.android.framework.base.ui.BaseFragment;

import java.util.List;

/**
 * Created on 16/8/25.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class BasePagerLoaderFragment extends BaseFragment {
    private FrameLayout mRootView;
    private boolean mIsLoading = false;
    private PullMode mPullMode = PullMode.PULL_DOWN;
    private Pager mPager = new Pager();
    private StateView mStateView;
    private boolean isFirstLoad = true;
    private boolean mIsRefreshViewPull = false;
    protected boolean mIsNeedFooterView = true;
    private final View.OnClickListener mRefreshClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mStateView.setState(StateView.State.LOADING);
            loadHomePage();
        }
    };

    /**
     * 创建一个分页的ContentView
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    protected abstract View createContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    /**
     * 请求分页数据
     *
     * @param page 请求第几页
     */
    protected abstract void requestData(int page);

    /**
     * 初始化相关数据和View（例如Adapter的初始化和设置）
     */
    protected abstract void initViews();

    /**
     * 更新上拉下拉刷新控件的刷新状态
     *
     * @param refresh true：刷新  false：取消刷新
     */
    protected abstract void updateRefreshViewState(boolean refresh);

    /**
     * 初始化上拉下拉刷新控件,主要是配置上拉下拉监听以及与ContentView的关联代码
     *
     * @param contentView
     * @param inflater
     */
    protected abstract void setupRefreshLayout(View contentView, LayoutInflater inflater);

    /**
     * 填充分页控件数据
     *
     * @param pageDataList List数据集合
     */
    protected abstract void updatePagerContentView(List pageDataList);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mPager.setTotal(Integer.MAX_VALUE);
            mRootView = (FrameLayout) inflater.inflate(R.layout.layout_pager_loader, null, false);
            initStatView();
            setupRefreshLayout(createContentView(inflater, container, savedInstanceState), inflater);
            setPullMode(mPullMode);
            initViews();
            mStateView.setState(StateView.State.LOADING);
            loadHomePage();
        }
        return mRootView;
    }

    private void initStatView() {
        mStateView = (StateView) mRootView.findViewById(R.id.state_view);
        TextView failRefreshText = (TextView) mRootView.findViewById(R.id.text_view_fail_refresh);
        failRefreshText.setOnClickListener(mRefreshClickListener);
    }

    public void setRefreshViewPull(boolean refreshViewPull) {
        mIsRefreshViewPull = refreshViewPull;
    }

    /**
     * 设置上拉下拉属性
     *
     * @param pullMode 上拉下拉类型
     */
    public void setPullMode(PullMode pullMode) {
        mPullMode = pullMode;
    }

    public PullMode getPullMode() {
        return mPullMode;
    }
    public boolean allowPullUp() {
        return mPullMode == PullMode.PULL_BOTH || mPullMode == PullMode.PULL_UP;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }


    /**
     * @return 获取当前请求页
     */
    protected int getCurrentPage() {
        return mPager.getCurrent();
    }

    /**
     * @return 当前请求的是否是第一页数据
     */
    public boolean isRequestHomePage() {
        return getCurrentPage() == Pager.HOME_PAGE;
    }

    /**
     * 请求page页数据
     *
     * @param page page
     */
    protected void loadPageData(int page) {
        LogUtils.d(TAG, "loadPageData --- page: " + page);
//        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
//            LogUtils.i(TAG, "Page Loader error, network is not available!");
//            if (page == mPager.getStart()) {
//                if (isFirstLoad) {
//                    mStateView.setState(StateView.State.FAILED);
//                } else {
//                    PopupUtils.showToast(R.string.network_no_connection);
//                }
//
//            } else {
//                PopupUtils.showToast(R.string.network_no_connection);
//            }
//            requestFinished();
//            return;
//        }
        if (isLoading()) {
            return;
        }
        setLoading(true);
        mPager.setCurrent(page);
        if (isRequestHomePage() && !mIsRefreshViewPull) {
            mStateView.setState(StateView.State.LOADING);
        } else {
            updateRefreshViewState(true);
        }
        mIsRefreshViewPull = false;
        requestData(page);
    }



    /**
     * 如果分页数据加载失败，在请求错误回调中重置当前page值
     */
    public void requestFailure() {
        requestFinished();
        if (!isRequestHomePage()) {
            mPager.setCurrent(mPager.getCurrent() - 1);
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
    }

    public void requestSuccess() {
        if (isRequestHomePage()) {
            mStateView.setState(StateView.State.SUCCESS);
        }
        requestFinished();
        isFirstLoad = false;
    }


    private void requestFinished() {
        updateRefreshViewState(false);
        setLoading(false);
    }

    /**
     * 请求第一页数据
     */
    protected void loadHomePage() {
        loadPageData(Pager.HOME_PAGE);
    }

    /**
     * 设置接口返回总页数
     *
     * @param totalPage totalPage
     */
    protected void setTotalPage(int totalPage) {
        mPager.setTotal(totalPage);
    }

    /**
     * 请求下一页数据
     */
    protected void loadNextPage() {
        if (hasNextPage()) {
            loadPageData(mPager.next());
        }
    }

    protected boolean hasNextPage() {
        return mPager.hasNext();
    }

    public StateView getStateView() {
        return mStateView;
    }

    public void hideFootView() {
        mIsNeedFooterView = false;
    }
}
