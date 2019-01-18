/**
* @FileName CircleProgressBar.java
* @Package com.read.view
* @Description TODO
* @Author Alpha
* @Date 2015-7-30 ����4:52:24 
* @Version V1.0

*/
package com.itg.ui.view;


import com.itg.iguide.R;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class CircleProgressBar extends View {

	 private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
	    private  final Interpolator SWEEP_INTERPOLATOR = new AccelerateDecelerateInterpolator();
	    private static final int ANGLE_ANIMATOR_DURATION = 1300;
	    private static final int SWEEP_ANIMATOR_DURATION = 600;
	    private static final int MIN_SWEEP_ANGLE = 30;
	    private static final int DEFAULT_BORDER_WIDTH = 2;
	    private final RectF fBounds = new RectF();

	    private ObjectAnimator mObjectAnimatorSweep;
	    private ObjectAnimator mObjectAnimatorAngle;
	    private boolean mModeAppearing = true;
	    private Paint mPaint;
	    private float mCurrentGlobalAngleOffset;
	    private float mCurrentGlobalAngle;
	    private float mCurrentSweepAngle;
	    private float mBorderWidth;
	    private boolean mRunning;
	    private int[] mColors;


	    public CircleProgressBar(Context context) {
	        this(context, null);
	    }

	    public CircleProgressBar(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
	        super(context, attrs, defStyleAttr);

	        float density = context.getResources().getDisplayMetrics().density;
	        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgress, defStyleAttr, 0);
	        mBorderWidth = a.getDimension(R.styleable.CircularProgress_borderWidth,
	                DEFAULT_BORDER_WIDTH * density);
	        a.recycle();
	        
	        mColors = new int[2];
	        mColors[0] = context.getResources().getColor(R.color.wait_circle);
	        mPaint = new Paint();
	        mPaint.setAntiAlias(true);
	        mPaint.setStyle(Paint.Style.STROKE);
	        mPaint.setStrokeCap(Cap.ROUND);
	        mPaint.setStrokeWidth(mBorderWidth);
	        mPaint.setColor(mColors[0]);

	        setupAnimations();
	    }

	    private void start() {
	        if (mRunning) {
	            return;
	        }
	        mRunning = true;
	        mObjectAnimatorAngle.start();
	        mObjectAnimatorSweep.start();
	        invalidate();
	    }

	    private void stop() {
	        if (!mRunning) {
	            return;
	        }
	        mRunning = false;
	        mObjectAnimatorAngle.cancel();
	        mObjectAnimatorSweep.cancel();
	        invalidate();
	    }

	

	    @Override
	    protected void onVisibilityChanged(View changedView, int visibility) {
	        super.onVisibilityChanged(changedView, visibility);
	        if (visibility == VISIBLE) {
	            start();
	        } else {
	            stop();
	        }
	    }

	    @Override
	    protected void onAttachedToWindow() {
	        start();
	        super.onAttachedToWindow();
	    }

	    @Override
	    protected void onDetachedFromWindow() {
	        stop();
	        super.onDetachedFromWindow();
	    }
	    @Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        super.onSizeChanged(w, h, oldw, oldh);
	       
	        fBounds.left = mBorderWidth / 2f + .5f;
	        fBounds.right = w - mBorderWidth / 2f - .5f;
	        fBounds.top = mBorderWidth / 2f + .5f;
	        fBounds.bottom = h - mBorderWidth / 2f - .5f;

	    }

	    @Override
	    public void draw(Canvas canvas) {
	        super.draw(canvas);
	        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
	        float sweepAngle = mCurrentSweepAngle;
	        if (mModeAppearing) {
	            mPaint.setColor(mColors[0]);
	            sweepAngle += MIN_SWEEP_ANGLE;
	        } else {
	            startAngle = startAngle + sweepAngle;
	            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
	        }
	        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);//������
	    }


	    private void toggleAppearingMode() {
	        mModeAppearing = !mModeAppearing;
	        if (mModeAppearing) {
//	            mCurrentColorIndex = ++mCurrentColorIndex % 4;
//	            mNextColorIndex = ++mNextColorIndex % 4;
	            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
	        }
	    }
	    // ////////////////////////////////////////////////////////////////////////////
	    // ////////////// Animation

	    private Property<CircleProgressBar, Float> mAngleProperty = new Property<CircleProgressBar, Float>(Float.class, "angle") {
	        @Override
	        public Float get(CircleProgressBar object) {
	            return object.getCurrentGlobalAngle();
	        }

	        @Override
	        public void set(CircleProgressBar object, Float value) {
	            object.setCurrentGlobalAngle(value);
	        }
	    };

	    private Property<CircleProgressBar, Float> mSweepProperty = new Property<CircleProgressBar, Float>(Float.class, "arc") {
	        @Override
	        public Float get(CircleProgressBar object) {
	            return object.getCurrentSweepAngle();
	        }

	        @Override
	        public void set(CircleProgressBar object, Float value) {
	            object.setCurrentSweepAngle(value);
	        }
	    };

	    private void setupAnimations() {
	        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
	        mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
	        mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
	        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.INFINITE);
	        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);

	        mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - MIN_SWEEP_ANGLE *2);
	        mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
	        mObjectAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
	        mObjectAnimatorSweep.setRepeatMode(ValueAnimator.INFINITE);
	        mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
	        mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
	            @Override
	            public void onAnimationStart(Animator animation) {

	            }

	            @Override
	            public void onAnimationEnd(Animator animation) {

	            }

	            @Override
	            public void onAnimationCancel(Animator animation) {

	            }

	            @Override
	            public void onAnimationRepeat(Animator animation) {
	                toggleAppearingMode();
	            }
	        });
	      
	    }

	    public void setCurrentGlobalAngle(float currentGlobalAngle) {
	        mCurrentGlobalAngle = currentGlobalAngle;
	        invalidate();
	    }

	    public float getCurrentGlobalAngle() {
	        return mCurrentGlobalAngle;
	    }

	    public void setCurrentSweepAngle(float currentSweepAngle) {
	        mCurrentSweepAngle = currentSweepAngle;
	        invalidate();
	    }

	    public float getCurrentSweepAngle() {
	        return mCurrentSweepAngle;
	    }
}
