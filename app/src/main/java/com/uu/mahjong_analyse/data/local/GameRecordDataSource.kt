package com.uu.mahjong_analyse.data.local

import com.uu.mahjong_analyse.data.entity.GameRecord

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */

interface GameRecordDataSource {
    interface GetGameRecordListCallBack{
        fun onSuccess(list: List<GameRecord>)
        fun onFailure()
    }
    fun getGameRecordList(getGameRecordListCallBack: GetGameRecordListCallBack)

    fun insertGameRecord(gameRecord: GameRecord)
}