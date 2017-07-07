package com.uu.mahjong_analyse.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.uu.mahjong_analyse.R;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Nagisa on 2016/6/24.
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    public Context mContext;
    public BaseActivity() {
        mContext = this;
    }
    public static final String TAG = "uu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //保持屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0以上直接用系统提供的方法
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        init();
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
    }

    public  void init() {
        initView();
        initData();
        initEvent();
    }

    public abstract void initView();
    public abstract void initData();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
