package io.julian.imitate.jikelike.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.julian.imitate.jikelike.R;

/**
 * @author Zhu Liang
 */

public class FavorView extends ViewGroup {

    private static final float SHINING_SELECTED_DETAL = 1.25F;
    private static final float SHINING_RING_DETAL = 1.5F;
    private static final float SHINING_CONTENT_DETAL = 2.25F;

    private ImageView mLikeSelected;
    private ImageView mLikeShining;
    private View mLikeRing;

    public FavorView(Context context) {
        this(context, null);
    }

    public FavorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        createLikeView();
    }

    private void createLikeView() {
        mLikeSelected = new ImageView(getContext());
        mLikeSelected.setImageResource(R.drawable.ic_messages_like_selected);
        addView(mLikeSelected);

        mLikeShining = new ImageView(getContext());
        mLikeShining.setImageResource(R.drawable.ic_messages_like_selected_shining);
        addView(mLikeShining);

        mLikeRing = new View(getContext());
        addView(mLikeRing);
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

}
