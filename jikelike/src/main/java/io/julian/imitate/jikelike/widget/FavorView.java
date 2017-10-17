package io.julian.imitate.jikelike.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import io.julian.imitate.jikelike.R;

/**
 * @author Zhu Liang
 */

public class FavorView extends ViewGroup {

    private static final float SHINING_SELECTED_DETAL = 1.25F;
    private static final float SHINING_RING_DETAL = 1.5F;
    private static final float SHINING_CONTENT_DETAL = 2.25F;

    private static final long DEFAULT_DURATION = 200L;

    private ImageView mLikeSelected;
    private ImageView mLikeShining;
    private LikeRingView mLikeRing;

    private long mDuration = DEFAULT_DURATION;

    private boolean isLikeSelected;

    private PopTextView mPopTextView;

    public FavorView(Context context) {
        this(context, null);
    }

    public FavorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FavorView);
        try {
            isLikeSelected = ta.getBoolean(R.styleable.FavorView_likeSelected, false);
        } finally {
            ta.recycle();
        }
        createLikeView();
    }

    private void createLikeView() {
        mLikeSelected = new ImageView(getContext());
        addView(mLikeSelected);

        mLikeShining = new ImageView(getContext());
        mLikeShining.setImageResource(R.drawable.ic_messages_like_selected_shining);
        addView(mLikeShining);

        mLikeRing = new LikeRingView(getContext());
        addView(mLikeRing);

        if (isLikeSelected) {
            mLikeSelected.setImageResource(R.drawable.ic_messages_like_selected);
            mLikeShining.setVisibility(VISIBLE);
            mLikeRing.setVisibility(VISIBLE);
        } else {
            mLikeSelected.setImageResource(R.drawable.ic_messages_like_unselected);
            mLikeShining.setVisibility(INVISIBLE);
            mLikeRing.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final int pleft = getPaddingLeft();
        final int ptop = getPaddingTop();
        final int pright = getPaddingRight();
        final int pbottom = getPaddingBottom();

        if (widthMode == MeasureSpec.AT_MOST
                && heightMode == MeasureSpec.AT_MOST) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);

            final int shiningWidth = mLikeShining.getMeasuredWidth();
            final int shiningHeight = mLikeShining.getMeasuredHeight();
            final int ringWidth = (int) (shiningWidth * SHINING_RING_DETAL);
            final int ringMeasureWidth = MeasureSpec.makeMeasureSpec(ringWidth, MeasureSpec.EXACTLY);
            mLikeRing.measure(ringMeasureWidth, ringMeasureWidth);

            final int widthSize = (int) (shiningWidth * SHINING_CONTENT_DETAL) + pleft + pright;
            final int heightSize = (int) (shiningHeight * SHINING_CONTENT_DETAL) + ptop + pbottom;

            setMeasuredDimension(widthSize, heightSize);
        } else {
            final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            final int contentWidth = Math.min(widthSize - pleft - pright, heightSize - ptop - pbottom);
            final int ringWidth = (int) (contentWidth * SHINING_RING_DETAL / SHINING_CONTENT_DETAL);
            final int selectedWidth = (int) (contentWidth * SHINING_SELECTED_DETAL / SHINING_CONTENT_DETAL);
            final int shiningWidth = (int) (contentWidth / SHINING_CONTENT_DETAL);

            final int ringMeasureSpec, selectedMeasureSpec, shiningMeasureSpec;
            ringMeasureSpec = MeasureSpec.makeMeasureSpec(ringWidth, MeasureSpec.EXACTLY);
            selectedMeasureSpec = MeasureSpec.makeMeasureSpec(selectedWidth, MeasureSpec.EXACTLY);
            shiningMeasureSpec = MeasureSpec.makeMeasureSpec(shiningWidth, MeasureSpec.EXACTLY);

            mLikeRing.measure(ringMeasureSpec, ringMeasureSpec);
            mLikeSelected.measure(selectedMeasureSpec, selectedMeasureSpec);
            mLikeShining.measure(shiningMeasureSpec, shiningMeasureSpec);

            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();

        final int ringLeft = childLeft / 2 + (width - mLikeRing.getMeasuredWidth()) / 2;
        final int ringTop = childTop / 2 + (height - mLikeRing.getMeasuredHeight()) / 2;
        final int ringWidth = mLikeRing.getMeasuredWidth();
        final int ringHeight = mLikeRing.getMeasuredHeight();
        mLikeRing.layout(ringLeft, ringTop, ringLeft + ringWidth, ringTop + ringHeight);

        final int selectedWidth = mLikeSelected.getMeasuredWidth();
        final int selectedHeight = mLikeSelected.getMeasuredHeight();
        final int selectedLeft = childLeft / 2 + (width - selectedWidth) / 2;
        final int selectedTop = childTop / 2 + (height - selectedHeight) / 2;
        mLikeSelected.layout(selectedLeft, selectedTop, selectedLeft + selectedWidth,
                selectedTop + selectedHeight);

        final int shiningWidth = mLikeShining.getMeasuredWidth();
        final int shiningHeight = mLikeShining.getMeasuredHeight();
        final int shiningLeft = childLeft / 2 + (width - shiningWidth) / 2;
        final int shiningTop = selectedTop - shiningHeight / 2;
        mLikeShining.layout(shiningLeft, shiningTop, shiningLeft + shiningWidth,
                shiningTop + shiningHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown();
                break;
            case MotionEvent.ACTION_UP:
                toggle();
                break;
        }
        return true;
    }

    private void handleActionDown() {
        mLikeSelected.animate().scaleX(0.6f).scaleY(0.6f).setDuration(200).start();
        mLikeShining.setPivotX(mLikeShining.getMeasuredWidth() / 2);
        mLikeShining.setPivotY(mLikeShining.getMeasuredHeight());
        mLikeShining.animate().scaleX(0.6f).scaleY(0.6f).setDuration(200).start();
    }

    public void toggle() {
        setLikeSelected(!isLikeSelected);
    }

    public void setLikeSelected(boolean selected) {
        if (isLikeSelected != selected) {
            isLikeSelected = selected;

            if (selected) {
                mLikeSelected.setImageResource(R.drawable.ic_messages_like_selected);
                mLikeShining.setImageResource(R.drawable.ic_messages_like_selected_shining);
                mLikeSelected.setVisibility(VISIBLE);
                mLikeShining.setVisibility(VISIBLE);
                mLikeRing.setVisibility(VISIBLE);

                animateLikeSelected();
            } else {
                mLikeSelected.setImageResource(R.drawable.ic_messages_like_unselected);
                mLikeShining.setVisibility(INVISIBLE);
                mLikeRing.setVisibility(INVISIBLE);

                animateLikeUnselected();
            }
        }
    }

    private void animateLikeSelected() {
        ValueAnimator likeSelectedAnimator = ValueAnimator.ofFloat(1f);
        likeSelectedAnimator.setInterpolator(new OvershootInterpolator(4.0f));
        likeSelectedAnimator.setDuration(mDuration);
        likeSelectedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                mLikeSelected.setScaleX(scale);
                mLikeSelected.setScaleY(scale);
            }
        });

        ValueAnimator likeShiningAnimator = ValueAnimator.ofFloat(1f);
        likeShiningAnimator.setInterpolator(new OvershootInterpolator(4.0f));
        likeShiningAnimator.setDuration((long) (mDuration * 1.5));
        likeShiningAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                mLikeShining.setScaleX(scale);
                mLikeShining.setScaleY(scale);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(likeSelectedAnimator, likeShiningAnimator);
        animatorSet.start();
        mLikeRing.startAnimatorSet();
    }

    private void animateLikeUnselected() {
        ValueAnimator likeSelectedAnimator = ValueAnimator.ofFloat(0.7f, 1f);
        likeSelectedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                mLikeSelected.setScaleX(scale);
                mLikeSelected.setScaleY(scale);
            }
        });
        likeSelectedAnimator.start();
    }
}
