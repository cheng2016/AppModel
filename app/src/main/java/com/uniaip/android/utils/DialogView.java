package com.uniaip.android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uniaip.android.R;

/**
 * 作者: ysc
 * 时间: 2017/1/12
 */

public class DialogView {
    private Context mContext;
    private Dialog mAlertDialog;
    private TextView mTvContent;
    private ImageView mIvOpen;
    private RelativeLayout mRlayMoney;

    //默认样式
    public DialogView(Context mContext) {
        this.mContext = mContext;
//        mAlertDialog = new AlertDialog.Builder(this.mContext, R.style.my_dialog).create();
        mAlertDialog = new Dialog(this.mContext, R.style.my_dialog);
    }


    /**
     * 确认Dialog
     *
     * @param mHandler
     * @param text
     */
    public void showDetermine(final Handler mHandler, String text, int what) {
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(((Activity) mContext).getLayoutInflater().inflate(R.layout.dialog_determine, null));

        LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(
                (int) (((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth() / 1.5),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup loy = (ViewGroup) mAlertDialog.findViewById(R.id.lay_dialog);

        Window window = mAlertDialog.getWindow();
        window.setGravity(Gravity.CENTER);//显示中间
        loy.setLayoutParams(imgparams);
        ((TextView) mAlertDialog.findViewById(R.id.tv_dialog_text)).setText(text);

        mAlertDialog.findViewById(R.id.tv_dialog_remind_commit).setOnClickListener(v -> {
            mHandler.sendEmptyMessage(what);
            mAlertDialog.dismiss();
        });
        mAlertDialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(v -> {
            mHandler.sendEmptyMessage(-99);
            mAlertDialog.dismiss();
        });
    }



    public void dismissAlertDialog() {
        if (null != mAlertDialog) {
            mAlertDialog.dismiss();
        }
    }


}
