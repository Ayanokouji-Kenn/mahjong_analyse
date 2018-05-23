package com.uu.mahjong_analyse.data

import com.uu.mahjong_analyse.data.entity.TempGameDO

/**
 * 持有唯一对局信息
 * @author:xzj
 * @date: 2018/5/23 16:33
 */
object GameModle {
    private var INSTANCE: TempGameDO? = null
    fun getInstance(): TempGameDO {
        return INSTANCE ?: TempGameDO().also { INSTANCE = it }
    }

    /**
     * 进程被杀等情况，需要从数据库恢复数据时，set进来
     */
    fun set(tempGameDO: TempGameDO) {
        INSTANCE = tempGameDO
    }

    fun init() {
        INSTANCE = TempGameDO()
    }
}