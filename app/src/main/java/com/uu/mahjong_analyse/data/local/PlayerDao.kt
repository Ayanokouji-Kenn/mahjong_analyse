package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.*
import com.uu.mahjong_analyse.data.entity.Player
import io.reactivex.Flowable

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player_record")  fun getPlayerList(): Flowable<List<Player>>

    @Update fun updatePlayer(player:Player)

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertPlayer(player: Player):Long

    @Query("SELECT * FROM player_record WHERE name=:name") fun selectPlayer(name:String):Flowable<Player>

    @Delete fun deleteALL(list: List<Player>)
}