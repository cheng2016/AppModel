package com.uniaip.android.home.invoice.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.home.invoice.adapter.MapDataAdapter;
import com.uniaip.android.home.invoice.adapter.TextSelectedAdapter;
import com.uniaip.android.home.invoice.models.CheckInfo;
import com.uniaip.android.utils.DialogView;
import com.uniaip.android.utils.UtilsAll;
import com.zaaach.citypicker.CityPickerActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;


/**
 * 百度地图
 * 作者: ysc
 * 时间: 2017/1/14
 */

public class MapActivity extends BaseActivity implements View.OnClickListener {
    private final int BAIDU_READ_PHONE_STATE = 1101;
    private static final int REQUEST_CODE_PICK_CITY = 233;
    @BindView(R.id.rv_map_keyword)
    RecyclerView mRv;
    @BindView(R.id.lv_map_data)
    ListView mLv;//商家列表
    @BindView(R.id.mv_baidu)
    MapView mMapView;
    @BindView(R.id.lay_map_search)
    LinearLayout mLaySearch;//搜索
    @BindView(R.id.tv_locationCity)
    TextView mTvRight;//城市
    @BindView(R.id.iv_map_location)
    ImageView mIvLocation;//定位
    @BindView(R.id.iv_map_plus)
    ImageView mIvPlus;//放大
    @BindView(R.id.iv_map_reduce)
    ImageView mIvReduce;//缩小
    BaiduMap mBaiduMap;
    private PoiSearch mPoiCity;
    private PoiSearch mPoiSearch;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;
    private double mLongitude = -99;//经度
    private double mLatitude = -99;//维度
    private MapDataAdapter mAdapter;
    private TextSelectedAdapter mAdp;
    private String searchText = "美食";
    private List<CheckInfo> strs;//选项
    private GeoCoder mGeoCoder;
    private LocationClientOption option;
    private PoiNearbySearchOption pnso;
    private List<PoiInfo> dataList = new ArrayList<>();//商家列表数据
    private DialogView mDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_map;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            initAll();
        }
//        mTvTitle.setText(R.string.me_text16);
        findViewById(R.id.iv_search_down).setVisibility(View.VISIBLE);
    }

    private void initAll() {
        initView();
        operationMap();
        getListener();
        initData();
    }

    public void showContacts() {
        initAll();
    }

    private void initView() {

        mTvRight.setVisibility(View.VISIBLE);
        mLaySearch.setVisibility(View.VISIBLE);

        strs = new ArrayList<>();
        strs.add(new CheckInfo("美食", true));
        strs.add(new CheckInfo("娱乐", false));
        strs.add(new CheckInfo("餐饮", false));
        strs.add(new CheckInfo("交通", false));
        mAdp = new TextSelectedAdapter(MapActivity.this, mHandler, strs);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv.setLayoutManager(lin);
        mRv.setAdapter(mAdp);
        mPoiSearch = PoiSearch.newInstance();
        mPoiCity = PoiSearch.newInstance();
        myListener = new MyLocationListener();
        mBaiduMap = mMapView.getMap();// 开启定位图层
        mGeoCoder = GeoCoder.newInstance();
    }

    private void operationMap() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(new LatLng(mLatitude, mLongitude)).zoom(18).build();// 设置级别
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);// 注册定位监听接口
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        mPoiCity.setOnGetPoiSearchResultListener(getCityInfo);
        pnso = new PoiNearbySearchOption();
        pnso.radius(200);  // 检索半径，单位是米
        pnso.pageCapacity(30);  // 默认每页30条
        /**
         * 设置定位参数
         */
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
//      option.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
        option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start(); // 调用此方法开始定位
    }

    private void getListener() {
        mLaySearch.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mIvLocation.setOnClickListener(this);
        mIvPlus.setOnClickListener(this);
        mIvReduce.setOnClickListener(this);
        mLv.setOnItemClickListener((parent, view, position, id) -> {
            PoiInfo poi = dataList.get(position);
            Intent intent = new Intent();
            intent.putExtra("address", new CheckInfo(poi.name, poi.city, poi.address, poi.location.longitude, poi.location.latitude));
            setResult(1101, intent);
            finish();
        });
        mBaiduMap.setOnMyLocationClickListener(() -> false);

        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {//没有检索到结果
//                    return;
//                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {//没有找到检索结果
                    return;
                }
//                addrStr = result.getAddressDetail().countryName + result.getAddressDetail().province + result.getAddressDetail().city + result.getAddressDetail().district;
                mTvRight.setText(result.getAddressDetail().city);
            }
        });

        //移动地图监听
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LatLng mCenterLatLng = mapStatus.target;
                mLongitude = mCenterLatLng.longitude;
                mLatitude = mCenterLatLng.latitude;
                //先清除图层
                mBaiduMap.clear();
                searchNeayBy(searchText);
            }
        });
    }

    private void initData() {
        mAdapter = new MapDataAdapter(this);
        mLv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.lay_map_search://搜索
                intent = new Intent(MapActivity.this, SearchShopActivity.class);
                intent.putExtra("city", mTvRight.getText().toString());
                startActivityForResult(intent, 1201);
                break;
            case R.id.tv_locationCity://城市
                intent = new Intent(MapActivity.this, CityPickerActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PICK_CITY);
                break;
            case R.id.iv_map_location://定位
                operationMap();
                break;
            case R.id.iv_map_plus://放大
                if (mBaiduMap.getMapStatus().zoom == mBaiduMap.getMaxZoomLevel()) {
                    break;
                }
                if ((mBaiduMap.getMapStatus().zoom + 1) > mBaiduMap.getMaxZoomLevel()) {
                    mapZoom(mBaiduMap.getMaxZoomLevel());
                } else {
                    mapZoom(mBaiduMap.getMapStatus().zoom + 1);
                }
                break;
            case R.id.iv_map_reduce://缩小
                if (mBaiduMap.getMapStatus().zoom == mBaiduMap.getMinZoomLevel()) {
                    break;
                }
                if ((mBaiduMap.getMapStatus().zoom - 1) < mBaiduMap.getMinZoomLevel()) {
                    mapZoom(mBaiduMap.getMinZoomLevel());
                } else {
                    mapZoom(mBaiduMap.getMapStatus().zoom - 1);
                }
                break;
        }
    }

    private void mapZoom(float num) {
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(num).build()));//设置缩放级别
    }


    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        public void onGetPoiResult(PoiResult result) {
            //获取POI检索结果
            if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                if (mAdapter.getDataList() != null) {
                    mAdapter.getDataList().clear();
                    mAdapter.notifyDataSetChanged();
                }
                if (result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND && mLongitude <= 10 && mLatitude <= 10) {
//                    if(null==mDialog)
//                        mDialog=new DialogView(mContext);
                    toast("未能获取到定位权限，无法获取位置信息。请前往应用详情手动添加相关权限。");
//                    mDialog.showDetermine(mHandler,"未能获取到定位权限，请前往应用详情手动添加相关权限。",400);
                }
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                if (result.getAllPoi() != null && result.getAllPoi().size() > 0) {
                    dataList.clear();
                    dataList.addAll(result.getAllPoi());
                    mAdapter.setDataList(dataList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };
    OnGetPoiSearchResultListener getCityInfo = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR && poiResult.getAllPoi() != null) {
                if (poiResult.getAllPoi().size() > 0) {
                    mLatitude = poiResult.getAllPoi().get(0).location.latitude;
                    mLongitude = poiResult.getAllPoi().get(0).location.longitude;
                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(new LatLng(mLatitude, mLongitude)).zoom(18).build());
                    mBaiduMap.setMapStatus(mMapStatusUpdate);

                    dataList.clear();
                    dataList.addAll(poiResult.getAllPoi());
                    mAdapter.setDataList(dataList);
                    mAdapter.notifyDataSetChanged();
                }

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
     * 搜索周边
     */
    private void searchNeayBy(final String str) {

        new Thread(() -> {
            Looper.prepare();
            // POI初始化搜索模块，注册搜索事件监听

            pnso.keyword(str);
            pnso.location(new LatLng(mLatitude, mLongitude));
            mPoiSearch.searchNearby(pnso);  // 发起附近检索请求
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(mLatitude, mLongitude)));
            Looper.loop();
        }).start();
    }

    /**
     * 搜索城市
     *
     * @param str
     */
    private void searchCity(final String str) {
        new Thread(() -> {
            Looper.prepare();

            PoiCitySearchOption cityOption = new PoiCitySearchOption();
            cityOption.city(str);
            cityOption.keyword(str);
            cityOption.pageCapacity(50);
            mPoiCity.searchInCity(cityOption);
            Looper.loop();
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
//        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        try {
            mMapView.onResume();
        } catch (Exception e) {

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }
            mLongitude = location.getLongitude();
            mLatitude = location.getLatitude();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            //将当前位置加入List里面
            PoiInfo info = new PoiInfo();
            info.address = location.getAddrStr();
            info.city = location.getCity();
            info.location = ll;
            info.name = location.getAddrStr();
            dataList.add(info);
//            mTvRight.setText(location.getCity());
//            adapter.notifyDataSetChanged();


            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            //画标志
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(ll);
            converter.from(CoordinateConverter.CoordType.COMMON);
//            LatLng convertLatLng = converter.convert();
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 15.0f);
//            mBaiduMap.animateMapStatus(u);

            //画当前定位标志
            MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(uc);

            mMapView.showZoomControls(false);
            searchNeayBy(searchText);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
                    strs = mAdp.getmLt();
                    searchText = (String) msg.obj;
                    searchNeayBy(searchText);
                    break;
                case 400:
                    finish();
                    UtilsAll.goAPPDetails(mContext);
                    break;
                case -99:
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initAll();
                } else {
                    // 没有获取到权限，做特殊处理
                    toast("获取位置权限失败，请手动开启");
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE_PICK_CITY && data != null) {
                    String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                    mTvRight.setText(city);
                    searchCity(city);
                }
                break;
            case 1201:
                if (data != null) {
                    CheckInfo check = (CheckInfo) data.getSerializableExtra("address");
//                    mLatitude = check.getLatitude();
//                    mLongitude = check.getLongitude();
//                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus( new MapStatus.Builder().target(new LatLng(mLatitude, mLongitude)).zoom(18).build());
//                    mBaiduMap.setMapStatus(mMapStatusUpdate);
//                    searchNeayBy(searchText);
                    Intent intent = new Intent();
                    intent.putExtra("address", check);
                    setResult(1101, intent);
                    finish();
                }
                break;
        }

    }


}
