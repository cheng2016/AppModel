package com.uniaip.android.root;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.http.SHDAPI;

import butterknife.BindView;

/**
 * H5页面
 * 作者: ysc
 * 时间: 2017/1/20
 */

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.wv_notice)
    WebView mWv;
    @BindView(R.id.tv_title_name)
    TextView mTvTitle;
    @BindView(R.id.iv_title_left)
    ImageView iv_title_left;
    @BindView(R.id.tv_notice_button)
    TextView mTvButton;//立即购买


    private int type = 0; //1:提现须知 2：消费补贴须知 3:客服 4:读懂少花点 5:商务合作 6:注册协议 7:套餐详情 8:平台协议
//    private PackagesInfo info;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_notice;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();
        getListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //抑制软键盘
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initView() {
        mTvButton.setText(getString(R.string.common_text110));
        WebSettings webSettings = mWv.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void getListener() {
        iv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (type == 6 || type == 7) {
                } else {
                    mTvTitle.setText(s);
                }

            }
        });

//        mTvButton.setOnClickListener(v -> {
//            if (info.getId().equals("1")) {
//                getAddresslist(0, info);//有商城名称
//            } else {
//                getAddresslist(1, info);//实物套餐
//            }
//
//        });
    }

    private void initData() {

        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case 1://提现须知
                mWv.loadUrl(SHDAPI.url + "html/appH5/tixianxuzhi.html");
                break;
            case 2://消费补贴须知
                mWv.loadUrl(SHDAPI.url + "html/appH5/xiaofeibutiexuzhi.html");
                break;
            case 3://读懂少花点
                mWv.loadUrl(SHDAPI.url + "html/appH5/dudongshaohuadian.html");
                break;
            case 4://商务合作
                mWv.loadUrl(SHDAPI.url + "html/appH5/kaigoushangwenda.html");
                break;
            case 5://客服
                mWv.loadUrl(SHDAPI.url + "html/appH5/kefu.html");
                break;
            case 6://注册协议
                mWv.loadUrl(SHDAPI.url + "html/appH5/xieyi.html");
                mTvTitle.setText(getString(R.string.title_text4));
                break;
            case 7://套餐详情
//                mTvButton.setVisibility(View.VISIBLE);
//                mTvButton.setText(getString(R.string.pack_text3));
//                mTvTitle.setText(getString(R.string.title_text3));
//                info = (PackagesInfo) getIntent().getSerializableExtra("info");
//                String url = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\"><title></title><style>*{padding:0;margin:0}body{width:100%;margin-top:4px;font-family:sans-serif;font-size:1em}li{list-style:none}a{text-decoration:none;-webkit-tap-highlight-color:transparent}img{max-width:100%;float:left;clear:both;}p{max-width:100%}</style></head><body>";
//                mWv.loadDataWithBaseURL("about:blank", url + info.getContent() + "</body></html>", "text/html", "utf-8", null);
                break;
            case 8://平台协议
                mWv.loadUrl(SHDAPI.url + "html/appH5/shareEC.html");
                break;
        }
    }


//    /**
//     * 获取地址 getaddresslist
//     */
//    private void getAddresslist(int type, final PackagesInfo info) {
//        //进度框
//        showProgress();
//        rxDestroy(UniaipAPI.getaddresslist(UniaipApplication.user.getToken(), UniaipApplication.user.getId(), info.getId())).subscribe(addressModel -> {
//            try {
//                if (addressModel.isOK()) {
//                    AddressInfo addressInfo = new AddressInfo();
//                    if (null != addressModel.getData()) {
//                        addressInfo = addressModel.getData();
//                    }
//                    addressInfo.setPrice(info.getPrice());
//                    addressInfo.setGoodid(info.getId());
//                    Intent intent = new Intent(this, SubmitAddressActivity.class);
//                    intent.putExtra("type", type);//实体无数据
//                    intent.putExtra("info", addressInfo);
//                    startActivity(intent);
//                } else if (TextUtils.equals(addressModel.getCode(), "10002")) {
//                    Intent intent = new Intent(this, LoginActivity.class);
//                    intent.putExtra("type", 99);
//                    startActivity(intent);
//                    exit();
//                } else {
//                    toast(addressModel.getMsg());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            dismissProgress();
//        }, e -> {
//            toast(getString(R.string.error_text));
//            dismissProgress();
//        });
//    }


}
