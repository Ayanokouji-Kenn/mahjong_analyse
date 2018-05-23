package com.uu.mahjong_analyse.data.local

import com.uu.mahjong_analyse.data.entity.Player
import io.reactivex.Flowable

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */

interface PlayerDataSource {
    fun insertPlayer(name:String)
    fun selectPlayer(name:String):Flowable<Player>
    fun getPlayers():Flowable<List<Player>>
    fun updatePlayer(player:Player)
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