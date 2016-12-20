package com.liking.treadmill.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liking.treadmill.R;
import com.liking.treadmill.app.LikingThreadMillApplication;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:22
 * version 1.0.0
 */

public class IToast {


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
        //获取TextView
        TextView title = (TextView) view.findViewById(R.id.toast_TextView);
        //设置显示的内容
        title.setText(massage);
        Toast toast = new Toast(context);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
        //设置显示时间
        toast.setDuration(show_length);

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
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
        //设置显示时间
        toast.setDuration(show_length);

        toast.setView(view);
        toast.show();
    }
}
