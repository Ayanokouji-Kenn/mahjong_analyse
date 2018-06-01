package com.uu.mahjong_analyse.data.local

import com.uu.mahjong_analyse.data.entity.GameRecord
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */


class GameRecordDataSourceImpl(val gameRecordDao: GameRecordDao): GameRecordDataSource {
    override fun getGameRecordList(): Flowable<List<GameRecord>> {
        return gameRecordDao.selectList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(gameRecord: GameRecord):Completable {
       return Completable.fromAction{
            gameRecordDao.update(gameRecord)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun add(gameRecord: GameRecord):Completable {
        return Completable.fromAction {
            gameRecordDao.insert(gameRecord)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private var INSTANCE : GameRecordDataSourceImpl?=null
        fun getInstance(gameRecordDao: GameRecordDao) = synchronized(GameRecordDataSourceImpl::class){
            INSTANCE?:GameRecordDataSourceImpl(gameRecordDao).also { INSTANCE = it }
        }
    }
}