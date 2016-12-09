package com.aaron.android.framework.base.ui.actionbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.R;
import com.aaron.android.framework.utils.ResourceUtils;

/**
 * Tool Bar Controller
 * Created on 15/6/26.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class ToolBarController implements View.OnClickListener {
    public static final int FLAG_LEFT = 1;
    public static final int FLAG_RIGHT = 2;
    private Context mContext;
    private Toolbar mToolbar;
    private ImageView mLeftIcon;
    private ImageView mRightIcon;
    private TextView mTitleText;
    private TextView mLeftMenuText;
    private TextView mRightMenuText;
    private View mRootView;
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public static final int ID_MENU_LEFT = 1;
    public static final int ID_MENU_RIGHT = 2;

    /**
     * 构造器
     *
     * @param context 上下文资源
     */
    public ToolBarController(Context context) {
        this(context, null);
    }

    public ToolBarController(Context context, Toolbar toolbar) {
        if (context == null) {
            throw new IllegalArgumentException("action bar parent must not be null");
        }
        mContext = context;
        init(toolbar);
    }

    private void init(Toolbar toolbar) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mRootView = layoutInflater.inflate(R.layout.layout_app_bar, null, false);
        if (toolbar == null) {
            mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        } else {
            mToolbar = toolbar;
        }
        mTitleText = (TextView) mToolbar.findViewById(R.id.textview_toolbar_title);
        mLeftMenuText = (TextView) mToolbar.findViewById(R.id.textview_toolbar_left);
        mLeftMenuText.setTag(ID_MENU_LEFT);
        mRightMenuText = (TextView) mToolbar.findViewById(R.id.textview_toolbar_right);
        mRightMenuText.setTag(ID_MENU_RIGHT);
        mLeftIcon = (ImageView) mToolbar.findViewById(R.id.imageview_toolbar_left_icon);
        mRightIcon = (ImageView) mToolbar.findViewById(R.id.imageview_toolbar_right_icon);
        mLeftMenuText.setOnClickListener(this);
        mRightMenuText.setOnClickListener(this);
    }

    public void setCustomView(View view) {
        if (view == null) {
            throw new NullPointerException("custom view must not be null");
        }
        mRootView = view;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public View getRootView() {
        return mRootView;
    }

    /**
     * 设置标题
     *
     * @param title 标题内容
     */
    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    /**
     * 设置标题
     *
     * @param res 标题内容
     */
    public void setTitle(int res) {
        mTitleText.setText(res);
    }

    /**
     * 添加Menu
     *
     * @param menuId MenuId
     * @param text   Menu text
     */
    public void showMenu(int menuId, String text) {
        if (menuId == ID_MENU_LEFT) {
            showMenuView(mLeftMenuText, text);
            mLeftIcon.setVisibility(View.GONE);
        } else if (menuId == ID_MENU_RIGHT) {
            showMenuView(mRightMenuText, text);
        }
    }

    public void showMenu(int menuId, String text, View.OnClickListener onClickListener) {
        if (menuId == ID_MENU_LEFT) {
            showMenuView(mLeftMenuText, text, onClickListener);
            mLeftIcon.setVisibility(View.GONE);
        } else if (menuId == ID_MENU_RIGHT) {
            showMenuView(mRightMenuText, text, onClickListener);
        }
    }

    /**
     * 隐藏菜单按钮
     *
     * @param menuId menuId
     */
    public void hideMenu(int menuId) {
        if (menuId == ID_MENU_LEFT) {
            mLeftMenuText.setVisibility(View.GONE);
        } else if (menuId == ID_MENU_RIGHT) {
            mRightMenuText.setVisibility(View.GONE);
        }
    }

    /**
     * 设置Home Icon
     *
     * @param drawable        Drawable资源
     * @param onClickListener 点击事件
     */
    public void setLeftIcon(Drawable drawable, View.OnClickListener onClickListener) {
        if (drawable != null) {
            mLeftMenuText.setVisibility(View.GONE);
            mLeftIcon.setVisibility(View.VISIBLE);
            mLeftIcon.setImageDrawable(drawable);
            mLeftIcon.setOnClickListener(onClickListener);
        } else {
            mLeftIcon.setOnClickListener(null);
            mLeftIcon.setVisibility(View.GONE);
        }

    }

    public void setRightIcon(Drawable drawable, View.OnClickListener onClickListener) {
        if (drawable != null) {
            mRightMenuText.setVisibility(View.GONE);
            mRightIcon.setVisibility(View.VISIBLE);
            mRightIcon.setImageDrawable(drawable);
            mRightIcon.setOnClickListener(onClickListener);
        } else {
            mRightIcon.setOnClickListener(null);
            mRightIcon.setVisibility(View.GONE);
        }
    }

    public void setLeftMenuTextDrawable(int flag, int drawableResId) {
        Drawable drawable = ResourceUtils.getDrawable(drawableResId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        switch (flag) {
            case FLAG_LEFT:
                mLeftMenuText.setCompoundDrawables(drawable, null, null, null);
                break;
            case FLAG_RIGHT:
                mLeftMenuText.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }
    }

    public void setRightMenuTextDrawable(int flag, int drawableResId) {
        Drawable drawable = ResourceUtils.getDrawable(drawableResId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        switch (flag) {
            case FLAG_LEFT:
                mRightMenuText.setCompoundDrawables(drawable, null, null, null);
                break;
            case FLAG_RIGHT:
                mRightMenuText.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }
    }

    /**
     * 设置Menu的监听
     *
     * @param onMenuItemClickListener OnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        mOnMenuItemClickListener = onMenuItemClickListener;
    }

    private void showMenuView(TextView view, String text) {
        view.setVisibility(View.VISIBLE);
        view.setText(text);
    }

    private void showMenuView(TextView view, String text, View.OnClickListener onClickListener) {
        view.setVisibility(View.VISIBLE);
        view.setText(text);
        view.setOnClickListener(onClickListener);
    }

    public void setLeftMenuTextColor(int resId) {
        if (mLeftMenuText != null) {
            mLeftMenuText.setTextColor(ResourceUtils.getColor(resId));
        }
    }

    public void setRightMenuTextColor(int resId) {
        if (mRightMenuText != null) {
            mRightMenuText.setTextColor(ResourceUtils.getColor(resId));
        }
    }

    @Override
    public void onClick(View view) {
        if (mOnMenuItemClickListener == null) {
            return;
        }
        int menuId = (int) view.getTag();
        mOnMenuItemClickListener.onMenuClick(menuId);
    }

    /**
     * ToolBar Menu监听器
     */
    public interface OnMenuItemClickListener {
        /**
         * Menu点击
         *
         * @param menuId MenuID
         */
        void onMenuClick(int menuId);
    }

    /**
     * 销毁相关资源，点击监听
     */
    public void destroy() {
        setLeftIcon(null, null);
        setRightIcon(null, null);
        mRightMenuText.setOnClickListener(null);
        mLeftMenuText.setOnClickListener(null);
        mOnMenuItemClickListener = null;
    }

}
