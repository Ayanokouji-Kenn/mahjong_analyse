package com.uu.mahjong_analyse.ui

import android.app.Application
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Bundle
import com.uu.mahjong_analyse.base.BaseVM
import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.data.entity.Player

/**
 * @description
 * @auther xuzijian
 * @date 2017/10/24 14:24.
 */

class MainVM(app: Application) : BaseVM(app) {
    var isStart = false
    var hePlayer: String = ""
    var chang = ObservableInt(10)  //默认是东一 十位数表示场，个位数表示本场
    var gong = ObservableInt(0)  //流局产生的供托,有人和牌则清零
    var benchang = ObservableInt(0)
    val players = ObservableArrayList<Player?>().apply { for (i in 1..4) add(null) }  //存放开局东一时候的  东南西北player
    var fgShow = ObservableField<String>()

    /**
     * @see players
     * 选过人之后，回到MainFg了，从数据库中查出这4个人的信息，设置到players里
     */
    fun setPlayers(data: Bundle) {
        val array = data.getParcelableArray("players")
        players[0] = array[0] as Player?
        players[1] = array[1] as Player?
        players[2] = array[2] as Player?
        players[3] = array[3] as Player?
    }

    /**
     * 恢复数据。从数据库中读取临时对局数据，如果有的话
     */
    fun recover() {
        mComposite.add(
                gameInfoRepository.fetchList()
                        .subscribe({
                            //没有数据就不管了，需要用的时候掉GameModle.getInstance会初始化
                            if(it.isNotEmpty())
                            GameModle.set(it.last())
                        }, {
                        }))
    }

    /**
     * 处理流局
     */
    fun liuju() {
        mComposite.add(
                gameInfoRepository.add(GameModle.getInstance())
                        .subscribe()
                )
    }

    /**
     * 清除掉上一句的tempGameDo
     */
    fun clearTempData() {
        GameModle.init()
        gameInfoRepository.clear()
    }
}
