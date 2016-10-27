package com.uu.mahjong_analyse.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.MyApplication;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.bean.PlayerRecord;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.view.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nagisa on 2016/7/1.
 */
public class GetScoreActivity extends BaseActivity {


    @BindView(R.id.cb_richi)
    CheckBox mCbRichi;
    @BindView(R.id.cb_ihhatsu)
    CheckBox mCbIhhatsu;
    @BindView(R.id.rb_tsumo)
    RadioButton mRbTsumo;
    @BindView(R.id.rb_ronn)
    RadioButton mRbRonn;
    @BindView(R.id.tv_chong)
    TextView mTvChong;
    @BindView(R.id.et_point)
    TextView mEtPoint;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.tv_fan)
    TextView mTvFan;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private List<String> mPlayers;
    private String mChongPlayer;
    private String mPlayer;
    private String mFan;
    private int mPoint_int;


    @Override
    public void initData() {
        Intent intent = getIntent();
        mPlayer = intent.getStringExtra("player");
        mPlayers = new ArrayList<>();
        mPlayers.add(MyApplication.param.get("east"));
        mPlayers.add(MyApplication.param.get("west"));
        mPlayers.add(MyApplication.param.get("south"));
        mPlayers.add(MyApplication.param.get("north"));
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_getscore);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setTitle("当局数据");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.tv_chong, R.id.btn_confirm, R.id.tv_fan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_chong:
                if(mRbRonn.isChecked()) {
                    showChongPlayerDialog();
                }
                break;
            case R.id.tv_fan:
                showFanDialog();

                break;
            case R.id.btn_confirm:
                saveData();
                break;
        }
    }

    String[] fan = {"满贯以下", "满贯", "跳满", "倍满", "三倍满", "役满"};

    private void showFanDialog() {
        final WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("选择番数").setItems(fan).setButtonText("确定").setDialogStyle(Color
                .parseColor("#fc97a9")).setCount(5).show();
        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, Object s) {
                mFan = (String) s;
                mTvFan.setText("番数：" + mFan);
            }
        });
    }

    private void showChongPlayerDialog() {

        final WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("选择放铳人").setItems(mPlayers).setButtonText("确定").setDialogStyle(Color
                .parseColor("#fc97a9")).setCount(5).show();
        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, Object s) {
                mChongPlayer = (String) s;
                mTvChong.setText("放铳人：" + mChongPlayer);
            }
        });
    }

    private void saveData() {
        PlayerRecord playerRecord = DBDao.selectPlayer(mPlayer);
        ContentValues cv = new ContentValues();
        if(mCbRichi.isChecked()) {  //立直
            cv.put("richi_count", playerRecord.richi_count + 1);
        }
        if(mRbTsumo.isChecked()) {  //自摸
            cv.put("tsumo", playerRecord.tsumo + 1);
            if(mCbRichi.isChecked()) {  //立直和了
                cv.put("richi_he", playerRecord.richi_he + 1);
            }
            cv.put("he_count", playerRecord.he_count + 1);
        }
        if(mRbRonn.isChecked()) {   //荣和
            cv.put("ronn", playerRecord.ronn + 1);
            if(mCbRichi.isChecked()) {  //立直和了
                cv.put("richi_he", playerRecord.richi_he + 1);
            }
            cv.put("he_count", playerRecord.he_count + 1);
        }
        if(mCbIhhatsu.isChecked()) {    //一发
            cv.put("ihhatsu_count", playerRecord.ihhatsu_count + 1);
        }

        if(mFan != null) {
//            no_manguan integer" +          //满贯以下
//            ",manguan integer" +             //满贯
//                    ",tiaoman integer" +             //跳满
//                    ",beiman integer" +              //倍满
//                    ",sanbeiman integer" +           //三倍满
//                    ",yakuman integer" +
            switch (mFan) {
                case "满贯以下":
                    cv.put("no_manguan", playerRecord.no_manguan + 1);
                    break;
                case "满贯":
                    cv.put("manguan", playerRecord.manguan + 1);
                    break;
                case "跳满":
                    cv.put("tiaoman", playerRecord.tiaoman + 1);
                    break;
                case "倍满":
                    cv.put("beiman", playerRecord.beiman + 1);
                    break;
                case "三倍满":
                    cv.put("sanbeiman", playerRecord.sanbeiman + 1);
                    break;

                case "役满":
                    cv.put("yakuman", playerRecord.yakuman + 1);
                    break;
            }
        }

        String point = mEtPoint.getText().toString().trim();
        if(!TextUtils.isEmpty(point)) {
            mPoint_int = Integer.parseInt(point);
            if(playerRecord.he_point_max < mPoint_int) {
                cv.put("he_point_max", mPoint_int);
            }

            cv.put("he_point_sum", playerRecord.he_point_sum + mPoint_int);
        }

        DBDao.updatePlayerData(mPlayer, cv);


        if(!TextUtils.isEmpty(mChongPlayer)) {
            //存储放铳人数据
            PlayerRecord chongPlayer = DBDao.selectPlayer(mChongPlayer);
            ContentValues cv_chong = new ContentValues();
            cv_chong.put("chong_count", chongPlayer.chong_count + 1);
            cv_chong.put("chong_point_sum", chongPlayer.chong_point_sum + mPoint_int);
            DBDao.updatePlayerData(mChongPlayer, cv_chong);
        }

        //当有人自摸或者荣和的话，所有人的发牌次数都要+1，否则只是存储richi之类的数据，不增长发牌数

        if(mRbTsumo.isChecked() || mRbRonn.isChecked()) {
            for(String player : mPlayers) {
                PlayerRecord playerRecord1 = DBDao.selectPlayer(player);
                ContentValues cv1 = new ContentValues();
                cv1.put("total_deal",playerRecord1.total_deal+1);
                DBDao.updatePlayerData(player, cv1);
            }
        }
        Toast.makeText(this, "该场数据保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
