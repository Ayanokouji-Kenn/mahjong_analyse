package com.uu.mahjong_analyse.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uu.mahjong_analyse.Utils.Constant;
import com.uu.mahjong_analyse.base.MyApplication;
import com.uu.mahjong_analyse.bean.GameRecord;
import com.uu.mahjong_analyse.bean.PlayerRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nagisa on 2016/6/25.
 */
public class DBDao {
    public static SQLiteDatabase getDataBase() {
        MyOpenHelper myOpenHelper = new MyOpenHelper(MyApplication.instance);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        return db;
    }

    public static void insertGame(ContentValues cv) {
        SQLiteDatabase db = getDataBase();
        db.insert(Constant.TABLE_GAME_RECORD, null, cv);
        db.close();
    }

    public static void insertPlayer(String table, String name) {
        SQLiteDatabase db = getDataBase();
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("total_deal",0);
        cv.put("tsumo",0);
        cv.put("ronn",0);
        cv.put("he_count",0);
        cv.put("he_point_sum",0);
        cv.put("he_point_max",0);
        cv.put("richi_count",0);
        cv.put("richi_he",0);
        cv.put("ihhatsu_count",0);
        cv.put("chong_count",0);
        cv.put("chong_point_sum",0);
        cv.put("top",0);
        cv.put("second",0);
        cv.put("third",0);
        cv.put("last",0);
        cv.put("fei",0);
        cv.put("score_sum",0);
        cv.put("no_manguan",0);
        cv.put("manguan",0);
        cv.put("tiaoman",0);
        cv.put("beiman",0);
        cv.put("sanbeiman",0);
        cv.put("yakuman",0);
        db.insert(table, null, cv);
        db.close();
    }

    public String name;                     //名字
    public int total_games;                 //总局数
    public int total_deal;                  //发牌数
    public int tsumo;                       //自摸
    public int ronn;                        //荣和
    public int he_count;                    //总和了数
    public int he_point_sum;                //和了点数之和，用于算平均和了点数
    public int he_point_max;                //最大和了点数
    public int richi_count;                 //立直次数，用来计算立直率
    public int richi_he;                    //立直和了的次数  用来计算立直和了率
    public int ihhatsu_count;               //一发的次数 用来计算一发率
    public int chong_count;                 //放铳的次数
    public int chong_point_sum;             //放铳点数之和  用来计算平均放铳点数
    public int top;                         //一位次数
    public int second;                      //二位次数
    public int third;                       //三位次数
    public int last;                        //四位次数      场均顺位由以上四者平均得
    public int fei;                         //被飞次数   注意 被飞不一定是四位
    public int score_sum;                   //场均马点
    public int no_manguan;                  //满贯以下
    public int manguan;                     //满贯
    public int tiaoman;                     //跳满
    public int beiman;                      //倍满
    public int sanbeiman;                   //三倍满
    public int yakuman;                     //役满*/

    public static PlayerRecord selectPlayer(String name) {

        SQLiteDatabase db = getDataBase();
        String sql = "select * from " + Constant.TABLE_PLAYER_RECORD + " where name = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{name});

        if(cursor.moveToNext()) {
            PlayerRecord pr = new PlayerRecord();
            pr.name = cursor.getString(cursor.getColumnIndex("name"));
            pr.total_games = cursor.getInt(cursor.getColumnIndex("total_games"));
            pr.total_deal = cursor.getInt(cursor.getColumnIndex("total_deal"));
            pr.tsumo = cursor.getInt(cursor.getColumnIndex("tsumo"));
            pr.ronn = cursor.getInt(cursor.getColumnIndex("ronn"));
            pr.he_count = cursor.getInt(cursor.getColumnIndex("he_count"));
            pr.he_point_sum = cursor.getInt(cursor.getColumnIndex("he_point_sum"));
            pr.he_point_max = cursor.getInt(cursor.getColumnIndex("he_point_max"));
            pr.richi_count = cursor.getInt(cursor.getColumnIndex("richi_count"));
            pr.richi_he = cursor.getInt(cursor.getColumnIndex("richi_he"));
            pr.ihhatsu_count = cursor.getInt(cursor.getColumnIndex("ihhatsu_count"));
            pr.chong_count = cursor.getInt(cursor.getColumnIndex("chong_count"));
            pr.chong_point_sum = cursor.getInt(cursor.getColumnIndex("chong_point_sum"));
            pr.top = cursor.getInt(cursor.getColumnIndex("top"));
            pr.second = cursor.getInt(cursor.getColumnIndex("second"));
            pr.third = cursor.getInt(cursor.getColumnIndex("third"));
            pr.last = cursor.getInt(cursor.getColumnIndex("last"));
            pr.fei = cursor.getInt(cursor.getColumnIndex("fei"));
            pr.score_sum = cursor.getInt(cursor.getColumnIndex("score_sum"));
            pr.no_manguan = cursor.getInt(cursor.getColumnIndex("no_manguan"));
            pr.manguan = cursor.getInt(cursor.getColumnIndex("manguan"));
            pr.tiaoman = cursor.getInt(cursor.getColumnIndex("tiaoman"));
            pr.beiman = cursor.getInt(cursor.getColumnIndex("beiman"));
            pr.sanbeiman = cursor.getInt(cursor.getColumnIndex("sanbeiman"));
            pr.yakuman = cursor.getInt(cursor.getColumnIndex("yakuman"));
            cursor.close();
            db.close();
            return  pr;
        }
        cursor.close();
        db.close();
        return null;
    }

    public static List<String> getPlayers() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getDataBase();
        String[] selectionArgs = null;
        Cursor cursor = db.rawQuery("SELECT * FROM player_record", selectionArgs);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(name);
        }
        return list;

    }


    public static boolean updatePlayerData(String name,ContentValues cv) {
        SQLiteDatabase dataBase = getDataBase();
        int update = dataBase.update(Constant.TABLE_PLAYER_RECORD, cv, "name = ?", new String[]{name});
        dataBase.close();
        return update != -1;
    }


    public static List<GameRecord> getGameRecord() {
        List<GameRecord> list = new ArrayList<>();
        SQLiteDatabase db = getDataBase();
        Cursor cursor = db.query(Constant.TABLE_GAME_RECORD, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            GameRecord gr = new GameRecord();
            gr.date = cursor.getString(cursor.getColumnIndex("date"));
            gr.top = cursor.getString(cursor.getColumnIndex("top"));
            gr.second = cursor.getString(cursor.getColumnIndex("second"));
            gr.third = cursor.getString(cursor.getColumnIndex("third"));
            gr.last = cursor.getString(cursor.getColumnIndex("last"));
            list.add(gr);
        }
        cursor.close();
        db.close();
        return list;
    }


}
