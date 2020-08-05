package com.example.findgame;


import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.androidlib.BaseFragment;
import com.example.findgame.album.AlbumFragment;
import com.example.findgame.classification.ClassificationFragment;
import com.example.findgame.information.InformationFragment;
import com.example.findgame.rank.RankFragment;
import com.example.findgame.recommend.RecommendFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @author 4399lyh
 */
public class FindGameFragment extends BaseFragment {

    @BindView(R2.id.find_game_tab)
    SlidingTabLayout xTabLayout;
    @BindView(R2.id.find_game_viewpager)
    ViewPager findGameViewPager;
    @BindView(R2.id.iv_bottom_line)
    ImageView ivBottomLine;
    @BindView(R2.id.tv_item_start)
    TextView tvItemStart;
    @BindView(R2.id.iv_shadow)
    ImageView ivShadow;

    private void firstSetGone() {
        ivBottomLine.setVisibility(View.GONE);
        tvItemStart.setVisibility(View.GONE);
        ivShadow.setVisibility(View.GONE);
    }

    private void firstSetVisible() {
        ivBottomLine.setVisibility(View.VISIBLE);
        tvItemStart.setVisibility(View.VISIBLE);
        ivShadow.setVisibility(View.VISIBLE);
    }

    private void firstSetSelect() {
        ivBottomLine.setBackgroundResource(R.drawable.download_button);
        tvItemStart.setTextColor(this.getResources().getColor(R.color.head_green));
        tvItemStart.setTextSize(15f);
        tvItemStart.setTypeface(Typeface.DEFAULT_BOLD);
    }


    private void firstSetUnSelect() {
        ivBottomLine.setBackground(null);
        tvItemStart.setTextColor(this.getResources().getColor(R.color.black));
        tvItemStart.setTypeface(Typeface.DEFAULT);
        tvItemStart.setTextSize(12f);
    }


    @Override
    protected int bindLayout() {
        return R.layout.fragment_find_game;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        List<String> titles = new ArrayList<>();
        titles.add("推荐");
        titles.add("分类");
        titles.add("排行");
        titles.add("专辑");
        titles.add("喜欢");
        titles.add("喜欢");
        titles.add("喜欢");
        titles.add("喜欢");
        titles.add("喜欢");
        titles.add("喜欢");


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecommendFragment());
        fragments.add(new ClassificationFragment());
        fragments.add(new RankFragment());
        fragments.add(new AlbumFragment());
        fragments.add(new InformationFragment());
        fragments.add(new InformationFragment());
        fragments.add(new InformationFragment());
        fragments.add(new InformationFragment());
        fragments.add(new InformationFragment());
        fragments.add(new InformationFragment());

        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), fragments, titles);
        findGameViewPager.setOffscreenPageLimit(10);
        findGameViewPager.setAdapter(adapter);
        xTabLayout.setViewPager(findGameViewPager, titles.toArray(new String[0]));
        findGameViewPager.setCurrentItem(0);
        xTabLayout.getTitleView(0).setTypeface(Typeface.DEFAULT_BOLD);
        xTabLayout.getTitleView(0).setTextSize(15f);

        firstSetGone();

        tvItemStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findGameViewPager.setCurrentItem(0);
            }
        });

        xTabLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollX == 0) {
                    firstSetGone();
                } else {
                    firstSetVisible();
                }
            }
        });

        findGameViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    firstSetSelect();
                } else {
                    firstSetUnSelect();
                }
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < xTabLayout.getTabCount(); i++) {
                    if (position == i) {
                        xTabLayout.getTitleView(position).setTypeface(Typeface.DEFAULT_BOLD);
                        xTabLayout.getTitleView(position).setTextSize(15f);
                    } else {
                        xTabLayout.getTitleView(i).setTextSize(12f);
                        xTabLayout.getTitleView(i).setTypeface(Typeface.DEFAULT);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public static class FragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
