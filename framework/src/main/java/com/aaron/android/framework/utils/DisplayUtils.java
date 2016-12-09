package com.aaron.android.framework.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.aaron.android.framework.R;

/**
 * Created on 下午7:59.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class DisplayUtils {


    private static DisplayMetrics sDisplayMetrics = null;
    private static Configuration sConfiguration = null;

    /**
     * ldpi
     */
    public static final String EX_DENSITY_LOW = "_ldpi";
    /**
     * mdpi
     */
    public static final String EX_DENSITY_MEDIUM = "_mdpi";
    /**
     * hdpi
     */
    public static final String EX_DENSITY_HIGH = "_hdpi";
    /**
     * xhdpi
     */
    public static final String EX_DENSITY_XHIGH = "_xhdpi";
    /**
     * xxhdpi
     */
    public static final String EX_DENSITY_XXHIGH = "_xxhdpi";

    private static final float ROUND_DIFFERENCE = 0.5f;
    private static final int DENSITY_400 = 400;

    /**
     * 初始化操作
     * @param context context
     */
    public static void init(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
        sConfiguration = context.getResources().getConfiguration();
    }

    /**
     * 配置发生变化
     * @param context context
     * @param newConfiguration newConfiguration
     */
    public static void onConfigurationChanged(Context context, Configuration newConfiguration) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
        sConfiguration = newConfiguration;
    }

    /**
     * 获取屏幕宽度 单位：像素
     * @return 屏幕宽度
     */
    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度 单位：像素
     * @return 屏幕高度
     */
    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }

    /**
     * 获取Density
     * @return Density
     */
    public static float getDensity() {
        return sDisplayMetrics.density;
    }

    /**
     * 获取DensityDpi
     * @return DensityDpi
     */
    public static int getDensityDpi() {
        return sDisplayMetrics.densityDpi;
    }

    /**
     * dp 转 px
     * 注意正负数的四舍五入规则
     * @param dp dp值
     * @return 转换后的像素值
     */
    public static int dp2px(int dp) {
        return (int)(dp * sDisplayMetrics.density + (dp > 0 ? ROUND_DIFFERENCE : -ROUND_DIFFERENCE));
    }

    /**
     * px 转 dp
     * 注意正负数的四舍五入规则
     * @param px px值
     * @return 转换后的dp值
     */
    public static int px2dp(int px) {
        return (int)(px / sDisplayMetrics.density + (px > 0 ? ROUND_DIFFERENCE : -ROUND_DIFFERENCE));
    }

    /**
     * get bitmap density
     * @return String
     */
    public static String getBitmapDensityStr() {
        switch (getBitmapDensity()) {
            case DisplayMetrics.DENSITY_LOW:
                return EX_DENSITY_LOW;
            case DisplayMetrics.DENSITY_MEDIUM:
                return EX_DENSITY_MEDIUM;
            case DisplayMetrics.DENSITY_HIGH:
                return EX_DENSITY_HIGH;
            case DisplayMetrics.DENSITY_XHIGH:
                return EX_DENSITY_XHIGH;
            case DisplayMetrics.DENSITY_XXHIGH:
                return EX_DENSITY_XXHIGH;
            default:
                return "";
        }
    }

    /**
     * 获取bitmapDensity
     * @return bitmapDensity
     */
    public static int getBitmapDensity() {
        int densityDpi = sDisplayMetrics.densityDpi;
        if (densityDpi <= DisplayMetrics.DENSITY_LOW) {
            return DisplayMetrics.DENSITY_LOW;
        } else if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            return DisplayMetrics.DENSITY_MEDIUM;
        } else if (densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return DisplayMetrics.DENSITY_HIGH;
        } else if (densityDpi <= DisplayMetrics.DENSITY_XHIGH) {
            return DisplayMetrics.DENSITY_XHIGH;
        } else if (densityDpi <= /*DisplayMetrics.DENSITY_400*/DENSITY_400) {
            return DisplayMetrics.DENSITY_XHIGH;
        } else {
            return DisplayMetrics.DENSITY_XXHIGH;
        }
    }

    /**
     * 获取ActionBar高度
     * @param context 上下文资源
     * @return int
     */
    public static int getActionBarSize(Context context) {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    /**
     * 是否为竖屏
     * @return true/false
     */
    public static boolean isPortrait() {
        return sConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT
                || (sConfiguration.orientation == Configuration.ORIENTATION_UNDEFINED && getHeightPixels() > getWidthPixels());
    }
}
