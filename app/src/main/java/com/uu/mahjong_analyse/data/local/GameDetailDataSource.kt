package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.uu.mahjong_analyse.data.entity.GameDetailDO
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Dao
interface GameDetailDao {
    @Insert
    fun add(GameDetailDO: GameDetailDO)

    @Query("SELECT * FROM game_detail")
    fun fetchInfoList(): Single<List<GameDetailDO>>

    @Delete
    fun delete(GameDetailDO: GameDetailDO)

    @Query("DELETE FROM game_detail")
    fun clear()
}

interface GameDetailDataSource {
    fun add(GameDetailDO: GameDetailDO):Completable
    fun clear():Completable
    fun fetchList(): Single<List<GameDetailDO>>
}

class GameDetailDataSourceImpl(private val GameDetailDODao: GameDetailDao) : GameDetailDataSource {
    override fun add(GameDetailDO: GameDetailDO) =
        Completable.fromAction {
            GameDetailDODao.add(GameDetailDO)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    override fun clear() =
        Completable.fromAction {
            GameDetailDODao.clear()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    override fun fetchList(): Single<List<GameDetailDO>> {
        return GameDetailDODao.fetchInfoList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private var INSTANCE: GameDetailDataSourceImpl? = null
        fun getInstance(GameDetailDODao: GameDetailDao) =
                INSTANCE ?: GameDetailDataSourceImpl(GameDetailDODao).also { INSTANCE = it }

    }
}

class GameDetailRepository(private val GameDetailDODataSourceImpl: GameDetailDataSourceImpl) : GameDetailDataSource {
    override fun add(GameDetailDO: GameDetailDO) = GameDetailDODataSourceImpl.add(GameDetailDO)

    override fun clear() = GameDetailDODataSourceImpl.clear()

    override fun fetchList(): Single<List<GameDetailDO>> = GameDetailDODataSourceImpl.fetchList()

    companion object {
        private var INSTANCE: GameDetailRepository? = null
        fun getInstance(GameDetailDODataSourceImpl: GameDetailDataSourceImpl) =
                INSTANCE
                        ?: GameDetailRepository(GameDetailDODataSourceImpl).also { INSTANCE = it }

    }
}