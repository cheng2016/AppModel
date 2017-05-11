package com.uniaip.android.home.invoice.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.uniaip.android.R;
import com.uniaip.android.base.BaseActivity;
import com.uniaip.android.home.invoice.adapter.InvoiceRecordAdapter;
import com.uniaip.android.home.invoice.models.InvoiceRecordModel;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 补贴记录
 */

public class InvoiceRecordActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_main_title_right)
    TextView tvRight;
    @BindView(R.id.recordList)
    PullLoadMoreRecyclerView recordRecyclerView;

    private InvoiceRecordAdapter invoiceRecordAdapter;
    private RecyclerView mRecyclerView;
    private List<InvoiceRecordModel> recordList;
    private int mCount = 1;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_invoice_record;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        getListener();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    toast("删除");
                    break;
                case 2:
                    toast("撤销");
                    break;
            }
        }
    };

    private void initView(){
        tvRight.setVisibility(View.GONE);
        tvTitle.setText(R.string.text6);
        mRecyclerView = recordRecyclerView.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //线性
        recordRecyclerView.setLinearLayout();
        recordRecyclerView.setRefreshing(true);
        recordRecyclerView.setFooterViewText("loading");
        recordRecyclerView.setFooterViewTextColor(R.color.white);
        recordRecyclerView.setFooterViewBackgroundColor(R.color.text_color_type2);
        recordRecyclerView.setColorSchemeResources(android.R.color.holo_red_dark,android.R.color.holo_blue_dark);
        recordList = new ArrayList<>();
        invoiceRecordAdapter = new InvoiceRecordAdapter(InvoiceRecordActivity.this,recordList,mHandler);
        recordRecyclerView.setAdapter(invoiceRecordAdapter);

//        //网格
//        recordRecyclerView.setGridLayout(2);//参数为列数
//        //瀑布流
//        recordRecyclerView.setStaggeredGridLayout(2);//参数为列数
//        //快速Top
//        recordRecyclerView.scrollToTop();
//        //刷新结束
//        recordRecyclerView.setPullLoadMoreCompleted();
        getData();
    }

    private void getData(){
        recordList.add(new InvoiceRecordModel("http://img.qq1234.org/uploads/allimg/150409/1J6345630-0.jpg","波动拳","18:33","300","44","已通过","这是一个牛B的武林高手"));
        recordList.add(new InvoiceRecordModel("http://www.qq1234.org/uploads/allimg/150409/1J6341Q5-16.jpg","火云掌","18:34","320","40","未通过","这是一个牛B的老爷子"));
        recordList.add(new InvoiceRecordModel("http://www.qq1234.org/uploads/allimg/150409/1J6344447-12.jpg","ddd","18:35","340","45","审核中","这是一个牛B的dddd"));
        recordList.add(new InvoiceRecordModel("http://img.qq1234.org/uploads/allimg/150409/1J6345630-0.jpg","ggg","18:34","350","55","审核中","这是一个牛B的dssd"));
        recordList.add(new InvoiceRecordModel("http://img.qq1234.org/uploads/allimg/150409/1J6345630-0.jpg","hhh","18:31","380","88","未通过","这是一个牛B的武fdf"));
        recordList.add(new InvoiceRecordModel("http://wenwen.soso.com/p/20120131/20120131160600-1342074465.jpg","yy","18:41","321","55","已通过","这是一个牛B的武ggd手"));
        recordList.add(new InvoiceRecordModel("http://img.qq1234.org/uploads/allimg/150409/1J6345630-0.jpg","hhh","18:22","311","22","已通过","这是一个牛B的武ssd手"));
        invoiceRecordAdapter.addAllData(recordList);
        recordRecyclerView.setPullLoadMoreCompleted();
    }
    private void getListener(){
        recordRecyclerView.setOnPullLoadMoreListener(this);
    }

    public void clearData() {
        invoiceRecordAdapter.clearData();
        invoiceRecordAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        Log.e("wxl", "onRefresh");
        setRefresh();
        getData();
    }

    @Override
    public void onLoadMore() {
        Log.e("wxl", "onLoadMore");
        mCount = mCount + 1;
        getData();
    }

    private void setRefresh() {
        invoiceRecordAdapter.clearData();
        mCount = 1;
    }
}
