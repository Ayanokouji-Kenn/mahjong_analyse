package com.uu.mahjong_analyse.utils

import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.data.entity.Player

object ConvertHelper {
    /**
     * 将场次换算成string
     */
    fun parseChang(n: Int): String {
        val benChang = n % 10
        val chang = n / 10
        val sb = StringBuilder()
        sb.append(when (chang) {
            1 -> "东一"
            2 -> "东二"
            3 -> "东三"
            4 -> "东四"
            5 -> "南一"
            6 -> "南二"
            7 -> "南三"
            8 -> "南四"
            9 -> "西一"   //半庄结束所有人不过3W 会西入
            else -> ""
        })
                .append(
                        when (benChang) {
                            1 -> "一本场"
                            2 -> "二本场"
                            3 -> "三本场"
                            4 -> "四本场"
                            5 -> "五本场"
                            6 -> "六本场"
                            7 -> "七本场"
                            8 -> "八本场"
                            else -> ""
                        })
        return sb.toString()
    }

    fun isOya(name: String): Boolean {
        val seat = getSeat(name)
        return seat == (GameModle.getInstance().chang / 10 % 4 - 1)
    }

    fun getOyaPlayer() :Player?{
        return when(GameModle.getInstance().chang / 10 % 4 - 1){
            EAST -> GameModle.getInstance().eastPlayer
            SOUTH -> GameModle.getInstance().southPlayer
            WEST -> GameModle.getInstance().westPlayer
            NORTH -> GameModle.getInstance().northPlayer
            else -> null
        }
    }

    /**
     * 找不到的话返回-1
     */
    fun getSeat(name: String): Int {
        return when (name) {
            GameModle.getInstance().eastPlayer?.name -> EAST
            GameModle.getInstance().southPlayer?.name -> SOUTH
            GameModle.getInstance().westPlayer?.name -> WEST
            GameModle.getInstance().northPlayer?.name -> NORTH
            else -> -1
        }
    }

    fun getPlayerByName(name: String?): Player? {
        return when (name) {
            GameModle.getInstance().eastPlayer?.name -> GameModle.getInstance().eastPlayer
            GameModle.getInstance().southPlayer?.name -> GameModle.getInstance().southPlayer
            GameModle.getInstance().westPlayer?.name -> GameModle.getInstance().westPlayer
            GameModle.getInstance().northPlayer?.name -> GameModle.getInstance().northPlayer
            else -> null
        }
    }

    fun getBenchang():Int{
        return GameModle.getInstance().chang %10
    }
}