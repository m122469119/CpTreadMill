package com.aaron.android.framework.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aaron.android.framework.R;
import com.aaron.android.framework.utils.ViewUtils;

import java.lang.ref.WeakReference;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created on 16/1/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class HDialogController {
    private Context mContext;
    private Dialog mDialog;
    private Window mWindow;
    private CharSequence mTitle;
    private CharSequence mMessage;

    private View mRootView;
    private View mCustomView;
    private View mContentView;
    private int mCustomViewLayoutResId;

    private int mViewSpacingLeft;
    private int mViewSpacingTop;
    private int mViewSpacingRight;
    private int mViewSpacingBottom;
    private boolean mViewSpacingSpecified = false;

    private Button mButtonPositive;
    private CharSequence mButtonPositiveText;

    private Button mButtonNegative;
    private CharSequence mButtonNegativeText;
    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mMessageView;
    private View mCustomTitleView;
    private Message mButtonPositiveMessage;
    private Message mButtonNegativeMessage;
    private View mTitleDivider;
    private int mIconId = 0;
    private Drawable mIcon;
    private Handler mHandler;

    private final View.OnClickListener mButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Message message;
            if (v == mButtonPositive && mButtonPositiveMessage != null) {
                message = Message.obtain(mButtonPositiveMessage);
            } else if (v == mButtonNegative && mButtonNegativeMessage != null) {
                message = Message.obtain(mButtonNegativeMessage);
            } else {
                message = null;
            }

            if (message != null) {
                message.sendToTarget();
            }

            mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, mDialog)
                    .sendToTarget();
        }
    };

    private static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;

                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    public HDialogController(Context context, Dialog dialog, Window window) {
        mContext = context;
        mDialog = dialog;
        mWindow = window;
        mHandler = new ButtonHandler(dialog);
        initViews();
    }

    private void initViews() {
        mRootView = View.inflate(mContext, R.layout.view_base_dialog, null);
        mContentView = mWindow.findViewById(R.id.view_dialog_custom);
        mIconView = (ImageView) mWindow.findViewById(R.id.imageview_dialog_title_icon);
        mTitleView = (TextView) mWindow.findViewById(R.id.textview_dialog_title);
        mButtonPositive = (Button) mWindow.findViewById(R.id.button_dialog_confirm);
        mButtonNegative = (Button) mWindow.findViewById(R.id.button_dialog_cancel);
        mTitleDivider = mWindow.findViewById(R.id.dialog_title_divider);
    }

    private boolean setupTitle(ViewGroup topPanel) {
        boolean hasTitle = true;

        if (mCustomTitleView != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            topPanel.addView(mCustomTitleView, 0, lp);
            View titleView = mWindow.findViewById(R.id.layout_base_dialog_title_view);
            titleView.setVisibility(View.GONE);
        } else {
            mIconView = (ImageView) mWindow.findViewById(R.id.imageview_dialog_title_icon);
            final boolean hasTextTitle = !TextUtils.isEmpty(mTitle);
            if (hasTextTitle) {
                mTitleView = (TextView) mWindow.findViewById(R.id.textview_dialog_title);
                mTitleView.setText(mTitle);
                if (mIconId != 0) {
                    mIconView.setBackgroundResource(mIconId);
                } else if (mIcon != null) {
                    ViewUtils.setBackground(mIconView, mIcon);
                } else {
                    mTitleView.setPadding(mIconView.getPaddingLeft(),
                            mIconView.getPaddingTop(),
                            mIconView.getPaddingRight(),
                            mIconView.getPaddingBottom());
                    mIconView.setVisibility(View.GONE);
                }
            } else {
                topPanel.setVisibility(View.GONE);
                hasTitle = false;
            }
        }
        return hasTitle;
    }

    public void buildContent() {
        mDialog.setContentView(mRootView);
        setupView();
    }

    private void setupView() {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        final ViewGroup buttonsPanel = (ViewGroup) mWindow.findViewById(R.id.view_dialog_buttons);
        final ViewGroup titlePanel = (ViewGroup) mWindow.findViewById(R.id.view_dialog_title);
        final ViewGroup contentPanel = (ViewGroup) mWindow.findViewById(R.id.view_dialog_content);
        final FrameLayout customPanel = (FrameLayout) mWindow.findViewById(R.id.view_dialog_custom);
        final boolean hasButtons = setupButtons(buttonsPanel);
        final boolean hasMessageContent = setupMessageContent(contentPanel);
        final boolean hasTitle = setupTitle(titlePanel);

        final View buttonPanel = mWindow.findViewById(R.id.view_dialog_buttons);
        if (!hasButtons) {
            buttonPanel.setVisibility(View.GONE);
        }

        final View customView;
        if (mCustomView != null) {
            customView = mCustomView;
        } else if (mCustomViewLayoutResId != 0) {
            customView = inflater.inflate(mCustomViewLayoutResId, customPanel, false);
        } else {
            customView = null;
        }

        final boolean hasCustomView = customView != null;
        if (!hasCustomView || !canTextInput(customView)) {
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        if (hasCustomView) {
            customPanel.addView(customView, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            if (mViewSpacingSpecified) {
                customPanel.setPadding(
                        mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
            }
        } else {
            customPanel.setVisibility(View.GONE);
        }


    }

    private boolean setupMessageContent(View contentView) {
        ScrollView scrollView = (ScrollView) mWindow.findViewById(R.id.scroll_dialog_message);
        scrollView.setFocusable(false);
        mMessageView = (TextView) scrollView.findViewById(R.id.textview_message);
        if (mMessageView == null) {
            return false;
        }
        if (mMessage != null) {
            mMessageView.setText(mMessage);
            return true;
        } else {
            mMessageView.setVisibility(View.GONE);
            scrollView.removeView(mMessageView);
            contentView.setVisibility(View.GONE);
        }
        return false;
    }

    static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }

        if (!(v instanceof ViewGroup)) {
            return false;
        }

        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            v = vg.getChildAt(i);
            if (canTextInput(v)) {
                return true;
            }
        }

        return false;
    }

    private boolean setupButtons(View buttonsView) {
        int BIT_BUTTON_POSITIVE = 1;
        int BIT_BUTTON_NEGATIVE = 2;
        int whichButtons = 0;
        mButtonPositive = (Button) buttonsView.findViewById(R.id.button_dialog_confirm);
        mButtonPositive.setOnClickListener(mButtonHandler);

        mButtonNegative = (Button) buttonsView.findViewById(R.id.button_dialog_cancel);
        mButtonNegative.setOnClickListener(mButtonHandler);

        View mButtonDivider = buttonsView.findViewById(R.id.button_dialog_divider);
        int buttonCount = 0;

        if (TextUtils.isEmpty(mButtonPositiveText)) {
            mButtonPositive.setVisibility(View.GONE);
        } else {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
            buttonCount++;
        }

        if (TextUtils.isEmpty(mButtonNegativeText)) {
            mButtonNegative.setVisibility(View.GONE);
        } else {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
            buttonCount++;
        }


//        if (shouldCenterSingleButton(mContext)) {
        if (whichButtons == BIT_BUTTON_POSITIVE) {
            centerButton(mButtonPositive);
        } else if (whichButtons == BIT_BUTTON_NEGATIVE) {
            centerButton(mButtonNegative);
        }
        if (buttonCount > 1) {
            mButtonDivider.setVisibility(View.VISIBLE);
        } else {
            mButtonDivider.setVisibility(View.GONE);
        }
//        }

        return whichButtons != 0;
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.weight = 0.5f;
        button.setLayoutParams(params);
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setTitleColor(int color) {
        if (mTitleView != null) {
            mTitleView.setTextColor(color);
        }
    }

    public void setCustomTitle(View customTitleView) {
        mCustomTitleView = customTitleView;
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    public void setCustomView(int layoutResId) {
        mCustomView = null;
        mCustomViewLayoutResId = layoutResId;
        mViewSpacingSpecified = false;
    }

    public void setCustomView(View view) {
        mCustomView = view;
        mCustomViewLayoutResId = 0;
        mViewSpacingSpecified = false;
    }

    public void setCustomView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
                              int viewSpacingBottom) {
        mCustomView = view;
        mCustomViewLayoutResId = 0;
        mViewSpacingSpecified = true;
        mViewSpacingLeft = viewSpacingLeft;
        mViewSpacingTop = viewSpacingTop;
        mViewSpacingRight = viewSpacingRight;
        mViewSpacingBottom = viewSpacingBottom;
    }

    public void setButton(int whichButton, CharSequence text,
                          DialogInterface.OnClickListener listener, Message msg) {

        if (msg == null && listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }
        switch (whichButton) {

            case DialogInterface.BUTTON_POSITIVE:
                mButtonPositiveText = text;
                mButtonPositiveMessage = msg;
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                mButtonNegativeText = text;
                mButtonNegativeMessage = msg;
                break;

            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }



    public void setIcon(int resId) {
        mIcon = null;
        mIconId = resId;

        if (mIconView != null) {
            if (resId != 0) {
                mIconView.setImageResource(mIconId);
            } else {
                mIconView.setVisibility(View.GONE);
            }
        }
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
        mIconId = 0;

        if (mIconView != null) {
            if (icon != null) {
                mIconView.setImageDrawable(icon);
            } else {
                mIconView.setVisibility(View.GONE);
            }
        }
    }

    public Button getButton(int whichButton) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                return mButtonPositive;
            case DialogInterface.BUTTON_NEGATIVE:
                return mButtonNegative;
            default:
                return null;
        }
    }

}
