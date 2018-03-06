package com.uu.mahjong_analyse.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.bean.PlayerRecord;
import com.uu.mahjong_analyse.databinding.ActivityPlayerinfoBinding;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.utils.CommonApi;

import java.util.List;
import java.util.Locale;
/**
 * @auther Nagisa.
 * @date 2016/7/3.
 */
public class PlayerInfoActivity extends BaseActivity implements View.OnClickListener {

    private OptionsPickerView mSelectPlayerDialog;
    private String mPlayer;
    private ActivityPlayerinfoBinding mBinding;

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_playerinfo);
        mPlayer = getIntent().getStringExtra("player");
        CommonApi.setToolbar(this, (Toolbar) findViewById(R.id.toolbar), "战绩");

    }

    @Override
    public void initData() {
        if (mPlayer != null) {
            mPlayer = mPlayer.substring(3);
            mBinding.tvSelect.setText(mPlayer);
            showData(mPlayer);
        }
    }

    @Override
    public void initEvent() {
        mBinding.tvSelect.setOnClickListener(this);
    }

//    @OnClick(R.id.tv_select)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select:
                if (mSelectPlayerDialog == null) {
                    final List<String> players = DBDao.getPlayers();
                    if (players.size() == 0) {
                        Toast.makeText(this, "暂未存储过对局", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSelectPlayerDialog = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            mBinding.tvSelect.setText(players.get(options1));
                            showData(players.get(options1));
                        }
                    })
                            .setSubmitColor(getResources().getColor(R.color.colorAccent))
                            .setCancelColor(getResources().getColor(R.color.colorAccent))
                            .build();
                    mSelectPlayerDialog.setPicker(players);


//                    mSelectPlayerDialog = new WheelViewDialog<>(this);
//                    mSelectPlayerDialog.setItems(players)
//                            .setCount(5)
//                            .setDialogStyle(getResources().getColor(R.color.colorPrimary))
//                            .setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener<String>() {
//                                @Override
//                                public void onItemClick(int position, String s) {
//                                    Log.d(getClass().getSimpleName(), "onItemClick: " + s);
//                                    mBinding.tvSelect.setText(s);
//                                    showData(s);
//                                }
//                            });
                }
                if (mSelectPlayerDialog.isShowing()) {
                    return;
                }
                mSelectPlayerDialog.show();
                break;
        }
    }

    private void showData(String playerName) {
        PlayerRecord record = DBDao.selectPlayer(playerName);
        float vii = record.richi_count * 1f / record.total_deal;
        Log.d(getClass().getSimpleName(), "initData: " + vii);
        mBinding.tvRichi.setText(String.format(Locale.CHINESE, "立直率：%.2f", vii));
        mBinding.tvIhhatsu.setText(String.format(Locale.CHINESE, "一发率：%.2f", record.ihhatsu_count * 1f / record.richi_count));
        mBinding.tvHelv.setText(String.format(Locale.CHINESE, "和了率：%.2f", record.he_count * 1f / record.total_deal));
        mBinding.tvChonglv.setText(String.format(Locale.CHINESE, "放铳率：%.2f", record.chong_count * 1f / record.total_deal));
        mBinding.tvHelv.setText(String.format(Locale.CHINESE, "立直和了率：%.2f", record.richi_he * 1f / record.total_deal));
        mBinding.tvHeAvg.setText(String.format(Locale.CHINESE, "平均和了点：%.2f", record.he_point_sum * 1f / record.he_count));
        mBinding.tvChongAvg.setText(String.format(Locale.CHINESE, "平均放铳点：%.2f", record.chong_point_sum * 1f / record.chong_count));
        mBinding.tvTop.setText(String.format(Locale.CHINESE, "一位率：%.2f", record.top * 1f / record.total_games));
        mBinding.tvSecond.setText(String.format(Locale.CHINESE, "二位率：%.2f", record.second * 1f / record.total_games));
        mBinding.tvThird.setText(String.format(Locale.CHINESE, "三位率：%.2f", record.third * 1f / record.total_games));
        mBinding.tvLast.setText(String.format(Locale.CHINESE, "四位率：%.2f", record.last * 1f / record.total_games));
        mBinding.tvShunwei.setText(String.format(Locale.CHINESE, "平均顺位：%.2f", (record.top + record.second * 2 + record.third * 3 + record.last * 4) * 1f / record
         .total_games));
        mBinding.tvScoreAvg.setText(String.format(Locale.CHINESE, "场均得点：%.2f", record.score_sum * 1f / record.total_games));
        mBinding.tvNomanguan.setText(String.format(Locale.CHINESE, "满贯未满：%.2f", record.no_manguan * 1f / record.he_count));
        mBinding.tvManguan.setText(String.format(Locale.CHINESE, "满贯：%.2f", record.manguan * 1f / record.he_count));
        mBinding.tvTiaoman.setText(String.format(Locale.CHINESE, "跳满：%.2f", record.tiaoman * 1f / record.he_count));
        mBinding.tvBeiman.setText(String.format(Locale.CHINESE, "倍满：%.2f", record.beiman * 1f / record.he_count));
        mBinding.tvSanbeiman.setText(String.format(Locale.CHINESE, "三倍满：%.2f", record.sanbeiman * 1f / record.he_count));
        mBinding.tvYikuman.setText(String.format(Locale.CHINESE, "役满：%.2f", record.yakuman * 1f / record.he_count));
        mBinding.tvDealSum.setText(String.format(Locale.CHINESE, "对战局数：%d", record.total_deal));
        mBinding.tvGameSum.setText(String.format(Locale.CHINESE, "对战场次：%d", record.total_games));
        Log.d(getClass().getSimpleName(), "initData: =====" + record.top + ":" + record.second + ":" + record.third + ":" + record.last);
        Log.d(getClass().getSimpleName(), "initData: =====" + record.score_sum);
    }

}
