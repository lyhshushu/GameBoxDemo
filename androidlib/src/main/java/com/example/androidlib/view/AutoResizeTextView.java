package com.example.androidlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class AutoResizeTextView extends TextView {

    private static final int MAX_SIZE = 1000;
    private static final int MIN_SIZE = 5;
    private TextPaint mTextPaint;
    private float mSpacingMulti = 1.0f;
    private float mSpacingAdd = 0.0f;
    private boolean needAdapt = false;
    private boolean adapting = false;


    public AutoResizeTextView(Context context) {
        super(context);
        init();
    }

    public AutoResizeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoResizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new TextPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (adapting) {
            return;
        }
        if (needAdapt) {
            adaptTextSize();
        } else {
            super.onDraw(canvas);
        }
    }

    private void adaptTextSize() {
        CharSequence text = getText();
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        if (viewHeight == 0 || viewWidth == 0 || TextUtils.isEmpty(text)) {
            return;
        }
        adapting = true;
        float bottom = MIN_SIZE;
        float top = MAX_SIZE;
        float mid = 0;
        while (bottom <= top) {
            mid = (bottom + top) / 2;
            mTextPaint.setTextSize(mid);
            int textWidth = (int) mTextPaint.measureText(text, 0, text.length());
            int textHeight = getTextHeight(text, viewWidth);
            if (textWidth < viewWidth && textHeight < viewWidth) {
                bottom = mid + 1;
            } else {
                top = mid - 1;
            }
        }
        float newSize = mid - 1;
        setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
        adapting = false;
        needAdapt = false;
        invalidate();
    }

    private int getTextHeight(CharSequence text, int targetWidth) {
        StaticLayout layout = new StaticLayout(text, mTextPaint, targetWidth, Layout.Alignment.ALIGN_NORMAL, mSpacingMulti, mSpacingAdd, true);
        return layout.getHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        needAdapt = true;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        needAdapt = true;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        mSpacingMulti = mult;
        mSpacingAdd = add;
    }
}
