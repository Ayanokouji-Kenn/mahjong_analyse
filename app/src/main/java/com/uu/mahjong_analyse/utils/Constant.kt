package com.uu.mahjong_analyse.utils


/**
 * Created by Nagisa on 2016/6/25.
 */
const val EAST = 0
const val SOUTH = 1
const val WEST = 2
const val NORTH = 3

object Constant {

    val RX_LIUJU_RESULT = "rx_liuju_result"
    val RX_RICHI_RESULT = "rx_richi_result"

    val EAST = "east"
    val SOUTH = "south"
    val WEST = "west"
    val NORTH = "north"
    val CHANG = "chang"
    val GONG_TUO = "gongtuo"

    val MANN_GANN = "满贯"
    val HANE_MANN = "跳满"
    val BAI_MANN = "倍满"
    val SANN_BAI_MANN = "三倍满"
    val YAKUMAN = "役满"

    object Table {
        var TABLE_PLAYER_RECORD = "player_record"
        var TABLE_GAME_RECORD = "game_record"
        val TABLE_NAME = "tableName"
    }


}
