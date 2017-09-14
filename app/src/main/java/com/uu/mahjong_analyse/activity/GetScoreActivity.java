package com.uu.mahjong_analyse.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.bean.PlayerRecord;
import com.uu.mahjong_analyse.databinding.ActivityGetscoreBinding;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.utils.Constant;
import com.uu.mahjong_analyse.utils.SPUtils;
import com.uu.mahjong_analyse.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Nagisa on 2016/7/1.
 */
public class GetScoreActivity extends BaseActivity {

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
    private int benchang;
    private static ArrayMap<String, Integer> qinRonnMap = new ArrayMap<>();
    private static ArrayMap<String, Integer> qinTsumoMap = new ArrayMap<>();
    private static ArrayMap<String, Integer> ziRonnMap = new ArrayMap<>();
    private static ArrayMap<String, Integer> ziTsumoMap = new ArrayMap<>();
    private static List<String> mFanList = new ArrayList<>();
    private static List<List<String>> mFuList = new ArrayList<>();  //满贯以下 满贯  跳满..
    private static List<List<List<String>>> mFanlist2 = new ArrayList<>();  //第三列数据，1翻 2翻 3翻 4翻

    static {
        initFanData();
        initQinRonnMap();
        initQinTsumoMap();
        initZiTsumoMap();
        initZiRonnMap();
    }

    private boolean isOya;
    private ActivityGetscoreBinding mBinding;


    @Override
    public void initData() {

        Intent intent = getIntent();
        mPlayer = intent.getStringExtra("player");
        oyaName = intent.getStringExtra("oya");
        gong = intent.getIntExtra("gong", 0);
        benchang = intent.getIntExtra("benchang", 0);
        isOya = TextUtils.equals(mPlayer, oyaName);
        mPlayers = new ArrayList<>();
        mPlayers.add(SPUtils.getString(Constant.EAST, ""));
        mPlayers.add(SPUtils.getString(Constant.WEST, ""));
        mPlayers.add(SPUtils.getString(Constant.NORTH, ""));
        mPlayers.add(SPUtils.getString(Constant.SOUTH, ""));
    }

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_getscore);
        mBinding.setListener(mListener);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle("当局数据");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void initEvent() {
        mBinding.rgHepai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_tsumo) {
                    mBinding.tvChong.setVisibility(View.GONE);
                    mChongPlayer = "";
                } else {
                    mBinding.tvChong.setVisibility(View.VISIBLE);
                }
            }
        });
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

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_chong:
                    if (!mBinding.rbRonn.isChecked()) {
                        mBinding.rbRonn.setChecked(true);
                    }
                    showChongPlayerDialog();
                    break;
                case R.id.tv_fan:
                    if (mBinding.rbRonn.isChecked() || mBinding.rbTsumo.isChecked()) {
                        showFanDialog();
                    } else {
                        ToastUtils.show(mContext, "请选点击自摸或荣和");
                    }
                    break;
                case R.id.btn_confirm:
                    if (checkData()) {
                        saveData();
                    }
                    break;
            }
        }
    };

    private boolean checkData() {
        if (mBinding.rbRonn.isChecked() && TextUtils.isEmpty(mChongPlayer)) {
            ToastUtils.show(mContext, "没有选择放铳人");
            return false;
        } else if (TextUtils.isEmpty(mFan)) {
            ToastUtils.show(mContext, "没有选择番种");
            return false;
        } else if (mBinding.etPoint.getText().length() == 0) {
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


    private void showFanDialog() {

        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String key;
                //满贯以下
                if (options1 == 0) {
                    key = mFuList.get(options1).get(options2) + mFanlist2.get(options1).get(options2).get(options3); //这里取出来的应该是  110符1翻 这种字符串
                } else {
                    key = mFanList.get(options1);
                }
                if (TextUtils.equals("25符2翻", key) && mBinding.rbTsumo.isChecked()) {
                    ToastUtils.show(mContext, "2翻的七对子不可能自摸哦~");
                    return;
                }
                if (isOya) {
                    if (mBinding.rbTsumo.isChecked()) {
                        mPoint_int = qinTsumoMap.get(key);
                    } else if (mBinding.rbRonn.isChecked()) {
                        mPoint_int = qinRonnMap.get(key);
                    }
                } else {
                    if (mBinding.rbTsumo.isChecked()) {

                        mPoint_int = ziTsumoMap.get(key);
                    } else if (mBinding.rbRonn.isChecked()) {
                        mPoint_int = ziRonnMap.get(key);
                    }
                }

                setFan(key);
                mBinding.etPoint.setText(String.valueOf(mPoint_int));
            }
        })
                .setSubmitColor(getResources().getColor(R.color.colorAccent))
                .setCancelColor(getResources().getColor(R.color.colorAccent))
                .build();
        optionsPickerView.setPicker(mFanList, mFuList, mFanlist2);
        optionsPickerView.show();

//
//        final WheelViewDialog<String> dialog = new WheelViewDialog<>(this);
//        dialog.setTitle("选择番数").setItems(fan).setButtonText("确定").setDialogStyle(Color
//                .parseColor("#fc97a9")).setCount(5).show();
//        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener<String>() {
//            @Override
//            public void onItemClick(int position, String s) {
//                setFan(s);
//            }
//        });

    }


    private void setFan(String s) {
        mFan = s;
        mBinding.tvFan.setText("番数：" + mFan);
    }

    private void showChongPlayerDialog() {
        OptionsPickerView dialog = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mChongPlayer = mPlayers.get(options1);
                mBinding.tvChong.setText("放铳人：" + mChongPlayer);
            }
        })
                .setTitleText("选择放铳人")
                .setSubmitColor(getResources().getColor(R.color.colorAccent))
                .setCancelColor(getResources().getColor(R.color.colorAccent))
                .build();
        dialog.setPicker(mPlayers);
        dialog.show();
//
//        final WheelViewDialog dialog = new WheelViewDialog(this);
//        dialog.setTitle("选择放铳人").setItems(mPlayers).setButtonText("确定").setDialogStyle(Color
//                .parseColor("#fc97a9")).setCount(5).show();
//        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
//            @Override
//            public void onItemClick(int position, Object s) {
//                mChongPlayer = (String) s;
//                mTvChong.setText("放铳人：" + mChongPlayer);
//            }
//        });
    }

    private void saveData() {
        PlayerRecord playerRecord = DBDao.selectPlayer(mPlayer);
        ContentValues cv = new ContentValues();
        if (mBinding.cbRichi.isChecked()) {  //立直
            cv.put("richi_count", playerRecord.richi_count + 1);
        }
        if (mBinding.rbTsumo.isChecked()) {  //自摸
            cv.put("tsumo", playerRecord.tsumo + 1);
            if (mBinding.cbRichi.isChecked()) {  //立直和了
                cv.put("richi_he", playerRecord.richi_he + 1);
            }
            cv.put("he_count", playerRecord.he_count + 1);
        }
        if (mBinding.rbRonn.isChecked()) {   //荣和
            cv.put("ronn", playerRecord.ronn + 1);
            if (mBinding.cbRichi.isChecked()) {  //立直和了
                cv.put("richi_he", playerRecord.richi_he + 1);
            }
            cv.put("he_count", playerRecord.he_count + 1);
        }
        if (mBinding.cbIhhatsu.isChecked()) {    //一发
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

        String point = mBinding.etPoint.getText().toString().trim();
        if (!TextUtils.isEmpty(point)) {
            mPoint_int = Integer.parseInt(point);
//            和牌最大值要加上供托的
            if (playerRecord.he_point_max < mPoint_int + gong + benchang * 300) {
                cv.put("he_point_max", mPoint_int + gong + benchang * 300);
            }

            cv.put("he_point_sum", playerRecord.he_point_sum + mPoint_int + gong + benchang * 300);
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

        //当有人自摸或者荣和的话，所有人的发牌次数都要+1

        if (mBinding.rbTsumo.isChecked() || mBinding.rbRonn.isChecked()) {
            for (String player : mPlayers) {
                PlayerRecord playerRecord1 = DBDao.selectPlayer(player);
                ContentValues cv1 = new ContentValues();
                cv1.put("total_deal", playerRecord1.total_deal + 1);
                DBDao.updatePlayerData(player, cv1);
            }
        }

        //改变点数
        //和牌人点数
        SPUtils.putInt(mPlayer, SPUtils.getInt(mPlayer, Integer.MIN_VALUE) + mPoint_int + gong + benchang * 300);
        //如果放铳的话，放铳人点数变化
        if (mBinding.rbRonn.isChecked() && !TextUtils.isEmpty(mChongPlayer)) {
            SPUtils.putInt(mChongPlayer, SPUtils.getInt(mChongPlayer, Integer.MIN_VALUE) - mPoint_int - benchang * 300);
        } else {
            //亲家自摸，那么其他三家平分点数
            if (TextUtils.equals(oyaName, mPlayer)) {
                for (String player : mPlayers) {
                    if (!TextUtils.equals(player, mPlayer)) {
                        SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - (mPoint_int / 3) - benchang * 100);
                    }
                }
            } else {
                //子家自摸，亲家付二分之一，另2家各付四分之一。
                // 这里有点小问题，比如40符1番，荣和是1300，自摸1500是700/400，并不是按照公式的750/350---已解决
                for (String player : mPlayers) {
                    if (!TextUtils.equals(player, mPlayer)) {
                        switch (mPoint_int) {
                            case 1100:  //30符1翻
                                setZiJiaTsumo(player, 500, 300);
                                break;
                            case 1500:  //40符1翻  20符2翻
                                setZiJiaTsumo(player, 700, 400);
                                break;
                            case 2700:  //80符1翻  40符2翻  20符3翻
                                setZiJiaTsumo(player, 1300, 700);
                                break;
                            case 3100:  //90符1翻
                                setZiJiaTsumo(player, 1500, 800);
                                break;
                            case 4700:  //70符2翻
                                setZiJiaTsumo(player, 2300, 1200);
                                break;
                            case 5900:  //90符2翻
                                setZiJiaTsumo(player, 2900, 1500);
                                break;
                            default:
                                setZiJiaTsumo(player, mPoint_int / 2, mPoint_int / 4);
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

    private void setZiJiaTsumo(String player, int oyaPoint, int ziPoint) {
        if (TextUtils.equals(oyaName, player)) {
            SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - oyaPoint - benchang * 100);
        } else {
            SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - ziPoint - benchang * 100);
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

    private static void initZiRonnMap() {
        ziRonnMap.put("30符1翻", 1000);
        ziRonnMap.put("40符1翻", 1300);
        ziRonnMap.put("50符1翻", 1600);
        ziRonnMap.put("60符1翻", 2000);
        ziRonnMap.put("70符1翻", 2300);
        ziRonnMap.put("80符1翻", 2600);
        ziRonnMap.put("90符1翻", 2900);
        ziRonnMap.put("100符1翻", 3200);
        ziRonnMap.put("110符1翻", 3600);

        ziRonnMap.put("20符2翻", 1300);
        ziRonnMap.put("25符2翻", 1600);
        ziRonnMap.put("30符2翻", 2000);
        ziRonnMap.put("40符2翻", 2600);
        ziRonnMap.put("50符2翻", 3200);
        ziRonnMap.put("60符2翻", 3900);
        ziRonnMap.put("70符2翻", 4500);
        ziRonnMap.put("80符2翻", 5200);
        ziRonnMap.put("90符2翻", 5800);
        ziRonnMap.put("100符2翻", 6400);
        ziRonnMap.put("110符2翻", 7100);

        ziRonnMap.put("20符3翻", 2600);
        ziRonnMap.put("25符3翻", 3200);
        ziRonnMap.put("30符3翻", 3900);
        ziRonnMap.put("40符3翻", 5200);
        ziRonnMap.put("50符3翻", 6400);

        ziRonnMap.put("20符4翻", 5200);
        ziRonnMap.put("25符4翻", 6400);

        ziRonnMap.put("满贯", 8000);
        ziRonnMap.put("跳满", 12000);
        ziRonnMap.put("倍满", 16000);
        ziRonnMap.put("三倍满", 24000);
        ziRonnMap.put("役满", 32000);
    }

    private static void initZiTsumoMap() {
        ziTsumoMap.put("30符1翻", 1100);
        ziTsumoMap.put("40符1翻", 1500);
        ziTsumoMap.put("50符1翻", 1600);
        ziTsumoMap.put("60符1翻", 2000);
        ziTsumoMap.put("70符1翻", 2400);
        ziTsumoMap.put("80符1翻", 2700);
        ziTsumoMap.put("90符1翻", 3100);
        ziTsumoMap.put("100符1翻", 3200);
        ziTsumoMap.put("110符1翻", 3600);

        ziTsumoMap.put("20符2翻", 1500);
        ziTsumoMap.put("30符2翻", 2000);
        ziTsumoMap.put("40符2翻", 2700);
        ziTsumoMap.put("50符2翻", 3200);
        ziTsumoMap.put("60符2翻", 4000);
        ziTsumoMap.put("70符2翻", 4700);
        ziTsumoMap.put("80符2翻", 5200);
        ziTsumoMap.put("90符2翻", 5900);
        ziTsumoMap.put("100符2翻", 6400);
        ziTsumoMap.put("110符2翻", 7200);

        ziTsumoMap.put("20符3翻", 2700);
        ziTsumoMap.put("25符3翻", 3200);
        ziTsumoMap.put("30符3翻", 4000);
        ziTsumoMap.put("40符3翻", 5200);
        ziTsumoMap.put("50符3翻", 6400);

        ziTsumoMap.put("20符4翻", 5200);
        ziTsumoMap.put("25符4翻", 6400);

        ziTsumoMap.put("满贯", 8000);
        ziTsumoMap.put("跳满", 12000);
        ziTsumoMap.put("倍满", 160);
        ziTsumoMap.put("三倍满", 24000);
        ziTsumoMap.put("役满", 32000);
    }

    private static void initQinTsumoMap() {
        qinTsumoMap.put("30符1翻", 1500);
        qinTsumoMap.put("40符1翻", 2100);
        qinTsumoMap.put("50符1翻", 2400);
        qinTsumoMap.put("60符1翻", 3000);
        qinTsumoMap.put("70符1翻", 3600);
        qinTsumoMap.put("80符1翻", 3900);
        qinTsumoMap.put("90符1翻", 4500);
        qinTsumoMap.put("100符1翻", 4800);
        qinTsumoMap.put("110符1翻", 5400);

        qinTsumoMap.put("20符2翻", 2100);
        qinTsumoMap.put("30符2翻", 3000);
        qinTsumoMap.put("40符2翻", 3900);
        qinTsumoMap.put("50符2翻", 4800);
        qinTsumoMap.put("60符2翻", 6000);
        qinTsumoMap.put("70符2翻", 6900);
        qinTsumoMap.put("80符2翻", 7800);
        qinTsumoMap.put("90符2翻", 8700);
        qinTsumoMap.put("100符2翻", 9600);
        qinTsumoMap.put("110符2翻", 10800);

        qinTsumoMap.put("20符3翻", 3900);
        qinTsumoMap.put("25符3翻", 6000);
        qinTsumoMap.put("30符3翻", 5800);
        qinTsumoMap.put("40符3翻", 7800);
        qinTsumoMap.put("50符3翻", 9600);

        qinTsumoMap.put("20符4翻", 7800);
        qinTsumoMap.put("25符4翻", 9600);

        qinTsumoMap.put("满贯", 12000);
        qinTsumoMap.put("跳满", 18000);
        qinTsumoMap.put("倍满", 24000);
        qinTsumoMap.put("三倍满", 36000);
        qinTsumoMap.put("役满", 48000);
    }

    private static void initQinRonnMap() {
        qinRonnMap.put("30符1翻", 1500);
        qinRonnMap.put("40符1翻", 2000);
        qinRonnMap.put("50符1翻", 2400);
        qinRonnMap.put("60符1翻", 2900);
        qinRonnMap.put("70符1翻", 3400);
        qinRonnMap.put("80符1翻", 3900);
        qinRonnMap.put("90符1翻", 4400);
        qinRonnMap.put("100符1翻", 4800);
        qinRonnMap.put("110符1翻", 5300);

        qinRonnMap.put("20符2翻", 2000);
        qinRonnMap.put("25符2翻", 2400);
        qinRonnMap.put("30符2翻", 2900);
        qinRonnMap.put("40符2翻", 3900);
        qinRonnMap.put("50符2翻", 4800);
        qinRonnMap.put("60符2翻", 5800);
        qinRonnMap.put("70符2翻", 6800);
        qinRonnMap.put("80符2翻", 7700);
        qinRonnMap.put("90符2翻", 8700);
        qinRonnMap.put("100符2翻", 9600);
        qinRonnMap.put("110符2翻", 10600);

        qinRonnMap.put("20符3翻", 3900);
        qinRonnMap.put("25符3翻", 4800);
        qinRonnMap.put("30符3翻", 5800);
        qinRonnMap.put("40符3翻", 7700);
        qinRonnMap.put("50符3翻", 9600);

        qinRonnMap.put("20符4翻", 7700);
        qinRonnMap.put("25符4翻", 9600);

        qinRonnMap.put("满贯", 12000);
        qinRonnMap.put("跳满", 18000);
        qinRonnMap.put("倍满", 24000);
        qinRonnMap.put("三倍满", 36000);
        qinRonnMap.put("役满", 48000);
    }

    private static void initFanData() {
        //第一列显示番数
        mFanList.add("满贯以下");
        mFanList.add("满贯");
        mFanList.add("跳满");
        mFanList.add("倍满");
        mFanList.add("三倍满");
        mFanList.add("役满");
        //第二列显示符，只有满贯以下才需要符，所以添加5个空集合
        List<String> fuList = new ArrayList<>();
        fuList.add("20符");
        fuList.add("25符");
        fuList.add("30符");
        fuList.add("40符");
        fuList.add("50符");
        fuList.add("60符");
        fuList.add("70符");
        fuList.add("80符");
        fuList.add("90符");
        fuList.add("100符");
        fuList.add("110符");
        ArrayList<String> emptyList = new ArrayList<>();
        emptyList.add("");
        mFuList.add(fuList);
        mFuList.add(emptyList);
        mFuList.add(emptyList);
        mFuList.add(emptyList);
        mFuList.add(emptyList);
        mFuList.add(emptyList);
        //第三列显示满贯以下的哪一种
        ArrayList<String> fu20_25fan = new ArrayList<>();
        ArrayList<String> fu30_40_50fan = new ArrayList<>();
        ArrayList<String> fu60_110fan = new ArrayList<>();
        List<List<String>> fan2 = new ArrayList<>();
        List<List<String>> fan2Empty = new ArrayList<>();
        fan2Empty.add(emptyList);

        fu20_25fan.add("2翻");
        fu20_25fan.add("3翻");
        fu20_25fan.add("4翻");

        fu30_40_50fan.add("1翻");
        fu30_40_50fan.add("2翻");
        fu30_40_50fan.add("3翻");

        fu60_110fan.add("1翻");
        fu60_110fan.add("2翻");

        fan2.add(fu20_25fan);   //20符和25符只有2 3 4翻
        fan2.add(fu20_25fan);
        fan2.add(fu30_40_50fan); //30 40 50符只有1 2 3翻
        fan2.add(fu30_40_50fan);
        fan2.add(fu30_40_50fan);
        fan2.add(fu60_110fan);   //60~110符 只有1 2翻
        fan2.add(fu60_110fan);
        fan2.add(fu60_110fan);
        fan2.add(fu60_110fan);
        fan2.add(fu60_110fan);
        fan2.add(fu60_110fan);
        mFanlist2.add(fan2);
        mFanlist2.add(fan2Empty);
        mFanlist2.add(fan2Empty);
        mFanlist2.add(fan2Empty);
        mFanlist2.add(fan2Empty);
        mFanlist2.add(fan2Empty);
    }
}
