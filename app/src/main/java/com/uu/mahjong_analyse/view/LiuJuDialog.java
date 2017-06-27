package com.uu.mahjong_analyse.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.Constant;
import com.uu.mahjong_analyse.Utils.rx.RxBus;

import java.util.ArrayList;

/**
 * @auther xuzijian
 * @date 2017/6/9 18:05.
 * 流局的时候，点击中间的场次弹出的dialog，主要用于选择该次流局哪几家有过立直，而产生了供托
 */

public class LiuJuDialog implements DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    private String[] mChoices;
    private boolean[] mChecks = new boolean[4];
    private Context mContext;
    private AlertDialog mInstance;

    public LiuJuDialog(Context context, String[] choices) {
        mContext = context;
        mChoices = choices;

    }

    public void show() {
        if (mInstance == null) {
            mInstance = new AlertDialog.Builder(mContext).setTitle("流局")
                    .setMessage("请选择流局玩家")
                    .setMultiChoiceItems(mChoices, mChecks, this)
                    .setPositiveButton(mContext.getString(R.string.confirm), this)
                    .setNegativeButton(mContext.getString(R.string.cancel), this).create();
        }
        mInstance.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            ArrayList<String> richiPlayers = new ArrayList<>();
            for (int i = 0; i < mChecks.length; i++) {
                if (mChecks[i]) {
                    richiPlayers.add(mChoices[i]);
                }
            }
            RxBus.getInstance().send(richiPlayers, Constant.RX_LIUJU_RESULT);
            dialog.dismiss();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        mChecks[which] = isChecked;
    }
}
