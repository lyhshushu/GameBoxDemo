package com.example.androidlib.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class AutoLineFeedLayoutManager extends RecyclerView.LayoutManager {

    public AutoLineFeedLayoutManager() {
        setAutoMeasureEnabled(true);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int curLineWidth = 0, curLineTop = 0;
        int lastLineMaxHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
            int height = getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
            curLineWidth += width;
            if (curLineWidth <= getWidth()) {
                layoutDecorated(view, curLineWidth - width + params.leftMargin, curLineTop + params.topMargin, curLineWidth - params.rightMargin, curLineTop + height - params.bottomMargin);
                lastLineMaxHeight = Math.max(lastLineMaxHeight, height);
            } else {
                curLineWidth = width;
                if (lastLineMaxHeight == 0) {
                    lastLineMaxHeight = height;
                }
                curLineTop += lastLineMaxHeight;
                layoutDecorated(view, params.leftMargin, curLineTop + params.topMargin, width - params.rightMargin, curLineTop + height - params.bottomMargin);
                lastLineMaxHeight = height;
            }
        }
    }
}
