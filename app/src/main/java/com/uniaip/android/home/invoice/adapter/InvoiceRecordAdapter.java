package com.uniaip.android.home.invoice.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uniaip.android.R;
import com.uniaip.android.home.invoice.activity.SubsidyDetailsActivity;
import com.uniaip.android.home.invoice.models.InvoiceRecordModel;
import com.uniaip.android.utils.DialogView;
import com.uniaip.android.utils.PopuWindViewUtlis;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class InvoiceRecordAdapter extends RecyclerView.Adapter<InvoiceRecordAdapter.ViewHolder> {
    private Context mContext;
    private List<InvoiceRecordModel> recordList;
    private DialogView dialogView;
    private Handler mHandler;
    private PopuWindViewUtlis popuWindViewUtlis;
    public InvoiceRecordAdapter(Context con, List<InvoiceRecordModel> dataList,Handler handler) {
        this.mContext = con;
        this.recordList = dataList;
        this.mHandler = handler;
        if (null == dialogView) {
            dialogView = new DialogView(mContext);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_record_list, parent, false);
        return new ViewHolder(v);
    }

    public int getSize() {
        return this.recordList.size();
    }

    public void addAllData(List<InvoiceRecordModel> dataList) {
        this.recordList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.recordList.clear();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_RecordInfo.setText(recordList.get(position).getInfo());
        holder.tv_RecordMoney.setText(recordList.get(position).getMoney());
        holder.tv_RecordType.setText(recordList.get(position).getType_name());
        holder.tv_RecordTime.setText(recordList.get(position).getTime());
        holder.tv_RecordPoint.setText(recordList.get(position).getPoint());
        holder.tv_RecordState.setText(recordList.get(position).getState());
        Glide.with(mContext).load(recordList.get(position).getImg_url()).into(holder.img_recordIcon);

        //跳转到详情页
        holder.img_RecordDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SubsidyDetailsActivity.class));
            }
        });

        //撤销，删除
        holder.img_RecordItemOpe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuWindViewUtlis = new PopuWindViewUtlis(v,mContext);
                popuWindViewUtlis.showPopuWindRecordDel(mHandler);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_recordIcon;
        private ImageView img_RecordItemOpe;
        private ImageView img_RecordDetail;
        private TextView tv_RecordType;
        private TextView tv_RecordTime;
        private TextView tv_RecordMoney;
        private TextView tv_RecordPoint;
        private TextView tv_RecordState;
        private TextView tv_RecordInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            img_recordIcon = (ImageView) itemView.findViewById(R.id.img_recordIcon);
            img_RecordItemOpe = (ImageView) itemView.findViewById(R.id.img_RecordItemOpe);
            img_RecordDetail = (ImageView) itemView.findViewById(R.id.img_RecordDetail);
            tv_RecordType = (TextView) itemView.findViewById(R.id.tv_RecordType);
            tv_RecordTime = (TextView) itemView.findViewById(R.id.tv_RecordTime);
            tv_RecordMoney = (TextView) itemView.findViewById(R.id.tv_RecordMoney);
            tv_RecordPoint = (TextView) itemView.findViewById(R.id.tv_RecordPoint);
            tv_RecordState = (TextView) itemView.findViewById(R.id.tv_RecordState);
            tv_RecordInfo = (TextView) itemView.findViewById(R.id.tv_RecordInfo);
        }
    }

}
