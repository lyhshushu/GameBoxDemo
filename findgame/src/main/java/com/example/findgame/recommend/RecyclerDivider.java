package com.example.findgame.recommend;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Paint mPaint;
    private Drawable mDivider;
    private static final int SPACE = 20;
    private boolean mSpace;
    private int mDividerHeight = 2;

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mSpace) {
            outRect.left = SPACE;
            outRect.right = SPACE;
            outRect.bottom = SPACE;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = SPACE;
            }
        } else {
            outRect.set(0, 0, 0, mDividerHeight);
        }
    }

    /**
     * 默认分割线,高度2px
     *
     * @param context
     */
    public RecyclerDivider(Context context) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#ebebeb"));
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    /**
     * 设置分割：
     *
     * @param mSpace 判断是否设置分割
     */
    public RecyclerDivider(boolean mSpace) {
        this.mSpace = mSpace;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c,parent);
    }

    /**
     * 绘制纵向列表时的分隔线  这时分隔线是横着的
     * 每次 left相同，top根据child变化，right相同，bottom也变化
     *
     * @param canvas Canvas
     * @param parent RecyclerView
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
