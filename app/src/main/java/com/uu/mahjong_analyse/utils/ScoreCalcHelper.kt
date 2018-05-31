package com.uu.mahjong_analyse.utils

import com.uu.mahjong_analyse.data.GameModle

/**
 * 点数变化时，进行计算的帮助类
 * @author:xzj
 * @date: 2018/5/30 10:14
 */
object ScoreCalcHelper {
    /**
     * @param point 被人自摸的点数
     * @return 返回一个对子，A是庄家应该支付的点数，B是子家应该支付的点数
     */
    fun getTsumoPointPair(point:Int):Pair<Int,Int>{
        return when(point) {
            1100 -> Pair(500,300)
            1500 -> Pair(700,400)
            2700 -> Pair(1300,700)
            3100 -> Pair(1500,800)
            4700 -> Pair(2300,1200)
            5900 -> Pair(2900,1500)
            else -> Pair(point/2,point/4)
        }
    }

    fun handleRichi(){
        val richi = GameModle.getInstance().richi
        val mRichis = booleanArrayOf(
                richi and 0b1000 == 0b1000,
                richi and 0b0100 == 0b0100,
                richi and 0b0010 == 0b0010,
                richi and 0b0001 == 0b0001
        )
        //根据立直情况处理供托
        var richiCount =0
        mRichis.forEach { if(it) richiCount++ }
        GameModle.getInstance().gong += (richiCount*1000)
        when{
            richi and 0b1000 == 0b1000 -> GameModle.getInstance().eastPlayer?.richiCount?.plus(1)
             richi and 0b0100 == 0b0100 -> GameModle.getInstance().southPlayer?.richiCount?.plus(1)
             richi and 0b0010 == 0b0010 -> GameModle.getInstance().westPlayer?.richiCount?.plus(1)
             richi and 0b0001 == 0b0001 -> GameModle.getInstance().northPlayer?.richiCount?.plus(1)
        }
    }
    fun handleLiuju(){
        val tingpai = GameModle.getInstance().tingPai
        val richi = GameModle.getInstance().richi
        val mTingPais = booleanArrayOf(
                tingpai and 0b1000 == 0b1000,
                tingpai and 0b0100 == 0b0100,
                tingpai and 0b0010 == 0b0010,
                tingpai and 0b0001 == 0b0001
        )
        val mRichis = booleanArrayOf(
                richi and 0b1000 == 0b1000,
                richi and 0b0100 == 0b0100,
                richi and 0b0010 == 0b0010,
                richi and 0b0001 == 0b0001
        )
        GameModle.getInstance().tingPai=0
        GameModle.getInstance().richi = 0
        val tingPaiList = mTingPais.filter { it }
//                        不听罚符
        var punishPoint = 0
        var earnPoint = 0
        when (tingPaiList.size) {
            1 -> {
                punishPoint = 1000
                earnPoint = 3000
            }
            2 -> {
                punishPoint = 1500
                earnPoint = 1500
            }
            3 -> {
                punishPoint = 3000
                earnPoint = 1000
            }
        }
        for (i in 0..3) {
            when (i) {
                0 -> {
                    if (mRichis[i]) {
                        GameModle.getInstance().east -= 1000
                        GameModle.getInstance().eastPlayer?.richiCount?.plus(1)
                    }
                    if (mTingPais[i]) {
                        GameModle.getInstance().east += earnPoint
                    } else {
                        GameModle.getInstance().east -= punishPoint
                    }
                    GameModle.getInstance().eastPlayer?.totalDeal?.plus(1)
                }
                1 -> {
                    if (mRichis[i]) {
                        GameModle.getInstance().south -= 1000
                    GameModle.getInstance().southPlayer?.richiCount?.plus(1)
                    }
                    if (mTingPais[i]) {
                        GameModle.getInstance().south += earnPoint
                    } else {
                        GameModle.getInstance().south -= punishPoint
                    }
                    GameModle.getInstance().southPlayer?.totalDeal?.plus(1)
                }
                2 -> {
                    if (mRichis[i]) {
                        GameModle.getInstance().west -= 1000
                        GameModle.getInstance().westPlayer?.richiCount?.plus(1)
                    }
                    if (mTingPais[i]) {
                        GameModle.getInstance().west += earnPoint
                    } else {
                        GameModle.getInstance().west -= punishPoint
                    }
                    GameModle.getInstance().westPlayer?.totalDeal?.plus(1)
                }
                3 -> {
                    if (mRichis[i]) {
                        GameModle.getInstance().north -= 1000
                        GameModle.getInstance().northPlayer?.richiCount?.plus(1)
                    }
                    if (mTingPais[i]) {
                        GameModle.getInstance().north += earnPoint
                    } else {
                        GameModle.getInstance().north -= punishPoint
                    }
                    GameModle.getInstance().northPlayer?.totalDeal?.plus(1)
                }
            }
        }
//        根据立直情况处理供托
        var richiCount =0
        mRichis.forEach { if(it) richiCount++ }
        GameModle.getInstance().gong += (richiCount*1000)

//        根据听牌情况处理是否连庄
        nextChang(mTingPais[(GameModle.getInstance().chang / 10 - 1) % 4])
    }

    /**
     * 根据是否连庄 改变chang的值
     */
    fun nextChang(isLianZhuang:Boolean) {
        if (isLianZhuang) {
//          连庄，个位+1
            GameModle.getInstance().chang += 1
        } else {
//          下庄，十位+1
            GameModle.getInstance().chang += (10 - GameModle.getInstance().chang % 10)
        }
    }
}