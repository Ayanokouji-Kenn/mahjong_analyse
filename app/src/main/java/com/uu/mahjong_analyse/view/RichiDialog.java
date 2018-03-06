package com.uu.mahjong_analyse.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.uu.mahjong_analyse.R;

import java.util.ArrayList;

/**
 * @auther xuzijian
 * @date 2017/7/6 13:58.
 */

public class RichiDialog implements DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener{
    private String[] mPlayers;
    private boolean[] mRichis = new boolean[4];     //记录立直
    private Context mContext;
    private AlertDialog mInstance;
    public RichiDialog(Context context,String[] players) {
        mContext = context;
        mPlayers = players;
    }

    public void show() {
        if (mInstance == null) {
            mInstance = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                    .setTitle("请选择已经立直的玩家")
                    .setMultiChoiceItems(mPlayers,mRichis,this)
                    .setNegativeButton(mContext.getResources().getString(R.string.cancel),this)
                    .setPositiveButton(mContext.getString(R.string.confirm), this).create();
        }
        if (!mInstance.isShowing()) {
            mInstance.show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            ArrayList<String> richiPlayers = new ArrayList<>();
            for(int i = 0;i<mPlayers.length;i++) {
                if (mRichis[i]) {
                    richiPlayers.add(mPlayers[i]);
                }
            }
//            RxBus.getInstance().send(richiPlayers, Constant.RX_RICHI_RESULT);
        }
        mInstance.dismiss();
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        mRichis[which] = isChecked;
    }
}
