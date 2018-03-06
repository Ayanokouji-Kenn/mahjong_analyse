package com.uu.mahjong_analyse.data

import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.data.local.PlayerDataSourceImpl

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */


class PlayerRepository(private val playerDataSourceImpl: PlayerDataSourceImpl) :PlayerDataSource{
    override fun delPlayers(list: List<Player>) =playerDataSourceImpl.delPlayers(list)

    override fun insertPlayer(name: String) =  playerDataSourceImpl.insertPlayer(name)

    override fun selectPlayer(name: String) =  playerDataSourceImpl.selectPlayer(name)

    override fun getPlayers() = playerDataSourceImpl.getPlayers()

    override fun updatePlayer(player: Player) =  playerDataSourceImpl.updatePlayer(player)


    companion object {

        private var INSTANCE: PlayerRepository? = null

        @JvmStatic fun getInstance(playerDataSourceImpl: PlayerDataSourceImpl) =
                INSTANCE ?: synchronized(PlayerRepository::class.java) {
                    INSTANCE ?: PlayerRepository(playerDataSourceImpl)
                            .also { INSTANCE = it }
                }
    }
}