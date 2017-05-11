package com.uniaip.android.home.invoice.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.uniaip.android.R;
import com.uniaip.android.home.invoice.models.CheckInfo;

import java.util.List;


/**
 * 作者: ysc
 * 时间: 2017/1/19
 */

public class TextSelectedAdapter extends RecyclerView.Adapter<TextSelectedAdapter.ViewHolder> {

    private Context mContext;
    private Handler mHandler;
    private List<CheckInfo> mList;

    public TextSelectedAdapter(Context mContext, Handler mHandler, List<CheckInfo> mList) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mList = mList;
    }

    public List<CheckInfo> getmLt() {
        return mList;
    }

    public void setmLt(List<CheckInfo> mLt) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextSelectedAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_radio_string, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.showData(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;


        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.tv_text);
        }

        public void showData(CheckInfo data,int position){
            mTv.setText(data.getName());
            if(data.isSelected()){
                mTv.setBackgroundResource(R.drawable.bg_button13);
                mTv.setTextColor(mContext.getResources().getColor(R.color.white));
            }else{
                mTv.setBackgroundResource(0);
                mTv.setTextColor(mContext.getResources().getColor(R.color.text_black));
            }

            mTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!data.isSelected()){
                        int sign = 0;//记录位置
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).isSelected()) {
                                sign = i;//获取之前选中位置
                            }
                        }
                        mList.get(sign).setSelected(false);
                        mList.get(position).setSelected(true);
                        notifyItemChanged(sign);
                        notifyItemChanged(position);
                        Message message=new Message();
                        message.what=101;
                        message.obj=mList.get(position).getName();
                        mHandler.sendMessage(message);
                    }
                }
            });
        }
    }
}
