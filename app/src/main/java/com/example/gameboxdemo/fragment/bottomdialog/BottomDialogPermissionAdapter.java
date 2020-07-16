package com.example.gameboxdemo.fragment.bottomdialog;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.gameboxdemo.R;
import com.example.gameboxdemo.bean.AppPermissionBean;

import java.util.List;

public class BottomDialogPermissionAdapter extends BaseQuickAdapter<AppPermissionBean, BaseViewHolder> {
    public BottomDialogPermissionAdapter(int layoutResId, @Nullable List<AppPermissionBean> data) {
        super(layoutResId, data);
    }

    public BottomDialogPermissionAdapter(@Nullable List<AppPermissionBean> data) {
        super(data);
    }

    public BottomDialogPermissionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppPermissionBean item) {
        helper.setText(R.id.tv_permission_title, item.getPermissionTitle())
                .setText(R.id.tv_permission_detail, item.getPermissionDes());
    }
}
