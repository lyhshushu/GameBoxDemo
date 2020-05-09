package com.example.findgame.classification;

import android.widget.TextView;

import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;

import butterknife.BindView;

public class ClassificationFragment extends BaseFragment {

    @BindView(R2.id.tv_classification)
    TextView textView;
    @Override
    protected int bindLayout() {
        return R.layout.fragment_classification;
    }

    @Override
    protected void initView() {

    }
}
