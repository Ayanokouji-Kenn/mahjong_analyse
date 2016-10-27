package com.uu.mahjong_analyse.bean;

/**
 * Created by Nagisa on 2016/6/25.
 */
public class PlayerRecord {
    public String name;                     //名字
    public Integer total_games;                 //总局数
    public Integer total_deal;                  //发牌数
    public Integer tsumo;                       //自摸
    public Integer ronn;                        //荣和
    public Integer he_count;                    //总和了数
    public Integer he_point_sum;                //和了点数之和，用于算平均和了点数
    public Integer he_point_max;                //最大和了点数
    public Integer richi_count;                 //立直次数，用来计算立直率
    public Integer richi_he;                    //立直和了的次数  用来计算立直和了率
    public Integer ihhatsu_count;               //一发的次数 用来计算一发率
    public Integer chong_count;                 //放铳的次数
    public Integer chong_point_sum;             //放铳点数之和  用来计算平均放铳点数
    public Integer top;                         //一位次数
    public Integer second;                      //二位次数
    public Integer third;                       //三位次数
    public Integer last;                        //四位次数      场均顺位由以上四者平均得
    public Integer fei;                         //被飞次数   注意 被飞不一定是四位
    public Integer score_sum;                   //马点总和   除以局数就是平均马点
    public Integer no_manguan;                  //满贯以下
    public Integer manguan;                     //满贯
    public Integer tiaoman;                     //跳满
    public Integer beiman;                      //倍满
    public Integer sanbeiman;                   //三倍满
    public Integer yakuman;                     //役满*/

    public int score;  //临时存储每一场的马点数   不存往数据库
}
