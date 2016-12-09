package com.aaron.android.framework.base.mvp;

/**
 * Created on 16/2/22.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public interface BaseNetworkLoadView extends BaseView{
    /**
     * 网络连接异常时更新对应View
     */
    void handleNetworkFailure();
}
