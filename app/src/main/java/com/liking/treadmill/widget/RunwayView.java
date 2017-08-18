package com.liking.treadmill.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    int wayWidth;
    int wayHeight;

    int lineWayWidth = 60;

    private int bottomHeight = 30;

    int phase = 0;

    Handler handler;

    LinearGradient mLinearGradient;

    DashPathEffect mDashPathEffect;

    Matrix mMatrixDash;

    float[] dashFloat;

    float[] srcDash;

    float[] dstDash;

    Matrix mMatrixAdv;

    float[] dstAdv;

    private int leftImgH = 0;

//    private Bitmap leftImgBitmap;

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mPathWay = new Path();
        mPathDash = new Path();

        handler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    // 改变偏移值
                    phase = phase - 8;

                    if (phase < -(wayHeight + (40 * 10))) {
                        phase = 0;
                    }

                    leftImgH = leftImgH + 8;
                    if (leftImgH >= wayHeight) {
                        leftImgH = 0;
                    }
                }
                invalidate();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x123);
                    }
                }, 40);
            }
        };

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                handler.sendEmptyMessage(0x123);
//            }
//        }, 40);


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

        dashFloat = new float[]{wayHeight / 10 , 40};

        wayHeight = wayHeight - bottomHeight;

        //绘制梯形
        mPathWay.moveTo(wayWidth * 0.25f, 0);
        mPathWay.lineTo(wayWidth - (wayWidth * 0.25f), 0);
        mPathWay.lineTo(wayWidth, wayHeight);
        mPathWay.lineTo(0, wayHeight);

        //渐变
        mLinearGradient = new LinearGradient(wayWidth, 0, wayWidth, wayHeight, getColor(R.color.c3CBE96), getColor(R.color.c46C86E), Shader.TileMode.CLAMP);

//        //广告
////        mMatrixAdv = new Matrix();
////        dstAdv = new float[] { width * 0.25f, leftImgH, width - (width * 0.25f), leftImgH, width, wayHeight , 0, wayHeight };

        //虚线
        mPathDash.moveTo(wayWidth / 2 , 0);
        mPathDash.lineTo(wayWidth / 2 , wayHeight);

        mMatrixDash = new Matrix();
        srcDash = new float[] { 0, 0, wayWidth, 0, wayWidth, wayHeight, 0, wayHeight };
        dstDash = new float[] { wayWidth * 0.35f, 0, wayWidth - (wayWidth * 0.35f), 0, wayWidth, wayHeight * 2f, 0, wayHeight * 2f};
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

//        //绘制log
//        mMatrixAdv.setPolyToPoly(srcDash, 0, dstAdv, 0, srcDash.length >> 1);
//        canvas.setMatrix(mMatrixAdv);
//        canvas.drawBitmap(leftImgBitmap, width * 0.25f, leftImgH, mPaint);
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
        canvas.setMatrix(null);

        //底部矩形
        mPaint.reset();
        mPaint.setColor(getColor(R.color.c41B361));
        canvas.drawRect(0, wayHeight, wayWidth, wayHeight, mPaint);

        mDashPathEffect = null;
    }


    public int getColor(int cid) {
        return getResources().getColor(cid);
    }
}
