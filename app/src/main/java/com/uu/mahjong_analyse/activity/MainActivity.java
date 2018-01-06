package com.uu.mahjong_analyse.activity;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.bean.LiujuResult;
import com.uu.mahjong_analyse.databinding.ActivityMainBinding;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.fragment.LeftMenuFragment;
import com.uu.mahjong_analyse.utils.BitmapUtils;
import com.uu.mahjong_analyse.utils.Constant;
import com.uu.mahjong_analyse.utils.MagicFileChooser;
import com.uu.mahjong_analyse.utils.SPUtils;
import com.uu.mahjong_analyse.utils.rx.RxBus;
import com.uu.mahjong_analyse.view.LiuJuDialog;
import com.uu.mahjong_analyse.view.RichiDialog;
import com.uu.mahjong_analyse.vm.MainVM;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import rx.functions.Action1;

public class MainActivity extends BaseActivity {
    private static final int RC_PLAYERS = 1;
    private static final int RC_GET_SCORE = 2;
    private static final int RC_ALBUM = 3;


    private String[] mPlayers;
    private LeftMenuFragment mLeftMenuFragment;
    private Shimmer mShimmer;
    private String hePlayer;
    private ArrayList<ShimmerTextView> textViews;
    private MagicFileChooser magicFileChooser;
    private int chang = 0;  //0-7 代表东1到南4；
    private int gong = 0;  //流局产生的供托,有人和牌则清零
    private int benchang = 0;
    private SparseArray<String> changMap = new SparseArray<>();
    private HashMap<String, TextView> playerTvMap = new HashMap<>();
    private ActivityMainBinding mBinding;
    private Toolbar toolbar;
    private MainVM vm;
    private AlertDialog feedbackDialog;

    @Override
    public void initData() {
        vm = ViewModelProviders.of(this).get(MainVM.class);
        rotatePlayerName();
        mShimmer = new Shimmer();
        changMap.append(0, getString(R.string.east1));
        changMap.append(1, getString(R.string.east2));
        changMap.append(2, getString(R.string.east3));
        changMap.append(3, getString(R.string.east4));
        changMap.append(4, getString(R.string.south1));
        changMap.append(5, getString(R.string.south2));
        changMap.append(6, getString(R.string.south3));
        changMap.append(7, getString(R.string.south4));
        textViews = new ArrayList<>();
        textViews.add(mBinding.tvEast);
        textViews.add(mBinding.tvSouth);
        textViews.add(mBinding.tvWest);
        textViews.add(mBinding.tvNorth);

    }

    private void rotatePlayerName() {
        ObjectAnimator.ofFloat(mBinding.llSouth, "rotation", 0F, -90F).start();
        ObjectAnimator.ofFloat(mBinding.llWest, "rotation", 0F, 180F).start();
        ObjectAnimator.ofFloat(mBinding.llNorth, "rotation", 0F, 90F).start();
    }

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setListener(mListener);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
        toolbar.setPopupTheme(R.style.PopupMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerlayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mBinding.drawerlayout.addDrawerListener(mDrawerToggle);

        mLeftMenuFragment = new LeftMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_leftmenu, mLeftMenuFragment)
                .commitAllowingStateLoss();
    }


    public void initEvent() {
        //和牌后先谈选择立直的框，回调在这里
        RxBus.getInstance().toObservable(ArrayList.class, Constant.RX_RICHI_RESULT)
                .compose(this.<ArrayList>bindToLifecycle())
                .subscribe(new Action1<ArrayList>() {
                    @Override
                    public void call(ArrayList arrayList) {
                        handleRichi(arrayList);
                        Intent intent = new Intent(mContext, GetScoreActivity.class);
                        intent.putExtra("player", hePlayer);
                        intent.putExtra("oya", getOyaName());
                        intent.putExtra("gong", gong);
                        intent.putExtra("benchang", benchang);
                        if (isStart) {
                            openPage(true, RC_GET_SCORE, intent);
                        }
                    }
                });
        //流局逻辑全在这里
        RxBus.getInstance().toObservable(LiujuResult.class, Constant.RX_LIUJU_RESULT)
                .compose(this.<LiujuResult>bindToLifecycle())
                .subscribe(new Action1<LiujuResult>() {
                    @Override
                    public void call(LiujuResult liujuResult) {
                        //处理流局立直的数据
                        handleRichi(liujuResult.richiPlayers);
                        //处理流局听牌的数据
                        //如果庄家没听牌，那么进行下一场,否则本场+1
                        if (!liujuResult.tingpaiPlayers.contains(getOyaName())) {
                            nextChang();
                        } else {
                            mBinding.tvChang.setText(new SpanUtils().append(changMap.get(chang)).append(++benchang + "本场").setFontSize(14, true).create());
                        }
                        Set<String> players = playerTvMap.keySet();
                        switch (liujuResult.tingpaiPlayers.size()) {
                            case 1:
                                for (String player : players) {
                                    TextView tv = playerTvMap.get(player);
                                    if (TextUtils.equals(player, liujuResult.tingpaiPlayers.get(0))) {
                                        int score = SPUtils.getInt(player, Integer.MIN_VALUE);
                                        tv.setText(String.valueOf(score + 3000));
                                        SPUtils.putInt(player, score + 3000);
                                    } else {
                                        int score = SPUtils.getInt(player, Integer.MIN_VALUE);
                                        tv.setText(String.valueOf(score - 1000));
                                        SPUtils.putInt(player, score - 1000);
                                    }
                                }

                                break;
                            case 2:
                                for (String player : players) {
                                    TextView tv = playerTvMap.get(player);
                                    if (liujuResult.tingpaiPlayers.contains(player)) {
                                        int score = SPUtils.getInt(player, Integer.MIN_VALUE);
                                        tv.setText(String.valueOf(score + 1500));
                                        SPUtils.putInt(player, score + 1500);
                                    } else {
                                        int score = SPUtils.getInt(player, Integer.MIN_VALUE);
                                        tv.setText(String.valueOf(score - 1500));
                                        SPUtils.putInt(player, score - 1500);
                                    }
                                }
                                break;
                            case 3:
                                for (String player : players) {
                                    TextView tv = playerTvMap.get(player);
                                    if (!liujuResult.tingpaiPlayers.contains(player)) {
                                        int score = SPUtils.getInt(player, Integer.MIN_VALUE);
                                        tv.setText(String.valueOf(score - 3000));
                                        SPUtils.putInt(player, score - 3000);
                                    } else {
                                        int score = SPUtils.getInt(player, Integer.MIN_VALUE);
                                        tv.setText(String.valueOf(score + 1000));
                                        SPUtils.putInt(player, score + 1000);
                                    }
                                }
                                break;
                            default:   //剩下的是0和4的情况，即所有人都听 或所有人都不听，那么不处理
                                break;
                        }
                    }
                });

    }

    /**
     * 遍历立直玩家集合，取出当前分数，扣掉1000，存回sp，供托+1000
     */
    private void handleRichi(ArrayList<String> richiPlayers) {
        for (String richiPlayer : richiPlayers) {
            TextView tv = playerTvMap.get(richiPlayer);
            int score = SPUtils.getInt(richiPlayer, Integer.MIN_VALUE);
            tv.setText(String.valueOf(score - 1000));
            SPUtils.putInt(richiPlayer, score - 1000);
            gong += 1000;
        }
        mBinding.tvGong.setText(gong == 0 ? "" : String.valueOf(gong));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PLAYERS && resultCode == RESULT_OK && data != null) {
            mPlayers = (String[]) data.getCharSequenceArrayExtra("players");
            //把对局的四个人名字传过来，如果数据库中没有，就为他们建表,并将所有值变成0；
            for (int i = 0; i < mPlayers.length; i++) {
                if (DBDao.selectPlayer(mPlayers[i]) == null) {
                    DBDao.insertPlayer(Constant.Table.TABLE_PLAYER_RECORD, mPlayers[i]);
                }
            }
            mBinding.tvEast.setText(mPlayers[0]);
            mBinding.tvSouth.setText(mPlayers[1]);
            mBinding.tvWest.setText(mPlayers[2]);
            mBinding.tvNorth.setText(mPlayers[3]);
            playerTvMap.put(mPlayers[0], mBinding.tvEastPoint);
            playerTvMap.put(mPlayers[1], mBinding.tvSouthPoint);
            playerTvMap.put(mPlayers[2], mBinding.tvWestPoint);
            playerTvMap.put(mPlayers[3], mBinding.tvNorthPoint);

            mLeftMenuFragment.mLeftmenuDatas.clear();
            mLeftMenuFragment.mLeftmenuDatas.add("东家：" + SPUtils.getString(Constant.EAST, ""));
            mLeftMenuFragment.mLeftmenuDatas.add("西家：" + SPUtils.getString(Constant.WEST, ""));
            mLeftMenuFragment.mLeftmenuDatas.add("南家：" + SPUtils.getString(Constant.SOUTH, ""));
            mLeftMenuFragment.mLeftmenuDatas.add("北家：" + SPUtils.getString(Constant.NORTH, ""));
            mLeftMenuFragment.mLeftMenuAdapter.notifyDataSetChanged();

            startGame();

            Snackbar.make(toolbar, "谁和牌就点击谁的名字设置点数即可", Snackbar.LENGTH_INDEFINITE)
                    .setAction("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        } else if (requestCode == RC_GET_SCORE && resultCode == RESULT_OK) {
            //设置完分数，不是庄家则chang+1
            if (!TextUtils.equals(hePlayer, getOyaName())) {
                //改变场风和庄家闪光效果
                nextChang();
            } else {
                mBinding.tvChang.setText(new SpanUtils().append(changMap.get(chang)).append(++benchang + "本场").setFontSize(14, true).create());
            }
            //供托要清零
            gong = 0;
            mBinding.tvGong.setText(null);
            //改变四家分数
            for (String player : mPlayers) {
                playerTvMap.get(player).setText(String.valueOf(SPUtils.getInt(player, Integer.MIN_VALUE)));
            }
        } else if (requestCode == MagicFileChooser.ACTIVITY_FILE_CHOOSER) {
            if (resultCode == RESULT_OK) {
                if (magicFileChooser.onActivityResult(requestCode, resultCode, data)) {
                    File[] files = magicFileChooser.getChosenFiles();
                    if (files.length != 0) {
                        Bitmap bitmap = BitmapUtils.compressBitmap(mContext, files[0].getAbsolutePath(), ScreenUtils.getScreenWidth(), ScreenUtils
                                .getScreenHeight());
                        findViewById(R.id.rl_table).setBackground(new BitmapDrawable(getResources(), bitmap));
                    }
                }
            } else {
                findViewById(R.id.rl_table).setBackground(null);
            }

//            Uri selectedImage = data.getData();
//            if (selectedImage != null) {
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                // 获取选择照片的数据视图
//                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                // 从数据视图中获取已选择图片的路径
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String picturePath = cursor.getString(columnIndex);
//                cursor.close();
//                // 将图片显示到界面上
//                Bitmap bm = BitmapFactory.decodeFile(picturePath);
//
//                findViewById(R.id.rl_table).setBackground(new BitmapDrawable(bm));
//            }
        }
    }

    /**
     * 庄家下庄，进行下一场了
     */
    private void nextChang() {
        benchang = 0;
        mBinding.tvChang.setText(changMap.get(++chang));
        mShimmer.cancel();
        mShimmer.start(getOyaTextView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
//            设置对局数据
            case R.id.toolbar_game_record:
                openPage(true, -1, GameRecordActivity.class);
                return true;
//            查看个人数据
            case R.id.toolbar_persional_record:
                openPage(true, -1, PlayerInfoActivity.class);
                break;
            //直接修改数据库，暂不提供该功能
//            case R.id.toolbar_modify_record:
//                openPage(true, -1, ModifyDbActivity.class);
//                break;
            //变更背景
            case R.id.toolbar_change_desk:
                magicFileChooser = new MagicFileChooser(this);
                magicFileChooser.showFileChooser("image/*");
                break;
//            流局
            case R.id.toolbar_liuju:
                //开始对局了才可以点击流局
                if (isStart) {
                    if (mLiujuDialog == null) {
                        mLiujuDialog = new LiuJuDialog(mContext, mPlayers);
                    }
                    mLiujuDialog.show();
                } else {
                    ToastUtils.showShort(R.string.game_not_start);
                }
                break;
//            点数早见表
            case R.id.toolbar_point:
                startActivity(new Intent(mContext, ScanPointActivity.class));
                break;
            case R.id.toolbar_practice:
                startActivity(new Intent(mContext, PracticeActivity.class));
                break;
            case R.id.toolbar_about:
                if (feedbackDialog == null) {
                    View feedbackView = View.inflate(mContext, R.layout.dialog_feedback, null);
                    final EditText etFeedback = (EditText) feedbackView.findViewById(R.id.et_feedback);
                    TextView tv = (TextView) feedbackView.findViewById(R.id.tv);
                    PackageManager pm = getPackageManager();
                    PackageInfo pi = null;
                    try {
                        pi = pm.getPackageInfo(getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    tv.setText("当前版本：v" +pi.versionName);
                    feedbackDialog = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle(R.string.welcome_feedback)
                            .setView(feedbackView)
                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent data = new Intent(Intent.ACTION_SENDTO);
                                    data.setData(Uri.parse("mailto:nishino8818@qq.com"));
                                    data.putExtra(Intent.EXTRA_SUBJECT, "日麻分析反馈");
                                    data.putExtra(Intent.EXTRA_TEXT, etFeedback.getText().toString());
                                    startActivity(data);
                                }
                            }).create();
                }
                feedbackDialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private boolean isStart = false;
    private LiuJuDialog mLiujuDialog;

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_east:
                    openScorePage(mPlayers == null ? null : mPlayers[0]);
                    break;
                case R.id.ll_south:
                    openScorePage(mPlayers == null ? null : mPlayers[1]);
                    break;
                case R.id.ll_west:
                    openScorePage(mPlayers == null ? null : mPlayers[2]);
                    break;
                case R.id.ll_north:
                    openScorePage(mPlayers == null ? null : mPlayers[3]);
                    break;

                case R.id.fab_select_player:
                    if (isStart) {
                        ToastUtils.showShort("不支持中途换人，请先结束当前对局");
                    } else {
                        openPage(true, RC_PLAYERS, AddNewGameActivity.class);
                        mBinding.fabMenu.collapse();
                    }
                    break;
                case R.id.fab_start:
                    mBinding.fabMenu.collapse();
                    if (mPlayers == null) {
                        ToastUtils.showShort("还没选人呢");
                        return;
                    }
                    startGame();
                    break;

            }
        }
    };

    public void startGame() {
        if (!isStart) {
            mBinding.fabStart.setTitle("结束对局");
            mBinding.fabStart.setIcon(R.drawable.stop);
            isStart = true;
            //开局就将场次变成东一局
            SPUtils.putInt(Constant.CHANG, 1);
            mBinding.tvChang.setText(changMap.get(0));
            //东家闪光，右边栏东一变色
            mShimmer.start(mBinding.tvEast);

            //将所有人分数初始化为25000
            initScore();
        } else {
            openPage(true, -1, SetGameScoreActiivty.class);
            mBinding.fabStart.setTitle("开局");
            mBinding.fabStart.setIcon(R.mipmap.start_game);
            isStart = false;
        }
    }

    private void initScore() {
        mBinding.tvEastPoint.setText(getString(R.string._25000));
        mBinding.tvSouthPoint.setText(getString(R.string._25000));
        mBinding.tvWestPoint.setText(getString(R.string._25000));
        mBinding.tvNorthPoint.setText(getString(R.string._25000));
        SPUtils.putInt(mPlayers[0], 25000);
        SPUtils.putInt(mPlayers[1], 25000);
        SPUtils.putInt(mPlayers[2], 25000);
        SPUtils.putInt(mPlayers[3], 25000);
    }

    private void openScorePage(String player) {
        if (TextUtils.isEmpty(player)) {
            Toast.makeText(this, "人都还没选呢", Toast.LENGTH_SHORT).show();
            return;
        }
        hePlayer = player;
        new RichiDialog(mContext, mPlayers).show();
//        点击对话框的确定和取消后跳转到GetScoreActivity，逻辑在initEvent（）里
    }

    public void showDialog(View view) {
        switch (view.getId()) {
            case R.id.tv_east:
                String eastName = SPUtils.getString(Constant.EAST, "");
                break;
            case R.id.tv_south:
                String southName = SPUtils.getString(Constant.SOUTH, "");
                break;
            case R.id.tv_west:
                String westName = SPUtils.getString(Constant.WEST, "");
                break;
            case R.id.tv_north:
                String northName = SPUtils.getString(Constant.NORTH, "");
                break;

        }
    }

    private long backPressedTime;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - backPressedTime) < 3000L) {
            super.onBackPressed();
        } else {
            backPressedTime = System.currentTimeMillis();
            ToastUtils.showShort("你可能手滑了，再次点击退出应用");
        }
    }

    public void closeDrawerLayout() {
        mBinding.drawerlayout.closeDrawers();
    }

    /**
     * 获取庄家名字
     */
    private String getOyaName() {
        return mPlayers[chang % 4];
    }

    private ShimmerTextView getOyaTextView() {
        return textViews.get(chang % 4);
    }
}
