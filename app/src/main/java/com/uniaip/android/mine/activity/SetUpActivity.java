package com.uniaip.android.mine.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.utils.DialogView;

import butterknife.BindView;

/**
 * 设置
 * 作者: ysc
 * 时间: 2017/5/6
 */

public class SetUpActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_view_button)
    TextView mTvBun;//注销

    DialogView mDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initListener();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title_name)).setText(getString(R.string.title_text8));
        mDialog = new DialogView(this);
    }

    private void initListener() {
        mTvBun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_view_button://注销
                mDialog.showDetermine(mHandler, "确定要注销账号吗？", 400);
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 400:



                    break;
            }
        }
    };
}
