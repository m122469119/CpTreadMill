package com.aaron.android.framework.base.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;

import com.aaron.android.framework.R;
import com.aaron.android.framework.utils.ResourceUtils;

/**
 * Created on 15/6/2.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public class HBaseDialog extends AppCompatDialog {

    private HDialogController mDialogController;

    /**
     * 默认样式的Dialog
     * @param context 上下文资源
     */
    public HBaseDialog(Context context) {
        this(context, R.style.Theme_Dialog);
    }

    /**
     * Constructor
     * @param context 上下文资源
     * @param theme 样式
     */
    public HBaseDialog(Context context, int theme) {
        this(context, theme, true, null);
    }

    public HBaseDialog(Context context, int theme, boolean cancelable, OnCancelListener cancelListener) {
        super(context, theme);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
        mDialogController = new HDialogController(getContext(), this, getWindow());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogController.buildContent();
    }

    public Button getButton(int whichButton) {
        return mDialogController.getButton(whichButton);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mDialogController.setTitle(title);
    }

    /**
     * 设置自定义标题View
     * @param customTitleView 自定义标题View
     */
    public void setCustomTitle(View customTitleView) {
        mDialogController.setCustomTitle(customTitleView);
    }

    public void setTitleColor(int color) {
        mDialogController.setTitleColor(color);
    }

    /**
     * 设置文本内容
     * @param message CharSequence
     */
    public void setMessage(CharSequence message) {
        mDialogController.setMessage(message);
    }

    /**
     * 设置自定义ContentView
     *
     * @param view View
     */
    public void setCustomView(View view) {
        mDialogController.setCustomView(view);
    }

    public void setCustomView(int layoutId) {
        mDialogController.setCustomView(layoutId);
    }

    /**
     * 设置自定义ContentView
     *
     * @param view              View
     * @param viewSpacingLeft   内容距离dialog左边的距离
     * @param viewSpacingTop    内容距离dialog上边的距离
     * @param viewSpacingRight  内容距离dialog右边的距离
     * @param viewSpacingBottom 内容距离dialog下边的距离
     */
    public void setCustomView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
                        int viewSpacingBottom) {
        mDialogController.setCustomView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    /**
     * 设置下方的按钮
     *
     * @param whichButton 取值和Dialog使用方法一致DialogInterface.BUTTON_POSITIVE DialogInterface.BUTTON_NEGATIVE
     * @param text        设置对应按钮的Text
     * @param msg         Message
     */
    public void setButton(int whichButton, CharSequence text, Message msg) {
        mDialogController.setButton(whichButton, text, null, msg);
    }

    /**
     * 设置下方的按钮
     *
     * @param whichButton 取值和Dialog使用方法一致DialogInterface.BUTTON_POSITIVE DialogInterface.BUTTON_NEGATIVE
     * @param text        设置对应按钮的Text
     * @param listener    设置对应按钮的点击事件
     */
    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        mDialogController.setButton(whichButton, text, listener, null);
    }

    /**
     * 设置Icon图标
     *
     * @param resId 图标资源Id
     */
    public void setIcon(int resId) {
        mDialogController.setIcon(resId);
    }

    /**
     * 设置Icon图标
     *
     * @param icon 图标资源Drawable
     */
    public void setIcon(Drawable icon) {
        mDialogController.setIcon(icon);
    }

    public static class Builder {
        private HBaseDialog mDialog;
        private int mTheme;
        private Context mContext;

        public Builder(Context context, int theme) {
            mContext = context;
            mDialog = new HBaseDialog(context, theme);
        }

        public Builder(Context context) {
            mContext = context;
            mDialog = new HBaseDialog(context);
        }

        public Context getContext() {
            return mContext;
        }

        public Builder setTitle(int resId) {
            mDialog.setTitle(ResourceUtils.getString(resId));
            return this;
        }

        public Builder setTitle(CharSequence title) {
            mDialog.setTitle(title);
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            mDialog.setIcon(drawable);
            return this;
        }

        public Builder setIcon(int resId) {
            mDialog.setIcon(resId);
            return this;
        }

        public Builder setCustomTitle(View titleView) {
            mDialog.setCustomTitle(titleView);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mDialog.setMessage(message);
            return this;
        }

        public Builder setMessage(int resId) {
            mDialog.setMessage(ResourceUtils.getString(resId));
            return this;
        }

        public Builder setCustomView(View customView) {
            mDialog.setCustomView(customView);
            return this;
        }

        public Builder setCustomView(int layoutId) {
            mDialog.setCustomView(layoutId);
            return this;
        }

        public Builder setCustomView(View customView, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
                                     int viewSpacingBottom){
            mDialog.setCustomView(customView, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
            return this;
        }

        public Builder setTitleColor(int color) {
            mDialog.setTitleColor(color);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener onClickListener) {
            mDialog.setButton(DialogInterface.BUTTON_POSITIVE, text, onClickListener);
            return this;
        }

        public Builder setPositiveButton(int resId, OnClickListener onClickListener) {
            mDialog.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getString(resId), onClickListener);
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener onClickListener) {
            mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, text, onClickListener);
            return this;
        }

        public Builder setNegativeButton(int resId, OnClickListener onClickListener) {
            mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getString(resId), onClickListener);
            return this;
        }

        public HBaseDialog create() {
            return mDialog;
        }

        public void show() {
            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }
}
