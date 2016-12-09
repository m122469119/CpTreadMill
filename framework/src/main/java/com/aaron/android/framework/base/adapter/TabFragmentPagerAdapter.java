package com.aaron.android.framework.base.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ran.huang
 * @version 7.0.0
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SlidingTabFragmentPagerAdapter";
    private List<FragmentBinder> mFragmentBinders;
    private Context mContext;

    /**
     * 构造函数
     *
     * @param context         context
     * @param fragmentManager fragmentManager
     * @param fragmentBinders FragmentBinder 列表
     */
    public TabFragmentPagerAdapter(Context context, FragmentManager fragmentManager, List<FragmentBinder> fragmentBinders) {
        super(fragmentManager);
        mContext = context;
        mFragmentBinders = fragmentBinders;
        if (mFragmentBinders == null) {
            throw new IllegalArgumentException("FragmentBinders must not be null!");
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentBinders.get(position).getFragment();
    }

    /**
     * clear
     */
    public void clear() {
        mContext = null;
    }

    @Override
    public int getCount() {
        return mFragmentBinders.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final CharSequence title = mFragmentBinders.get(position).getCharSequenceTitle();
        return title == null ? super.getPageTitle(position) : title.toString();
    }

    @Override
    public long getItemId(int position) {
        return mFragmentBinders.get(position).getId();
    }

    @Override
    public int getItemPosition(Object object) {
        for (FragmentBinder binder : mFragmentBinders) {
            if (null != binder && object.equals(binder.getFragment())) {
                return super.getItemPosition(object);
            }
        }
        return POSITION_NONE;
    }

    /**
     * 添加FragmentBinder
     * @param fragmentBinder fragmentBinder
     */
    public void addFragmentBinder(FragmentBinder fragmentBinder) {
        if (mFragmentBinders == null) {
            mFragmentBinders = new ArrayList<FragmentBinder>();
        }
        mFragmentBinders.add(fragmentBinder);
        notifyDataSetChanged();
    }

    /**
     * fragmentBinder是否存在
     * @param id  id
     * @return true false
     */
    public boolean isFragmentBinderExisted(long id) {
        if (mFragmentBinders != null) {
            for (FragmentBinder fragmentBinder : mFragmentBinders) {
                if (fragmentBinder.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除指定FragmentBinder
     * @param id 删除的FragmentBinder 的 ID
     */
    public void removeFragmentBinder(long id) {
        if (mFragmentBinders != null) {
            for (FragmentBinder fragmentBinder : mFragmentBinders) {
                if (fragmentBinder.getId() == id) {
                    mFragmentBinders.remove(fragmentBinder);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    /**
     * FragmentBinder
     */
    public static class FragmentBinder {
        private long mId;
        private int mTabTitleResId;
        private CharSequence mCharSequenceTitle;
        private int mTabIconResId;
        private Fragment mFragment;

        /**
         * @param id            id
         * @param tabTitleResId 标题资源id
         * @param tabIconResId  标题Icon资源id
         * @param fragment      Fragment
         */
        public FragmentBinder(long id, int tabTitleResId, int tabIconResId, Fragment fragment) {
            mId = id;
            mTabTitleResId = tabTitleResId;
            mTabIconResId = tabIconResId;
            mFragment = fragment;
        }

        /**
         * @param id            id
         * @param charSequenceTitle         charSequenceTitle
         * @param tabIconResId  标题Icon资源id
         * @param fragment      Fragment
         */
        public FragmentBinder(long id, CharSequence charSequenceTitle, int tabIconResId, Fragment fragment) {
            mId = id;
            mCharSequenceTitle = charSequenceTitle;
            mTabIconResId = tabIconResId;
            mFragment = fragment;
        }

        /**
         *
         * @return 文字title
         */
        public CharSequence getCharSequenceTitle() {
            return mCharSequenceTitle;
        }

        /**
         * 设置标签文字
         * @param title 文字
         */
        public void setCharSequenceTitle(CharSequence title) {
            mCharSequenceTitle = title;
        }

        /**
         * 获取Fragment对于的Tab项标题资源Id
         *
         * @return Fragment对于的Tab项标题资源Id
         */
        public int getTabTitleResId() {
            return mTabTitleResId;
        }

        /**
         * 获取Fragment对于的Tab项图标资源Id
         *
         * @return Fragment对于的Tab项图标资源Id
         */
        public int getTabIconResId() {
            return mTabIconResId;
        }

        /**
         * 获取唯一标识
         *
         * @return Id
         */
        public long getId() {
            return mId;
        }

        /**
         * 获取Fragment
         *
         * @return fragment
         */
        public Fragment getFragment() {
            return mFragment;
        }
    }
}
