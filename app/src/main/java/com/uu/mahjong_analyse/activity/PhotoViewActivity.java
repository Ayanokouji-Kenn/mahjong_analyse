package com.uu.mahjong_analyse.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @auther xuzijian
 * @date 2017/6/30 17:01.
 */

public class PhotoViewActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photo_view)
    PhotoView photoView;

    @Override
    public void initView() {
        setContentView(R.layout.activity_photo_view);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("点数速查");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public void initData() {
        photoView.setImageResource(R.drawable.point_table);
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

}
