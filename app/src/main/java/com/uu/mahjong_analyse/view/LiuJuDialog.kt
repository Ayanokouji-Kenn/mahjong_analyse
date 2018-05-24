package com.uu.mahjong_analyse.view

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.blankj.rxbus.RxBus
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.bean.LiujuResult
import com.uu.mahjong_analyse.data.GameModle
import java.util.*

/**
 * @auther xuzijian
 * @date 2017/6/9 18:05.
 * 流局的时候弹出的dialog，主要用于选择该次流局哪几家有过立直，而产生了供托
 */

class LiuJuDialog(private val mContext: Context) : DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    private val mPlayers: Array<String> = arrayOf(GameModle.getInstance().eastName
            , GameModle.getInstance().southName
            , GameModle.getInstance().westName
            , GameModle.getInstance().northName) //玩家名字
    private val mTingPais = BooleanArray(4)  //记录流局听牌
    private val mRichis = BooleanArray(4)     //记录立直

    fun show() {
        AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert_Self).setTitle("流局了，请选择听牌玩家")
                .setMultiChoiceItems(mPlayers, mTingPais, this)
                .setPositiveButton(mContext.getString(R.string.confirm), this)
                .setNegativeButton(mContext.getString(R.string.cancel), this).create()
                .show()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            dialog.dismiss()
            val richiDialog = AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert_Self).setTitle("请选择立直的玩家")
                    .setMultiChoiceItems(mPlayers, mRichis) { _, _, isChecked ->
                        //选择立直玩家
                        mRichis[which] = isChecked
                    }
                    .setPositiveButton(mContext.getString(R.string.confirm)) { _, _ ->
                        val tingPaiList = mTingPais.filter { it }
//                        不听罚符
                        val punishPoint =  when(tingPaiList.size) {
                            1-> 1000
                            2-> 1500
                            3-> 3000
                            else -> 0
                        }
                        for (i in 0..3) {
                            when (i) {
                                0 -> {
                                    if(mRichis[i]) GameModle.getInstance().east -= 1000
                                    if(mTingPais[i]) {
                                        GameModle.getInstance().east+=punishPoint
                                    }else {
                                        GameModle.getInstance().east-=punishPoint
                                    }
                                }
                                1 -> {
                                    if(mRichis[i]) GameModle.getInstance().south -= 1000
                                    if(mTingPais[i]) {
                                        GameModle.getInstance().south+=punishPoint
                                    }else {
                                        GameModle.getInstance().south-=punishPoint
                                    }
                                }
                                2 -> {
                                    if(mRichis[i]) GameModle.getInstance().west -= 1000
                                    if(mTingPais[i]) {
                                        GameModle.getInstance().west+=punishPoint
                                    }else {
                                        GameModle.getInstance().west-=punishPoint
                                    }
                                }
                                3 -> {
                                    if(mRichis[i]) GameModle.getInstance().north -= 1000
                                    if(mTingPais[i]) {
                                        GameModle.getInstance().north+=punishPoint
                                    }else {
                                        GameModle.getInstance().north-=punishPoint
                                    }
                                }
                            }
                        }
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
        mTingPais[which] = isChecked
    }
}
