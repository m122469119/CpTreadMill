package com.aaron.android.framework.utils;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * ListView相关工具方法
 * @author xiaoming.liu
 * @version 1.0.0
 */
public class ListViewUtils {

    /**
     * 是否允许页滚动
     * @param firstVisibleItem 第一项位置
     * @param visibleItemCount 可见项数量
     * @param totalItemCount 总数据量
     * @return 是否允许页滚动
     */
    public static boolean isPageScrollEnable(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        return (totalItemCount >= visibleItemCount) && ((totalItemCount - firstVisibleItem) <= visibleItemCount);
    }

    /**
     * @param firstVisibleItem 第一个显示项位置
     * @param visibleItemCount 当前显示数量
     * @param totalItemCount ListView总个数
     * @param lastItem 倒数项数
     * @return 是否为倒数第几项显示
     */
    public static boolean isLastVisibleItem(int firstVisibleItem, int visibleItemCount, int totalItemCount, int lastItem) {
        return (totalItemCount >= visibleItemCount) && visibleItemCount + firstVisibleItem >= totalItemCount - lastItem;
    }

    public static int getListViewHeight(ListView listView) {
        int totalHeight = 0;
        ListAdapter adapter = listView.getAdapter();
        if (null != adapter) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listView);
                if (null != listItem) {
                    // 注意ListView子项必须为LinearLayout才能调用该方法
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            return totalHeight
                    + (listView.getDividerHeight() * (listView.getCount() - 1));
        }
        return 0;
    }

    public static int getRealClickPosition(long id) {
        int index = (int) id;
        if (index < 0) {
            return -1;
        }
        return index;
    }
}
