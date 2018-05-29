package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.uu.mahjong_analyse.data.entity.TempGameDO
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Dao
interface TempGameInfoDao {
    @Insert
    fun add(tempGameDO: TempGameDO)

    @Query("SELECT * FROM temp_game")
    fun fetchInfoList(): Single<List<TempGameDO>>

    @Delete
    fun delete(tempGameDO: TempGameDO)

    @Query("DELETE FROM temp_game")
    fun clear()
}

interface TempGameInfoDataSource {
    fun add(tempGameDO: TempGameDO):Completable
    fun clear():Completable
    fun fetchList(): Single<List<TempGameDO>>
}

class TempGameInfoDataSourceImpl(private val tempGameInfoDao: TempGameInfoDao) : TempGameInfoDataSource {
    override fun add(tempGameDO: TempGameDO) =
        Completable.fromAction {
            tempGameInfoDao.add(tempGameDO)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    override fun clear() =
        Completable.fromAction {
            tempGameInfoDao.clear()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    override fun fetchList(): Single<List<TempGameDO>> {
        return tempGameInfoDao.fetchInfoList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private var INSTANCE: TempGameInfoDataSourceImpl? = null
        fun getInstance(tempGameInfoDao: TempGameInfoDao) =
                INSTANCE ?: TempGameInfoDataSourceImpl(tempGameInfoDao).also { INSTANCE = it }

    }
}

class TempGameInfoRepository(private val tempGameInfoDataSourceImpl: TempGameInfoDataSourceImpl) : TempGameInfoDataSource {
    override fun add(tempGameDO: TempGameDO) = tempGameInfoDataSourceImpl.add(tempGameDO)

    override fun clear() = tempGameInfoDataSourceImpl.clear()

    override fun fetchList(): Single<List<TempGameDO>> = tempGameInfoDataSourceImpl.fetchList()

    companion object {
        private var INSTANCE: TempGameInfoRepository? = null
        fun getInstance(tempGameInfoDataSourceImpl: TempGameInfoDataSourceImpl) =
                INSTANCE
                        ?: TempGameInfoRepository(tempGameInfoDataSourceImpl).also { INSTANCE = it }

    }
}