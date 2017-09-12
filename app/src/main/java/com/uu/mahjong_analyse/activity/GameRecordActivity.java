package com.uu.mahjong_analyse.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.Toolbar;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.adapter.GameRecordAdapter;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.bean.GameRecord;
import com.uu.mahjong_analyse.databinding.ActivityGamerecordBinding;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.utils.CommonApi;

import java.util.List;

/**
 * @auther Nagisa.
 * @date 2016/7/20.
 */
public class GameRecordActivity extends BaseActivity {


    private List<GameRecord> mGameRecord;
    private GameRecordAdapter mGameRecordAdapter;
    private ActivityGamerecordBinding mBinding;

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_gamerecord);
        CommonApi.setToolbar(this, (Toolbar) findViewById(R.id.toolbar),"战斗记录");
    }

    @Override
    public void initData() {
        mGameRecord = DBDao.getGameRecord();
        mGameRecordAdapter = new GameRecordAdapter(this, mGameRecord);
        mBinding.listview.setAdapter(mGameRecordAdapter);
        mBinding.listview.setDivider(null);
    }

    @Override
    public void initEvent() {

    }


}
