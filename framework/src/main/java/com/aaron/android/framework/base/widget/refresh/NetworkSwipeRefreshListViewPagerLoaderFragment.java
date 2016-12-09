package com.aaron.android.framework.base.widget.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.framework.R;
import com.aaron.android.framework.base.widget.listview.BaseListAdapter;
import com.aaron.android.framework.utils.ListViewUtils;

import java.util.List;

/**
 * Created on 16/10/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public abstract class NetworkSwipeRefreshListViewPagerLoaderFragment extends BaseSwipeRefreshPagerLoaderFragment {
    protected ListView mListView;
    private BaseListAdapter mListAdapter;
    private View mFooterView;

    @Override
    protected View createContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListView = createListView();
        if (mListView == null) {
            getDefaultListView(inflater);
        }
        mFooterView = inflater.inflate(R.layout.layout_network_footer, mListView, false);
        mFooterView.findViewById(R.id.footer_split_line).setVisibility(View.VISIBLE);
        return mListView;
    }

    private void getDefaultListView(LayoutInflater inflater) {
        mListView = (ListView) inflater.inflate(R.layout.common_listview, null, false);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!isLoading() && ListViewUtils.isLastVisibleItem(firstVisibleItem, visibleItemCount, totalItemCount, 3)
                        && allowPullUp()) {
                    loadNextPage();
                }
            }
        });
    }

    protected ListView createListView() {
        return null;
    }

    @Override
    protected void updatePagerContentView(List pageDataList) {
        updateListView(pageDataList);
    }

    /**
     * 数据回调后，通知ListView更新
     *
     * @param listData DataListExtraResult
     */
    public void updateListView(List listData) {
        if (mListAdapter != null) {
            if (ListUtils.isEmpty(listData)) {
                if (isRequestHomePage()) {
                    clearListViewContent();
                    getStateView().setState(StateView.State.NO_DATA);
                } else {
                    setTotalPage(getCurrentPage());
                    if (mIsNeedFooterView) {
                        mListView.addFooterView(mFooterView, null, false);
                    }
                }
            } else {
                getStateView().setState(StateView.State.SUCCESS);
                if (isRequestHomePage()) {
                    mListAdapter.setData(listData);
                } else {
                    mListAdapter.addData(listData);
                }
                setTotalPage(getCurrentPage() + 1);
                if (mIsNeedFooterView) {
                    mListView.removeFooterView(mFooterView);
                }
                mListAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void clearListViewContent() {
        if (mListAdapter != null) {
            mListAdapter.setData(null);
            mListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @return 获取ListView适配器
     */
    public BaseListAdapter getListAdapter() {
        return mListAdapter;
    }

    /**
     * 设置ListView适配器Adapter
     *
     * @param adapter BaseAdapter
     */
    public void setListAdapter(BaseListAdapter adapter) {
        mListAdapter = adapter;
        mListView.setAdapter(mListAdapter);
    }
}
