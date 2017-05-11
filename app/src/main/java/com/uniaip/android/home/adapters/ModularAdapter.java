package com.uniaip.android.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uniaip.android.R;
import com.uniaip.android.home.invoice.activity.UploadInvoiceActivity;
import com.uniaip.android.home.models.ModularInfo;

import java.util.List;

/**
 * 作者: ysc
 * 时间: 2017/4/25
 */

public class ModularAdapter extends BaseAdapter {

    private Context mContext;
    private List<ModularInfo> mList;

    public ModularAdapter(Context mContext, List<ModularInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_modular, null);
            mViewHolder.mLayMain = (LinearLayout) convertView.findViewById(R.id.lay_item_main);
            mViewHolder.mIvIcon = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            mViewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_item_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

//        ViewGroup.LayoutParams mPar = mViewHolder.mIvImg.getLayoutParams();
//        mPar.height = (int) (LAppLication.mManager.getDefaultDisplay().getWidth() / 2.27);
//        mViewHolder.mIvImg.setLayoutParams(mPar);
        if (!mList.get(position).getIcon().equals(""))
            Glide.with(mContext).load(mList.get(position).getIcon()).into(mViewHolder.mIvIcon);

        mViewHolder.mTvName.setText(mList.get(position).getName());


        mViewHolder.mLayMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("2".equals(mList.get(position).getId())){
                    mContext.startActivity(new Intent(mContext, UploadInvoiceActivity.class));
                }
            }
        });
        return convertView;
    }


    private class ViewHolder {
        private ImageView mIvIcon;
        private TextView mTvName;
        private LinearLayout mLayMain;
    }
}
