package com.uu.mahjong_analyse.data.local

import com.uu.mahjong_analyse.data.entity.GameRecord
import io.reactivex.Completable

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */


class GameRecordReposity(val gameRecordDataSourceImpl: GameRecordDataSourceImpl): GameRecordDataSource {
    override fun update(gameRecord: GameRecord): Completable {
        return gameRecordDataSourceImpl.update(gameRecord)
    }

    override fun add(gameRecord: GameRecord): Completable {
        return gameRecordDataSourceImpl.add(gameRecord)
    }

    override fun getGameRecordList() =gameRecordDataSourceImpl.getGameRecordList()


    companion object {
        private var INSTANCE : GameRecordReposity?=null
        fun getInstance(gameRecordDataSourceImpl: GameRecordDataSourceImpl) = synchronized(GameRecordReposity::class){
            INSTANCE
                    ?: GameRecordReposity(gameRecordDataSourceImpl).also { INSTANCE = it }
        }
    }
}