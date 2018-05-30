package com.uu.mahjong_analyse.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * 记录对局详细数据，只保留最近一次
 * 记录当前对局的情况，每进行一局，往数据库里刷一次
 * @author:xzj
 * @date: 2018/5/22 10:21
 */
@Entity(tableName = "temp_game")
data class TempGameDO(
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null,
        var east: Int = 25000,
        var south: Int = 25000,
        var west: Int = 25000,
        var north: Int = 25000,
        @ColumnInfo(name = "he_point")
        var hePoint: Int = 0, //和了点数
        @ColumnInfo(name = "he_name")
        var heName:String?=null, //该局和了的人，流局则为空，一炮双响 算作另一条数据
        @ColumnInfo(name = "is_tsumo")
        var isTsumo: Boolean? = null,// true自摸   false铳和  null流局
        var chong :String? = null, //isTsumo为false 这个字段才有值,表示放铳的人
        var chang: Int = 10,   //当前局数，10表示东一，11表示东一，82表示南四二本场，该字段既可以表示当前局数，也可以由本场算出本场棒是多少
        var gong: Int = 0, //供托，流局后存放立直棒
        @Ignore var eastPlayer:Player?=null,
        @Ignore var westPlayer :Player?=null,
        @Ignore var southPlayer:Player?=null,
        @Ignore var northPlayer:Player?=null,
        @Ignore var richi:Int=0,//后四位bit表示东南西北是否立直比如，15=0b1111 就是四家都立直了，下面的听牌同理
        @Ignore var tingPai:Int=0
        ) {
}