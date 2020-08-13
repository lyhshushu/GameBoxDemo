package com.example.gameboxdemo.fragment;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidlib.BaseFragment;
import com.example.androidlib.utils.AutoLineFeedLayoutManager;
import com.example.androidlib.view.MyHorizontalRecyclerView;
import com.example.findgame.recommend.RecyclerDivider;
import com.example.gameboxdemo.R;
import com.example.gameboxdemo.bean.GameInfActBean;
import com.example.gameboxdemo.fragment.bottomdialog.BottomDialogPermissionAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class GameInfIntroduceFragemt extends BaseFragment {
    @BindView(R.id.rv_game_int_pic_list)
    MyHorizontalRecyclerView rvGameIntPicList;
    @BindView(R.id.tv_game_introduce)
    TextView tvGameIntroduce;
    @BindView(R.id.tv_game_inf_detail)
    TextView tvGameInfDetail;
    @BindView(R.id.tv_warm_tip)
    TextView tvWarmTip;
    @BindView(R.id.tv_tip_detail)
    TextView tvTipDetail;
    @BindView(R.id.cl_warm_tip)
    ConstraintLayout clWarmTip;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.tv_creator)
    TextView tvCreator;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_permission)
    TextView tvPermission;
    @BindView(R.id.tv_language_type)
    TextView tvLanguageType;
    @BindView(R.id.tv_creator_name)
    TextView tvCreatorName;
    @BindView(R.id.tv_version_num)
    TextView tvVersionNum;
    @BindView(R.id.tv_permission_name)
    TextView tvPermissionName;
    @BindView(R.id.cl_main_inf)
    ConstraintLayout clMainInf;
    @BindView(R.id.tv_game_inf_tag_title)
    TextView tvGameInfTagTitle;
    @BindView(R.id.rv_game_inf_tags)
    RecyclerView rvGameInfTags;
    @BindView(R.id.cl_game_inf_tags)
    ConstraintLayout clGameInfTags;

    private Context mContext;
    private GameInfPicAdapter gameInfPicAdapter;

    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior dialogBehavior;
    private RecyclerView rvDialog;
    private GameInfActBean gameInfActBean;
    private GameInfTagAdapter gameInfTagAdapter;


    @Override
    protected int bindLayout() {
        return R.layout.fragment_game_inf_introduce;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mContext = getContext();
        gameInfPicAdapter = new GameInfPicAdapter(R.layout.item_game_inf_pic);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvGameIntPicList.setLayoutManager(layoutManager);
        rvGameIntPicList.setAdapter(gameInfPicAdapter);
        gameInfTagAdapter = new GameInfTagAdapter(R.layout.item_game_inf_tag);
        rvGameInfTags.setLayoutManager(new AutoLineFeedLayoutManager());
        rvGameInfTags.setAdapter(gameInfTagAdapter);
    }

    @Override
    protected void applyData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getGameBean(GameInfActBean messageEvent) {
        gameInfTagAdapter.setNewData(messageEvent.getAppTags());
        gameInfTagAdapter.notifyDataSetChanged();
        gameInfPicAdapter.setNewData(messageEvent.getAppScreen());
        gameInfPicAdapter.notifyDataSetChanged();
        tvGameInfDetail.setText(messageEvent.getAppInfDetail());
        tvGameInfDetail.setMaxLines(3);
        if ("".equals(messageEvent.getAppWarmTip())) {
            clWarmTip.setVisibility(View.GONE);
        }
        tvTipDetail.setText(Html.fromHtml(messageEvent.getAppWarmTip()));
        tvCreatorName.setText(messageEvent.getAppCreator());
        tvVersionNum.setText(messageEvent.getAppVersion());
        gameInfActBean = messageEvent;
    }

    @Override
    protected void bindListener() {
        tvPermissionName.setOnClickListener(this);
        tvGameInfDetail.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void widgetClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_game_inf_detail:
                if (tvGameInfDetail.getMaxLines() == 3) {
                    tvGameInfDetail.setMaxLines(100);
                } else {
                    tvGameInfDetail.setMaxLines(3);
                }
                break;
            case R.id.tv_permission_name:
                createDialog();
                bottomSheetDialog.show();
                break;
            default:
                break;
        }
    }

    private void createDialog() {
        View view = View.inflate(mContext, R.layout.dialog_bottom_like, null);
        TextView textView = view.findViewById(R.id.not_like_reason);
        textView.setText("该应用需要访问以下权限");
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setTextSize(15);
        rvDialog = view.findViewById(R.id.rv_reasons);
        rvDialog.setLayoutManager(new LinearLayoutManager(mContext));
        rvDialog.addItemDecoration(new RecyclerDivider(mContext));
        BottomDialogPermissionAdapter adapter = new BottomDialogPermissionAdapter(R.layout.item_permission);
        rvDialog.setHasFixedSize(true);
        adapter.setNewData(gameInfActBean.getAppPermissionBeans());
        adapter.notifyDataSetChanged();
        rvDialog.setAdapter(adapter);

        bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomDialog);
        bottomSheetDialog.setContentView(view);

        dialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        dialogBehavior.setPeekHeight(getPeekHeight());

    }

    protected int getPeekHeight() {
        int peekHeight = getResources().getDisplayMetrics().heightPixels;
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 2;
    }

    public static class GameInfTagAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public GameInfTagAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        public GameInfTagAdapter(@Nullable List<String> data) {
            super(data);
        }

        public GameInfTagAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_game_inf_tag, item);
        }
    }

}

