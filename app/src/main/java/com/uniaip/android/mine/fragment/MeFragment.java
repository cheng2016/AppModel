package com.uniaip.android.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniaip.android.R;
import com.uniaip.android.mine.activity.SetUpActivity;
import com.uniaip.android.base.BaseFragment;

import butterknife.BindView;

/**
 * 作者: ysc
 * 时间: 2017/4/24
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_title_name)
    TextView mTvTitle;//标题
    @BindView(R.id.iv_title_right)
    ImageView mIvRight;//设置
    @BindView(R.id.tv_me_red)
    TextView mTvRed;//红包
    @BindView(R.id.tv_me_pack)
    TextView mTvPack;//套餐订单
    @BindView(R.id.tv_me_data)
    TextView mTvData;//数据收藏

    @BindView(R.id.tv_me_feedback)
    TextView mTvFeedback;//意见反馈
    @BindView(R.id.tv_me_about)
    TextView mTvAbout;//关于我们

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initView();
        initListener();
    }

    private void initView() {
        mIvRight.setVisibility(View.VISIBLE);
        mTvTitle.setText(getString(R.string.title_text7));
    }

    private void initListener() {
        mTvRed.setOnClickListener(this);
        mTvPack.setOnClickListener(this);
        mTvData.setOnClickListener(this);
        mTvFeedback.setOnClickListener(this);
        mTvAbout.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_me_red://红包

                break;
            case R.id.tv_me_pack://套餐订单

                break;
            case R.id.tv_me_data://数据收藏

                break;
            case R.id.tv_me_feedback://意见反馈

                break;
            case R.id.tv_me_about://关于我们

                break;
            case R.id.iv_title_right://设置
                startActivity(new Intent(mContext, SetUpActivity.class));
                break;
        }
    }
}
