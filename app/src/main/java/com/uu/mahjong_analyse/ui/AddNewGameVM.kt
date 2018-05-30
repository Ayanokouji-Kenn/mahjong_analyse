package com.uu.mahjong_analyse.ui

import android.app.Application
import com.blankj.utilcode.util.ToastUtils
import com.uu.mahjong_analyse.base.BaseVM
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.data.local.MajongDatabase
import com.uu.mahjong_analyse.data.local.PlayerDao
import com.uu.mahjong_analyse.utils.SingleLiveEvent

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/09
 *     desc  :
 * </pre>
 */


class AddNewGameVM(app: Application) : BaseVM(app) {
    private var playDao: PlayerDao = MajongDatabase.getInstance(mApp).playerDao()
    val showPickerLiveData = SingleLiveEvent<Void>()
    val players = mutableListOf<Player>()
    val selectPlayers = arrayOfNulls<Player>(4)
    fun addPlayer(name: String) {
        mComposite.add(
                playerRepository.insertPlayer(name)
                        .subscribe({
                            ToastUtils.showShort("新增玩家成功")
                            getPlayers()
                        }, { ToastUtils.showShort("有重名 请重新输入") })
        )
    }

    fun getPlayers() {
        mComposite.add(
                playerRepository.getPlayers()
                        .subscribe {
                            if (it.isNotEmpty()) {
                                players.clear()
                                players.addAll(it)
                                showPickerLiveData.call()
                            }
                        }
        )
    }
}