package com.uu.mahjong_analyse.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.CommonApi;
import com.uu.mahjong_analyse.Utils.MyApplication;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.view.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nagisa on 2016/6/24.
 */
public class AddNewGameActivity extends BaseActivity {

    @BindView(R.id.tv_east)
    TextView mTvEast;
    @BindView(R.id.tv_south)
    TextView mTvSouth;
    @BindView(R.id.tv_west)
    TextView mTvWest;
    @BindView(R.id.tv_north)
    TextView mTvNorth;
    @BindView(R.id.text_input_layout)
    TextInputLayout mTextInputLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_ok)
    TextView mTvOk;
    private List<String> datas;
//    String[] names = {"大⑨", "OOX", "司令", "SM", "怪蜀黍"};
    String[] select = new String[4];


    @Override
    public void initData() {
        datas = DBDao.getPlayers();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_new_game);
        ButterKnife.bind(this);
        mTextInputLayout.setHint("输入新来小伙伴名字");
        CommonApi.setToolbar(this, mToolbar,"设置该局玩家");
    }

    @Override
    public void initEvent() {

    }

    public void showDialog(final View view) {
        final WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("wheelview dialog")
                .setItems(datas)
                .setButtonText("确定")
                .setDialogStyle(getResources().getColor(R.color.colorPrimary))
                .setCount(5)
                .show();
        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, Object s) {
                String mSelectName = (String) s;
                switch (view.getId()) {
                    case R.id.tv_east:
                        mTvEast.setText("东家：" + mSelectName);
                        select[0] = mSelectName;
                        MyApplication.param.put("east", mSelectName);
                        break;
                    case R.id.tv_west:
                        mTvWest.setText("西家：" + mSelectName);
                        select[1] = mSelectName;
                        MyApplication.param.put("west", mSelectName);
                        break;
                    case R.id.tv_south:
                        mTvSouth.setText("南家：" + mSelectName);
                        select[2] = mSelectName;
                        MyApplication.param.put("south", mSelectName);
                        break;
                    case R.id.tv_north:
                        mTvNorth.setText("北家：" + mSelectName);
                        select[3] = mSelectName;
                        MyApplication.param.put("north", mSelectName);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.tv_east, R.id.tv_south, R.id.tv_west, R.id.tv_north})
    public void onClick(View view) {

        if(datas.size() != 0) {
            showDialog(view);
        }else {
            Toast.makeText(this, "请先添加小伙伴", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.fab)
    public void onClick() {
        EditText et = mTextInputLayout.getEditText();
        Editable text = et.getText();
        String trim = text.toString().trim();

        if(!TextUtils.isEmpty(trim)) {
            datas.add(trim);
            Toast.makeText(this, "小伙伴加入了", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "小伙伴是空名？", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_ok)
    public void ok(View view) {
        Log.d(getClass().getSimpleName(), "ok: ============点击事件");
        for(int i = 0; i < select.length; i++) {
            if(TextUtils.isEmpty(select[i])) {
                Toast.makeText(this, "你在逗我么，人选齐了？", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent data = new Intent();
        data.putExtra("players", select);
        setResult(RESULT_OK, data);
        finish();
    }



}
