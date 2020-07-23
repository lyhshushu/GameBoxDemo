package com.example.androidlib;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.testinject.injectutil.InjectManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author lyh
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final long FAST_CLICK_DURATION = 500L;
    private long lastClickTime;
    public static final String FINISH_VIDEO_ACTION = "receiver_action_finish";

    protected BaseActivity activity;
    private Unbinder bun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(getLayoutId());
        InjectManager.inject(this);
        bun = ButterKnife.bind(this);
        activity = this;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        getWindow().setAttributes(lp);
        // 设置页面全屏显示
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        if (isHaveBar()) {
            setToolBar();
        }
        initView();
        bindListener();
        initData();
    }


    protected boolean isHaveBar() {
        return true;
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


    private void setToolBar() {
        ImmersionBar.with(activity)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .init();
    }

    /**
     * 点击时间
     *
     * @param v 控件
     */
    protected void widgetClick(View v) {

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
//    public abstract int getLayoutId();

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
    protected void bindListener() {
    }

}
