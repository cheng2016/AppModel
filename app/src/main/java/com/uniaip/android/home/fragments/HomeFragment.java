package com.uniaip.android.home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.uniaip.android.R;
import com.uniaip.android.home.adapters.ModularAdapter;
import com.uniaip.android.home.adapters.PosterAdapter;
import com.uniaip.android.base.BaseFragment;
import com.uniaip.android.home.models.ModularInfo;
import com.uniaip.android.views.NoScrollGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者: ysc
 * 时间: 2017/4/24
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.bn_home)
    Banner mBanner;
    @BindView(R.id.gv_home_modular)
    NoScrollGridView mGvModula;
    @BindView(R.id.rv_home_poster)
    RecyclerView mRvPoster;

    PosterAdapter mAdpPoster;//海报适配器

    private List<String> imgsTest;
    private List<String> titlesTest;
    private List<ModularInfo> mLtModular;
    private PosterAdapter posterAdapter;
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initTestData();
        initView();
        initBanner();
        initData();
    }

    private void initView() {
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPoster.setLayoutManager(lin);
        posterAdapter = new PosterAdapter(mContext,mLtModular);
        mRvPoster.setAdapter(posterAdapter);
    }

    private void initBanner() {

        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(imgsTest);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(titlesTest);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    private void initData() {
        mGvModula.setAdapter(new ModularAdapter(mContext, mLtModular));
    }

    private void initTestData() {
        imgsTest = new ArrayList<>();
        imgsTest.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493116186740&di=834457ee107417243f17abe94b0aede5&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F011f4356d56e336ac7252ce6e85df4.jpg");
        imgsTest.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493116320088&di=acfe3bbc623912accde2414dd0aafb85&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F019e1c568496fd32f8759f04a9ee73.jpg");
        imgsTest.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493116323913&di=9d65bbe9067d62a9a0abde68bafca21d&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F016f76560b3b646ac7251df8d86979.jpg");
        imgsTest.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493116319503&di=28ab16e71961256fa04a158c71e772a7&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01670c5721693c32f875a399c2aa3d.jpg");
        imgsTest.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4291007083,3309100963&fm=23&gp=0.jpg");
        titlesTest = new ArrayList<>();
        titlesTest.add("标题1");
        titlesTest.add("标题2");
        titlesTest.add("标题3");
        titlesTest.add("标题4");
        titlesTest.add("标题5");
        mLtModular = new ArrayList<>();
        mLtModular.add(new ModularInfo("0", "", "推广"));
        mLtModular.add(new ModularInfo("1", "", "套餐"));
        mLtModular.add(new ModularInfo("2", "", "消费补贴"));
        mLtModular.add(new ModularInfo("3", "", "商城"));
        mLtModular.add(new ModularInfo("4", "", "链接用户"));
        mLtModular.add(new ModularInfo("5", "", "大数据"));
        mLtModular.add(new ModularInfo("6", "", "新手入门"));
        mLtModular.add(new ModularInfo("-99", "", "更多"));

    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path.toString()).into(imageView);
        }
    }
}
