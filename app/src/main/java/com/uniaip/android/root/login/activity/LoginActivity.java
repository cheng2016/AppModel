package com.uniaip.android.root.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniaip.android.R;
import com.uniaip.android.main.activity.MainActivity;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.http.SHDAPI;
import com.uniaip.android.utils.CheckUtil;
import com.uniaip.android.utils.SP;

import butterknife.BindView;

/**
 * 登录
 * 作者: ysc
 * 时间: 2017/4/24
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_log_register)
    TextView mTvReg;//注册
    @BindView(R.id.tv_log_reset)
    TextView mTvRes;//找回密码
    @BindView(R.id.tv_view_button)
    TextView mTvBtn;//登录
    @BindView(R.id.et_log_ph)
    EditText mEtPh;//账号
    @BindView(R.id.et_log_psw)
    EditText mEtPsw;//密码
    @BindView(R.id.iv_input_clean)
    ImageView mIvClean;//清除账号

    private final SP sp = new SP();
    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getListener();
    }

    private void getListener() {
        mTvReg.setOnClickListener(this);
        mTvRes.setOnClickListener(this);
        mTvBtn.setOnClickListener(this);
        mIvClean.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_log_register://注册
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.tv_log_reset://找回密码
                startActivity(new Intent(mContext, ResetPswdActivity.class));
                break;
            case R.id.tv_view_button://登录
                if (inspectData()) {
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }
                break;
            case R.id.iv_input_clean://清除
                mEtPh.setText("");
                break;
        }

    }

    /**
     * 检测账号密码是否合法
     *
     * @return
     */
    private boolean inspectData() {
        if (!CheckUtil.checktel(mEtPh.getText().toString().replace(" ", ""))) {
            toast(getString(R.string.login_text4));
            return false;
        }
        if (mEtPsw.getText().toString().length() < 6) {
            toast(getString(R.string.login_text5));
            return false;
        }
        return true;
    }

    /**
     * 登录
     */
    private void login() {
        //进度框
        showProgress();
        rxDestroy(SHDAPI.login(mEtPh.getText().toString().replace(" ", ""), mEtPsw.getText().toString())).subscribe(userModel -> {
            try {
                if (userModel.isOK()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    toast(userModel.getMsg());
//                    sp.save(Contanls.TOKEN, userModel.getData().getToken());
//                    sp.save(Contanls.UID, userModel.getData().getId());
//                    sp.save(Contanls.UPHONE, userModel.getData().getPhone());
                    finish();
                } else {
                    toast(userModel.getMsg());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismissProgress();
        }, e -> {
            toast(getString(R.string.error_text));
            dismissProgress();
        });
    }
}
