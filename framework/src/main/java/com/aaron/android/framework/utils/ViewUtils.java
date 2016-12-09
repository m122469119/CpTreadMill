package com.aaron.android.framework.utils;

import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * Created on 15/6/3.
 *
 * @author ran.huang
 * @version 3.0.1
 *
 *
 * 做View的不同android版本平台Api差异性兼容
 */
public class ViewUtils {

    private static long sLastClickTime;
    private static final long TIME_DURATION = TimeUnit.MILLISECONDS.toNanos(500);
    /**
     * 判断是否为快速双击事件（用来判断在一个view上连续双击），目前的时间间隔为500毫秒
     *
     * BaseActivity中已对快速点击事件进行处理，继承于BaseActivity中View的点击事件无需再处理，
     * 但在Dialog及PopupWindow中，仍需要进行此判断。
     *
     * @return true： 是快速双击事件
     */
    public static boolean isFastDoubleClick() {
        long time = System.nanoTime();
        if (time - sLastClickTime < TIME_DURATION) {
            return true;
        }
        sLastClickTime = time;
        return false;
    }

    /**
     * 设置View的背景，sdk16以上使用setBackground替代setBackgroundDrawable
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable) {
        if (view == null) {
            return;
        }
        if (SDKVersionUtils.hasJellyBean()) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 在View渲染完成前,可通过该方法获取View的高度
     * @param view View
     * @return View的高度
     */
    public static int calculateViewHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }

    /**
     * 在View渲染完成前,可通过该方法获取View的宽度
     * @param view View
     * @return View的宽度
     */
    public static int calculateViewWidth(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredWidth();
    }

}
