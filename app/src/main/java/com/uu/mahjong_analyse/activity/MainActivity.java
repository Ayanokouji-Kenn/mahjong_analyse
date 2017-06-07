package com.uu.mahjong_analyse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.Utils.Constant;
import com.uu.mahjong_analyse.base.MyApplication;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.db.DBDao;
import com.uu.mahjong_analyse.fragment.LeftMenuFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_PLAYERS = 1;
    private static final int REQUEST_GET_SCORE = 2;

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;
    @BindView(R.id.tv_east)
    ShimmerTextView mTvEast;
    @BindView(R.id.tv_south)
    ShimmerTextView mTvSouth;
    @BindView(R.id.tv_west)
    ShimmerTextView mTvWest;
    @BindView(R.id.tv_north)
    ShimmerTextView mTvNorth;
    @BindView(R.id.rl_table)
    RelativeLayout mRlTable;
    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.rightmenu_rg)
    RadioGroup rightmenu_radiogroup;

    private String[] mPlayers;
    private LeftMenuFragment mLeftMenuFragment;
    private Shimmer mShimmer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);


    }

    @Override
    public void initData() {
        mShimmer = new Shimmer();

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle("麻将统计分析工具");
        mToolbar.setPopupTheme(R.style.PopupMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mLeftMenuFragment = new LeftMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_leftmenu, mLeftMenuFragment).commitAllowingStateLoss();
    }



    private int chang = 1;  //1-8 代表东1到南4；
    public void initEvent() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(true, REQUEST_PLAYERS, AddNewGameActivity.class);
            }
        });

        rightmenu_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_east1:
                        chang = 1;

                        break;
                    case R.id.rb_east2:
                        chang = 2;
                        mShimmer.cancel();
                        mShimmer.start(mTvSouth);
                        break;
                    case R.id.rb_east3:
                        chang = 3;
                        mShimmer.cancel();
                        mShimmer.start(mTvWest);
                        break;
                    case R.id.rb_east4:
                        chang = 4;
                        mShimmer.cancel();
                        mShimmer.start(mTvNorth);
                        break;
                    case R.id.rb_south1:
                        chang = 5;
                        mShimmer.cancel();
                        mShimmer.start(mTvEast);
                        break;
                    case R.id.rb_south2:
                        chang = 6;
                        mShimmer.cancel();
                        mShimmer.start(mTvSouth);
                        break;
                    case R.id.rb_south3:
                        chang = 7;
                        mShimmer.cancel();
                        mShimmer.start(mTvWest);
                        break;
                    case R.id.rb_south4:
                        chang = 8;
                        mShimmer.cancel();
                        mShimmer.start(mTvNorth);
                        break;

                }
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PLAYERS && resultCode == RESULT_OK) {
            mPlayers = (String[]) data.getCharSequenceArrayExtra("players");
            //把对局的四个人名字传过来，如果数据库中没有，就为他们建表,并将所有值变成0；
            for(int i = 0; i < mPlayers.length; i++) {
                if(DBDao.selectPlayer(mPlayers[i].toString()) == null) {
                    DBDao.insertPlayer(Constant.Table.TABLE_PLAYER_RECORD, mPlayers[i].toString());
                }
            }
            mTvEast.setText(MyApplication.param.get("east"));
            mTvWest.setText(MyApplication.param.get("west"));
            mTvSouth.setText(MyApplication.param.get("south"));
            mTvNorth.setText(MyApplication.param.get("north"));


            mLeftMenuFragment.mLeftmenuDatas.clear();
            mLeftMenuFragment.mLeftmenuDatas.add("东家：" + MyApplication.param.get("east"));
            mLeftMenuFragment.mLeftmenuDatas.add("西家：" + MyApplication.param.get("west"));
            mLeftMenuFragment.mLeftmenuDatas.add("南家：" + MyApplication.param.get("south"));
            mLeftMenuFragment.mLeftmenuDatas.add("北家：" + MyApplication.param.get("north"));
            mLeftMenuFragment.mLeftMenuAdapter.notifyDataSetChanged();
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.toolbar_game_record:
                openPage(true,-1,GameRecordActivity.class);
                return true;
            case R.id.toolbar_persional_record:
                openPage(true,-1,PlayerInfoActivity.class);
                break;
            case R.id.toolbar_modify_record:
                openPage(true,-1,ModifyDbActivity.class);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private boolean isStart = false;

    @OnClick({R.id.tv_east, R.id.tv_south, R.id.tv_west, R.id.tv_north, R.id.btn})
    public void onClick(View view) {
        Intent intent = new Intent(this, GetScoreActivity.class);
        String east = MyApplication.param.get("east");
        String south = MyApplication.param.get("south");
        String north = MyApplication.param.get("north");
        String west = MyApplication.param.get("west");
        switch (view.getId()) {
            case R.id.tv_east:
                if(TextUtils.isEmpty(east)) {
                    Toast.makeText(this, "人都还没选呢，点个JJ", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("player", east);
                if(isStart) {
                    openPage(true, REQUEST_GET_SCORE, intent);
                }
                break;
            case R.id.tv_south:

                if(TextUtils.isEmpty(south)) {
                    Toast.makeText(this, "人都还没选呢，点个JJ", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("player", south);
                intent.putExtra("player", south);
                if(isStart) {
                    openPage(true, REQUEST_GET_SCORE, intent);
                }
                break;
            case R.id.tv_west:

                if(TextUtils.isEmpty(west)) {
                    Toast.makeText(this, "人都还没选呢，点个JJ", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("player", west);
                intent.putExtra("player", west);
                if(isStart) {
                    openPage(true, REQUEST_GET_SCORE, intent);
                }
                break;
            case R.id.tv_north:

                if(TextUtils.isEmpty(north)) {
                    Toast.makeText(this, "人都还没选呢，点个JJ", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("player", north);
                intent.putExtra("player", north);
                if(isStart) {
                    openPage(true, REQUEST_GET_SCORE, intent);
                }
                break;
            case R.id.btn:
                if(TextUtils.isEmpty(east) || TextUtils.isEmpty(west) || TextUtils.isEmpty(south) || TextUtils.isEmpty(north)) {
                    Toast.makeText(this, "还没选人呢", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isStart) {
                    mBtn.setText("对战中...");
                    isStart = true;

                    //东家闪光，右边栏东一变色
                    mShimmer.start(mTvEast);
                    rightmenu_radiogroup.check(R.id.rb_east1);
                } else {
                    openPage(true, -1, SetGameScoreActiivty.class);
                    mBtn.setText("开局");
                    isStart = false;
                }
                break;
        }
    }

    public void showDialog(View view) {
        switch (view.getId()) {
            case R.id.tv_east:
                String eastName = MyApplication.param.get("east");
                break;
            case R.id.tv_south:
                String southName = MyApplication.param.get("south");
                break;
            case R.id.tv_west:
                String westName = MyApplication.param.get("west");
                break;
            case R.id.tv_north:
                String northName = MyApplication.param.get("north");
                break;

        }
    }

    public void closeDrawerLayout() {
        mDrawerLayout.closeDrawers();



    }
}
