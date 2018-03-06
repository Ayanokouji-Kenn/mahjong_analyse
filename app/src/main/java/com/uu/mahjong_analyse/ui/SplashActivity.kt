package com.uu.mahjong_analyse.ui

import com.uu.mahjong_analyse.base.BaseActivity
import com.uu.mahjong_analyse.helper.PermissionHelper

/**
 * @auther xuzijian
 * @date 2017/6/27 10:44.
 */

class SplashActivity : BaseActivity(), PermissionHelper.OnPermissionGrantedListener {

    override fun initView() {
//        setContentView(R.layout.activity_splash)
        PermissionHelper.requestStorage(this)
    }


    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPermissionGranted() {
        openPage(true,-1,MainActivity::class.java)
        finish()
    }

    override fun initEvent() {

    }

}
