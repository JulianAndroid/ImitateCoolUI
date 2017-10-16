package io.julian.imitate.jikelike.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import io.julian.imitate.jikelike.util.DimenUtils;

/**
 * @author Zhu Liang
 */

public class LikeRingView extends View {

    private static final String TAG = "LikeRingView";

    private Paint mRingPaint = new Paint();

    private int mCurrentColor = Color.parseColor("#E05846");
    private float mCurrentScale = 0;

    private AnimatorSet mAnimatorSet;

    public LikeRingView(Context context) {
        this(context, null);
    }

    public LikeRingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(DimenUtils.dpToPx(2));

        createAnimator();
    }

    private void createAnimator() {
        ValueAnimator colorAnimator = ValueAnimator.ofInt(255, 0);
        /*colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setDuration(1000);*/
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                mCurrentColor = Color.argb(alpha, 224, 88, 70);
                Log.d(TAG, "mCurrentColor: " + mCurrentColor);
                invalidate();
            }
        });

        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0, 1.0f);
        /*scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimator.setDuration(1000);*/
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentScale = (float) animation.getAnimatedValue();
                Log.d(TAG, "mCurrentScale: " + mCurrentScale);
            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(colorAnimator, scaleAnimator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int widthSize = getMeasuredWidth();
        final int heightSize = getMeasuredHeight();

        final int cx = widthSize / 2;
        final int cy = heightSize / 2;
        final int radius = Math.min(widthSize, heightSize) / 2;

        mRingPaint.setColor(mCurrentColor);

        canvas.drawCircle(cx, cy, radius * mCurrentScale, mRingPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        endAnimatorSet();
    }

    public void startAnimatorSet() {
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    public void endAnimatorSet() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }
}
