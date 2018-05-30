package com.uu.mahjong_analyse.view

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.blankj.rxbus.RxBus
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.bean.LiujuResult
import com.uu.mahjong_analyse.data.GameModle

/**
 * @auther xuzijian
 * @date 2017/6/9 18:05.
 * 流局的时候弹出的dialog，主要用于选择该次流局哪几家有过立直，而产生了供托
 */

class LiuJuDialog(private val mContext: Context) : DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    private val mPlayers: Array<String> = arrayOf(GameModle.getInstance().eastPlayer?.name?:""
            , GameModle.getInstance().southPlayer?.name?:""
            , GameModle.getInstance().westPlayer?.name?:""
            , GameModle.getInstance().northPlayer?.name?:"") //玩家名字
    private val mTingPais = BooleanArray(4)  //记录流局听牌
    private val mRichis = BooleanArray(4)     //记录立直

    fun show() {
        AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                .setTitle("流局了，请选择听牌玩家")
                .setMultiChoiceItems(mPlayers, mTingPais, this)
                .setPositiveButton(mContext.getString(R.string.confirm), this)
                .setNegativeButton(mContext.getString(R.string.cancel), this).create()
                .show()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            dialog.dismiss()
            val richiDialog = AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                    .setTitle("请选择立直的玩家")
                    .setMultiChoiceItems(mPlayers, mRichis) { _, who, isChecked ->
                        //选择立直玩家
                        updateRichiOrTingPai(true,who,isChecked)
                    }
                    .setPositiveButton(mContext.getString(R.string.confirm)) { _, _ ->
                        RxBus.getDefault().post(LiujuResult())
                    }
                    .setNegativeButton(mContext.getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }.create()
            richiDialog.show()
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss()
        }
    }

    /**
     * 选择听牌玩家
     */
    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        updateRichiOrTingPai(false,which,isChecked)
    }

    /**
     * 将听牌和立直的人的数据存进gamemodle
     */
    fun updateRichiOrTingPai(isRichi: Boolean, who: Int, isChecked: Boolean) {
        if (isRichi) {
            var richi = GameModle.getInstance().richi
            richi = when (who) {
                0 -> if (isChecked) richi or 0b1000 else richi and 0b0111
                1 -> if (isChecked) richi or 0b0100 else richi and 0b1011
                2 -> if (isChecked) richi or 0b0010 else richi and 0b1101
                else -> if (isChecked) richi or 0b0001 else richi and 0b1110
            }
            GameModle.getInstance().richi = richi
        }else {
            var tingpai = GameModle.getInstance().tingPai
            tingpai = when (who) {
                0 -> if (isChecked) tingpai or 0b1000 else tingpai and 0b0111
                1 -> if (isChecked) tingpai or 0b0100 else tingpai and 0b1011
                2 -> if (isChecked) tingpai or 0b0010 else tingpai and 0b1101
                else -> if (isChecked) tingpai or 0b0001 else tingpai and 0b1110
            }
            GameModle.getInstance().tingPai = tingpai
        }

    }
}
