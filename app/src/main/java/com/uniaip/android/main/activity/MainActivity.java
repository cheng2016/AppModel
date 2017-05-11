package com.uniaip.android.main.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.utils.APPUtils;
import com.uniaip.android.utils.SP;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private final SP sp = new SP();
    @BindView(R.id.rg_tab)
    RadioGroup mRgTab;

    private int themeType = 0;//0:少花点 1:国腾

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        getAppTheme();
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getListener();
        initData();
    }

    private void getListener() {
        mRgTab.setOnCheckedChangeListener((group, checkedId) -> showFragment(mFragments.get(group.indexOfChild(group.findViewById(checkedId)))));
    }

    private void initData() {
        FragmentManager fm = getSupportFragmentManager();
        mFragments.add(fm.findFragmentById(R.id.f_home));
        mFragments.add(fm.findFragmentById(R.id.f_act));
        mFragments.add(fm.findFragmentById(R.id.f_news));
        mFragments.add(fm.findFragmentById(R.id.f_me));
        APPUtils.notShowInputMode(this);
        showFragment(mFragments.get(0));
//        fm.hide(f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_shd:
//                if (themeType != 0) {
//                    sp.save(Contanls.APP_THEME, 0);
//                    finish();
//                    final Intent themeintent1 = getIntent();
//                    themeintent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(themeintent1);
//                    overridePendingTransition(0, 0);
//                }
//                break;
//            case R.id.btn_gt:
//                if (themeType != 1) {
//                    sp.save(Contanls.APP_THEME, 1);
//                    finish();
//                    final Intent themeintent2 = getIntent();
//                    themeintent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(themeintent2);
//                    overridePendingTransition(0, 0);
//                }
//                break;
        }
    }

    /**
     * 显示tag
     *
     * @param show
     */
    private void showFragment(Fragment show) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().show(show);
        for (Fragment f : mFragments) {
            if (show == f)
                continue;
            ft.hide(f);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 获取主题
     */
    private void getAppTheme() {
//        themeType = sp.load(Contanls.APP_THEME, 0);
        switch (themeType) {
            case 0: //少花点
                setTheme(R.style.AppTheme);
                break;
            case 1://国腾
                setTheme(R.style.APPThemeGT);
                break;
        }
    }


}
