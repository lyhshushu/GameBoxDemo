package com.example.findgame.recommend;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidlib.BaseFragment;
import com.example.androidlib.utils.GsonUtils;
import com.example.androidlib.view.ImageButtonWithText;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.bean.AdvertisementBean;
import com.example.findgame.bean.GameInfBean;
import com.example.findgame.bean.MyGameBean;
import com.example.findgame.recommend.controller.MvcListener;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;


public class RecommendFragment extends BaseFragment {

    @BindView(R2.id.pic_one)
    ImageView picOne;
    @BindView(R2.id.pic_two)
    ImageView picTwo;
    @BindView(R2.id.ll_image)
    LinearLayout llImage;
    @BindView(R2.id.ibw_new_game)
    ImageButtonWithText ibwNewGame;
    @BindView(R2.id.ibw_invincible)
    ImageButtonWithText ibwInvincible;
    @BindView(R2.id.ibw_online_game)
    ImageButtonWithText ibwOnlineGame;
    @BindView(R2.id.ibw_necessary)
    ImageButtonWithText ibwNecessary;
    @BindView(R2.id.ibw_money)
    ImageButtonWithText ibwMoney;
    @BindView(R2.id.ll_tags)
    LinearLayout llTags;
    @BindView(R2.id.rv_game_inf)
    RecyclerView rvGameInf;
    @BindView(R2.id.rv_advertisement)
    RecyclerView rvAdvertisement;
    @BindView(R2.id.rv_my_game)
    RecyclerView rvMyGame;
    @BindView(R2.id.tv_my_game)
    TextView tvMyGame;
    @BindView(R2.id.delete_my_game)
    ImageView deleteMyGame;
    @BindView(R2.id.my_game)
    ConstraintLayout myGame;
    @BindView(R2.id.update_apk_img)
    ImageView updateApkImg;
    @BindView(R2.id.update_apk_name)
    TextView updateApkName;
    @BindView(R2.id.update_apk_detail)
    TextView updateApkDetail;
    @BindView(R2.id.apk_star)
    RatingBar apkStar;
    @BindView(R2.id.update_apk_edition)
    TextView updateApkEdition;
    @BindView(R2.id.update_apk_size)
    TextView updateApkSize;
    @BindView(R2.id.update_apk_delete)
    ImageView updateApkDelete;
    @BindView(R2.id.update_apk_button)
    Button updateApkButton;
    @BindView(R2.id.update_apk)
    ConstraintLayout updateApk;
    @BindView(R2.id.my_game_more)
    Button myGameMore;

    private GameInfAdapter mGameInfAdapter;
    private AdInfAdapter mAdInfAdapter;
    private MyGameAdapter mMyGameAdapter;
    private List<GameInfBean> mGameInfData;
    private List<AdvertisementBean> mAdInfData;
    private List<MyGameBean> mMyGameData;
    private Context mContext;


    @Override
    protected int bindLayout() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {
        mContext = getContext();
        mGameInfAdapter = new GameInfAdapter(R.layout.item_game_inf);
        rvGameInf.setLayoutManager(new LinearLayoutManager(mContext));
        rvGameInf.addItemDecoration(new RecyclerDivider(mContext));
        rvGameInf.setAdapter(mGameInfAdapter);

        mAdInfAdapter = new AdInfAdapter(R.layout.item_ad_inf);
        rvAdvertisement.setLayoutManager(new LinearLayoutManager(mContext));
        rvAdvertisement.addItemDecoration(new RecyclerDivider(mContext));
        rvAdvertisement.setAdapter(mAdInfAdapter);

        mMyGameAdapter = new MyGameAdapter(R.layout.item_my_game);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvMyGame.setLayoutManager(linearLayoutManager);
        rvMyGame.setAdapter(mMyGameAdapter);

    }

    @Override
    protected void bindListener() {
        //推荐预览图
        picOne.setOnClickListener(this);
        picTwo.setOnClickListener(this);
        //5大推荐板块
        ibwNewGame.setOnClickListener(this);
        ibwInvincible.setOnClickListener(this);
        ibwOnlineGame.setOnClickListener(this);
        ibwOnlineGame.setOnClickListener(this);
        ibwMoney.setOnClickListener(this);
        //我的游戏板块
        deleteMyGame.setOnClickListener(this);
        myGameMore.setOnClickListener(this);
        //游戏盒更新模块
        updateApkDelete.setOnClickListener(this);
        updateApkButton.setOnClickListener(this);

        mMyGameAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.icon_my_game) {
                Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        mGameInfAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.csl_game_inf) {
                Toast.makeText(mContext, mGameInfData.get(position).getGameName() + "csl_game_inf", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.bt_download) {
                Toast.makeText(mContext, mGameInfData.get(position).getGameName() + "bt_download_game_inf", Toast.LENGTH_SHORT).show();
            }
        });

        mAdInfAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if(id==R.id.csl_ad_game){
                Toast.makeText(mContext,mAdInfData.get(position).getAdName()+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void widgetClick(View v) {
        int id = v.getId();
        //library中的id不是final的不可使用switch
        //验证点击功能
        if (id == R.id.pic_one) {
            Toast.makeText(mContext, "pic_one", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.pic_two) {
            Toast.makeText(mContext, "pic_two", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_new_game) {
            Toast.makeText(mContext, "ibw_new_game", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_invincible) {
            Toast.makeText(mContext, "ibw_invincible", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_online_game) {
            Toast.makeText(mContext, "ibw_online_game", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_necessary) {
            Toast.makeText(mContext, "ibw_necessary", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_money) {
            Toast.makeText(mContext, "ibw_money", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.delete_my_game) {
            Toast.makeText(mContext, "delete_my_game", Toast.LENGTH_SHORT).show();
            myGame.setVisibility(View.GONE);
        } else if (id == R.id.my_game_more) {
            Toast.makeText(mContext, "my_game_more", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.update_apk_delete) {
            Toast.makeText(mContext, "update_apk_delete", Toast.LENGTH_SHORT).show();
            updateApk.setVisibility(View.GONE);
        } else if (id == R.id.update_apk_button) {
            Toast.makeText(mContext, "update_apk_button", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void applyData() {
        mGameInfData = GameInfBean.getGameInfData();
        mAdInfData = AdvertisementBean.getAdInfData();
        mMyGameData = getAppList();

        mGameInfAdapter.setNewData(mGameInfData);
        mAdInfAdapter.setNewData(mAdInfData);
        mMyGameAdapter.setNewData(mMyGameData);
    }

    private List<MyGameBean> getAppList() {
        List<MyGameBean> appList = new LinkedList<>();
        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            MyGameBean myGameBean = new MyGameBean();
            myGameBean.appIcon = packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager());
            if (((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) && packageInfo.applicationInfo.packageName.contains("game")) {
                appList.add(myGameBean);
            } else {

            }
        }
        return appList;
//        PackageManager packageManager=getActivity().getPackageManager();

    }

//    @Override
//    public void onSuccess(String str) {
//        try {
//            String adPicOne=str;
//        }catch (Exception e){
//            Log.e("reCommend",e+"");
//        }
//    }
//
//    @Override
//    public void onFailed() {
//        Toast.makeText(mContext,"传输失败",Toast.LENGTH_SHORT).show();
//    }
}
