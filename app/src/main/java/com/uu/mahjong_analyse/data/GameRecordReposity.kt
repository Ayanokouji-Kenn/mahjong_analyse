package com.uu.mahjong_analyse.data

import com.uu.mahjong_analyse.data.entity.GameRecord
import com.uu.mahjong_analyse.data.local.GameRecordDataSourceImpl

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */


class GameRecordReposity(val gameRecordDataSourceImpl: GameRecordDataSourceImpl):GameRecordDataSource {
    override fun getGameRecordList(getGameRecordListCallBack: GameRecordDataSource.GetGameRecordListCallBack) {
        gameRecordDataSourceImpl.getGameRecordList(getGameRecordListCallBack)
    }

    override fun insertGameRecord(gameRecord: GameRecord) {
        gameRecordDataSourceImpl.insertGameRecord(gameRecord)
    }

    companion object {
        private var INSTANCE :GameRecordReposity?=null
        fun getInstance(gameRecordDataSourceImpl: GameRecordDataSourceImpl) = synchronized(GameRecordReposity::class){
            INSTANCE?:GameRecordReposity(gameRecordDataSourceImpl).also { INSTANCE = it }
        }
    }
}