package com.aaron.android.framework.base.mvp;

import android.content.Context;

import com.aaron.android.framework.base.eventbus.BaseMessage;

import de.greenrobot.event.EventBus;

/**
 * Created on 15/6/30.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public abstract class BasePresenter<T extends BaseView> {
    protected Context mContext;
    protected T mView;
    protected final String TAG = getClass().getSimpleName();

    public BasePresenter(Context context, T mainView) {
        mContext = context;
        mView = mainView;
    }

    public void postEvent(BaseMessage message) {
        EventBus.getDefault().post(message);
    }

}
