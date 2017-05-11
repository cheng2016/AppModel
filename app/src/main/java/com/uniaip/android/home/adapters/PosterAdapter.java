package com.uniaip.android.home.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.uniaip.android.R;
import com.uniaip.android.home.models.ModularInfo;

import java.util.List;

/**
 * 海报列表适配器
 * 作者: ysc
 * 时间: 2017/4/25
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder>{

    private Context mContext;
    private List<ModularInfo> mList;

    public PosterAdapter(Context mContext, List<ModularInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_poster_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.showData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvPoster;

        ViewHolder(View itemView) {
            super(itemView);
            mIvPoster = (ImageView) itemView.findViewById(R.id.iv_poster_icon);
        }

        void showData(ModularInfo info) {
            if (!info.getIcon().equals(""))
                Glide.with(mContext).load(info.getIcon()).into(mIvPoster);

        }


    }
}
