package com.uu.mahjong_analyse.Utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uu.mahjong_analyse.R;

/**
 * @auther Nagisa.
 * @date 2016/7/2.
 */
public class CommonApi {
    /**
     * 设置toolbar回退按钮和标题文字
     * */
    public static void setToolbar(final AppCompatActivity activity, Toolbar mToolbar, String title) {
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setTitle(title);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.overridePendingTransition(R.anim.page_left_in,R.anim.page_to_right);
            }
        });
    }
}
