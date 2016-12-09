package com.aaron.android.framework.base.widget.refresh;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页器，实现分页逻辑.
 *
 * @author xiaoming.liu
 * @version 1.0.0
 */
public class Pager {

    private static final int DEFAULT_PAGE_TOTAL = 1;
    public static final int HOME_PAGE = 1;

    private int mStart = HOME_PAGE;
    private int mCurrent = mStart;
    private int mTotal = DEFAULT_PAGE_TOTAL;
    private boolean mIsStartPage = true;

    /**
     * 设置 mIsStartPage
     *
     * @param isStartPage boolean
     */
    public void setIsStartPage(boolean isStartPage) {
        mIsStartPage = isStartPage;
    }

    /**
     * 设置开始页，默认为1
     *
     * @param startIndex 开始页
     */
    public void setStart(int startIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("startIndex must >= 0");
        }
        mStart = startIndex;
    }

    /**
     * 设置总页数，默认为1
     *
     * @param total 总页数
     */
    public void setTotal(int total) {
        if (total < 1) {
            throw new IllegalArgumentException("total must >= 1");
        }
        mTotal = total;
    }

    /**
     * 设置当前请求页
     *
     * @param currentIndex 当前请求页
     */
    public void setCurrent(int currentIndex) {
        if (currentIndex < mStart || currentIndex > end()) {
            throw new IllegalArgumentException("currentIndex must be >= PageStart and <= pageEnd");
        }
        mCurrent = currentIndex;
    }

    /**
     * 获取当前页
     *
     * @return 当前页
     */
    public int getCurrent() {
        return mCurrent;
    }

    /**
     * 获取开始页
     *
     * @return 开始页
     */
    public int getStart() {
        return mStart;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public int getTotal() {
        return mTotal;
    }

    /**
     * 请求下一页数据
     *
     * @return 下一页
     */
    public int next() {
        mIsStartPage = false;
        return mCurrent + 1;
    }

    /**
     * 移动标志位到下一个位置
     */
    public void moveToNext() {
        mCurrent++;
        mIsStartPage = false;
    }

    /**
     * 是否已经过了最后一页
     *
     * @param pageIndex 请求的页码
     * @return 是否已经过了最后一夜
     */
    public boolean isOver(int pageIndex) {
        return pageIndex > end();
    }

    /**
     * @return 判断是否为第一次加载
     */
    public boolean isStartPage() {
        return mIsStartPage && mCurrent == mStart;
    }

    /**
     * 最后一页页码
     *
     * @return 最后一页页码
     */
    public int end() {
        return mStart + mTotal - 1;
    }

    /**
     * @return 是否还有下一页
     */
    public boolean hasNext() {
        return mCurrent < mTotal;
    }

    /**
     * 分页
     *
     * @param datas    需要分页的原始数据
     * @param pageSize 每页数据量
     * @param <Data>   适配的数据类型
     * @return 分好页的数据
     */
    public static <Data> List<List<Data>> split(List<Data> datas, int pageSize) {
        if (datas == null || datas.isEmpty()) {
            throw new IllegalArgumentException("invalid datas");
        }

        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be > 0");
        }

        int size = datas.size();
        int pageTotal = (int) Math.ceil((double) size / (double) pageSize);

        List<List<Data>> pageDatas = new ArrayList<List<Data>>();
        for (int i = 0; i < pageTotal - 1; i++) {
            pageDatas.add(datas.subList(i * pageSize, (i + 1) * pageSize));
        }
        pageDatas.add(datas.subList((pageTotal - 1) * pageSize, size));

        return pageDatas;
    }
}
