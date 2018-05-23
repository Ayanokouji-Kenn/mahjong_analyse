package com.uu.mahjong_analyse.data.local

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.uu.mahjong_analyse.data.entity.Player
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */


class PlayerDataSourceImpl(val playerDao: PlayerDao) : PlayerDataSource {
    override fun delPlayers(list: List<Player>) {
        playerDao.deleteALL(list)
    }

    override fun selectPlayer(name: String): Flowable<Player> = playerDao.selectPlayer(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun getPlayers() = playerDao.getPlayerList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun insertPlayer(name: String) {
        Single.just(name)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    playerDao.insertPlayer(Player().apply { this.name = it })
                    ToastUtils.showShort("新建用户成功")
                }, { LogUtils.d(it.message) })
    }


    override fun updatePlayer(player: Player) {
        Single.just(player)
                .subscribeOn(Schedulers.io())
                .subscribe({ playerDao.updatePlayer(it)
                ToastUtils.showShort("更新用户信息成功")
                }, { ToastUtils.showShort(it.message) })
    }


    companion object {
        private var INSTANCE: PlayerDataSourceImpl? = null
        fun getInstance(playerDao: PlayerDao): PlayerDataSourceImpl =
                synchronized(PlayerDataSourceImpl::class.java) {
                    INSTANCE ?: PlayerDataSourceImpl(playerDao).also { INSTANCE = it }
                }
    }
}
