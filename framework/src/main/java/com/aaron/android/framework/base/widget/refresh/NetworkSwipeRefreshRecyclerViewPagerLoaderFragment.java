package com.aaron.android.framework.base.widget.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.framework.R;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.RecyclerItemDecoration;

import java.util.List;

/**
 * Created on 16/8/24.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class NetworkSwipeRefreshRecyclerViewPagerLoaderFragment extends BaseSwipeRefreshPagerLoaderFragment {
    protected RecyclerView mRecyclerView;
    private BaseRecycleViewAdapter mRecyclerViewAdapter;
    private View mFooterView;

    @Override
    protected View createContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = createRecyclerView();
        if (mRecyclerView == null) {
            getDefaultRecyclerView();
        }
        mFooterView = inflater.inflate(R.layout.layout_network_footer, mRecyclerView, false);
        return mRecyclerView;
    }

    protected RecyclerView createRecyclerView() {
        return null;
    }

    private void getDefaultRecyclerView() {
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isSlideToBottom(recyclerView) && allowPullUp()) {
                    loadNextPage();
                }
            }
        });
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        return !ViewCompat.canScrollVertically(recyclerView, 1);
    }

    public void setRecyclerViewPadding(int left, int top, int right, int bottom) {
        mRecyclerView.setPadding(left, top, right, bottom);
        mRecyclerView.setClipToPadding(false);
    }

    public void setDivider(int drawableId) {
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL,
                drawableId));
    }

    @Override
    protected void updatePagerContentView(List pageDataList) {
        updateRecyclerView(pageDataList);
    }

    /**
     * 数据回调后，通知ListView更新
     *
     * @param listData DataListExtraResult
     */
    /**
     * 数据回调后，通知ListView更新
     *
     * @param listData DataListExtraResult
     */
    private void updateRecyclerView(List listData) {
        if (mRecyclerViewAdapter != null) {
            if (ListUtils.isEmpty(listData)) {
                if (isRequestHomePage()) {
                    clearListViewContent();
                    getStateView().setState(StateView.State.NO_DATA);
                } else {
                    mRecyclerViewAdapter.addFooterView(mFooterView);
                }
                setTotalPage(getCurrentPage());
            } else {
                getStateView().setState(StateView.State.SUCCESS);
                if (isRequestHomePage()) {
                    mRecyclerViewAdapter.setData(listData);
                } else {
                    mRecyclerViewAdapter.addData(listData);
                }
                mRecyclerViewAdapter.removeFooterView(mFooterView);
                mRecyclerViewAdapter.notifyDataSetChanged();
                setTotalPage(getCurrentPage() + 1);
            }
        }
    }

    protected void clearListViewContent() {
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.setData(null);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @return 获取ListView适配器
     */
    public RecyclerView.Adapter getRecyclerAdapter() {
        return mRecyclerViewAdapter;
    }

    /**
     * 设置ListView适配器Adapter
     *
     * @param adapter BaseAdapter
     */
    public void setRecyclerAdapter(BaseRecycleViewAdapter adapter) {
        mRecyclerViewAdapter = adapter;
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

}
