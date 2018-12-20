package com.example.perry.yoursidesystem.fragment.bodytestfragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.perry.yoursidesystem.R;
import com.example.perry.yoursidesystem.test.LogUtil;

/**
 * Created by yang_zzheng on 2016/7/12
 * yangzhizheng2012@163.com
 */
public class CirclePgBar extends View {


    private Paint mBackPaint;
    private Paint mFrontPaint;
    private Paint mTextPaint;
    //    private float mStrokeWidth = 40;
    private float mStrokeWidth = 20;
    private float mHalfStrokeWidth = mStrokeWidth / 2;
    //    private float mRadius = 100;
    private float mRadius = 70;
    private RectF mRect;
    private float mProgress = 0;
    //目标值，想改多少就改多少
    private float targetProgress = 10;
    private float mMax = 100;
    private int mWidth;
    private int mHeight;
    private String unit = "";
    private int barColor = Color.GREEN;
    private int textColor = barColor;


    public void setParameter(float max, float
            targetProgress, int braColor, String unit) {
        this.mMax = max;
        this.targetProgress = targetProgress;
        this.barColor = braColor;
        textColor = braColor;
        this.unit = unit;
        LogUtil.w("tag", "max:" + this.mMax + ",targetProgress:" + this.targetProgress 
                + "," + "braColor:" + this.barColor + ",unit:" + this.unit);
        init();
        requestLayout();
        invalidate();

    }


    public CirclePgBar(Context context) {
        super(context);
        init();
    }

    public CirclePgBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CirclePgBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    //完成相关参数初始化
    private void init() {
        mBackPaint = new Paint();
        mBackPaint.setColor(Color.WHITE);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.STROKE);
//        mBackPaint.setStrokeWidth(mStrokeWidth);

        mFrontPaint = new Paint();
        mFrontPaint.setColor(barColor);
        mFrontPaint.setAntiAlias(true);
        mFrontPaint.setStyle(Paint.Style.STROKE);
//        mFrontPaint.setStrokeWidth(mStrokeWidth);


        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
//        mtextpaint.settextsize(16);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }


    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    int measureCount = 1;

    //重写测量大小的onMeasure方法和绘制View的核心方法onDraw()
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.w("bar", "measure调用" + (measureCount++));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getRealSize(widthMeasureSpec);
        mHeight = getRealSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        mRadius=(Math.min(mWidth,mHeight)-dp2px(getContext(),10))/2.15f;
        mStrokeWidth=mRadius/4;
        mHalfStrokeWidth=mStrokeWidth / 2;
        mBackPaint.setStrokeWidth(mStrokeWidth);
        mFrontPaint.setStrokeWidth(mStrokeWidth);
        mTextPaint.setTextSize(mStrokeWidth);
        Log.w("bar", mWidth + ":" + mHeight+";radius:"+mRadius);
        Log.w("bar", "10dp=" + dp2px(getContext(), 10));
        Log.w("b3r", "Min:" + Math.min(mWidth, mHeight));

    }

    int drawCount = 1;

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.w("tag", "max2:" + this.mMax + ",targetProgress2:" + this
                .targetProgress + "," +
                "braColor2:" + this.barColor + ",unit2:" + this.unit);
        initRect();
        float angle = mProgress / mMax * 360;
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBackPaint);
        canvas.drawArc(mRect, -90, angle, false, mFrontPaint);
        int progress;
        if (!unit.equals("℃")) {
            progress = (int) mProgress;
            canvas.drawText(progress + unit, mWidth / 2 + mHalfStrokeWidth / 3, mHeight
                    / 2 + mHalfStrokeWidth, mTextPaint);
        } else {
            canvas.drawText(mProgress + unit, mWidth / 2 + mHalfStrokeWidth / 3,
                    mHeight / 2 + mHalfStrokeWidth, mTextPaint);
        }

        if (mProgress < targetProgress) {
            mProgress ++;
            invalidate();
        }
        if(mProgress>targetProgress){
            mProgress--;
            invalidate();
        }
        
        
//        Log.w("bar", "draw调用" + (drawCount++));

    }

    public int getRealSize(int measureSpec) {
        int result = 1;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            //自己计算
            result = size;
        }

        return result;
    }

    private void initRect() {
        if (mRect == null) {
            mRect = new RectF();
            int viewSize = (int) (mRadius * 2);
            int left = (mWidth - viewSize) / 2;
            int top = (mHeight - viewSize) / 2;
            int right = left + viewSize;
            int bottom = top + viewSize;
            mRect.set(left, top, right, bottom);
        }
    }
}