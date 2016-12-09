package com.aaron.android.framework.base.widget.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created on 15/7/30.
 *
 * @author ran.huang
 * @version 1.0.0
 * @param <T> BaseData
 */
public abstract class BaseRecycleViewHolder<T> extends RecyclerView.ViewHolder {

    protected int mPosition;
    /**
     * Constructor
     * @param itemView Item Root View
     */
    public BaseRecycleViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 绑定ItemView内容
     * @param object BaseData泛型
     */
    public abstract void bindViews(T object);

    /**
     * 是否为HeaderView
     * @return
     */
    public boolean isHeaderHolder() {
        return getItemViewType() == BaseRecycleViewAdapter.TYPE_HEADER;
    }

    /**
     * 是否为FooterView
     * @return
     */
    public boolean isFooterHolder() {
        return getItemViewType() == BaseRecycleViewAdapter.TYPE_FOOTER;
    }
}
