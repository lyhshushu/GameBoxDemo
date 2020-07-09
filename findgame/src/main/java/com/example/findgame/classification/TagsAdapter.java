package com.example.findgame.classification;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;

import java.util.List;

public class TagsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TagsAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    public TagsAdapter(@Nullable List<String> data) {
        super(data);
    }

    public TagsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_tags, item);
    }
}
