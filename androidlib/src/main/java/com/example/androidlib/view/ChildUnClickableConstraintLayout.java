package com.example.androidlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ChildUnClickableConstraintLayout extends ConstraintLayout {


    public ChildUnClickableConstraintLayout(Context context) {
        super(context);
    }

    public ChildUnClickableConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildUnClickableConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
