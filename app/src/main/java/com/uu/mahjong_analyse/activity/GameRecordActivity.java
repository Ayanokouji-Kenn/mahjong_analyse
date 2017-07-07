package com.uu.mahjong_analyse.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.CommonApi;
import com.uu.mahjong_analyse.adapter.GameRecordAdapter;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.bean.GameRecord;
import com.uu.mahjong_analyse.db.DBDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @auther Nagisa.
 * @date 2016/7/20.
 */
public class GameRecordActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.listview)
    ListView mListview;
    private List<GameRecord> mGameRecord;
    private GameRecordAdapter mGameRecordAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_gamerecord);
        ButterKnife.bind(this);
        CommonApi.setToolbar(this, mToolbar,"战斗记录");
    }

    @Override
    public void initData() {
        mGameRecord = DBDao.getGameRecord();
        mGameRecordAdapter = new GameRecordAdapter(this, mGameRecord);
        mListview.setAdapter(mGameRecordAdapter);
        mListview.setDivider(null);
    }

    @Override
    public void initEvent() {

    }


}
