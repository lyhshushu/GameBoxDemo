package com.example.androidlib;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author lyh
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final long FAST_CLICK_DURATION = 500L;
    private long lastClickTime;


    protected BaseActivity activity;
    private Unbinder bun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutId());
        bun = ButterKnife.bind(this);
        activity = this;
        initView();
        bindListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bun.unbind();
    }

    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        long timeDuration = now - lastClickTime;
        lastClickTime = now;
        return 0L < timeDuration && timeDuration < FAST_CLICK_DURATION;
    }

    /**
     * 点击时间
     * @param v 控件
     */
    protected void widgetClick(View v){

    }
    @Override
    public void onClick(View v) {
        if (!isFastClick()) {
            this.widgetClick(v);
        }
    }

    /**
     * 获取子类Activity的id
     *
     * @return int
     */
    public abstract int getLayoutId();

    /**
     * 设置View
     */
    public abstract void initView();

    /**
     * 设置data数据
     */
    public abstract void initData();

    /**
     * 绑定监听事件
     */
    protected void bindListener(){}

}
