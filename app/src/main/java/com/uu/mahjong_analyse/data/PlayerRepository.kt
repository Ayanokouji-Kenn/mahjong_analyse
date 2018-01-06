package com.uu.mahjong_analyse.data

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */


class PlayerRepository {
    companion object {

        private var INSTANCE: PlayerRepository? = null

        @JvmStatic fun getInstance() =
                INSTANCE ?: synchronized(PlayerRepository::class.java) {
                    INSTANCE ?: PlayerRepository()
                            .also { INSTANCE = it }
                }
    }
}