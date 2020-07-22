package com.example.androidlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class ToolbarAlphaBehavior extends CoordinatorLayout.Behavior<Toolbar> {
    private int offset = 0;
    private int startOffset = 0;
    private int endOffset = 0;
    private Context context;

    public ToolbarAlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull Toolbar child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull Toolbar child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        startOffset = 0;
        endOffset = 100 - child.getHeight();
        offset += dyConsumed;
        if (offset <= startOffset) {
            child.getBackground().setAlpha(0);
        } else if (offset > startOffset && offset < endOffset) {
            float percent = (float) (offset - startOffset) / endOffset;
            int alpha = Math.round(percent * 255);
            child.getBackground().setAlpha(alpha);
        } else if (offset >= endOffset) {
            child.getBackground().setAlpha(255);
        }
    }
}
