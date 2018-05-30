package com.uu.mahjong_analyse.ui

import android.app.Application
import com.uu.mahjong_analyse.base.BaseVM
import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.data.entity.Player

class SetScoreVM(app:Application):BaseVM(app) {
    val players = GameModle.getInstance().run { listOf(eastPlayer,southPlayer,westPlayer,northPlayer) }
}