package com.liking.treadmill.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.liking.treadmill.R;


public class ColorfulRingProgressView extends View {


    private float mPercent = 75;
    private float mStrokeWidth;

    private int mBgColor = Color.parseColor("#BABCC3");
    private float mStartAngle = 0;
    private int mFgColorStart = 0xffffe400;
    private int mFgColorEnd = 0xffff4800;
//    private BitmapDrawable mStartTagDrawable = null;

    private LinearGradient mShader;
    private Context mContext;
    private RectF mOval_bg;
    private RectF mOval;
    private Paint mPaint;
    private Paint mPaintTag;


    public ColorfulRingProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ColorfulRingProgressView,
                0,0);

        try {
            mBgColor = a.getColor(R.styleable.ColorfulRingProgressView_bgColor, 0xffe1e1e1);
            mFgColorEnd = a.getColor(R.styleable.ColorfulRingProgressView_fgColorEnd, 0xffff4800);

            mFgColorStart = a.getColor(R.styleable.ColorfulRingProgressView_fgColorStart, 0xffffe400);
            mPercent = a.getFloat(R.styleable.ColorfulRingProgressView_percent, 75);
            mStartAngle = a.getFloat(R.styleable.ColorfulRingProgressView_startAngle, 0)+270;
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.ColorfulRingProgressView_colorfulStrokeWidth, dp2px(21));
//            mStartTagDrawable = (BitmapDrawable) a.getDrawable(R.styleable.ColorfulRingProgressView_startTagImg);
            System.out.println("**** m"+mStrokeWidth);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintTag = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private int dp2px(float dp) {
        return (int) (mContext.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setShader(null);
        mPaint.setColor(mBgColor);
        canvas.drawArc(mOval, 0, 360, false, mPaint);
        mPaint.setShader(mShader);
        canvas.drawArc(mOval, mStartAngle, mPercent * 3.6f, false, mPaint);

//        if(mStartTagDrawable != null) {
//            float r = mOval.width() / 2;
//            int x = (int) (mOval.centerX()   +   r   *   Math.cos(mStartAngle   *   3.14   / 180 ));
//            int y = (int) (mOval.centerY()   +   r   *  Math.sin(mStartAngle   *   3.14   / 180 ));
//            canvas.drawCircle(Math.abs(x), Math.abs(y) , mStrokeWidth / 2 + 6 , mPaintTag);
//            Bitmap bitmap = mStartTagDrawable.getBitmap();
//            canvas.drawBitmap(bitmap, Math.abs(x) - (bitmap.getWidth() / 2),
//                    Math.abs(y) - (bitmap.getHeight() / 2), mPaintTag);
//
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateOval();
        mShader = new LinearGradient(mOval.left, mOval.top,
                mOval.left, mOval.bottom, mFgColorStart, mFgColorEnd, Shader.TileMode.MIRROR);
    }

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float mPercent) {
        this.mPercent = mPercent;
        refreshTheLayout();
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        mPaint.setStrokeWidth(mStrokeWidth);
        updateOval();
        refreshTheLayout();
    }

    private void updateOval() {
        int xp = getPaddingLeft() + getPaddingRight();
        int yp = getPaddingBottom() + getPaddingTop();
        mOval_bg = new RectF(getPaddingLeft() + (mStrokeWidth - 6), getPaddingTop() + (mStrokeWidth - 6),
                getPaddingLeft() + (getWidth() - xp) - (mStrokeWidth - 6),
                getPaddingTop() + (getHeight() - yp) - (mStrokeWidth - 6));
        mOval = new RectF(getPaddingLeft() + mStrokeWidth, getPaddingTop() + mStrokeWidth,
                getPaddingLeft() + (getWidth() - xp) - mStrokeWidth,
                getPaddingTop() + (getHeight() - yp) - mStrokeWidth);
    }

    public void setStrokeWidthDp(float dp) {
        this.mStrokeWidth = dp2px(dp);
        mPaint.setStrokeWidth(mStrokeWidth);
        updateOval();
        refreshTheLayout();
    }

    public void refreshTheLayout() {
        invalidate();
        requestLayout();
    }

    public void setProgressColor(int bgColor, int fgColorStart, int fgColorEnd) {
        this.mBgColor = bgColor;
        this.mFgColorStart = fgColorStart;
        this.mFgColorEnd = fgColorEnd;
        updateOval();
        mShader = new LinearGradient(mOval.left, mOval.top,
                mOval.left, mOval.bottom, mFgColorStart, mFgColorEnd, Shader.TileMode.MIRROR);
        refreshTheLayout();
    }

    public int getFgColorStart() {
        return mFgColorStart;
    }

    public void setFgColorStart(int mFgColorStart) {
        this.mFgColorStart = mFgColorStart;
        updateOval();
        mShader = new LinearGradient(mOval.left, mOval.top,
                mOval.left, mOval.bottom, mFgColorStart, mFgColorEnd, Shader.TileMode.MIRROR);
        refreshTheLayout();
    }

    public int getFgColorEnd() {
        return mFgColorEnd;
    }

    public void setFgColorEnd(int mFgColorEnd) {
        this.mFgColorEnd = mFgColorEnd;
        updateOval();
        mShader = new LinearGradient(mOval.left, mOval.top,
                mOval.left, mOval.bottom, mFgColorStart, mFgColorEnd, Shader.TileMode.MIRROR);
        refreshTheLayout();
    }


    public float getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle + 270;
        refreshTheLayout();
    }
}
