package com.uniaip.android.root.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;

import butterknife.BindView;

/**
 * 注册
 * 作者: ysc
 * 时间: 2017/4/24
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_reg_back)
    ImageView mIvBack;//返回
    @BindView(R.id.tv_reg_log)
    TextView mTvLog;
    @BindView(R.id.tv_reg_agreement)
    TextView mTvAgr;//协议
    @BindView(R.id.tv_view_button)
    TextView mTvBtn;//提交注册

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        intiListener();
    }

    private void initView() {
        mTvBtn.setText(getString(R.string.btn_text6));
    }

    private void intiListener() {
        mTvLog.setOnClickListener(this);
        mTvAgr.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_reg_back://返回
            case R.id.tv_reg_log://登录
                finish();
                break;
            case R.id.tv_reg_agreement://协议

                break;
            case R.id.tv_view_button://提交注册
                startActivity(new Intent(mContext, CerifyCodeActivity.class));
                break;
        }
    }


}
