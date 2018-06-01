package com.uu.mahjong_analyse.data

import com.uu.mahjong_analyse.R.string.richi
import com.uu.mahjong_analyse.data.entity.GameDetailDO

/**
 * 持有唯一对局信息
 * @author:xzj
 * @date: 2018/5/23 16:33
 */
object GameModle {
    private var INSTANCE: GameDetailDO? = null
    fun getInstance(): GameDetailDO {
        return  INSTANCE ?: GameDetailDO().also { INSTANCE = it }
    }

    /**
     * 进程被杀等情况，需要从数据库恢复数据时，set进来
     */
    fun set(GameDetailDO: GameDetailDO) {
        INSTANCE = GameDetailDO
    }

    /**
     * 开启一个新的半庄的时候，需要初始化
     */
    fun init() {
        INSTANCE = GameDetailDO()
    }

    /**
     * 设置完点数之后，清除掉上一局的临时数据
     */
    fun clearJuData(){
        INSTANCE?.run {
            hePoint=0
            heName=null
            isTsumo=null
            chong=null
            gong=0
            richi=0
            tingPai=0
        }
    }
}