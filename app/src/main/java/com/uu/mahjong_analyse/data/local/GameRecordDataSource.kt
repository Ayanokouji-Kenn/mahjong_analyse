package com.uu.mahjong_analyse.data.local

import com.uu.mahjong_analyse.data.entity.GameRecord
import io.reactivex.Completable
import io.reactivex.Flowable

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
    fun getGameRecordList():Flowable<List<GameRecord>>
    fun update(gameRecord: GameRecord):Completable
    fun add(gameRecord: GameRecord):Completable
}