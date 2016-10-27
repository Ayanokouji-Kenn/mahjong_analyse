package com.uu.mahjong_analyse.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uu.mahjong_analyse.R;

import butterknife.ButterKnife;

/**
 * Created by Nagisa on 2016/6/24.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;
    public BaseActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public  void init() {
        initView();
        initData();
        initEvent();
    }

    public abstract void initData();
    public abstract void initView();
    public abstract void initEvent();

    public void openPage(boolean nextPage, int requestcode, Intent intent) {
        anim(nextPage,requestcode,intent);
    }

    public void openPage(boolean nextPage, int requestcode, Class activity) {
        Intent intent = new Intent(this, activity);
        anim(nextPage,requestcode,intent);
    }

    private void anim(boolean nextPage,int requestcode,Intent intent) {
        if(requestcode == -1) {
            startActivity(intent);
        }else {
            startActivityForResult(intent,requestcode);
        }
        if(nextPage) {

            overridePendingTransition(R.anim.page_right_in,R.anim.page_to_left);
        }else {
            overridePendingTransition(R.anim.page_left_in,R.anim.page_to_right);
        }
    }
}
