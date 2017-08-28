package com.liking.treadmill.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;

/**
 * Created on 2017/08/16
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class RunwayView extends View {

    private Paint mPaint;

    private Path mPathWay;
    private Path mPathDash;

    private int wayWidth;
    private int wayHeight;

    private int lineWayWidth = 24;

    private int bottomHeight = 30;

    private int phase = 0;

    private Handler handler;

    private static final int MESSAGE_WHAT = 0x123;

    private LinearGradient mLinearGradient;

    private DashPathEffect mDashPathEffect;

    private Matrix mMatrixDash;

    private float[] dashFloat;

    private float[] srcDash;

    private float[] dstDash;

    private Matrix mMatrixAdv;

    private float[] dstAdv;

    private int leftImgH = 0;

    private Bitmap leftImgBitmap;

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mPathWay = new Path();
        mPathDash = new Path();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_WHAT) {
                    // 改变偏移值
                    phase = phase - 6;

                    if (phase < -(wayHeight + (14 * 30))) {
                        phase = 0;
                    }

//                    leftImgH = leftImgH + 8;
//                    if (leftImgH >= wayHeight) {
//                        leftImgH = 0;
//                    }

                    invalidate();
                    startRun();
                }

            }
        };

//        startRun();
//        leftImgBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
//        leftImgH = -leftImgBitmap.getHeight();
    }

    public RunwayView(Context context) {
        super(context);
        init();
    }

    public RunwayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RunwayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        wayWidth = getWidth();
        wayHeight = getHeight();

        dashFloat = new float[]{wayHeight / 14, 30};

        wayHeight = wayHeight - bottomHeight;

        //绘制梯形
        mPathWay.moveTo(wayWidth * 0.25f, 0);
        mPathWay.lineTo(wayWidth - (wayWidth * 0.25f), 0);
        mPathWay.lineTo(wayWidth, wayHeight);
        mPathWay.lineTo(0, wayHeight);

        //渐变
        mLinearGradient = new LinearGradient(wayWidth, 0, wayWidth, wayHeight, getColor(R.color.c3CBE96), getColor(R.color.c46C86E), Shader.TileMode.CLAMP);

//        //广告logo
//        mMatrixAdv = new Matrix();
//        dstAdv = new float[] { wayWidth * 0.25f, leftImgH, wayWidth - (wayWidth * 0.25f), leftImgH, wayWidth, wayHeight , 0, wayHeight };

        //虚线
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPathDash.moveTo(wayWidth / 2, 0);
        mPathDash.lineTo(wayWidth / 2, wayHeight);

        mMatrixDash = new Matrix();
        srcDash = new float[]{0, 0, wayWidth, 0, wayWidth, wayHeight, 0, wayHeight};
        dstDash = new float[]{wayWidth * 0.35f, 0, wayWidth - (wayWidth * 0.35f), 0, wayWidth, wayHeight , 0, wayHeight};
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("info", "left:" + getLeft() + ";right:" + getRight() + ";top:" + getTop() + ";bottom:" + getBottom());
        Log.e("info", "w:" + getWidth() + "h:" + getHeight());

        mPaint.reset();
        mPaint.setShader(mLinearGradient);
        canvas.drawPath(mPathWay, mPaint);
        mPaint.setShader(null);

        //绘制log
//        mMatrixAdv.setPolyToPoly(srcDash, 0, dstAdv, 0, srcDash.length >> 1);
//        canvas.setMatrix(mMatrixAdv);
//        canvas.drawBitmap(leftImgBitmap, wayWidth * 0.25f, leftImgH, mPaint);
//        canvas.setMatrix(null);

        /* 路线 */
        mDashPathEffect = new DashPathEffect(dashFloat, phase);
        mPaint.setPathEffect(mDashPathEffect);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWayWidth);

        mMatrixDash.setPolyToPoly(srcDash, 0, dstDash, 0, srcDash.length >> 1);
        canvas.setMatrix(mMatrixDash);
        canvas.drawPath(mPathDash, mPaint);
        mPaint.setPathEffect(null);
        canvas.setMatrix(null);

        //底部矩形
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getColor(R.color.c41B361));
        canvas.drawRect(0, wayHeight, wayWidth, wayHeight + bottomHeight, mPaint);

        mDashPathEffect = null;
    }


    public void startRun() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                handler.sendEmptyMessage(MESSAGE_WHAT);
//            }
//        }, 40);
    }

    public void stopRun() {
//        handler.removeMessages(MESSAGE_WHAT);
    }

    private int getColor(int cid) {
        return ResourceUtils.getColor(cid);
    }
}
