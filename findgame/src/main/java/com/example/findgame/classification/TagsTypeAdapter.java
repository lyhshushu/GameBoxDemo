package com.example.findgame.classification;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.AllClassificationBean;

import java.util.List;

public class TagsTypeAdapter extends BaseQuickAdapter<AllClassificationBean, BaseViewHolder> {
    public TagsTypeAdapter(int layoutResId, @Nullable List<AllClassificationBean> data) {
        super(layoutResId, data);
    }

    public TagsTypeAdapter(@Nullable List<AllClassificationBean> data) {
        super(data);
    }

    public TagsTypeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllClassificationBean item) {
        helper.setText(R.id.tv_tags_name, item.getName())
                .addOnClickListener(R.id.cl_classification_tags);

        RecyclerView rvTags = helper.getView(R.id.rv_tags);
        TagsAdapter tagsAdapter = new TagsAdapter(R.layout.item_tags);
        GridLayoutManager layoutManagerTags = new GridLayoutManager(mContext, 4);
        tagsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.tv_tags) {
                Toast.makeText(mContext, item.getCollectionPicUrl().get(position), Toast.LENGTH_SHORT).show();
            }
        });

        rvTags.setLayoutManager(layoutManagerTags);
        rvTags.setAdapter(tagsAdapter);


        tagsAdapter.setNewData(item.getCollectionPicUrl());
        tagsAdapter.notifyDataSetChanged();

    }
}
