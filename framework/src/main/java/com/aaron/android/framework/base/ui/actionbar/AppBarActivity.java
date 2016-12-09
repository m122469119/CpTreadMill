package com.aaron.android.framework.base.ui.actionbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.R;
import com.aaron.android.framework.base.ui.swipeback.app.SwipeBackActivity;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.framework.utils.SDKVersionUtils;

/**
 * Created on 15/6/26.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public class AppBarActivity extends SwipeBackActivity {
    private LinearLayout mRootView;
    private FrameLayout mContentView;
    private ToolBarController mToolBarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (SDKVersionUtils.hasKitKat()) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.setContentView(mRootView);
    }

    @Override
    public void setContentView(View view) {
        if (mContentView.getChildCount() > 0) {
            mContentView.removeAllViews();
        }
        mContentView.addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        mContentView.addView(view, layoutParams);
    }

    protected void init() {
        initRootView();
        initToolBar();
        initContentView();
    }

    /**
     * 初始化RootView
     */
    private void initRootView() {
        mRootView = new LinearLayout(this);
        mRootView.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolBarController = new ToolBarController(this);
        showHomeUpIcon(R.drawable.back);
        mRootView.addView(mToolBarController.getRootView());
        setSupportActionBar(mToolBarController.getToolbar());
    }

    public void setCustomToolBar(View view) {
        if (mRootView == null) {
            return;
        }
        mRootView.removeAllViews();
        mToolBarController = new ToolBarController(this);
        mToolBarController.setCustomView(view);
        showHomeUpIcon(R.drawable.back);
        mRootView.addView(mToolBarController.getRootView());
        mRootView.addView(mContentView, new LinearLayout.LayoutParams(-1, -1));
    }

    /**
     * 初始化Content View
     */
    private void initContentView() {
        mContentView = new FrameLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        mRootView.addView(mContentView, layoutParams);
    }

    /**
     * 获取RootView
     *
     * @return View
     */
    public View getRootView() {
        return mRootView;
    }

    /**
     * 在onDestroy生命周期中，回收销毁一些资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarController.destroy();
    }

    /**
     * @return 获取ToolBarController
     */
    public ToolBarController getToolBarController() {
        return mToolBarController;
    }

    /**
     * 隐藏ToolBar
     */
    protected void hideAppBar() {
        mRootView.removeView(mToolBarController.getRootView());
    }

    /**
     * @param title 设置标题
     */
    public void setTitle(String title) {
        mToolBarController.setTitle(title);
    }

    /**
     * @param resId 设置标题
     */
    public void setTitle(int resId) {
        mToolBarController.setTitle(resId);
    }



    /**
     * 设置左边的文字按钮
     *
     * @param text 文字内容
     */
    public void showLeftMenu(String text) {
        if (StringUtils.isEmpty(text)) {
            mToolBarController.hideMenu(ToolBarController.ID_MENU_LEFT);
            return;
        }
        mToolBarController.showMenu(ToolBarController.ID_MENU_LEFT, text);
    }

    /**
     * 设置左边的文字按钮
     *
     * @param text 文字内容
     */
    public void showLeftMenu(String text, View.OnClickListener onClickListener) {
        if (StringUtils.isEmpty(text)) {
            mToolBarController.hideMenu(ToolBarController.ID_MENU_LEFT);
            return;
        }
        mToolBarController.showMenu(ToolBarController.ID_MENU_LEFT, text, onClickListener);
    }

    public void setMenuTextColor(int menuId, int resId) {
        if (menuId == ToolBarController.ID_MENU_LEFT) {
            mToolBarController.setLeftMenuTextColor(resId);
        } else if (menuId == ToolBarController.ID_MENU_RIGHT) {
            mToolBarController.setRightMenuTextColor(resId);
        }
    }

    /**
     * 设置右边的文字按钮
     *
     * @param text 文字内容
     */
    public void showRightMenu(String text) {
        if (StringUtils.isEmpty(text)) {
            mToolBarController.hideMenu(ToolBarController.ID_MENU_RIGHT);
            return;
        }
        mToolBarController.showMenu(ToolBarController.ID_MENU_RIGHT, text);
    }

    public void showRightMenu(String text, View.OnClickListener onClickListener) {
        if (StringUtils.isEmpty(text)) {
            mToolBarController.hideMenu(ToolBarController.ID_MENU_RIGHT);
            return;
        }
        mToolBarController.showMenu(ToolBarController.ID_MENU_RIGHT, text, onClickListener);
    }

    /**
     * 设置左边图片按钮
     *
     * @param resId 图片按钮资源
     */
    public void showHomeUpIcon(int resId) {
        //默认左边图标点击监听是关闭当前的Activity
        showHomeUpIcon(resId, mDefaultHomeIconClickListener);
    }

    /**
     * 隐藏左边图标
     */
    public void hideHomeUpIcon() {
        showHomeUpIcon(0);
    }

    /**
     * 设置右边图片按钮
     *
     * @param resId           图片按钮资源
     * @param onClickListener 点击事件监听
     */
    public void showHomeUpIcon(int resId, View.OnClickListener onClickListener) {
        Drawable drawable = null;
        if (resId != 0) {
            drawable = ResourceUtils.getDrawable(resId);
        }
        mToolBarController.setLeftIcon(drawable, onClickListener);
    }

    /**
     * 设置右边图片按钮
     *
     * @param resId           图片按钮资源
     * @param onClickListener 点击事件监听
     */
    public void setRightIcon(int resId, View.OnClickListener onClickListener) {
        Drawable drawable = null;
        if (resId != 0) {
            drawable = ResourceUtils.getDrawable(resId);
        }
        mToolBarController.setRightIcon(drawable, onClickListener);
    }

    public void setLeftMenuTextDrawable(int flag, int resId) {
        mToolBarController.setLeftMenuTextDrawable(flag, resId);
    }

    public void setRightMenuTextDrawable(int flag, int resId) {
        mToolBarController.setRightMenuTextDrawable(flag, resId);
    }


    /**
     * 设置左边图片按钮
     *
     * @param resId 图片按钮资源
     */
    public void setRightIcon(int resId) {
        setRightIcon(resId, null);
    }


    /**
     * 设置菜单点击事件
     *
     * @param onMenuItemClickListener OnMenuItemClickListener
     */
    public void setMenuOnClickListener(ToolBarController.OnMenuItemClickListener onMenuItemClickListener) {
        mToolBarController.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    /**
     * 默认左边图片按钮监听器
     */
    private final View.OnClickListener mDefaultHomeIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

}
