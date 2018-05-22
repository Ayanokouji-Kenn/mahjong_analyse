package com.uu.mahjong_analyse.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.uu.mahjong_analyse.base.BaseVM
import com.uu.mahjong_analyse.data.PlayerDataSource
import com.uu.mahjong_analyse.data.PlayerRepository
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.data.local.MajongDatabase
import com.uu.mahjong_analyse.data.local.PlayerDao
import com.uu.mahjong_analyse.data.local.PlayerDataSourceImpl
import com.uu.mahjong_analyse.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/09
 *     desc  :
 * </pre>
 */


class AddNewGameVM(app:Application) : BaseVM(app) {
    private var playDao:PlayerDao = MajongDatabase.getInstance(mApp).playerDao()
    val showPickerLiveData = SingleLiveEvent<Void>()
    val players = mutableListOf<Player>()
    fun addPlayer(name:String) {
        Single.just(name)
                .subscribeOn(Schedulers.io())
                .subscribe( {
                    playDao.insertPlayer(Player(name = name))
                    ToastUtils.showShort("新增玩家成功")
                },{ToastUtils.showShort(it.message)})
    }

    fun getPlayers() {
        playDao.getPlayerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    players.clear()
                    players.addAll(it)
                    showPickerLiveData.call()
                }
    }
}