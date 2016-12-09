package com.aaron.android.framework.base.widget.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aaron.android.codelibrary.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 15/5/26.
 *
 * @author HuangRan
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    private List<T> mList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    protected abstract BaseViewHolder<T> createViewHolder(ViewGroup parent);

    protected View buildItemView() {
        return null;
    }

    public BaseListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     *
     * @return 获取上下文资源
     */
    public Context getContext() {
        return mContext;
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    /**
     *
     * @return 数据列表
     */
    public List<T> getDataList() {
        return mList;
    }

    /**
     * 设置数据
     * @param list List<T>
     */
    public void setData(List<T> list) {
        if (list == null) {
            mList = new ArrayList<>();
        } else {
            mList = new ArrayList<>(list);
        }
    }

    /**
     * 设置数据并且通知更新
     * @param list List<T>
     */
    public void setDataAndNotifyChanged(List<T> list) {
        setData(list);
        notifyDataSetChanged();
    }

    /**
     * 叠加数据
     * @param list List<T>
     */
    public void addData(List<T> list) {
        if (isEmptyList(list)) {
            return;
        }
        mList.addAll(list);
    }

    /**
     * 叠加数据并且通知更新
     * @param list List<T>
     */
    public void addDataAndNotifyChanged(List<T> list) {
        addData(list);
        notifyDataSetChanged();
    }

    /**
     * 移除数据
     * @param position 数据索引
     */
    public void remove(int position) {
        if (mList.size() > position) {
            mList.remove(position);
        }
    }
    /**
     * 移除数据并且通知更新
     * @param position 数据索引
     */
    public void removeAndNotifyChanged(int position) {
        if (mList.size() > position) {
            mList.remove(position);
        }
        notifyDataSetChanged();
    }

    private boolean isEmptyList(List<T> list) {
        return ListUtils.isEmpty(list);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T data = mList.get(position);
        BaseViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = createViewHolder(parent);
            viewHolder.mParentView = parent;
            convertView = viewHolder.getItemView();
        } else {
            viewHolder = (BaseViewHolder) convertView.getTag();
        }
        viewHolder.mPosition = position;
        viewHolder.bindViews(data);
        return convertView;
    }



    /**
     * Created on 15/7/4.
     *
     * @author ran.huang
     * @version 1.0.0
     * @param <T> 泛型
     */
    public abstract static class BaseViewHolder<T> {
        protected int mPosition;
        protected ViewGroup mParentView;
        private View mItemView;

        public abstract View inflateItemView(ViewGroup parentView);

        public BaseViewHolder(ViewGroup parentView) {
            mItemView = inflateItemView(parentView);
            mItemView.setTag(this);
        }
        /**
         * 刷新内部控件
         * @param object T
         */
        public abstract void bindViews(T object);

        public View getItemView() {
            return mItemView;
        }
    }
}
