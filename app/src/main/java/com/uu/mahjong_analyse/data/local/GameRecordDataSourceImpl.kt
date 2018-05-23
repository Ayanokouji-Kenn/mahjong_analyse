package com.uu.mahjong_analyse.data.local

import com.uu.mahjong_analyse.data.entity.GameRecord

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */


class GameRecordDataSourceImpl(val gameRecordDao: GameRecordDao): GameRecordDataSource {
    override fun getGameRecordList(getGameRecordListCallBack: GameRecordDataSource.GetGameRecordListCallBack) {
        val list = gameRecordDao.selectList()
        if (list.isEmpty()) {
            getGameRecordListCallBack.onFailure()
        }else {
            getGameRecordListCallBack.onSuccess(list)
        }
    }

    override fun insertGameRecord(gameRecord: GameRecord) {
        gameRecordDao.insert(gameRecord)
    }

    companion object {
        private var INSTANCE : GameRecordDataSourceImpl?=null
        fun getInstance(gameRecordDao: GameRecordDao) = synchronized(GameRecordDataSourceImpl::class){
            INSTANCE?:GameRecordDataSourceImpl(gameRecordDao).also { INSTANCE = it }
        }
    }
}