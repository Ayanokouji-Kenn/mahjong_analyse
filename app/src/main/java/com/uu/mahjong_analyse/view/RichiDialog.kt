package com.uu.mahjong_analyse.view

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.blankj.rxbus.RxBus
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.bean.RichiEvent
import com.uu.mahjong_analyse.data.GameModle
import java.util.*

/**
 * @auther xuzijian
 * @date 2017/7/6 13:58.
 */

class RichiDialog(private val mContext: Context) : DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    private val mRichis = BooleanArray(4)     //记录立直
    private var mInstance: AlertDialog? = null

    fun show() {
        if (mInstance == null) {
            val players  = GameModle.getInstance().run {
                arrayOf(eastPlayer?.name?:"",southPlayer?.name?:"",westPlayer?.name?:"",northPlayer?.name?:"") }
            mInstance = AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                    .setTitle("请选择已经立直的玩家")
                    .setMultiChoiceItems(players, mRichis, this)
                    .setNegativeButton(mContext.resources.getString(R.string.cancel), this)
                    .setPositiveButton(mContext.getString(R.string.confirm), this).create()
        }
        if (!mInstance!!.isShowing) {
            mInstance!!.show()
        }
    }

//    点击确定 取消按钮
    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            RxBus.getDefault().post(RichiEvent())
        }
        mInstance!!.dismiss()
    }

//    点击选择框
    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        updateRichi(which,isChecked)
    }

    fun updateRichi(who: Int, isChecked: Boolean) {
        var richi = GameModle.getInstance().richi
        richi = when (who) {
            0 -> if (isChecked) richi or 0b1000 else richi and 0b0111
            1 -> if (isChecked) richi or 0b0100 else richi and 0b1011
            2 -> if (isChecked) richi or 0b0010 else richi and 0b1101
            else -> if (isChecked) richi or 0b0001 else richi and 0b1110
        }
        GameModle.getInstance().richi = richi
    }

}
