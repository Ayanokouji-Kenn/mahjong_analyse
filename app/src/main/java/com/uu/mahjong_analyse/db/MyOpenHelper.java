package com.uu.mahjong_analyse.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by Nagisa on 2016/6/25.
 */
public class MyOpenHelper extends SQLiteOpenHelper {
    static String db_name = "mahjong_data.db";
    public MyOpenHelper(Context context) {
        super(context, db_name, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String player_record = "CREATE TABLE  player_record(_id integer primary key autoincrement" +
                ",name char(30)" +               //名字
                ",total_games integer" +         //总局数
                ",total_deal integer" +          //发牌数
                ",tsumo integer" +               //自摸
                ",ronn integer" +                //荣和
                ",he_count integer" +            //总和了数
                ",he_point_sum integer" +        //和了点数之和，用于算平均和了点数
                ",he_point_max integer" +        //最大和了点数
                ",richi_count integer" +         //立直次数，用来计算立直率
                ",richi_he integer" +            //立直和了的次数  用来计算立直和了率
                ",ihhatsu_count integer" +       //一发的次数 用来计算一发率
                ",chong_count integer" +         //放铳的次数
                ",chong_point_sum integer" +     //放铳点数之和  用来计算平均放铳点数
                ",top integer" +                 //一位次数
                ",second integer" +              //二位次数
                ",third integer" +               //三位次数
                ",last integer" +                //四位次数      场均顺位由以上四者平均得
                ",fei integer" +                 //被飞次数       被飞不一定是四位
                ",score_sum integer" +           //场均马点
                ",no_manguan integer" +          //满贯以下
                ",manguan integer" +             //满贯
                ",tiaoman integer" +             //跳满
                ",beiman integer" +              //倍满
                ",sanbeiman integer" +           //三倍满
                ",yakuman integer" +             //役满
                ");";
        String game_record = "CREATE TABLE  game_record(_id integer primary key autoincrement" +
                ",date text" +       //日期
                ",top text" +        //名字 + 得点
                ",second text" +        //名字 + 得点
                ",third text" +        //名字 + 得点
                ",last text" +        //名字 + 得点
                ");";


        db.execSQL(player_record);
        db.execSQL(game_record);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
