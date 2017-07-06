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

import com.iflytek.cloud.ui.RecognizerDialog;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.Constant;
import com.uu.mahjong_analyse.Utils.SPUtils;
import com.uu.mahjong_analyse.Utils.ToastUtils;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.bean.PlayerRecord;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.view.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    @BindView(R.id.tv_voice)
    TextView mTvVoice;
    private List<String> mPlayers;
    private String mChongPlayer;
    private String mPlayer;
    private String mFan;
    private int mPoint_int;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
    private RecognizerDialog mDialog;
    private String oyaName;
    private int gong;

    @Override
    public void initData() {
        Intent intent = getIntent();
        mPlayer = intent.getStringExtra("player");
        oyaName = intent.getStringExtra("oya");
        gong = intent.getIntExtra("gong", 0);
        mPlayers = new ArrayList<>();
        mPlayers.add(SPUtils.getString(Constant.EAST, ""));
        mPlayers.add(SPUtils.getString(Constant.WEST, ""));
        mPlayers.add(SPUtils.getString(Constant.NORTH, ""));
        mPlayers.add(SPUtils.getString(Constant.SOUTH, ""));
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
//        initVoiceDialog();
    }
//
//    private void initVoiceDialog() {
//        //1.创建 RecognizerDialog 对象
//        mDialog = new RecognizerDialog(this, new InitListener() {
//            @Override
//            public void onInit(int i) {
//                if (i != ErrorCode.SUCCESS) {
//                    Toast.makeText(mContext, "code=" + i, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        //若要将 RecognizerDialog 用于语义理解，必须添加以下参数设置，设置之后 onResult 回调返回将是语义理解的结果
//        // mDialog.setParameter("asr_sch", "1");
//        // mDialog.setParameter("nlp_version", "3.0");
//
//        //3.设置回调接口
//        mDialog.setListener(mRecoListener);
//        mDialog.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
//
//        //使用网站上传的语法文件时，只明确指定 SUBJECT，不用指定语法ID；使用在应用上传的则相反。
////        mDialog.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
////        mDialog.setParameter(SpeechConstant.SUBJECT, "asr");
//    }

    @OnClick({R.id.tv_chong, R.id.btn_confirm, R.id.tv_fan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_chong:
                if (mRbRonn.isChecked()) {
                    showChongPlayerDialog();
                }
                break;
            case R.id.tv_fan:
                showFanDialog();

                break;
            case R.id.btn_confirm:
                if (checkData()) {
                    saveData();
                }
                break;
        }
    }

    private boolean checkData() {
        if (mRbRonn.isChecked() && TextUtils.isEmpty(mChongPlayer)) {
            ToastUtils.show(mContext, "没有选择放铳人");
            return false;
        } else if (TextUtils.isEmpty(mFan)) {
            ToastUtils.show(mContext, "没有选择番种");
            return false;
        } else if (mEtPoint.getText().length() == 0) {
            ToastUtils.show(mContext, "没有输入点数");
            return false;
        }
        return true;
    }

    public static final int RC_RECORD_AUDIO = 1;

//    @OnLongClick(R.id.btn_confirm)
//    public boolean speech(View view) {
//        String[] permissions = {Manifest.permission.RECORD_AUDIO};
//        if (EasyPermissions.hasPermissions(this, Manifest.permission_group.MICROPHONE)) {
//            startSpeech();
//        } else {
//            EasyPermissions.requestPermissions(this, "麦克风权限", RC_RECORD_AUDIO, permissions);
//        }
//        return true;
//    }
//
//    @AfterPermissionGranted(RC_RECORD_AUDIO)
//    private void startSpeech() {
//        mIatResults.clear();
//        //开始收集听写信息
//        FlowerCollector.onEvent(this, "iat_recognize");
//        mTvVoice.setText(null);
//        mDialog.show();
//    }


    String[] fan = {"满贯以下", "满贯", "跳满", "倍满", "三倍满", "役满"};

    private void showFanDialog() {
        final WheelViewDialog<String> dialog = new WheelViewDialog<>(this);
        dialog.setTitle("选择番数").setItems(fan).setButtonText("确定").setDialogStyle(Color
                .parseColor("#fc97a9")).setCount(5).show();
        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String s) {
                setFan(s);
            }
        });
    }

    private void setFan(String s) {
        mFan = s;
        mTvFan.setText("番数：" + mFan);
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
        if (mCbRichi.isChecked()) {  //立直
            cv.put("richi_count", playerRecord.richi_count + 1);
        }
        if (mRbTsumo.isChecked()) {  //自摸
            cv.put("tsumo", playerRecord.tsumo + 1);
            if (mCbRichi.isChecked()) {  //立直和了
                cv.put("richi_he", playerRecord.richi_he + 1);
            }
            cv.put("he_count", playerRecord.he_count + 1);
        }
        if (mRbRonn.isChecked()) {   //荣和
            cv.put("ronn", playerRecord.ronn + 1);
            if (mCbRichi.isChecked()) {  //立直和了
                cv.put("richi_he", playerRecord.richi_he + 1);
            }
            cv.put("he_count", playerRecord.he_count + 1);
        }
        if (mCbIhhatsu.isChecked()) {    //一发
            cv.put("ihhatsu_count", playerRecord.ihhatsu_count + 1);
        }

        if (mFan != null) {
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
        if (!TextUtils.isEmpty(point)) {
            mPoint_int = Integer.parseInt(point);
//            和牌最大值要加上供托的
            if (playerRecord.he_point_max < mPoint_int+gong) {
                cv.put("he_point_max", mPoint_int+gong);
            }

            cv.put("he_point_sum", playerRecord.he_point_sum + mPoint_int+gong);
        }

        DBDao.updatePlayerData(mPlayer, cv);


        if (!TextUtils.isEmpty(mChongPlayer)) {
            //存储放铳人数据
            PlayerRecord chongPlayer = DBDao.selectPlayer(mChongPlayer);
            ContentValues cv_chong = new ContentValues();
            cv_chong.put("chong_count", chongPlayer.chong_count + 1);
            cv_chong.put("chong_point_sum", chongPlayer.chong_point_sum + mPoint_int);
            DBDao.updatePlayerData(mChongPlayer, cv_chong);
        }

        //当有人自摸或者荣和的话，所有人的发牌次数都要+1，否则只是存储richi之类的数据，不增长发牌数

        if (mRbTsumo.isChecked() || mRbRonn.isChecked()) {
            for (String player : mPlayers) {
                PlayerRecord playerRecord1 = DBDao.selectPlayer(player);
                ContentValues cv1 = new ContentValues();
                cv1.put("total_deal", playerRecord1.total_deal + 1);
                DBDao.updatePlayerData(player, cv1);
            }
        }

        //改变点数
        //和牌人点数
        SPUtils.putInt(mPlayer, SPUtils.getInt(mPlayer, Integer.MIN_VALUE) + mPoint_int+gong);
        //如果放铳的话，放铳人点数变化
        if (mRbRonn.isChecked() && !TextUtils.isEmpty(mChongPlayer)) {
            SPUtils.putInt(mChongPlayer, SPUtils.getInt(mChongPlayer, Integer.MIN_VALUE) - mPoint_int);
        } else {
            //亲家自摸，那么其他三家平分点数
            if (TextUtils.equals(oyaName, mPlayer)) {
                for (String player : mPlayers) {
                    if (!TextUtils.equals(player, mPlayer)) {
                        SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - (mPoint_int / 3));
                    }
                }
            } else {
                //子家自摸，亲家付二分之一，另2家各付四分之一。
                // 这里有点小问题，比如30符1番，荣和是1300，自摸1500是700/400，并不是按照公式的750/350---已解决
                for (String player : mPlayers) {
                    if (!TextUtils.equals(player, mPlayer)) {
                        switch (mPoint_int) {
                            case 1100:  //30符1翻
                                setZiJiaTsumo(player,500,300);
                                break;
                            case 1500:  //40符1翻  20符2翻
                                setZiJiaTsumo(player,700,400);
                                break;
                            case 2700:  //80符1翻  40符2翻  20符3翻
                                setZiJiaTsumo(player,1300,700);
                                break;
                            case 3100:  //90符1翻
                                setZiJiaTsumo(player,1500,800);
                                break;
                            case 4700:  //70符2翻
                                setZiJiaTsumo(player,2300,1200);
                                break;
                            case 5900:  //90符2翻
                                setZiJiaTsumo(player,2900,1500);
                                break;
                            default:
                                setZiJiaTsumo(player,mPoint_int / 2,mPoint_int / 4);
                                break;
                        }
                    }
                }
            }
        }
        Toast.makeText(this, "该场数据保存成功", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void setZiJiaTsumo(String player,int oyaPoint,int ziPoint) {
        if (TextUtils.equals(oyaName, player)) {
            SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - oyaPoint);
        } else {
            SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - ziPoint);
        }
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

//    private RecognizerDialogListener mRecoListener = new RecognizerDialogListener() {
//
//        @Override
//        public void onResult(RecognizerResult recognizerResult, boolean b) {
//            Log.d("uu", "onResult: " + recognizerResult.getResultString());
//            handleVoiceResult(recognizerResult);
//        }
//
//        @Override
//        public void onError(SpeechError speechError) {
//            Log.d("uu", "onError: " + speechError.getErrorDescription());
//        }
//    };
//
//    private void handleVoiceResult(RecognizerResult results) {
//        String text = JsonParser.parseIatResult(results.getResultString());
//
//        String sn = null;
//        // 读取json结果中的sn字段
//        try {
//            JSONObject resultJson = new JSONObject(results.getResultString());
//            sn = resultJson.optString("sn");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mIatResults.put(sn, text);
//        StringBuilder resultBuffer = new StringBuilder();
//        for (String key : mIatResults.keySet()) {
//            resultBuffer.append(mIatResults.get(key));
//        }
//        String result = resultBuffer.toString();
//        mTvVoice.setText(result);
//        if (result.contains(getString(R.string.richi))) {
//            mCbRichi.setChecked(true);
//        } else {
//            mCbRichi.setChecked(false);
//        }
//        if (result.contains(getString(R.string.ihhatsu))) {
//            mCbIhhatsu.setChecked(true);
//        } else {
//            mCbIhhatsu.setChecked(false);
//        }
//        if (result.contains(getString(R.string.tsumo))) {
//            mRbTsumo.setChecked(true);
//            mTvChong.setText("放铳人：");
//            setFanByVoice(result, false);
//        } else {
//            mRbRonn.setChecked(true);
//            setFanByVoice(result, true);
//        }
//        Matcher matcher = Pattern.compile("(\\d)+").matcher(result);
//        if (matcher.find()) {
//            mEtPoint.setText(matcher.group());
//        }
//
//    }
//
//    private void setFanByVoice(String result, boolean isChong) {
//
//        if (result.contains(Constant.MANN_GANN)) {
//            handleFanByVoice(result, Constant.MANN_GANN, isChong);
//        } else if (result.contains(Constant.HANE_MANN)) {
//            handleFanByVoice(result, Constant.HANE_MANN, isChong);
//        } else if (result.contains(Constant.BAI_MANN)) {
//            handleFanByVoice(result, Constant.BAI_MANN, isChong);
//        } else if (result.contains(Constant.SANN_BAI_MANN)) {
//            handleFanByVoice(result, Constant.SANN_BAI_MANN, isChong);
//        } else if (result.contains(Constant.YAKUMAN)) {
//            handleFanByVoice(result, Constant.YAKUMAN, isChong);
//        }
//    }
//
//    private void handleFanByVoice(String result, String fan, boolean isChong) {
//        setFan(fan);
//        if (isChong) {
//            int index = result.indexOf(fan);
//            String name = result.substring(0, index);
//            if (TextUtils.equals(name, "圈圈叉")) {
//                name = "OOX";
//            } else if (TextUtils.equals(name, "大舅")) {
//                name = "大⑨";
//            }
//            if (mPlayers.contains(name)) {
//                mTvChong.setText(name);
//            } else {
//                Toast.makeText(this, "在座玩家未发现该名字", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        // 开放统计 移动数据统计分析
//        FlowerCollector.onResume(this);
//        FlowerCollector.onPageStart(TAG);
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        // 开放统计 移动数据统计分析
//        FlowerCollector.onPageEnd(TAG);
//        FlowerCollector.onPause(this);
//        super.onPause();
//    }
}
