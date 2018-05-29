package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.*
import com.uu.mahjong_analyse.data.entity.Player
import io.reactivex.Flowable
import io.reactivex.Single

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

    @Insert(onConflict = OnConflictStrategy.FAIL) fun insertPlayer(player: Player)

    @Query("SELECT * FROM player_record WHERE name=:name") fun selectPlayer(name:String):Single<Player>

    @Delete fun deleteALL(list: List<Player>)
}