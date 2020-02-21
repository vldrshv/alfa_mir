package ru.alfabank.alfamir.ui.custom_views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


public class SplashProgressBar  extends View {

    private int mSize;
    private Paint mPaint;
    private RectF mRect;
    private float mIndeterminateSweep;
    private float mStartAngle;
    private float strokeWidth = 8;

    public SplashProgressBar(Context context) {
        super(context);
        init();
    }

    public SplashProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SplashProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#1875f0"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8f);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mIndeterminateSweep = 10f;

    }



    private void animateArch() {
        final ValueAnimator frontEndExtend = ValueAnimator.ofFloat(0, 360);
        frontEndExtend.setDuration(3000);
        frontEndExtend.setInterpolator(new LinearInterpolator());
        frontEndExtend.addUpdateListener(animation -> {
            mStartAngle = (Float) animation.getAnimatedValue();
            mIndeterminateSweep += 2;
            if (mIndeterminateSweep > 360)
                mIndeterminateSweep = 15f;
            invalidate();
        });
        frontEndExtend.start();
        frontEndExtend.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animateArch();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRect, mStartAngle, mIndeterminateSweep, false, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mRect.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);

//        int xPad = getPaddingLeft() + getPaddingRight();
//        int yPad = getPaddingTop() + getPaddingBottom();
//        int width = getMeasuredWidth() - xPad;
//        int height = getMeasuredHeight() - yPad;
//        mSize = (width < height) ? width : height;
//
//        setMeasuredDimension(mSize + xPad, mSize + yPad);
    }

    @Override
    protected void onLayout(boolean changed,int left,int top,int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animateArch();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
