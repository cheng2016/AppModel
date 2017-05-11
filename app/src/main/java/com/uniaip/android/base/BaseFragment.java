package com.uniaip.android.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import rx.Observable;

/**
 * 基础的Fragment
 * 作者: ysc
 * 时间: 2017/4/18
 */

public abstract class BaseFragment extends RxFragment {
    public Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext=this.getContext();
        ButterKnife.bind(this, view);
        init(view, savedInstanceState);
    }

    /**
     * Rx生命周期
     */
    public <T> Observable<T> rxDestroy(Observable<T> observable) {
        return observable.compose(bindUntilEvent(FragmentEvent.DESTROY));
    }

    /**
     * 获取Fragment布局ID
     *
     * @return 布局ID
     */
    protected abstract int getLayoutID();

    /**
     * 初始化完成，替代{@link #onViewCreated(View, Bundle)}
     */
    protected abstract void init(View view, Bundle savedInstanceState);
    /**
     * Toast
     *
     * @param res 资源
     */
    public void toast(int res) {
        ((BaseActivity) getActivity()).toast(res);
    }

    /**
     * Toast
     *
     * @param text 文本
     */
    public void toast(String text) {
        ((BaseActivity) getActivity()).toast(text);
    }

    //进度框=============================

    /**
     * 显示进度框
     */
    public void showProgress() {
        ((BaseActivity) getActivity()).showProgress();
    }

    /**
     * 关闭进度框
     */
    public void dismissProgress() {
        ((BaseActivity) getActivity()).dismissProgress();
    }
}
