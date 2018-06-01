package com.uu.mahjong_analyse.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseActivity
import com.uu.mahjong_analyse.databinding.ActivityMainBinding
import com.uu.mahjong_analyse.fragment.LeftMenuFragment
import com.uu.mahjong_analyse.utils.MagicFileChooser
import com.uu.mahjong_analyse.utils.replaceFragmentInActivity
import com.uu.mahjong_analyse.utils.setupActionBar
import com.uu.mahjong_analyse.view.LiuJuDialog

class MainActivity : BaseActivity() {

    private var magicFileChooser: MagicFileChooser? = null
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var vm: MainVM
    private var toolbar: Toolbar? = null
    private var feedbackDialog: AlertDialog? = null

    private val mLiujuDialog: LiuJuDialog by lazy {
        LiuJuDialog(this)
    }

    private var backPressedTime: Long = 0


    override fun initData() {
    }

    override fun initEvent() {

    }

    override fun initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProviders.of(this).get(MainVM::class.java)
        mBinding.vm = vm
//        initActionbar()

        if (findFragment(MainFragment::class.java) == null)
            loadRootFragment(R.id.fl_content, MainFragment.newInstance())
        if(findFragment(LeftMenuFragment::class.java) == null)
            loadRootFragment(R.id.fl_leftmenu,LeftMenuFragment.getInstance())
    }

    private fun initActionbar() {
        setupActionBar(R.id.toolbar) {
            title = getString(R.string.app_name)
//            setDisplayHomeAsUpEnabled(true)
        }

//        控制侧边栏，保留项目
//        val mDrawerToggle = ActionBarDrawerToggle(this, mBinding.drawerlayout, toolbar, R.string.drawer_open,
//                R.string.drawer_close)
//        mDrawerToggle.syncState()
//        mBinding.drawerlayout.addDrawerListener(mDrawerToggle)
    }



    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
    }

    fun closeDrawerLayout() {
        mBinding.drawerlayout.closeDrawers()
    }

    companion object {
        private val RC_PLAYERS = 1
        private val RC_GET_SCORE = 2
        private val RC_ALBUM = 3
        val TAG_FG_MAIN = "TAG_FG_MAIN"
        val TAG_FG_ADD_NEW_GAME = "TAG_FG_ADD_NEW_GAME"
    }
}
