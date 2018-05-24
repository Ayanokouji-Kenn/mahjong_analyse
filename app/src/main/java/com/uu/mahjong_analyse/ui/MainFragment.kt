package com.uu.mahjong_analyse.ui

import android.animation.ObjectAnimator
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.blankj.rxbus.RxBus
import com.blankj.utilcode.util.ToastUtils
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseActivity
import com.uu.mahjong_analyse.base.BaseFragment
import com.uu.mahjong_analyse.bean.LiujuResult
import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.databinding.FragMainBinding
import com.uu.mahjong_analyse.utils.ConvertHelper
import com.uu.mahjong_analyse.utils.MagicFileChooser
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
        vm.recover()
        initObserver()
    }

    private fun initObserver() {
        RxBus.getDefault().subscribe<LiujuResult>(this,RxBus.Callback {
            refreshView()
            nextChang()
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

        return mBinding.root
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

//    override fun initEvent() {
//        //和牌后先谈选择立直的框，回调在这里
//        RxBus.getInstance().toObservable(ArrayList<*>::class.java, Constant.RX_RICHI_RESULT)
//                .subscribe { arrayList ->
//                    handleRichi(arrayList)
//                    val intent = Intent(activity, GetScoreActivity::class.java)
//                    intent.putExtra("player", hePlayer)
//                    intent.putExtra("oya", getOyaName())
//                    intent.putExtra("gong", gong)
//                    intent.putExtra("benchang", benchang)
//                    if (isStart) {
////                        (activity as BaseActivity).openPage(true, RC_GET_SCORE, intent)
//                    }
//                }
//        //流局逻辑全在这里
//        RxBus.getInstance().toObservable(LiujuResult::class.java, Constant.RX_LIUJU_RESULT)
//                .compose<LiujuResult>(this.bindToLifecycle<LiujuResult>())
//                .subscribe { liujuResult ->
//                    //处理流局立直的数据
//                    handleRichi(liujuResult.richiPlayers)
//                    //处理流局听牌的数据
//                    //如果庄家没听牌，那么进行下一场,否则本场+1
//                    if (!liujuResult.tingpaiPlayers.contains(getOyaName())) {
//                        nextChang()
//                    } else {
//                        mBinding.tvChang.setText(SpanUtils().append(changMap.get(chang)).append((++benchang).toString() + "本场").setFontSize(14, true).create())
//                    }
//                    val players = playerTvMap.keys
//                    when (liujuResult.tingpaiPlayers.size) {
//                        1 -> for (player in players) {
//                            val tv = playerTvMap[player]
//                            if (TextUtils.equals(player, liujuResult.tingpaiPlayers[0])) {
//                                val score = SPUtils.getInt(player, Integer.MIN_VALUE)
//                                tv.setText((score + 3000).toString())
//                                SPUtils.putInt(player, score + 3000)
//                            } else {
//                                val score = SPUtils.getInt(player, Integer.MIN_VALUE)
//                                tv.setText((score - 1000).toString())
//                                SPUtils.putInt(player, score - 1000)
//                            }
//                        }
//                        2 -> for (player in players) {
//                            val tv = playerTvMap[player]
//                            if (liujuResult.tingpaiPlayers.contains(player)) {
//                                val score = SPUtils.getInt(player, Integer.MIN_VALUE)
//                                tv.setText((score + 1500).toString())
//                                SPUtils.putInt(player, score + 1500)
//                            } else {
//                                val score = SPUtils.getInt(player, Integer.MIN_VALUE)
//                                tv.setText((score - 1500).toString())
//                                SPUtils.putInt(player, score - 1500)
//                            }
//                        }
//                        3 -> for (player in players) {
//                            val tv = playerTvMap[player]
//                            if (!liujuResult.tingpaiPlayers.contains(player)) {
//                                val score = SPUtils.getInt(player, Integer.MIN_VALUE)
//                                tv.setText((score - 3000).toString())
//                                SPUtils.putInt(player, score - 3000)
//                            } else {
//                                val score = SPUtils.getInt(player, Integer.MIN_VALUE)
//                                tv.setText((score + 1000).toString())
//                                SPUtils.putInt(player, score + 1000)
//                            }
//                        }
//                        else   //剩下的是0和4的情况，即所有人都听 或所有人都不听，那么不处理
//                        -> {
//                        }
//                    }
//                }
//
//    }

    /**
     * 遍历立直玩家集合，取出当前分数，扣掉1000，存回sp，供托+1000
     */
    private fun handleRichi(richiPlayers: ArrayList<String>) {
//        for (richiPlayer in richiPlayers) {
//            val tv = playerTvMap[richiPlayer]
//            val score = SPUtils.getInt(richiPlayer, Integer.MIN_VALUE)
//            tv?.text = (score - 1000).toString()
//            SPUtils.putInt(richiPlayer, score - 1000)
//            mBinding.vm?.gong?.set(mBinding.vm?.gong?.get()!!+1000)
//        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_PLAYERS && resultCode == Activity.RESULT_OK && data != null) {
//            playerTvMap.put(mPlayers!![0], mBinding.tvEastPoint)
//            playerTvMap.put(mPlayers!![1], mBinding.tvSouthPoint)
//            playerTvMap.put(mPlayers!![2], mBinding.tvWestPoint)
//            playerTvMap.put(mPlayers!![3], mBinding.tvNorthPoint)
//
//            startGame()
//            SnackbarUtils.with(mBinding.llChang)
//                    .setMessage("谁和牌就点击谁的名字设置点数即可")
//                    .setAction("知道了") { }.show()
//        } else if (requestCode == RC_GET_SCORE && resultCode == Activity.RESULT_OK) {
//            //设置完分数，不是庄家则chang+1
//            if (!TextUtils.equals(hePlayer, getOyaName())) {
//                //改变场风和庄家闪光效果
//                nextChang()
//            } else {
//                mBinding.tvChang.setText(SpanUtils().append(changMap.get(chang)).append((++benchang).toString() + "本场").setFontSize(14, true).create())
//            }
//            //供托要清零
//            gong = 0
//            mBinding.tvGong.setText(null)
//            //改变四家分数
//            for (player in mPlayers!!) {
//                playerTvMap[player]?.setText(SPUtils.getInt(player, Integer.MIN_VALUE).toString())
//            }
//        } else if (requestCode == MagicFileChooser.ACTIVITY_FILE_CHOOSER) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (magicFileChooser!!.onActivityResult(requestCode, resultCode, data)) {
//                    val files = magicFileChooser!!.chosenFiles
//                    if (files.size != 0) {
//                        val bitmap = BitmapUtils.compressBitmap(activity, files[0].absolutePath, ScreenUtils.getScreenWidth(), ScreenUtils
//                                .getScreenHeight())
//                        mBinding.rlTable.setBackground(BitmapDrawable(resources, bitmap))
//                    }
//                }
//            } else {
//                mBinding.rlTable.setBackground(null)
//            }
//        }
//    }

    /**
     * 庄家下庄，进行下一场了
     */
    private fun nextChang() {
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
        } else {
            (activity as BaseActivity).openPage(true, -1, SetGameScoreActiivty::class.java)
            mBinding.fabStart.title = "开局"
            mBinding.fabStart.setIcon(R.mipmap.start_game)
            vm.isStart = false
        }
    }

    private fun refreshView() {
//        玩家姓名
        mBinding.tvEast.text = GameModle.getInstance().eastName
        mBinding.tvWest.text = GameModle.getInstance().westName
        mBinding.tvNorth.text = GameModle.getInstance().northName
        mBinding.tvSouth.text = GameModle.getInstance().southName

//        玩家点数
        mBinding.tvEastPoint.text = GameModle.getInstance().east.toString()
        mBinding.tvSouthPoint.text = GameModle.getInstance().south.toString()
        mBinding.tvWestPoint.text = GameModle.getInstance().west.toString()
        mBinding.tvNorthPoint.text = GameModle.getInstance().north.toString()

//        场
        mBinding.tvChang.text = ConvertHelper.parseChang(GameModle.getInstance().chang)

//        庄家动画
        if (mShimmer.isAnimating) {
            mShimmer.cancel()
        }
        onBackPressedSupport()
        mShimmer.start(getOyaTextView())
    }

    private fun openScorePage(player: String?) {
        if (TextUtils.isEmpty(player)) {
            Toast.makeText(activity, "人都还没选呢", Toast.LENGTH_SHORT).show()
            return
        }
//        hePlayer = player
//        RichiDialog(activity, mPlayers).show()
        //        点击对话框的确定和取消后跳转到GetScoreActivity，逻辑在initEvent（）里
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

//            R.id.ll_east -> openScorePage(if (mPlayers == null) null else mPlayers!![0])
//            R.id.ll_south -> openScorePage(if (mPlayers == null) null else mPlayers!![1])
//            R.id.ll_west -> openScorePage(if (mPlayers == null) null else mPlayers!![2])
//            R.id.ll_north -> openScorePage(if (mPlayers == null) null else mPlayers!![3])
            R.id.fab_select_player -> if (vm.isStart) {
                ToastUtils.showShort(getString(R.string.cant_change_players_finish_this_game))
            } else {
                startForResult(AddNewGameFragment.getInstance(), RC_PLAYERS)
            }
            R.id.fab_start -> {
                if(GameModle.getInstance().eastName.isEmpty()
                ||GameModle.getInstance().westName.isEmpty()
                ||GameModle.getInstance().northName.isEmpty()
                ||GameModle.getInstance().southName.isEmpty()){
                    ToastUtils.showShort(getString(R.string.no_players))
                    return@OnClickListener
                }else {
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

    companion object {
        private val RC_PLAYERS = 1
        private val RC_GET_SCORE = 2
        private val RC_ALBUM = 3
        fun newInstance() = MainFragment()

    }
}