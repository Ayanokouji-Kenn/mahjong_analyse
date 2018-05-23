package com.uu.mahjong_analyse.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.view.textservice.SpellCheckerInfo
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.iflytek.cloud.thirdparty.P
import com.uu.mahjong_analyse.data.entity.TempGameDO
import io.reactivex.Single
import io.reactivex.internal.operators.completable.CompletableToSingle
import io.reactivex.schedulers.Schedulers

@Dao
interface TempGameInfoDao {
    @Insert
    fun add(tempGameDO: TempGameDO)
    @Query("SELECT * FROM temp_game") fun fetchInfoList():Single<List<TempGameDO>>
    @Delete
    fun delete(tempGameDO: TempGameDO)
    @Query("DELETE FROM TEMP_GAME") fun clear()
}

interface TempGameInfoDataSource {
    fun add(tempGameDO: TempGameDO)
    fun clear()
    fun fetchList():Single<List<TempGameDO>>
}

class TempGameInfoDataSourceImpl(private val tempGameInfoDao: TempGameInfoDao) :TempGameInfoDataSource{
    override fun add(tempGameDO: TempGameDO) {
        Single.just(1)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tempGameInfoDao.add(tempGameDO)
                },{LogUtils.w(it.message)})
    }

    override fun clear() {
        Single.just(1)
                .subscribeOn(Schedulers.io())
                .subscribe({tempGameInfoDao.clear()},{LogUtils.w(it.message)})
    }

    override fun fetchList(): Single<List<TempGameDO>> {
        return tempGameInfoDao.fetchInfoList()
    }

    companion object {
        private var INSTANCE :TempGameInfoDataSourceImpl?=null
        fun getInstance(tempGameInfoDao: TempGameInfoDao) =
                INSTANCE?:TempGameInfoDataSourceImpl(tempGameInfoDao).also { INSTANCE=it }

    }
}

class TempGameInfoRepository(private val tempGameInfoDataSourceImpl: TempGameInfoDataSourceImpl) :TempGameInfoDataSource{
    override fun add(tempGameDO: TempGameDO) = tempGameInfoDataSourceImpl.add(tempGameDO)

    override fun clear() =tempGameInfoDataSourceImpl.clear()

    override fun fetchList(): Single<List<TempGameDO>> =tempGameInfoDataSourceImpl.fetchList()

    companion object {
        private var INSTANCE :TempGameInfoRepository?=null
        fun getInstance(tempGameInfoDataSourceImpl: TempGameInfoDataSourceImpl) =
                INSTANCE?:TempGameInfoRepository(tempGameInfoDataSourceImpl).also { INSTANCE=it }

    }
}