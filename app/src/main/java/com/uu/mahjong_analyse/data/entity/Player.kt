package com.uu.mahjong_analyse.data.entity

import android.arch.persistence.room.*
import com.bigkoo.pickerview.model.IPickerViewData

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/06
 *     desc  :
 * </pre>
 */

@Entity(tableName = "player_record", indices = arrayOf(Index("name", unique = true)))
data class Player constructor(
        @PrimaryKey(autoGenerate = true) var id: Int? = null
        , var name: String = ""                   //名字
        , @ColumnInfo(name = "total_games") var totalGames: Int = 0               //总局数
        , @ColumnInfo(name = "total_deal") var totalDeal: Int = 0                  //发牌数
        , var tsumo: Int = 0                       //自摸
        , var ronn: Int = 0                        //荣和
        , @ColumnInfo(name = "he_count") var heCount: Int = 0                    //总和了数
        , @ColumnInfo(name = "he_point_sum") var hePointSum: Long = 0                //和了点数之和，用于算平均和了点数
        , @ColumnInfo(name = "he_point_max") var hePointMax: Int = 0                //最大和了点数
        , @ColumnInfo(name = "richi_count") var richiCount: Int = 0                 //立直次数，用来计算立直率
        , @ColumnInfo(name = "richi_he") var richiHe: Int = 0                    //立直和了的次数  用来计算立直和了率
        , @ColumnInfo(name = "ihhatsu_count") var ihhatsuCount: Int = 0               //一发的次数 用来计算一发率
        , @ColumnInfo(name = "chong_count") var chongCount: Int = 0                 //放铳的次数
        , @ColumnInfo(name = "chong_point_sum") var chongPointSum: Long = 0             //放铳点数之和  用来计算平均放铳点数
        , var top: Int = 0                         //一位次数
        , var second: Int = 0                      //二位次数
        , var third: Int = 0                       //三位次数
        , var last: Int = 0                        //四位次数      场均顺位由以上四者平均得
        , var fei: Int = 0                         //被飞次数   注意 被飞不一定是四位
        , var score_sum: Double = 0.0                 //马点总和   除以局数就是平均马点
        , @ColumnInfo(name = "no_manguan") var noManguan: Int = 0                  //满贯以下
        , var manguan: Int = 0                     //满贯
        , var tiaoman: Int = 0                     //跳满
        , var beiman: Int = 0                      //倍满
        , var sanbeiman: Int = 0                   //三倍满
        , var yakuman: Int = 0                     //役满
        , @Ignore
        var score:Double = 0.0, //临时存储每一场的马点数   不存往数据库
        @Ignore
        var seat: Int = -1

) : IPickerViewData {
    override fun getPickerViewText(): String = name
}