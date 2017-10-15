package io.julian.imitate.jikelike.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
            widthSize = MeasureSpec.getSize(widthMeasureSpec);
            heightSize = MeasureSpec.getSize(heightMeasureSpec);
            final int contentWidth = widthSize - pleft - pright;
            final int contentHeight = heightSize - ptop - pbottom;

            BitmapDrawable selectedDrawable = (BitmapDrawable) mLikeSelected.getDrawable();
            BitmapDrawable shiningDrawable = (BitmapDrawable) mLikeSelectedShining.getDrawable();
            float selectedWidth = selectedDrawable.getIntrinsicWidth();
            float selectedHeight = selectedDrawable.getIntrinsicHeight();
            float shiningWidth = shiningDrawable.getIntrinsicWidth();
            float shiningHeight = shiningDrawable.getIntrinsicHeight();

            final int drawableWidth = Math.max(selectedDrawable.getIntrinsicWidth(),
                    shiningDrawable.getIntrinsicWidth());
            final int drawableHeight = selectedDrawable.getIntrinsicHeight()
                    + shiningDrawable.getIntrinsicHeight() / 2;

            float desireAspect = Math.min(1.0f * contentWidth / drawableWidth,
                    1.0f * contentHeight / drawableHeight);

            selectedWidth *= desireAspect;
            selectedHeight *= desireAspect;
            shiningWidth *= desireAspect;
            shiningHeight *= desireAspect;

            mLikeSelectedShining.measure(
                    MeasureSpec.makeMeasureSpec((int) (shiningWidth), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec((int) (shiningHeight), MeasureSpec.EXACTLY));
            mLikeSelected.measure(
                    MeasureSpec.makeMeasureSpec((int) (selectedWidth), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec((int) (selectedHeight), MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();

        final int shiningLeft = childLeft / 2 + (width - mLikeSelectedShining.getMeasuredWidth()) / 2;
        final int shiningTop = childTop;
        final int shiningWidth = mLikeSelectedShining.getMeasuredWidth();
        final int shiningHeight = mLikeSelectedShining.getMeasuredHeight();
        mLikeSelectedShining.layout(shiningLeft, shiningTop, shiningLeft + shiningWidth,
                shiningTop + shiningHeight);

        final int selectLeft = childLeft / 2 + (width - mLikeSelected.getMeasuredWidth()) / 2;
        final int selectTop = childTop + shiningHeight / 2;
        final int selectWidth = mLikeSelected.getMeasuredWidth();
        final int selectHeight = mLikeSelected.getMeasuredHeight();
        mLikeSelected.layout(selectLeft, selectTop, selectLeft + selectWidth,
                selectTop + selectHeight);
    }
}
