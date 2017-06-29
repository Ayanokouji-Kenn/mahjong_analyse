package com.uu.mahjong_analyse.activity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.CommonApi;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.bean.PlayerRecord;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.view.wheelview.widget.WheelViewDialog;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @auther Nagisa.
 * @date 2016/7/3.
 */
public class PlayerInfoActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_richi)
    TextView mTvRichi;
    @BindView(R.id.tv_ihhatsu)
    TextView mTvIhhatsu;
    @BindView(R.id.tv_helv)
    TextView mTvHelv;
    @BindView(R.id.tv_chonglv)
    TextView mTvChonglv;
    @BindView(R.id.tv_he_avg)
    TextView mTvHeAvg;
    @BindView(R.id.tv_chong_avg)
    TextView mTvChongAvg;
    @BindView(R.id.tv_top)
    TextView mTvTop;
    @BindView(R.id.tv_second)
    TextView mTvSecond;
    @BindView(R.id.tv_third)
    TextView mTvThird;
    @BindView(R.id.tv_last)
    TextView mTvLast;
    @BindView(R.id.tv_shunwei)
    TextView mTvShunwei;
    @BindView(R.id.tv_nomanguan)
    TextView mTvNomanguan;
    @BindView(R.id.tv_manguan)
    TextView mTvManguan;
    @BindView(R.id.tv_tiaoman)
    TextView mTvTiaoman;
    @BindView(R.id.tv_beiman)
    TextView mTvBeiman;
    @BindView(R.id.tv_sanbeiman)
    TextView mTvSanbeiman;
    @BindView(R.id.tv_yikuman)
    TextView mTvYikuman;
    @BindView(R.id.tv_deal_sum)
    TextView mTvDeal;
    @BindView(R.id.tv_score_avg)
    TextView mTvScoreAvg;
    @BindView(R.id.tv_game_sum)
    TextView mTvGames;
    @BindView(R.id.tv_richi_he)
    TextView mTvRichiHeLv;
    @BindView(R.id.tv_select)
    TextView mTvSelect;
    private WheelViewDialog<String> mSelectPlayerDialog;
    private String mPlayer;

    @Override
    public void initView() {
        setContentView(R.layout.activity_playerinfo);
        ButterKnife.bind(this);
        mPlayer = getIntent().getStringExtra("player");
        CommonApi.setToolbar(this, mToolbar, "战绩");

    }

    @Override
    public void initData() {
        if (mPlayer != null) {
            mPlayer = mPlayer.substring(3);
            mTvSelect.setText(mPlayer);
            showData(mPlayer);
        }
    }

    @Override
    public void initEvent() {
    }

    @OnClick(R.id.tv_select)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select:
                if (mSelectPlayerDialog == null) {
                    List<String> players = DBDao.getPlayers();
                    if (players.size() == 0) {
                        Toast.makeText(this, "暂未存储过对局", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSelectPlayerDialog = new WheelViewDialog<>(this);
                    mSelectPlayerDialog.setItems(players)
                            .setCount(5)
                            .setDialogStyle(getResources().getColor(R.color.colorPrimary))
                            .setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener<String>() {
                                @Override
                                public void onItemClick(int position, String s) {
                                    Log.d(getClass().getSimpleName(), "onItemClick: " + s);
                                    mTvSelect.setText(s);
                                    showData(s);
                                }
                            });
                }
                if (mSelectPlayerDialog.isShowing()) {
                    return;
                }
                mSelectPlayerDialog.show();
                Log.d(getClass().getSimpleName(), "onClick: " + mSelectPlayerDialog.getStyle().textSize);

                break;
        }
    }

    private void showData(String playerName) {
        PlayerRecord record = DBDao.selectPlayer(playerName);
        float vii = record.richi_count * 1f / record.total_deal;
        Log.d(getClass().getSimpleName(), "initData: " + vii);
        mTvRichi.setText(String.format(Locale.CHINESE, "立直率：%.2f", vii));
        mTvIhhatsu.setText(String.format(Locale.CHINESE, "一发率：%.2f", record.ihhatsu_count * 1f / record.richi_count));
        mTvHelv.setText(String.format(Locale.CHINESE, "和了率：%.2f", record.he_count * 1f / record.total_deal));
        mTvChonglv.setText(String.format(Locale.CHINESE, "放铳率：%.2f", record.chong_count * 1f / record.total_deal));
        mTvRichiHeLv.setText(String.format(Locale.CHINESE, "立直和了率：%.2f", record.richi_he * 1f / record.total_deal));
        mTvHeAvg.setText(String.format(Locale.CHINESE, "平均和了点：%.2f", record.he_point_sum * 1f / record.he_count));
        mTvChongAvg.setText(String.format(Locale.CHINESE, "平均放铳点：%.2f", record.chong_point_sum * 1f / record.chong_count));
        mTvTop.setText(String.format(Locale.CHINESE, "一位率：%.2f", record.top * 1f / record.total_games));
        mTvSecond.setText(String.format(Locale.CHINESE, "二位率：%.2f", record.second * 1f / record.total_games));
        mTvThird.setText(String.format(Locale.CHINESE, "三位率：%.2f", record.third * 1f / record.total_games));
        mTvLast.setText(String.format(Locale.CHINESE, "四位率：%.2f", record.last * 1f / record.total_games));
        mTvShunwei.setText(String.format(Locale.CHINESE, "平均顺位：%.2f", (record.top + record.second * 2 + record.third * 3 + record.last * 4) * 1f / record
                .total_games));
        mTvScoreAvg.setText(String.format(Locale.CHINESE, "场均得点：%.2f", record.score_sum * 1f / record.total_games));
        mTvNomanguan.setText(String.format(Locale.CHINESE, "满贯未满：%.2f", record.no_manguan * 1f / record.he_count));
        mTvManguan.setText(String.format(Locale.CHINESE, "满贯：%.2f", record.manguan * 1f / record.he_count));
        mTvTiaoman.setText(String.format(Locale.CHINESE, "跳满：%.2f", record.tiaoman * 1f / record.he_count));
        mTvBeiman.setText(String.format(Locale.CHINESE, "倍满：%.2f", record.beiman * 1f / record.he_count));
        mTvSanbeiman.setText(String.format(Locale.CHINESE, "三倍满：%.2f", record.sanbeiman * 1f / record.he_count));
        mTvYikuman.setText(String.format(Locale.CHINESE, "役满：%.2f", record.yakuman * 1f / record.he_count));
        mTvDeal.setText(String.format(Locale.CHINESE, "对战局数：%d", record.total_deal));
        mTvGames.setText(String.format(Locale.CHINESE, "对战场次：%d", record.total_games));
        Log.d(getClass().getSimpleName(), "initData: =====" + record.top + ":" + record.second + ":" + record.third + ":" + record.last);
        Log.d(getClass().getSimpleName(), "initData: =====" + record.score_sum);
    }

}
