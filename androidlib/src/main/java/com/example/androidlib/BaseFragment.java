package com.example.androidlib;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author lyh
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    private static final long FAST_CLICK_DURATION = 500L;
    private long lastClickTime;

    private Unbinder bun;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootLayout = (ViewGroup) inflater.inflate(R.layout.activity_base, null);

        inflater.inflate(bindLayout(),rootLayout,true);
        bun= ButterKnife.bind(this,rootLayout);
        initParam();
        initView();
        bindListener();
        applyData();
        return rootLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
     * 资源文件绑定
     * @return layoutResId
     */
    protected abstract @LayoutRes int bindLayout();

    /**
     * 初始化数据
     */
    protected void initParam(){

    }
    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化监听事件
     */
    protected void bindListener(){

    }

    /**
     * 展示数据
     */
    protected void applyData(){

    }
}
