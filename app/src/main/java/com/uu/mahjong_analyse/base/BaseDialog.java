package com.uu.mahjong_analyse.base;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.uu.mahjong_analyse.R;

/**
 * @auther xuzijian
 * @date 2017/6/9 17:42.
 */

/**
 * 联系人列表
 * @author: laohu on 2016/12/24
 * @site: http://ittiger.cn
 */
public abstract class BaseDialog implements DialogInterface.OnClickListener {

    private AlertDialog mDialog;
    private Context mContext;

    public BaseDialog(Context context) {

        mContext = context;
    }

    public void show(String title,String msg) {
        if(mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(title)
                    .setCancelable(true)
                    .setNegativeButton(mContext.getString(R.string.cancel), this)
                    .setPositiveButton(mContext.getString(R.string.confirm), this);
            mDialog = builder.create();
        }
        mDialog.setMessage(msg);
        mDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            positiveClick();
            dialog.dismiss();
        } else if(which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }

    /**
     * 点击确定，除了关闭对话框以外，还要哪些操作
     * */
    abstract protected void positiveClick();
}
