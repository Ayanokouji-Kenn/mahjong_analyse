package com.uu.mahjong_analyse.ui

import android.app.Application
import com.uu.mahjong_analyse.base.BaseVM
import com.uu.mahjong_analyse.data.entity.GameRecord
import com.uu.mahjong_analyse.utils.SingleLiveEvent

class GameRecordListVM(app:Application):BaseVM(app) {
    val gameRecordList = SingleLiveEvent<List<GameRecord>>()
    fun getGameRecordList(){
        gameRecordRepository.getGameRecordList()
                .subscribe({
                    gameRecordList.value = it
                },{})
    }
}