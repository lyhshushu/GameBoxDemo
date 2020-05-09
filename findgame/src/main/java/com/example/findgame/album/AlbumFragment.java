package com.example.findgame.album;

import android.widget.TextView;

import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;

import butterknife.BindView;

public class AlbumFragment extends BaseFragment {

    @BindView(R2.id.tv_album)
    TextView textView;
    @Override
    protected int bindLayout() {
        return R.layout.fragment_album;
    }

    @Override
    protected void initView() {

    }
}
