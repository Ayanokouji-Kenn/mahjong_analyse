package com.uu.mahjong_analyse.data.local

import com.uu.mahjong_analyse.data.entity.Player
import io.reactivex.Completable
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

    override fun selectPlayer(name: String): Single<Player> = playerDao.selectPlayer(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun getPlayers() = playerDao.getPlayerList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun insertPlayer(name: String) =
            Completable.fromAction {
                playerDao.insertPlayer(Player(name = name))
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    override fun updatePlayer(player: Player) =
            Completable.fromAction { playerDao.updatePlayer(player) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    companion object {
        private var INSTANCE: PlayerDataSourceImpl? = null
        fun getInstance(playerDao: PlayerDao): PlayerDataSourceImpl =
                synchronized(PlayerDataSourceImpl::class.java) {
                    INSTANCE ?: PlayerDataSourceImpl(playerDao).also { INSTANCE = it }
                }
    }
}
