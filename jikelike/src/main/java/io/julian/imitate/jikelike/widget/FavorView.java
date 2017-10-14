package io.julian.imitate.jikelike.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.julian.imitate.jikelike.R;

/**
 * @author Zhu Liang
 */

public class FavorView extends ViewGroup {

    private static final String TAG = "FavorView";

    private ImageView mLikeSelected;
    private ImageView mLikeSelectedShining;
    private ImageView mLikeUnselected;

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
        mLikeSelectedShining = new ImageView(getContext());
        mLikeSelectedShining.setImageResource(R.drawable.ic_messages_like_selected_shining);
        addView(mLikeSelectedShining);

        mLikeSelected = new ImageView(getContext());
        mLikeSelected.setImageResource(R.drawable.ic_messages_like_selected);
        addView(mLikeSelected);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final int pleft = getPaddingLeft();
        final int ptop = getPaddingTop();
        final int pright = getPaddingRight();
        final int pbottom = getPaddingBottom();

        int widthSize;
        int heightSize;
        if (widthMode == MeasureSpec.AT_MOST
                && heightMode == MeasureSpec.AT_MOST) {

            measureChildren(widthMeasureSpec, heightMeasureSpec);

            int selectedWidth = mLikeSelected.getMeasuredWidth();
            int selectedHeight = mLikeSelected.getMeasuredHeight();
            int shiningWidth = mLikeSelectedShining.getMeasuredWidth();
            int shiningHeight = mLikeSelectedShining.getMeasuredHeight();
            widthSize = Math.max(selectedWidth, shiningWidth) + pleft + pright;
            heightSize = selectedHeight + shiningHeight / 2 + ptop + pbottom;
        } else {
            throw new UnsupportedOperationException("暂时不支持EXACTLY");
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();

        final int selectedShiningLeft = (width - mLikeSelectedShining.getMeasuredWidth()) / 2;
        final int selectedShiningTop = childTop;
        final int selectedShiningWidth = mLikeSelectedShining.getMeasuredWidth();
        final int selectedShiningHeight = mLikeSelectedShining.getMeasuredHeight();
        mLikeSelectedShining.layout(selectedShiningLeft, selectedShiningTop,
                selectedShiningLeft + selectedShiningWidth,
                selectedShiningTop + selectedShiningHeight);

        final int selectedLeft = childLeft;
        final int selectedTop = childTop + mLikeSelectedShining.getMeasuredHeight() / 2;
        final int selectedWidth = mLikeSelected.getMeasuredWidth();
        final int selectedHeight = mLikeSelected.getMeasuredHeight();
        mLikeSelected.layout(selectedLeft, selectedTop, selectedLeft + selectedWidth,
                selectedTop + selectedHeight);
    }
}
