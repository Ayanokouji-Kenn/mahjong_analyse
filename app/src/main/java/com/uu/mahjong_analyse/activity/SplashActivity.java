package com.uu.mahjong_analyse.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;

/**
 * @auther xuzijian
 * @date 2017/6/27 10:44.
 */

public class SplashActivity extends BaseActivity implements DialogInterface.OnClickListener{
    @Override
    public void initView() {
        setContentView(R.layout.activity_splash);
    }


    @Override
    public void initData() {


    }

    private AlertDialog mDialog;
    @Override
    protected void onResume() {
        super.onResume();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)  ==PackageManager.PERMISSION_DENIED
                        || ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED
                        ||ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)==  PackageManager.PERMISSION_DENIED
                        ||ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)==  PackageManager.PERMISSION_DENIED
                        ||ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)==  PackageManager.PERMISSION_DENIED)) {

            if (mDialog == null) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert);

                }else {
                    builder = new AlertDialog.Builder(mContext);
                }
                mDialog= builder.setTitle("需要开启一些权限")
                        .setMessage("因为加入了语音识别，所以需要获取一些手机状态、定位信息等权限，麻烦您通过一下")
                        .setPositiveButton(getString(R.string.confirm),this )
                        .setNegativeButton(getString(R.string.cancel),this)
                        .create();

            }

            mDialog.show();

        }else {
            Observable.timer(100, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        }
                    });
        }
    }


    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
            finish();
        }else if (which == DialogInterface.BUTTON_POSITIVE){
            startActivity(new Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + "com.uu.mahjong_analyse")));
        }
    }
}
