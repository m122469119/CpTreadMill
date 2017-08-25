package com.liking.treadmill.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created on 2017/08/25
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class TypefaceHelper {

    public static void setImpactFont(Context context, TextView view) {
        if(view != null) {
            view.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Impact.ttf"));
        }
    }
}
