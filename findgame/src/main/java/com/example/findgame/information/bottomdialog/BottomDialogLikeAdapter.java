package com.example.findgame.information.bottomdialog;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;

import java.util.List;

public class BottomDialogLikeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public BottomDialogLikeAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    public BottomDialogLikeAdapter(@Nullable List<String> data) {
        super(data);
    }

    public BottomDialogLikeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_dialog_tag, item)
                .addOnClickListener(R.id.cl_dialog_tag);
    }
}
