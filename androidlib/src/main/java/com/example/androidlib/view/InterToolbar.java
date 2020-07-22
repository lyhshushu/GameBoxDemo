package com.example.androidlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class InterToolbar extends Toolbar {
    public InterToolbar(Context context) {
        super(context);
    }

    public InterToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
