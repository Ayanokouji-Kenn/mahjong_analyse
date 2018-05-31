package com.uu.mahjong_analyse.ui

import android.app.Application
import com.uu.mahjong_analyse.base.BaseVM
import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.data.local.TempGameInfoRepository

class SetScoreVM(app:Application):BaseVM(app) {
    val players = GameModle.getInstance().run { listOf(eastPlayer,southPlayer,westPlayer,northPlayer) }
    fun savePlayer(){
        mComposite.add(playerRepository.updatePlayer(GameModle.getInstance().eastPlayer!!).subscribe())
        mComposite.add(playerRepository.updatePlayer(GameModle.getInstance().southPlayer!!).subscribe())
        mComposite.add(playerRepository.updatePlayer(GameModle.getInstance().westPlayer!!).subscribe())
        mComposite.add(playerRepository.updatePlayer(GameModle.getInstance().northPlayer!!).subscribe())
    }

    fun saveJu(){
        mComposite.add(gameInfoRepository.add(GameModle.getInstance()).subscribe())
    }
}