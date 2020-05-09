package com.example.findgame.information;

import android.widget.TextView;

import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;

import butterknife.BindView;

public class InformationFragment extends BaseFragment {

    @BindView(R2.id.tv_information)
    TextView textView;
    @Override
    protected int bindLayout() {
        return R.layout.fragment_information;
    }

    @Override
    protected void initView() {

    }
}
