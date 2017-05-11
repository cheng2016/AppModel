package com.uniaip.android.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.uniaip.android.R;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class PopuWindViewUtlis {

    private View fathreView;
    private Context mContext;
    private PopupWindow popupWindow;

    public PopuWindViewUtlis(View v, Context context) {
        this.fathreView = v;
        this.mContext = context;
        if(null == popupWindow){
            popupWindow = new PopupWindow(mContext);
        }
    }


    public void showPopuWindRecordDel(final Handler mHandler){
        View conentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_record, null, false);
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(150);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(150);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAsDropDown(fathreView,0,0);
        TextView del = (TextView) conentView.findViewById(R.id.record_Del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(1);
                popupWindow.dismiss();
            }
        });
        TextView back = (TextView) conentView.findViewById(R.id.record_Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(2);
                popupWindow.dismiss();
            }
        });
    }

}
