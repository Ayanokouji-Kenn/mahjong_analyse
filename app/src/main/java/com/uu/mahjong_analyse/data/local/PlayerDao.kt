package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.uu.mahjong_analyse.data.entity.Player

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */

@Dao
interface PlayerDao {
    @Query("select * FROM player_record")  fun getPlayerList():List<Player>
}