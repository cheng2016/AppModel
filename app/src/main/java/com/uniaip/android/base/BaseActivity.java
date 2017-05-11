package com.uniaip.android.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.uniaip.android.R;
import com.uniaip.android.utils.SP;
import com.zhx.statusbar.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;

import static com.uniaip.android.utils.Toast.show;

/**
 * 基础的Activity
 * 作者: ysc
 * 时间: 2017/4/18
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    private static final List<Activity> ACTIVITIES = new ArrayList<>();
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITIES.add(this);
        mContext=this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutID());
        StatusBarUtils.translucentStatusBar(this, findViewById(R.id.v_fitsSystemWindows));
        ButterKnife.bind(this);
        init(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ACTIVITIES.remove(this);
    }

    /**
     * 退出所有页面
     */
    protected void exit() {
        for (Activity act : ACTIVITIES) {
            act.finish();
        }
    }

    /**
     * 初始化布局
     *
     * @return 布局ID
     */
    protected abstract int getLayoutID();

    /**
     * 初始化完成，替代{@link #onCreate(Bundle)}
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * Rx生命周期
     */
    public <T> Observable<T> rxDestroy(Observable<T> observable) {
        return observable.compose(bindUntilEvent(ActivityEvent.DESTROY));
    }

    /**
     * Toast
     *
     * @param res 资源
     */
    public void toast(int res) {
        show(res);
    }

    /**
     * Toast
     *
     * @param text 文本
     */
    public void toast(String text) {
        show(text);
    }

    //进度框=============================
    private MaterialDialog mPD;
    /**
     * 显示进度框
     */
    public void showProgress() {
        if (mPD == null) {
            mPD = new MaterialDialog.Builder(this).content(R.string.dl_progress).progress(true, 0).canceledOnTouchOutside(false).build();
        }
        mPD.show();
    }

    /**
     * 关闭进度框
     */
    public void dismissProgress() {
        if (mPD != null) {
            mPD.dismiss();
        }
    }

}
