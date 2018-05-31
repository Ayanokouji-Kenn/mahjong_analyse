package com.uu.mahjong_analyse.ui;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.base.MyViewPagerAdapter;
import com.uu.mahjong_analyse.databinding.ActivityModifydbBinding;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.fragment.ModifyDBFfragment;
import com.uu.mahjong_analyse.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther xuzijian
 * @date 2017/6/7 11:11.
 */

public class ModifyDbActivity extends BaseActivity {

    private ActivityModifydbBinding mBinding;

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_modifydb);
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
            bundle.putString(Constant.Table.INSTANCE.getTABLE_NAME(),cursor.getString(0));
            fragment.setArguments(bundle);
            tableFragments.add(fragment);
        }
        cursor.close();
        db.close();
        mBinding.viewpager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),tableFragments));
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
    }

    @Override
    public void initEvent() {

    }

}
