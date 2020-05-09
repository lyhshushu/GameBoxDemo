package com.example.findgame.rank;

import android.widget.TextView;

import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;

import butterknife.BindView;

public class RankFragment extends BaseFragment {

    @BindView(R2.id.tv_rank)
    TextView textView;
    @Override
    protected int bindLayout() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initView() {

    }
}
