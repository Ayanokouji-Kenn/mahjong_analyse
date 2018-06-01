package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.*
import com.uu.mahjong_analyse.data.entity.GameRecord
import io.reactivex.Flowable

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
    @Update fun update(gameRecord:GameRecord)
    @Query("SELECT * FROM game_record") fun selectList():Flowable< List<GameRecord>>
}