package com.example.androidlib.utils;

import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

public class OutLineSetter {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setOutLine(final View v, final int radius) {
        v.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, v.getWidth(), v.getHeight(), radius);
            }
        });
        v.setClipToOutline(true);
    }
}
