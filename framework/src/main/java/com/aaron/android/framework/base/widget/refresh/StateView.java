package com.aaron.android.framework.base.widget.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.aaron.android.framework.R;

/**
 * Created on 15/8/4.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class StateView extends FrameLayout {
    private View mLoadingView;
    private ViewGroup mSuccessViewGroup;
    private View mFailedView;
    private View mNodataView;
    private View mNoLoginView;
    private View mAnimView;
//    private TextView mRefreshText;
    private Animation mAnimation;

    private OnRetryRequestListener mOnRetryRequestListener;

    /**
     *
     */
    public enum State {
        /**
         *
         */
        LOADING,
        /**
         *
         */
        SUCCESS,
        /**
         *
         */
        FAILED,
        /**
         *
         */
        NO_DATA,

        NO_LOGIN,
    }

    /**
     *
     */
    public interface OnRetryRequestListener {
        /**
         *
         */
        void onRetryRequested();
    }

    protected OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnRetryRequestListener != null) {
                mOnRetryRequestListener.onRetryRequested();
            }
        }
    };

    /**
     * @param context 上下文对象
     */
    public StateView(Context context) {
        this(context, null);
    }

    /**
     * 构造器
     *
     * @param context 上下文对象
     * @param attrs   属性值集合
     */
    public StateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造器
     *
     * @param context  上下文对象
     * @param attrs    属性值集合
     * @param defStyle 风格
     */
    public StateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateView);
        int failedId = typedArray.getResourceId(R.styleable.StateView_failed_view, -1);
        int successId = typedArray.getResourceId(R.styleable.StateView_success_view, -1);
        int loadingId = typedArray.getResourceId(R.styleable.StateView_loading_view, R.layout.stateview_loading_view);
        int nodataId = typedArray.getResourceId(R.styleable.StateView_nodata_view, -1);
        int noLoginId = typedArray.getResourceId(R.styleable.StateView_no_login_view, -1);

        final LayoutInflater inflater = LayoutInflater.from(context);

        if (successId != -1) {
            mSuccessViewGroup = (ViewGroup) inflater.inflate(successId, null);
            addView(mSuccessViewGroup, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        if (failedId != -1) {
            mFailedView = inflater.inflate(failedId, null);
            addView(mFailedView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        if (loadingId != -1) {
            mLoadingView = inflater.inflate(loadingId, null);
            addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        if (nodataId != -1) {
            mNodataView = inflater.inflate(nodataId, null);
            addView(mNodataView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        if (noLoginId != -1) {
            mNoLoginView = inflater.inflate(noLoginId, null);
            addView(mNoLoginView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        mAnimView = mLoadingView.findViewById(R.id.online_refresh_animation);
//        mRefreshText = (TextView) mLoadingView.findViewById(R.id.online_refresh_text);
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.stateview_loading_rotate);

        setState(State.SUCCESS);
    }

    /**
     * 设置失败的View
     *
     * @param failedView failedView
     */
    public void setFailedView(View failedView) {
        if (mFailedView != null) {
            removeView(failedView);
        }
        mFailedView = failedView;

        addView(failedView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置成功的View
     *
     * @param successView successView
     */
    public void setSuccessView(View successView) {
        if (mSuccessViewGroup != null) {
            removeView(successView);
        }
        mSuccessViewGroup = (ViewGroup) successView;

        addView(successView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置加载中的View
     *
     * @param loadingView loadingView
     */
    public void setLoadingView(View loadingView) {
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        mLoadingView = loadingView;

        addView(loadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    public void setNoLoginView(View noLoginView) {
        if (mNoLoginView != null) {
            removeView(mNoLoginView);
        }

        mNoLoginView = noLoginView;

        addView(noLoginView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置没有数据时的View
     *
     * @param nodataView View
     */
    public void setNodataView(View nodataView) {
        if (mNodataView != null) {
            removeView(mNodataView);
        }
        mNodataView = nodataView;
        addView(nodataView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置View的状态
     *
     * @param state State
     */
    public void setState(State state) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(state == State.LOADING ? View.VISIBLE : View.GONE);
        }

        if (mSuccessViewGroup != null) {
            mSuccessViewGroup.setVisibility(state == State.SUCCESS ? View.VISIBLE : View.GONE);
        }

        if (mFailedView != null) {
            mFailedView.setVisibility(state == State.FAILED ? View.VISIBLE : View.GONE);
        }

        if (mNodataView != null) {
            mNodataView.setVisibility(state == State.NO_DATA ? View.VISIBLE : View.GONE);
        }

        if (mNoLoginView != null) {
            mNoLoginView.setVisibility(state == State.NO_LOGIN ? View.VISIBLE : View.GONE);
        }

        if (state == State.LOADING) {
            mAnimView.startAnimation(mAnimation);
        } else {
            mAnimView.clearAnimation();
        }
    }


    /**
     * 设置重试监听器, 当加载失败时,点击界面调用
     *
     * @param onRetryRequestListener OnRetryRequestListener
     */
    public void setOnRetryRequestListener(OnRetryRequestListener onRetryRequestListener) {
        mOnRetryRequestListener = onRetryRequestListener;
    }

}
