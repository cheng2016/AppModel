package com.uniaip.android.home.invoice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.home.invoice.adapter.MapDataAdapter;
import com.uniaip.android.home.invoice.models.CheckInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 搜索商家
 * 作者: ysc
 * 时间: 2017/2/18
 */

public class SearchShopActivity extends BaseActivity {


    @BindView(R.id.prv_search_list)
    ListView mPrvList;
    @BindView(R.id.tv_title_right)
    TextView mTvRight;
    @BindView(R.id.et_search_input)
    EditText mEtInput;
    @BindView(R.id.lay_map_search)
    LinearLayout mLaySearch;


    private MapDataAdapter mAdp;
    private String city;
    private PoiSearch mPoiSearch;
    private List<PoiInfo> dataList = new ArrayList<>();//商家列表数据

    @Override
    protected int getLayoutID() {
        return R.layout.activity_search_shop;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        getListener();
        initData();
    }

    private void initView() {
        mTvRight.setText("搜索");
        mAdp = new MapDataAdapter(this);
        mPrvList.setAdapter(mAdp);
        mLaySearch.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化监听
     */
    private void getListener() {
        mPrvList.setOnItemClickListener((parent, view, position, id) -> {
            PoiInfo poi = dataList.get(position);
            Intent intent = new Intent();
            intent.putExtra("address", new CheckInfo(poi.name, poi.city, poi.address, poi.location.longitude, poi.location.latitude));
            setResult(1201, intent);
            finish();
        });
        mTvRight.setOnClickListener(v -> {
            if (!TextUtils.equals(mEtInput.getText().toString(), "")) {
                searchKeyword(mEtInput.getText().toString());
            } else {
                toast("请输入商家名称");
            }
        });
    }

    private void initData() {
        mPoiSearch = PoiSearch.newInstance();
        city = getIntent().getStringExtra("city");
    }

    OnGetPoiSearchResultListener getCityInfo = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            dismissProgress();
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR && poiResult.getAllPoi() != null) {
                if (poiResult.getAllPoi().size() > 0) {
                    dataList.clear();
                    dataList.addAll(poiResult.getAllPoi());
                    mAdp.setDataList(dataList);
                    mAdp.notifyDataSetChanged();
                } else {
                    toast("未搜索到相关数据");
                }
            } else {
                toast("未搜索到相关数据");
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

    /**
     * 关键字搜索
     *
     * @param str
     */
    private void searchKeyword(final String str) {
        showProgress();
        new Thread(() -> {
            Looper.prepare();
            mPoiSearch.setOnGetPoiSearchResultListener(getCityInfo);
            PoiCitySearchOption cityOption = new PoiCitySearchOption();
            cityOption.city(city);
            cityOption.keyword(str);
            cityOption.pageCapacity(50);
            mPoiSearch.searchInCity(cityOption);
            Looper.loop();
        }).start();
    }

}
