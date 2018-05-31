package com.uu.mahjong_analyse.ui

import android.animation.ObjectAnimator
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.blankj.rxbus.RxBus
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseFragment
import com.uu.mahjong_analyse.bean.LiujuResult
import com.uu.mahjong_analyse.bean.RichiEvent
import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.databinding.FragMainBinding
import com.uu.mahjong_analyse.utils.ConvertHelper
import com.uu.mahjong_analyse.utils.MagicFileChooser
import com.uu.mahjong_analyse.utils.ScoreCalcHelper
import com.uu.mahjong_analyse.view.LiuJuDialog
import com.uu.mahjong_analyse.view.RichiDialog
import me.yokeyword.fragmentation.ISupportFragment
import java.util.*

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */


class MainFragment : BaseFragment() {
    private lateinit var mBinding: FragMainBinding
    private lateinit var vm: MainVM
    private val tvArr = arrayOfNulls<ShimmerTextView>(4)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(activity).get(MainVM::class.java)
//        vm.recover()
    }


    private fun initObserver() {
        RxBus.getDefault().subscribe<LiujuResult>(this, RxBus.Callback {
            vm.liuju()
            refreshView()
            nextChang(true)
        })
        RxBus.getDefault().subscribe<RichiEvent>(this,RxBus.Callback {
            ScoreCalcHelper.handleRichi()
            startActivityForResult(Intent(activity,SetScoreActivity::class.java), RC_SET_SCORE)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getDefault().unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragMainBinding.inflate(inflater, container, false)
        mBinding.listener = mListener
        rotatePlayerName()
        initData()
        initObserver()
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.mComposite.dispose()
    }

    private fun rotatePlayerName() {
        ObjectAnimator.ofFloat(mBinding.llSouth, "rotation", 0f, -90f).start()
        ObjectAnimator.ofFloat(mBinding.llWest, "rotation", 0f, 180f).start()
        ObjectAnimator.ofFloat(mBinding.llNorth, "rotation", 0f, 90f).start()
    }


    val mShimmer = Shimmer()
    private var textViews = mutableListOf<ShimmerTextView>()//根据场次确定亲家
    private val playerTvMap = mapOf<String, TextView>()
    private var magicFileChooser: MagicFileChooser? = null
    private var feedbackDialog: AlertDialog? = null

    fun initData() {
        textViews.add(mBinding.tvEast)
        textViews.add(mBinding.tvSouth)
        textViews.add(mBinding.tvWest)
        textViews.add(mBinding.tvNorth)
    }

    /**
     * 庄家下庄，进行下一场了
     */
    private fun nextChang(isLiuju:Boolean) {
//        流局的情况已经自动判断过了，这里需要处理一下上一把和牌的人中有没有庄家
        if(!isLiuju) {
            if(SetScoreActivity.heNames.contains(ConvertHelper.getOyaPlayer()?.name?:"")){
                ScoreCalcHelper.nextChang(true)
            }else {
                ScoreCalcHelper.nextChang(false)
            }
        }
        SetScoreActivity.heNames.clear()
        if (mShimmer.isAnimating) {
            mShimmer.cancel()
        }
        mShimmer.start(getOyaTextView())
    }

    private fun getOyaTextView(): ShimmerTextView {
        return when (GameModle.getInstance().chang / 10 % 4) {
            1 -> mBinding.tvEast
            2 -> mBinding.tvSouth
            3 -> mBinding.tvWest
            else -> mBinding.tvNorth
        }
    }


    private var mLiujuDialog: LiuJuDialog? = null


    private fun startGame() {
        if (!vm.isStart) {
            mBinding.fabStart.title = "结束对局"
            mBinding.fabStart.setIcon(R.drawable.stop)
            vm.isStart = true
            vm.isStart = true
            mShimmer.start(getOyaTextView())
        } else {
            vm.clearTempData()
            refreshView()
//            (activity as BaseActivity).openPage(true, -1, SetGameScoreActiivty::class.java)
            mBinding.fabStart.title = "开局"
            mBinding.fabStart.setIcon(R.mipmap.start_game)
            vm.isStart = false
            mShimmer.cancel()
        }
    }

    private fun refreshView() {
//        玩家姓名
        mBinding.tvEast.text = GameModle.getInstance().eastPlayer?.name
        mBinding.tvWest.text = GameModle.getInstance().westPlayer?.name
        mBinding.tvNorth.text = GameModle.getInstance().northPlayer?.name
        mBinding.tvSouth.text = GameModle.getInstance().southPlayer?.name

//        玩家点数
        mBinding.tvEastPoint.text = GameModle.getInstance().east.toString()
        mBinding.tvSouthPoint.text = GameModle.getInstance().south.toString()
        mBinding.tvWestPoint.text = GameModle.getInstance().west.toString()
        mBinding.tvNorthPoint.text = GameModle.getInstance().north.toString()

//        场
        mBinding.tvChang.text = ConvertHelper.parseChang(GameModle.getInstance().chang)
//        供托
        mBinding.tvGong.text = GameModle.getInstance().gong.toString()
    }

    private fun openScorePage(player: Player?) {
        if (player == null) {
            Toast.makeText(activity, "人都还没选呢", Toast.LENGTH_SHORT).show()
            return
        }
        GameModle.getInstance().heName = player.name
        RichiDialog(activity).show()
    }


    private var backPressedTime: Long = 0

    override fun onSupportVisible() {
        super.onSupportVisible()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)
    }

    /**
     * 点击事件
     */
    private var mListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.ll_chang -> nextChang(false)
            R.id.ll_east -> openScorePage(GameModle.getInstance().eastPlayer)
            R.id.ll_south -> openScorePage(GameModle.getInstance().southPlayer)
            R.id.ll_west -> openScorePage(GameModle.getInstance().westPlayer)
            R.id.ll_north -> openScorePage(GameModle.getInstance().northPlayer)
            R.id.fab_select_player -> if (vm.isStart) {
                ToastUtils.showShort(getString(R.string.cant_change_players_finish_this_game))
            } else {
                startForResult(AddNewGameFragment.getInstance(), RC_PLAYERS)
            }
            R.id.fab_start -> {
                if (GameModle.getInstance().eastPlayer == null
                        || GameModle.getInstance().westPlayer == null
                        || GameModle.getInstance().southPlayer == null
                        || GameModle.getInstance().northPlayer == null) {
                    ToastUtils.showShort(getString(R.string.no_players))
                    return@OnClickListener
                } else {
                    startGame();
                }
            }
        }
        mBinding.fabMenu.collapse()
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        if (resultCode == ISupportFragment.RESULT_OK) {
            when (requestCode) {
                RC_PLAYERS -> {
                    refreshView()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SET_SCORE && resultCode == Activity.RESULT_OK){
            refreshView()
        }
    }

    companion object {
        private val RC_PLAYERS = 1
        private val RC_SET_SCORE = 2
        private val RC_ALBUM = 3
        fun newInstance() = MainFragment()

    }
}