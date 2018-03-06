package com.uu.mahjong_analyse.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */

@Entity(tableName = "game_record")
data class GameRecord @JvmOverloads constructor(
        @PrimaryKey  var id:Int=0,
        var date:String="",
        var top:String="",
        var second:String="",
        var third:String="",
        var last:String=""
){
}