package com.liking.treadmill.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
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
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created on 2017/08/16
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class RunwayView extends View {

    private float DEFAULT_SCALE = 1.45f;
    // 默认宽度
    private int DEFAULT_WIDTH = 834;
    // 默认高度
    private int DEFAULT_HEIGHT = 574;

    private int gradient_start_color = Color.parseColor("#3CBE96");

    private int gradient_end_color = Color.parseColor("#46C86E");

    private Paint paint;

    private Camera camera;
    private Matrix matrixCamera;

    private float degree = 40.0f;

    int wayWidth;
    int wayHeight;

    LinearGradient linearGradient;

    int phase = 0;

    int dashWidth = 26;
    int dashHeight = 60;//路线高度
    int dashGap = 40;//路线间隔

    int wayLeft;
    int wayTop;
    int wayRight;
    int wayBottom;

    int clipWidth;
    int clipheight;

    private Path mPathWay;
    private int bottomHeight = 26;

    private ObjectAnimator animator = ObjectAnimator.ofInt(this, "phase", 0, dashHeight + dashGap);

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        camera = new Camera();
        matrixCamera = new Matrix();

        mPathWay = new Path();

        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = -displayMetrics.density * 6;
        camera.setLocation(0, 0, newZ);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) (heightSpecSize * DEFAULT_SCALE), heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) (widthSpecSize / DEFAULT_SCALE));
        }
    }

    private int width;
    private int height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = getWidth();
        height = getHeight();

//        clipWidth = width / 10;
//        clipheight = height / 3;

        wayWidth = width - clipWidth;
        wayHeight = height - clipheight - bottomHeight;

        wayLeft = clipWidth;
        wayTop = clipheight;
        wayRight = wayWidth;
        wayBottom = wayHeight;

        //绘制梯形
        mPathWay.moveTo(wayWidth * 0.32f, 0);
        mPathWay.lineTo(wayWidth - (wayWidth * 0.32f), 0);
        mPathWay.lineTo(wayWidth, wayHeight);
        mPathWay.lineTo(0, wayHeight);

        //渐变
        linearGradient = new LinearGradient(wayLeft, wayTop, wayRight, wayBottom, gradient_start_color, gradient_end_color, Shader.TileMode.CLAMP);
    }

    @SuppressWarnings("unused")
    public void setPhase(int phase) {
        this.phase = phase;
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        int centerX = width / 2;
        int centerY = height / 2;

        paint.reset();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        // 划背景
        paint.setShader(linearGradient);
        canvas.drawPath(mPathWay, paint);
        paint.setShader(null);

        //底部矩形
        paint.setColor(ResourceUtils.getColor(R.color.c41B361));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, wayHeight, wayWidth, wayHeight + bottomHeight, paint);

        //旋转图层
        camera.save();
        matrixCamera.reset();
        camera.rotateX(degree);
        camera.getMatrix(matrixCamera);
        camera.restore();
        matrixCamera.preTranslate(-centerX, -centerY);
        matrixCamera.postTranslate(centerX, centerY);
        canvas.save();
        canvas.concat(matrixCamera);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dashWidth);

        int r = wayHeight % (dashHeight + dashGap);
        int count = wayHeight / (dashHeight + dashGap);
        int dashCount = r == 0 ? count : count + 1;
        for (int i = -3; i < dashCount; i++) {
            int startX = width / 2;
            int startY = phase + (dashHeight + dashGap) * i;
            int stopX = width / 2;
            int stopY = phase + (dashHeight + dashGap) * i + dashHeight;

            //去超出部分
            if (startY < wayTop - 200) {
                if (stopY > wayTop - 200) {
                    startY = wayTop - 200;
                } else {
                    startY = wayTop;
                    stopY = wayTop;
                }
            }
            if (startY > wayHeight - 2) {
                startY = wayHeight - 2;
            }
            if (stopY > wayHeight - 2) {
                stopY = wayHeight - 2;
            }
//            Log.e("info", "startY:" + startY + ";stopY:" + stopY);
            //划线
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void startRun() {
        animator.start();
    }

    public void stopRun() {
        animator.end();
    }

    @Override
    protected void onVisibilityChanged( View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == GONE) {
            animator.end();
        } else  if(visibility == VISIBLE){
            animator.start();
        }
    }

    public void gearShift(int level) {
        animator.setDuration(1200 / level);
    }

}
