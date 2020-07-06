package com.example.androidlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * @author 4399lyh
 */
public class MyHorizontalRecyclerView extends RecyclerView {

    private float lastX;
    private float lastY;
    private float newX;
    private float newY;
    private final static float MIN_MOVE_DIS = 10;

    public MyHorizontalRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MyHorizontalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            lastX = ev.getX();
//            lastY = ev.getY();
//        }
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            newX = ev.getX();
//            newY = ev.getY();
//            if (Math.abs(newX - lastX) > Math.abs(newY - lastY)) {
//                getParent().requestDisallowInterceptTouchEvent(true);
//            }
//        }
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
//            lastX = newX;
//            lastY = newY;
//        }
        ViewParent parent=this;
        while (!((parent=parent.getParent())instanceof ViewPager)){

        }
        parent.requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }
}
