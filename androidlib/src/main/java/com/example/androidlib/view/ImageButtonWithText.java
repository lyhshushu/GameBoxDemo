package com.example.androidlib.view;

import android.content.Context;
import android.graphics.Color;

import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class ImageButtonWithText extends LinearLayout {

    private ImageView imageView = null;
    private TextView textView = null;
    private int imageId;
//    private int imageHeight, imageWidth;
    private int textId, textColorId, textTopId;

    public ImageButtonWithText(Context context) {
        this(context, null);
    }

    public ImageButtonWithText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageButtonWithText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);
        if (imageView == null) {
            imageView = new ImageView(context);
        }
        if (textView == null) {
            textView = new TextView(context);
        }
        if (attrs == null) {
            return;
        }
        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);
            switch (attrName) {
                case "image":
                    imageId = attrs.getAttributeResourceValue(i, 0);
                    break;
                case "text":
                    textId = attrs.getAttributeResourceValue(i, 0);
                    break;
//                case "imageHeight":
//                    imageHeight = attrs.getAttributeResourceValue(i, 0);
//                    break;
//                case "imageWidth":
//                    imageWidth = attrs.getAttributeResourceValue(i, 0);
//                    break;
                case "textColor":
                    textColorId = attrs.getAttributeResourceValue(i, 0);
                    break;
                case "textTop":
                    textTopId = attrs.getAttributeResourceValue(i, 0);
                    break;
                default:
                    break;
            }
        }
        init();
    }

    private void init() {
        this.setText(textId);
        textView.setGravity(Gravity.CENTER);
        this.setTextColor(textColorId);
        this.setTextPaddingTop(textTopId);
        this.setImageSize();
        this.setImgResource(imageId);
        addView(imageView);
        addView(textView);
    }

    /**
     * 设置默认的图片
     *
     * @param resourceID 图片id
     */
    public void setImgResourceDefault(int resourceID) {
        imageId = resourceID;
        setImgResource(resourceID);
    }


    /**
     * 设置显示的图片
     *
     * @param resourceID 图片ID
     */
    private void setImgResource(int resourceID) {
        if (resourceID == 0) {
            this.imageView.setImageResource(0);
        } else {
            this.imageView.setImageResource(resourceID);
        }
    }

    private void setImageSize() {
        LayoutParams params=new LayoutParams(44,44);
        this.imageView.setLayoutParams(params);
    }


    /**
     * 设置显示的文字
     *
     * @param text
     */
    public void setText(int text) {
        this.textView.setText(text);
    }

    /**
     * 设置字体颜色(默认为黑色)
     *
     * @param color
     */
    private void setTextColor(int color) {
        if (color == 0) {
            this.textView.setTextColor(Color.BLACK);
        } else {
            this.textView.setTextColor(getResources().getColor(color));
        }
    }

    /**
     * 设置默认的颜色
     *
     * @param color 颜色ID
     */
    public void setTextDefaultColor(int color) {
        textColorId = color;
        setTextColor(color);
    }

    /**
     * 设置字体大小
     *
     * @param size
     */
    public void setTextSize(float size) {
        this.textView.setTextSize(size);
    }


    /**
     * 设置文字与上面的距离
     *
     * @param top
     */
    public void setTextPaddingTop(int top) {
        if (top != 0) {
            this.textView.setPadding(0, getResources().getDimensionPixelOffset(top), 0, 0);
        }
    }

}
