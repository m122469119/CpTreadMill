package com.liking.treadmill.widget;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.android.framework.utils.DisplayUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.app.LikingThreadMillApplication;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:22
 * version 1.0.0
 */

public class IToast {


    private static Toast toast;

    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            if(toast != null) {
                toast.cancel();
                toast = null;
            }
        }
    };
    /**
     * 展示toast==LENGTH_SHORT
     *
     * @param msg
     */
    public static void show(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 展示toast==LENGTH_LONG
     *
     * @param msg
     */
    public static void showLong(String msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    public static void showLong(int msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    private static void show(String massage, int show_length) {
        Context context = LikingThreadMillApplication.getInstance().getApplicationContext();
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtils.getWidthPixels() - 700, ViewGroup.LayoutParams.MATCH_PARENT);
        //获取TextView
        RelativeLayout toastLayout = (RelativeLayout)view.findViewById(R.id.toast_layout);
        toastLayout.setLayoutParams(params);
        TextView title = (TextView) view.findViewById(R.id.toast_TextView);
        //设置显示的内容
        title.setText(massage);
        mHandler.removeCallbacks(r);
        if (toast == null) {
            toast = new Toast(context);
            //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 100);
            toast.setDuration(show_length);
        }
        mHandler.postDelayed(r, 2000);
        //设置显示时间
        toast.setView(view);
        toast.show();
    }

    private static void show(int massage, int show_length) {
        Context context = LikingThreadMillApplication.getInstance().getApplicationContext();
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        //获取TextView
        TextView title = (TextView) view.findViewById(R.id.toast_TextView);
        //设置显示的内容
        title.setText(massage);
        Toast toast = new Toast(context);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 100);
        //设置显示时间
        toast.setDuration(show_length);

        toast.setView(view);
        toast.show();
    }
}
