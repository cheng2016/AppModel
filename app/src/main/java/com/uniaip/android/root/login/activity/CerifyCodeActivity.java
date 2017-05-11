package com.uniaip.android.root.login.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;
import butterknife.BindView;

/**
 * 注册验证码
 * 作者: ysc
 * 时间: 2017/5/6
 */
public class CerifyCodeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView mIvBack;//返回

    @Override
    protected int getLayoutID() {
        return R.layout.activity_code;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initListener();
    }

    private void initListener() {
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }

    }
}
