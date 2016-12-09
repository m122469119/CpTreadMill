package com.aaron.android.framework.base.widget.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 15/7/30.
 *
 * @param <T>  extends BaseData
 * @param <VH> extends BaseRecycleViewHolder<T>
 * @author ran.huang
 * @version 1.0.0
 */
public abstract class BaseRecycleViewAdapter<VH extends BaseRecycleViewHolder<T>, T>
        extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    protected final String TAG = getClass().getSimpleName();
    ;
    private List<T> mDataList = new ArrayList<>();

    private Context mContext;

    public static final int TYPE_HEADER = RecyclerView.INVALID_TYPE;
    public static final int TYPE_FOOTER = RecyclerView.INVALID_TYPE - 1;
    private List<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private List<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    private OnRecycleViewItemClickListener mOnRecycleViewItemClickListener;

    protected BaseRecycleViewAdapter(Context context) {
        mContext = context;
    }


    protected abstract VH createViewHolder(ViewGroup parent);

    /**
     * @return 获取上下文资源
     */
    public Context getContext() {
        return mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**和List添加子项的getView(int position,...)不同是的这里没有position
         * ,所以采取遍历HeaderFooter FixViewInfoList,通过isAdded的字段来返回列表
         * 中第一个没有被添加过的HeaderFooterView的方法来创建ViewHolder*/
        if (mHeaderViewInfos != null && viewType == TYPE_HEADER) {
            for (FixedViewInfo headerViewInfo : mHeaderViewInfos) {
                if (headerViewInfo.isAdded) {
                    continue;
                }
                headerViewInfo.isAdded = true;
                return new HeaderFooterViewHolder(headerViewInfo.view);
            }
        } else if (mFooterViewInfos != null && viewType == TYPE_FOOTER) {
            for (FixedViewInfo footerViewInfo : mFooterViewInfos) {
                if (footerViewInfo.isAdded) {
                    continue;
                }
                footerViewInfo.isAdded = true;
                return new HeaderFooterViewHolder(footerViewInfo.view);
            }
        }
        return createViewHolder(parent);
    }

    class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 设置HeaderView
     *
     * @param headerView View
     */
    public void addHeaderView(View headerView) {
        if (headerView == null) {
            return;
        }
        for (FixedViewInfo fixedViewInfo : mHeaderViewInfos) {
            if (fixedViewInfo.view == headerView) {
                return;
            }
        }
        mHeaderViewInfos.add(createFixedViewInfo(headerView));
        notifyDataSetChanged();
    }

    public void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }
        for (FixedViewInfo fixedViewInfo : mFooterViewInfos) {
            if (fixedViewInfo.view == footerView) {
                return;
            }
        }
        mFooterViewInfos.add(createFixedViewInfo(footerView));
        notifyDataSetChanged();
    }

    private FixedViewInfo createFixedViewInfo(View headerView) {
        FixedViewInfo headerViewInfo = new FixedViewInfo();
        headerViewInfo.view = headerView;
        headerViewInfo.isAdded = false;
        return headerViewInfo;
    }

    public void removeHeaderView(View headerView) {
//        List<FixedViewInfo> newFixedViewInfos = new ArrayList<>();
//        for (FixedViewInfo headerViewInfo : mHeaderViewInfos) {
//            if (headerView == headerViewInfo.view) {
//                newFixedViewInfos.add(headerViewInfo);
//            }
//        }
//        mHeaderViewInfos.removeAll(newFixedViewInfos);
        mHeaderViewInfos.clear();
        notifyDataSetChanged();
    }

    public void removeFooterView(View footerView) {
//        List<FixedViewInfo> newFixedViewInfos = new ArrayList<>();
//        for (FixedViewInfo footerViewInfo : mFooterViewInfos) {
//            if (footerView == footerViewInfo.view) {
//                newFixedViewInfos.add(footerViewInfo);
//            }
//        }
//        mFooterViewInfos.removeAll(newFixedViewInfos);
        mFooterViewInfos.clear();
        notifyDataSetChanged();
    }

    /**
     * @return 获取Adapter DataList数据集
     */
    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 设置RecycleView item事件监听回调
     *
     * @param onRecycleViewItemClickListener OnRecycleViewItemClickListener
     */
    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        mOnRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    /**
     * 设置数据源
     *
     * @param list List
     */
    public void setData(List<T> list) {
        if (list != null) {
            mDataList = new ArrayList<>(list);
        } else {
            mDataList = new ArrayList<>();
        }
    }

    /**
     * 添加数据源
     *
     * @param list List
     */
    public void addData(List<T> list) {
        if (!ListUtils.isEmpty(list)) {
            mDataList.addAll(list);
        }
    }

    private int getHeaderCount() {
        return mHeaderViewInfos == null ? 0 : mHeaderViewInfos.size();
    }

    private int getFooterCount() {
        return mFooterViewInfos == null ? 0 : mFooterViewInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        int headerCount = getHeaderCount();
        if (position < headerCount) {
            return TYPE_HEADER;
        }
        final int realPosition = position - headerCount;
        if (realPosition < mDataList.size()) {
            return super.getItemViewType(realPosition);
        }
        return TYPE_FOOTER;
    }


    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headerCount = getHeaderCount();
        if (position < headerCount) {
            return;
        }
        final int realPosition = position - headerCount;
        if (realPosition < mDataList.size()) {
            T data = mDataList.get(realPosition);
            VH viewHolder = (VH) holder;
            if (data != null) {
                /**绑定holder数据*/
                viewHolder.bindViews(data);
                viewHolder.mPosition = realPosition;
                /**ItemView设置监听*/
                View itemView = holder.itemView;
                if (itemView != null) {
                    itemView.setTag(realPosition);
                    itemView.setClickable(true);
                    itemView.setLongClickable(true);
                    itemView.setOnClickListener(this);
                    itemView.setOnLongClickListener(this);
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnRecycleViewItemClickListener != null) {
            mOnRecycleViewItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnRecycleViewItemClickListener != null
                && mOnRecycleViewItemClickListener.onItemLongClick(v, (Integer) v.getTag());
    }

    /**
     *
     */
    class FixedViewInfo {
        /**
         * 添加到RecyclerView的View
         */
        public View view;
        /**
         * 是否已经添加状态
         */
        public boolean isAdded;
    }

}
