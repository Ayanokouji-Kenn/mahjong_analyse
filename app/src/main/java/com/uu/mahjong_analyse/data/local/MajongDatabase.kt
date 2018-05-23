package com.uu.mahjong_analyse.data.local

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.uu.mahjong_analyse.data.entity.GameRecord
import com.uu.mahjong_analyse.data.entity.Player

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */

@Database(entities = arrayOf(Player::class, GameRecord::class), version = 2)
abstract class MajongDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun tempGameInfoDao(): TempGameInfoDao

    companion object {
        val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()
                database.execSQL("CREATE TABLE player_record_new (\n" +
                        "id INTEGER PRIMARY KEY NOT NULL,\n" +
                        "name TEXT NOT NULL,\n" +
                        "total_games INTEGER NOT NULL,\n" +
                        "total_deal INTEGER NOT NULL,\n" +
                        "tsumo INTEGER NOT NULL,\n" +
                        "ronn INTEGER NOT NULL,\n" +
                        "he_count INTEGER NOT NULL,\n" +
                        "he_point_sum INTEGER NOT NULL,\n" +
                        "he_point_max INTEGER NOT NULL,\n" +
                        "richi_count INTEGER NOT NULL,\n" +
                        "richi_he INTEGER NOT NULL,\n" +
                        "ihhatsu_count INTEGER NOT NULL,\n" +
                        "chong_count INTEGER NOT NULL,\n" +
                        "chong_point_sum INTEGER NOT NULL,\n" +
                        "top INTEGER NOT NULL,\n" +
                        "second INTEGER NOT NULL,\n" +
                        "third INTEGER NOT NULL,\n" +
                        "last INTEGER NOT NULL,\n" +
                        "fei INTEGER NOT NULL,\n" +
                        "score_sum INTEGER NOT NULL,\n" +
                        "no_manguan INTEGER NOT NULL,\n" +
                        "manguan INTEGER NOT NULL,\n" +
                        "tiaoman INTEGER NOT NULL,\n" +
                        "beiman INTEGER NOT NULL,\n" +
                        "sanbeiman INTEGER NOT NULL,\n" +
                        "yakuman INTEGER NOT NULL\n" +
                        ");\n")
                database.execSQL("INSERT INTO player_record_new (id,name,total_games,total_deal,tsumo,ronn," +
                        "he_count,he_point_sum,he_point_max,richi_count,richi_he,ihhatsu_count," +
                        "chong_count,chong_point_sum,top,second,third,last,fei,score_sum,no_manguan,manguan,tiaoman,beiman,sanbeiman,yakuman)" +
                        " SELECT  \n" +
                        "_id\n" +
                        ",IFNULL(name,'')\n" +
                        ",IFNULL(total_games,0)\n" +
                        ",IFNULL(total_deal,0)\n" +
                        ",IFNULL(tsumo,0)\n" +
                        ",IFNULL(ronn,0)\n" +
                        ",IFNULL(he_count,0)\n" +
                        ",IFNULL(he_point_sum,0)\n" +
                        ",IFNULL(he_point_max,0)\n" +
                        ",IFNULL(richi_count,0)\n" +
                        ",IFNULL(richi_he,0)\n" +
                        ",IFNULL(ihhatsu_count,0)\n" +
                        ",IFNULL(chong_count,0)\n" +
                        ",IFNULL(chong_point_sum,0)\n" +
                        ",IFNULL(top,0)\n" +
                        ",IFNULL(second,0)n" +
                        ",IFNULL(third,0)\n" +
                        ",IFNULL(last,0)\n" +
                        ",IFNULL(fei,0)\n" +
                        ",IFNULL(score_sum,0)\n" +
                        ",IFNULL(no_manguan,0)\n" +
                        ",IFNULL(manguan,0)\n" +
                        ",IFNULL(tiaoman,0)\n" +
                        ",IFNULL(beiman,0)\n" +
                        ",IFNULL(sanbeiman,0)\n" +
                        ",IFNULL(yakuman, 0) FROM player_record;")
                database.execSQL("DROP TABLE player_record;")
                database.execSQL("ALTER TABLE player_record_new RENAME TO player_record;")
                database.execSQL(
                        "CREATE TABLE game_record_new (\n" +
                        "id INTEGER PRIMARY KEY NOT NULL,\n" +
                        "date TEXT NOT NULL,\n" +
                        "top TEXT NOT NULL,\n" +
                        "second TEXT NOT NULL,\n" +
                        "third TEXT NOT NULL,\n" +
                        "last TEXT NOT NULL\n" +
                        ");")
                database.execSQL("INSERT INTO game_record_new (\n" +
                        "id,date,top,second,third,last\n" +
                        ") SELECT " +
                        "_id," +
                        "IFNULL(date,'')," +
                         "IFNULL(top,0)," +
                         "IFNULL(second,0)," +
                         "IFNULL(third,0)," +
                         "IFNULL(last ,0)" +
                        "FROM game_record;")
                database.execSQL("DROP TABLE game_record;")
                database.execSQL("ALTER TABLE game_record_new RENAME TO game_record;")
                database.execSQL("CREATE TABLE test (id INTEGER,name TEXT,age INTEGER);")
                database.setTransactionSuccessful()
                database.endTransaction()
            }

        }
        private var INSTANCE: MajongDatabase? = null
        private val lock = Any()
        fun getInstance(context: Context): MajongDatabase =
                synchronized(lock) {
                    INSTANCE ?: Room.databaseBuilder(context.applicationContext
                            , MajongDatabase::class.java
                            , "mahjong_data.db")
                            .addMigrations(migration_1_2)
                            .build()
                }
    }
}