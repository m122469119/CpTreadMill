package com.aaron.android.framework.utils;

import android.view.View;

/**
 * Created on 16/1/19.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class RenderUtils {
    /**
     * 关闭控件硬件加速
     * @param v View
     */
    public static void disableHardwareRendering(View v) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}
