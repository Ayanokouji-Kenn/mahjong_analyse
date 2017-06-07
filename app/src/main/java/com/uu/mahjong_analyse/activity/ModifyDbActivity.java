package com.uu.mahjong_analyse.activity;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.Constant;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.base.MyViewPagerAdapter;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.fragment.ModifyDBFfragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @auther xuzijian
 * @date 2017/6/7 11:11.
 */

public class ModifyDbActivity extends BaseActivity {
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    public void initView() {
        setContentView(R.layout.activity_modifydb);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

        List<Fragment> tableFragments = new ArrayList<>();
        //查出该数据库下所有的表
        SQLiteDatabase db = DBDao.getDataBase();
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while (cursor.moveToNext()) {
            ModifyDBFfragment fragment = new ModifyDBFfragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.Table.TABLE_NAME,cursor.getString(0));
            fragment.setArguments(bundle);
            tableFragments.add(fragment);
        }
        cursor.close();
        db.close();


        viewpager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),tableFragments));
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    public void initEvent() {

    }

}
