package com.uu.mahjong_analyse.data.local

import com.uu.mahjong_analyse.data.entity.Player
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */

interface PlayerDataSource {
    fun insertPlayer(name:String):Completable
    fun selectPlayer(name:String):Single<Player>
    fun getPlayers():Flowable<List<Player>>
    fun updatePlayer(player:Player):Completable
    fun delPlayers(list: List<Player>)
    interface GetPlayerCallBack{
        fun onSuccess(player: Player)
        fun onFailure()
    }

    interface GetPlayerListCallBack{
        fun onSuccess(list: List<Player>)
        fun onFailure()
    }
}