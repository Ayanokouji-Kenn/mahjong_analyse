package com.uu.mahjong_analyse.data

import com.uu.mahjong_analyse.data.entity.GameRecord

object GameRecordModle {
    var INSTANCE:GameRecord?=null
    fun getInstance():GameRecord {
        return INSTANCE?:GameRecord().also { INSTANCE = it }
    }
}