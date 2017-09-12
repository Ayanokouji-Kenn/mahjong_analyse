package com.uu.mahjong_analyse.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.utils.Constant;
import com.uu.mahjong_analyse.utils.rx.RxBus;
import com.uu.mahjong_analyse.bean.LiujuResult;

import java.util.ArrayList;

/**
 * @auther xuzijian
 * @date 2017/6/9 18:05.
 * 流局的时候，点击中间的场次弹出的dialog，主要用于选择该次流局哪几家有过立直，而产生了供托
 */

public class LiuJuDialog implements DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    private String[] mPlayers;                      //玩家名字
    private boolean[] mTingPais = new boolean[4];  //记录流局听牌
    private boolean[] mRichis = new boolean[4];     //记录立直
    private Context mContext;
    private AlertDialog mInstance;

    public LiuJuDialog(Context context, String[] choices) {
        mContext = context;
        mPlayers = choices;

    }

    public void show() {
        if (mInstance == null) {
            mInstance = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert_Self).setTitle("流局了，请选择听牌玩家")
                    .setMultiChoiceItems(mPlayers, mTingPais, this)
                    .setPositiveButton(mContext.getString(R.string.confirm), this)
                    .setNegativeButton(mContext.getString(R.string.cancel), this).create();
        }
        mInstance.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            dialog.dismiss();
            AlertDialog richiDialog = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert_Self).setTitle("请选择立直的玩家")
                    .setMultiChoiceItems(mPlayers, mRichis, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            //选择立直玩家
                            mRichis[which] = isChecked;
                        }
                    })
                    .setPositiveButton(mContext.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LiujuResult liujuResult = new LiujuResult();
                            liujuResult.tingpaiPlayers = new ArrayList<String>();
                            liujuResult.richiPlayers = new ArrayList<String>();
                            for (int i = 0; i < mPlayers.length; i++) {
                                if (mTingPais[i]) {
                                    liujuResult.tingpaiPlayers.add(mPlayers[i]);
                                }
                                if (mRichis[i]) {
                                    liujuResult.richiPlayers.add(mPlayers[i]);
                                }
                            }
                            RxBus.getInstance().send(liujuResult, Constant.RX_LIUJU_RESULT);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            richiDialog.show();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }

    /**
     * 选择听牌玩家
     * */
    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        mTingPais[which] = isChecked;
    }
}
