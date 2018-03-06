package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.uu.mahjong_analyse.data.entity.GameRecord

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */

@Dao
interface GameRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(gameRecord: GameRecord)

    @Query("SELECT * FROM game_record") fun selectList():List<GameRecord>
}