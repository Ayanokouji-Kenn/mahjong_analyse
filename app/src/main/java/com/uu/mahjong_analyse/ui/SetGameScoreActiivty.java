//package com.uu.mahjong_analyse.ui;
//
//import android.content.ContentValues;
//import android.content.DialogInterface;
//import android.databinding.DataBindingUtil;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.text.format.DateFormat;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.uu.mahjong_analyse.R;
//import com.uu.mahjong_analyse.base.BaseActivity;
//import com.uu.mahjong_analyse.bean.PlayerRecord;
//import com.uu.mahjong_analyse.databinding.ActivitySetGameScoreBinding;
//import com.uu.mahjong_analyse.db.DBDao;
//import com.uu.mahjong_analyse.utils.CommonApi;
//import com.uu.mahjong_analyse.utils.Constant;
//import com.uu.mahjong_analyse.utils.SPUtils;
//
//import java.util.Comparator;
//import java.util.Iterator;
//import java.util.TreeSet;
//
///**
// * @auther Nagisa.
// * @date 2016/7/2.
// * 设置每一局的和牌点数
// */
//public class SetGameScoreActiivty extends BaseActivity implements View.OnClickListener {
//
//
//
//    private int topBonus = 20;
//    private PlayerRecord mNorthPlayer;
//    private PlayerRecord mSouthPlayer;
//    private PlayerRecord mWestPlayer;
//    private PlayerRecord mEastPlayer;
//    private String[] names = new String[4];
//    private int mSum;
//    private ActivitySetGameScoreBinding mBinding;
//
//    @Override
//    public void initData() {
//        mEastPlayer = DBDao.selectPlayer( SPUtils.getString(Constant .EAST,""));
//        mWestPlayer = DBDao.selectPlayer( SPUtils.getString(Constant.WEST,""));
//        mSouthPlayer = DBDao.selectPlayer(SPUtils.getString(Constant.SOUTH,""));
//        mNorthPlayer = DBDao.selectPlayer(SPUtils.getString(Constant.NORTH,""));
//
//
//        names[0] = SPUtils.getString(Constant.EAST,"");
//        names[1] = SPUtils.getString(Constant.WEST,"");
//        names[2] = SPUtils.getString(Constant.SOUTH,"");
//        names[3] = SPUtils.getString(Constant.NORTH,"");
//
//        mBinding.etEast.setText(String.valueOf(SPUtils.getInt(names[0],25000)));
//        mBinding.etWest.setText(String.valueOf(SPUtils.getInt(names[1],25000)));
//        mBinding.etSouth.setText(String.valueOf(SPUtils.getInt(names[2],25000)));
//        mBinding.etNorth.setText(String.valueOf(SPUtils.getInt(names[3],25000)));
//    }
//
//    @Override
//    public void initView() {
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_game_score);
//
//        CommonApi.setToolbar(this, (Toolbar) findViewById(R.id.toolbar), "设置全局得点");
//    }
//
//    @Override
//    public void initEvent() {
//        mBinding.btnSave.setOnClickListener(this);
//    }
//
//
////    @OnClick(R.id.btn_save)
//    public void onClick(View view) {
//        String east = mBinding.etEast.getText().toString().trim();
//        String west = mBinding.etWest.getText().toString().trim();
//        String north = mBinding.etNorth.getText().toString().trim();
//        String south = mBinding.etSouth.getText().toString().trim();
//
//        if(TextUtils.isEmpty(east) || TextUtils.isEmpty(west) || TextUtils.isEmpty(south) || TextUtils.isEmpty(north)) {
//            Toast.makeText(this, "4个人都填了再保存啊喂！", Toast.LENGTH_SHORT).show();
//        } else {
////            ",date text" +       //日期
////                    ",top text" +        //名字 + 得点
////                    ",second text" +        //名字 + 得点
////                    ",third text" +        //名字 + 得点
////                    ",last text" +
//            Float east_d = (Float.parseFloat(east) - 30000) / 1000;
//            Float west_d = (Float.parseFloat(west) - 30000) / 1000;
//            Float south_d = (Float.parseFloat(south) - 30000) / 1000;
//            Float north_d = (Float.parseFloat(north) - 30000) / 1000;
//
//
//            int[] arr = new int[4];
//            arr[0] = Math.round(east_d);
//            arr[1] = Math.round(west_d);
//            arr[2] = Math.round(south_d);
//            arr[3] = Math.round(north_d);
//
//            mSum = 0;
//            for(int i = 0; i < 4; i++) {
//                mSum += arr[i];
//            }
//
//
//            mEastPlayer.score = Math.round(east_d);
//            mWestPlayer.score = Math.round(west_d);
//            mSouthPlayer.score = Math.round(south_d);
//            mNorthPlayer.score = Math.round(north_d);
//
//
//
//            TreeSet<PlayerRecord> ts = new TreeSet<>(new Comparator<PlayerRecord>() {
//                @Override
//                public int compare(PlayerRecord lhs, PlayerRecord rhs) {
//                    return lhs.score-rhs.score==0?1:lhs.score-rhs.score;
//                }
//            });
//            ts.add(mEastPlayer);
//            ts.add(mWestPlayer);
//            ts.add(mSouthPlayer);
//            ts.add(mNorthPlayer);
//
//            Iterator<PlayerRecord> iterator = ts.iterator();
//
//            ContentValues cv = new ContentValues();
//
//            View dialog_view = View.inflate(this, R.layout.game_score, null);
//            TextView tvTop = (TextView) dialog_view.findViewById(R.id.tv_top);
//            TextView tvSecond = (TextView) dialog_view.findViewById(R.id.tv_second);
//            TextView tvThird = (TextView) dialog_view.findViewById(R.id.tv_third);
//            TextView tvLast = (TextView) dialog_view.findViewById(R.id.tv_last);
//
//            saveData(names, iterator, cv,"last", tvLast);
//            saveData(names, iterator, cv,"third", tvThird);
//            saveData(names, iterator, cv,"second", tvSecond);
//            saveData(names, iterator, cv,"top", tvTop);
//
//            cv.put("date", (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()));
//            DBDao.insertGame(cv);
//            Toast.makeText(this, "全局得点保存成功", Toast.LENGTH_SHORT).show();
//
//            new AlertDialog.Builder(this).setView(dialog_view).setTitle("战绩").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    finish();
//                }
//            }).show();
//        }
//
//    }
//
//    private void setTopScore(int max, int sum,PlayerRecord pr) {
//        if(pr.score == max) {
//            pr.score -= sum; //sum一般情况下都是-20，如果有误差，那么抹平
//        }
//    }
//
//    private void saveData(String[] names, Iterator<PlayerRecord> iterator, ContentValues cv,String shunwei, TextView tv) {
//        if (iterator.hasNext()) {
//            PlayerRecord player = iterator.next();
//            for(String name : names) {
//                if(TextUtils.equals(name, player.name)) {
//
//                    ContentValues cv_player = new ContentValues();
//                    switch (shunwei) {
//                        case "last":
//                            cv_player.put(shunwei,player.last+1);
//                            break;
//                        case "third":
//                            cv_player.put(shunwei,player.third+1);
//                            break;
//                        case "second":
//                            cv_player.put(shunwei,player.second+1);
//                            break;
//                        case "top":
//                            cv_player.put(shunwei,player.top+1);
//                            break;
//                    }
//
//                    if(TextUtils.equals(shunwei, "top")) {
//                        player.score -= mSum;       //sum一般情况下应该是-20，这里就是加上了头名赏，如果有误差则抹平了
//                    }
//                    String result = player.name + ": " + player.score;
//                    cv.put(shunwei,result);
//                    cv_player.put("total_games", player.total_games + 1);
//                    cv_player.put("score_sum", player.score_sum + player.score );
//                    tv.setText(result);
//                    DBDao.updatePlayerData(player.name, cv_player);
//                }
//            }
//        }
//    }
//
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//}
