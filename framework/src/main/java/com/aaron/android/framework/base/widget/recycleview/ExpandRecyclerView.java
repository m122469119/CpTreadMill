package com.aaron.android.framework.base.widget.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 16/9/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class ExpandRecyclerView extends RecyclerView {
    private BaseRecycleViewAdapter mAdapter;

    public ExpandRecyclerView(Context context) {
        super(context);
    }

    public ExpandRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void addHeaderView(View view) {
        mAdapter.addHeaderView(view);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof BaseRecycleViewAdapter) {
            mAdapter = (BaseRecycleViewAdapter) getAdapter();
        }
    }
}
