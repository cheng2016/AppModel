package com.uniaip.android.home.invoice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.uniaip.android.R;

import java.util.List;

/**
 * 作者: ysc
 * 时间: 2017/1/18
 */

public class MapDataAdapter extends BaseAdapter {
    private List<PoiInfo> dataList;
    private Context mContext;

    public MapDataAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<PoiInfo> getDataList() {
        return dataList;
    }

    public void setDataList(List<PoiInfo> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_map_data, null);
            mViewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_map_name);
            mViewHolder.mTvAddress = (TextView) convertView.findViewById(R.id.tv_map_address);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mTvName.setText(dataList.get(position).name);
        mViewHolder.mTvAddress.setText(dataList.get(position).address);
        return convertView;
    }

    class ViewHolder {
        TextView mTvName; //名称
        TextView mTvAddress;//地址
    }
}
