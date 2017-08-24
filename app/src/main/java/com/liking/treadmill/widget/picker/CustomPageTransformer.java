package com.liking.treadmill.widget.picker;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by pavan on 25/04/17.
 */

import android.content.Context;
import android.widget.TextView;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.liking.treadmill.R;

public class CustomPageTransformer implements ViewPager.PageTransformer {

    private ViewPager viewPager;

    private Context context;

    public CustomPageTransformer(Context context) {
        this.context = context;
    }

    public void transformPage(View view, float position) {
        if (viewPager == null) {
            viewPager = (ViewPager) view.getParent();

        }

        view.setScaleY(1 - Math.abs(position));
        view.setScaleX(1 - Math.abs(position));
        view.setAlpha(1 - Math.abs(position));

        TextView textView = (TextView) view.findViewById(R.id.tv);
        int count = viewPager.getAdapter().getCount();
        if (textView != null) {
            float pos = Math.abs(position) * count;
            if (pos > 1) pos = 1;
            textView.setTextColor(getCurrentColor(pos,
                    ContextCompat.getColor(context, R.color.text_color_green),
                    ContextCompat.getColor(context, R.color.text_color_grey)));
        }
    }


    private int getCurrentColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }


}
