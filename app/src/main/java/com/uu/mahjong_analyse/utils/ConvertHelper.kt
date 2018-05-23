package com.uu.mahjong_analyse.utils

object ConvertHelper {
    /**
     * 将场次换算成string
     */
    fun parseChang(n:Int) :String{
        val benChang = n%10
        val chang = n/10
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
            else -> ""
        })
                .append(
        when(benChang) {
            1 -> "一本场"
            2 -> "二本场"
            3 -> "三本场"
            4 -> "四本场"
            5 -> "五本场"
            6 -> "六本场"
            7 -> "七本场"
            8 -> "八本场"
            else ->""
        })
        return sb.toString()
    }
}