package com.aaron.android.framework.base.widget.recycleview;

import android.view.View;

/**
 * Created on 15/8/27.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public interface OnRecycleViewItemClickListener {
    /**
     * recycleView Item点击事件
     * @param view View
     * @param position item 位置
     */
    void onItemClick(View view, int position);

    /**
     * recycleView Item长按事件
     * @param view View
     * @param position item 位置
     */
    boolean onItemLongClick(View view, int position);
}
