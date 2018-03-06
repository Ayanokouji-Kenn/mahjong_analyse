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
import com.uu.mahjong_analyse.utils.addFragmentToActivity
import com.uu.mahjong_analyse.utils.replaceFragmentInActivity
import com.uu.mahjong_analyse.utils.setupActionBar
import com.uu.mahjong_analyse.view.LiuJuDialog

class MainActivity : BaseActivity() {

    private var magicFileChooser: MagicFileChooser? = null
    private lateinit var mBinding: ActivityMainBinding
    private var toolbar: Toolbar? = null
    private lateinit var vm: MainVM
    private var feedbackDialog: AlertDialog? = null


    private var mLiujuDialog: LiuJuDialog? = null

    private var mListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.fab_select_player -> if (vm.isStart) {
                ToastUtils.showShort(getString(R.string.cant_change_players_finish_this_game))
            } else {
                supportFragmentManager.beginTransaction()
                        .add(R.id.fl_content, supportFragmentManager.findFragmentByTag(TAG_FG_ADD_NEW_GAME)
                                ?: AddNewGameFragment.getInstance(), TAG_FG_ADD_NEW_GAME)
                        .hide(supportFragmentManager.findFragmentByTag(TAG_FG_MAIN))
                        .addToBackStack(null)
                        .commit()
            }
            R.id.fab_start -> {
                for (player in vm.players) {
                    if (player == null) {
                        ToastUtils.showShort(getString(R.string.no_players))
                        return@OnClickListener
                    }
                }
            }
        }//startGame();
        mBinding.fabMenu.collapse()
    }


    private var backPressedTime: Long = 0


    override fun initData() {
    }

    override fun initEvent() {

    }

    override fun initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProviders.of(this).get(MainVM::class.java)
        mBinding.listener = mListener
        mBinding.vm = vm
        setupActionBar(R.id.toolbar) {
            title = getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
        }
        mBinding.toolbar.popupTheme = R.style.PopupMenu
        val mDrawerToggle = ActionBarDrawerToggle(this, mBinding.drawerlayout, toolbar, R.string.drawer_open,
                R.string.drawer_close)
        mDrawerToggle.syncState()
        mBinding.drawerlayout.addDrawerListener(mDrawerToggle)

//        supportFragmentManager.beginTransaction().add(R.id.fl_content,MainFragment.newInstance()).commit()
        addFragmentToActivity(MainFragment.newInstance(), R.id.fl_content, TAG_FG_MAIN)
        supportFragmentManager.findFragmentById(R.id.fl_leftmenu) ?: LeftMenuFragment()
                .let { replaceFragmentInActivity(it, R.id.fl_leftmenu) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        when (item.itemId) {
        //            设置对局数据
            R.id.toolbar_game_record -> {
                openPage(true, -1, GameRecordActivity::class.java)
                return true
            }
        //            查看个人数据
            R.id.toolbar_persional_record -> openPage(true, -1, PlayerInfoActivity::class.java)
        //直接修改数据库，暂不提供该功能
        //            case R.id.toolbar_modify_record:
        //                (activity as BaseActivity).openPage(true, -1, ModifyDbActivity.class);
        //                break;
        //变更背景
            R.id.toolbar_change_desk -> {
                magicFileChooser = MagicFileChooser(this)
                magicFileChooser!!.showFileChooser("image/*")
            }
        //            流局
            R.id.toolbar_liuju ->
                //开始对局了才可以点击流局
                if (vm.isStart) {
                    if (mLiujuDialog == null) {
                        mLiujuDialog = LiuJuDialog(this, vm.players.map { it!!.name }.toTypedArray())
                    }
                    mLiujuDialog!!.show()
                } else {
                    ToastUtils.showShort(R.string.game_not_start)
                }
        //            点数早见表
            R.id.toolbar_point -> startActivity(Intent(this, ScanPointActivity::class.java))
            R.id.toolbar_practice -> startActivity(Intent(this, PracticeActivity::class.java))
            R.id.toolbar_about -> {
                if (feedbackDialog == null) {
                    val feedbackView = View.inflate(this, R.layout.dialog_feedback, null)
                    val etFeedback = feedbackView.findViewById<View>(R.id.et_feedback) as EditText
                    val tv = feedbackView.findViewById<View>(R.id.tv) as TextView
                    val pm = packageManager
                    var pi: PackageInfo? = null
                    try {
                        pi = pm.getPackageInfo(this.getPackageName(), 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }

                    tv.text = "当前版本：v" + pi!!.versionName
                    feedbackDialog = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle(R.string.welcome_feedback)
                            .setView(feedbackView)
                            .setPositiveButton(getString(R.string.confirm)) { dialog, which ->
                                val data = Intent(Intent.ACTION_SENDTO)
                                data.data = Uri.parse("mailto:nishino8818@qq.com")
                                data.putExtra(Intent.EXTRA_SUBJECT, "日麻分析反馈")
                                data.putExtra(Intent.EXTRA_TEXT, etFeedback.text.toString())
                                startActivity(data)
                            }.create()
                }
                feedbackDialog!!.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag(TAG_FG_MAIN).isHidden) {
            supportFragmentManager.popBackStack()
        } else {
            if (System.currentTimeMillis() - backPressedTime < 3000L) {
                super.onBackPressed()
            } else {
                backPressedTime = System.currentTimeMillis()
                ToastUtils.showShort("你可能手滑了，再次点击退出应用")
            }
        }
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