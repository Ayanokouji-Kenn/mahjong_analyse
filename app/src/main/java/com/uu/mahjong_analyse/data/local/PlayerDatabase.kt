package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.uu.mahjong_analyse.data.entity.Player

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */

@Database(entities = arrayOf(Player::class), version = 1)
abstract class PlayerDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    companion object {
        private var INSTANCE: PlayerDatabase? = null
        private val lock = Any()
        fun getInstance(context: Context): PlayerDatabase =
                synchronized(lock) {
                    INSTANCE ?: Room.databaseBuilder(context.applicationContext
                            , PlayerDatabase::class.java
                            , "player.db")
                            .build()
                }
    }
}