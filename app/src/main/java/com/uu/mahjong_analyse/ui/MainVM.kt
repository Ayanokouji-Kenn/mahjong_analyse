package com.uu.mahjong_analyse.ui

import android.app.Application
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseVM
import com.uu.mahjong_analyse.data.PlayerRepository
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.data.local.MajongDatabase
import com.uu.mahjong_analyse.data.local.PlayerDataSourceImpl

/**
 * @description
 * @auther xuzijian
 * @date 2017/10/24 14:24.
 */

class MainVM(app: Application) : BaseVM(app) {
    var isStart = false
    var hePlayer: String = ""
    var chang = ObservableInt(0)  //0-7 代表东1到南4；
    var gong = ObservableInt(0)  //流局产生的供托,有人和牌则清零
    var benchang = ObservableInt(0)
    val players = ObservableArrayList<Player?>().apply { for (i in 1..4) add(null) }  //存放开局东一时候的  东南西北player
    var fgShow = ObservableField<String>()

    val playerReposity = PlayerRepository.getInstance(PlayerDataSourceImpl(MajongDatabase.getInstance(app).playerDao()))

    val totalPlayList = mutableListOf<Player>()
    fun getPlayers() {
        playerReposity.getPlayers()
                .subscribe({
                    LogUtils.d("zfdt---", "$it")
                    if (it.isNotEmpty()) {
                        totalPlayList.clear()
                        totalPlayList.addAll(it)
                    } else ToastUtils.showShort(mApp.getString(R.string.please_add_players_first))
                }, {LogUtils.d("zfdt---", it.message)})
    }

    fun insertPlayer(name: String) {
        playerReposity.insertPlayer(name)
        PermissionUtils.permission()
    }
}
