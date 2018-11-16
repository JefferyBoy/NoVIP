package com.novip.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.novip.R;
import com.novip.utils.DensityUtils;

public class CountDownView extends View {
    //圆轮颜色
    private int mRingColor;
    //圆轮宽度
    private float mRingWidth;
    //圆轮进度值文本大小
    private int mRingProgessTextSize;
    //宽度
    private int mWidth;
    //高度
    private int mHeight;
    private Paint mPaint;
    //圆环的矩形区域
    private RectF mRectF;
    //
    private int mProgessTextColor;
    private int mCountdownTime;
    private float mCurrentProgress;
    private OnCountDownFinishListener mListener;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mRingColor = a.getColor(R.styleable.CountDownView_ringColor, context.getResources().getColor(android.R.color.white));
        mRingWidth = a.getFloat(R.styleable.CountDownView_ringWidth, 6);
        mRingProgessTextSize = a.getDimensionPixelSize(R.styleable.CountDownView_progressTextSize, DensityUtils.sp2px(context, 10));
        mProgessTextColor = a.getColor(R.styleable.CountDownView_progressTextColor, context.getResources().getColor(android.R.color.white));
        mCountdownTime = a.getInteger(R.styleable.CountDownView_countdownTime, 3);
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        this.setWillNotDraw(false);
    }


    public void setCountdownTime(int mCountdownTime) {
        this.mCountdownTime = mCountdownTime;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = 2 * getMeasuredWidth() / 3;
        mHeight = 2 * getMeasuredHeight() / 3;
        mRectF = new RectF(0 + mRingWidth / 3, 0 + mRingWidth / 3,
                mWidth - mRingWidth / 3, mHeight - mRingWidth / 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         *圆环
         */
        //颜色
        mPaint.setColor(mRingColor);
        //空心
        mPaint.setStyle(Paint.Style.STROKE);
        //宽度
        mPaint.setStrokeWidth(mRingWidth);
        canvas.drawArc(mRectF, -90, mCurrentProgress - 360, false, mPaint);

        //绘制圆形，此处绘制的是图中红色区域
        Paint circlePaint=new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getResources().getColor(R.color.gray_7d7d7d));
        circlePaint.setAlpha(180);
        canvas.drawCircle(50,50,(mWidth/2-6),circlePaint);

        //绘制文本，可以根据需求进行更改，例如倒计时几秒
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
//        String text = mCountdownTime - (int) (mCurrentProgress / 360f * mCountdownTime) + "";
        String text = "跳过";
        textPaint.setTextSize(mRingProgessTextSize);
        textPaint.setColor(mProgessTextColor);

        //文字居中显示
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (int) ((mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(text, mRectF.centerX(), baseline, textPaint);


    }

    private ValueAnimator getValA(long countdownTime) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(countdownTime);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;
    }

    /**
     * 开始倒计时
     */
    public void startCountDown() {
        setClickable(false);
        ValueAnimator valueAnimator = getValA(mCountdownTime * 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                mCurrentProgress = (int) (360 * (i / 100f));
                invalidate();
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //倒计时结束回调
                if (mListener != null) {
                    mListener.countDownFinished();
                }
                setClickable(true);
            }

        });
    }
    //倒计时监听，可在countDownFinished（）方法中进行倒计时结束后的逻辑
    public void setAddCountDownListener(OnCountDownFinishListener mListener) {
        this.mListener = mListener;
    }

    public interface OnCountDownFinishListener {
        void countDownFinished();
    }
}
