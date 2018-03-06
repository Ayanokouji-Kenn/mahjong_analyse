package com.uu.mahjong_analyse.ui;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.blankj.utilcode.util.SpanUtils;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.adapter.ScanPointAdapter;
import com.uu.mahjong_analyse.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 点数速查
 * @auther xuzijian
 * @date 2017/6/30 17:01.
 */

public class ScanPointActivity extends BaseActivity {

    private int spanCount = 9;
    private Toolbar toolbar;

    @Override
    public void initView() {
        setContentView(R.layout.activity_scan_point);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("点数速查");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private List<CharSequence> mDatas = new ArrayList<>();

    @Override
    public void initData() {
//        photoView.setImageResource(R.drawable.point_table);
        initPointListData();
        ScanPointAdapter adapter = new ScanPointAdapter(this,mDatas);
//        mRvPoint.setLayoutManager(new GridLayoutManager(mContext, spanCount));
//        mRvPoint.setAdapter(adapter);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initPointListData() {
        mDatas.add(getString(R.string.fan4));
        mDatas.add(getString(R.string.fan3));
        mDatas.add(getString(R.string.fan2));
        mDatas.add(getString(R.string.fan1));
        mDatas.add(getString(R.string.fu));
        mDatas.add(getString(R.string.fan1));
        mDatas.add(getString(R.string.fan2));
        mDatas.add(getString(R.string.fan3));
        mDatas.add(getString(R.string.fan4));
        //20符
        mDatas.add(new SpanUtils().appendLine("7700").appendLine("2600").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("3900").appendLine("1300").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2000").appendLine("700").setFontSize(8,true).create());
        mDatas.add("/");
        mDatas.add(getString(R.string.fu20));
        mDatas.add("/");
        mDatas.add(new SpanUtils().appendLine("1300").appendLine("700/400").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2600").appendLine("1300/700").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("5200").appendLine("2600/1300").setFontSize(8,true).create());
        //30符
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("5800").appendLine("2000").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2900").appendLine("1000").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("1500").appendLine("500").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu30));
        mDatas.add(new SpanUtils().appendLine("1000").appendLine("500/300").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2000").appendLine("1000/500").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("3900").appendLine("2000/1000").setFontSize(8,true).create());
        mDatas.add("");
        //40符
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("7700").appendLine("2600").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("3900").appendLine("1300").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2000").appendLine("700").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu40));
        mDatas.add(new SpanUtils().appendLine("1300").appendLine("700/400").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2600").appendLine("1300/700").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("5200").appendLine("2600/1300").setFontSize(8,true).create());
        mDatas.add("");
        //50符
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("9600").appendLine("3200").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("4800").appendLine("1600").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2400").appendLine("800").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu50));
        mDatas.add(new SpanUtils().appendLine("1600").appendLine("800/400").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("3200").appendLine("1600/800").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("6400").appendLine("3200/1600").setFontSize(8,true).create());
        mDatas.add("");
        //60符
        mDatas.add("");
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("5800").appendLine("2000").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2900").appendLine("1000").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu60));
        mDatas.add(new SpanUtils().appendLine("2000").appendLine("1000/500").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("3900").appendLine("2000/1000").setFontSize(8,true).create());
        mDatas.add("");
        mDatas.add("");
        //70符
        mDatas.add("");
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("6800").appendLine("2300").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("3400").appendLine("1200").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu70));
        mDatas.add(new SpanUtils().appendLine("2300").appendLine("1200/600").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("4500").appendLine("2300/1200").setFontSize(8,true).create());
        mDatas.add("");
        mDatas.add("");
        //80符
        mDatas.add("");
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("7700").appendLine("2600").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("3900").appendLine("1300").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu80));
        mDatas.add(new SpanUtils().appendLine("2600").appendLine("1300/700").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("5200").appendLine("2600/1300").setFontSize(8,true).create());
        mDatas.add("");
        mDatas.add("");
        //90符
        mDatas.add("");
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("8700").appendLine("2900").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("4400").appendLine("1500").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu90));
        mDatas.add(new SpanUtils().appendLine("2900").appendLine("1500/800").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("5800").appendLine("2900/1500").setFontSize(8,true).create());
        mDatas.add("");
        mDatas.add("");
        //100符
        mDatas.add("");
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("9600").appendLine("3200").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("4800").appendLine("1600").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu100));
        mDatas.add(new SpanUtils().appendLine("3200").appendLine("1600/800").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("6400").appendLine("3200/1600").setFontSize(8,true).create());
        mDatas.add("");
        mDatas.add("");
        //110符
        mDatas.add("");
        mDatas.add("");
        mDatas.add(new SpanUtils().appendLine("10600").appendLine("3600").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("5300").appendLine("1800").setFontSize(8,true).create());
        mDatas.add(getString(R.string.fu110));
        mDatas.add(new SpanUtils().appendLine("3600").appendLine("1800/900").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("7100").appendLine("3600/1800").setFontSize(8,true).create());
        mDatas.add("");
        mDatas.add("");
        //25符
        mDatas.add(new SpanUtils().appendLine("9600").appendLine("3200").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("4800").appendLine("1600").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("2400").appendLine("/").setFontSize(8,true).create());
        mDatas.add("/");
        mDatas.add(getString(R.string.fu25));
        mDatas.add("/");
        mDatas.add(new SpanUtils().appendLine("1600").appendLine("/").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("3200").appendLine("1600/800").setFontSize(8,true).create());
        mDatas.add(new SpanUtils().appendLine("6400").appendLine("3200/1600").setFontSize(8,true).create());
    }


}
