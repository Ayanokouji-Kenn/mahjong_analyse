package com.uu.mahjong_analyse.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseFragment
import com.uu.mahjong_analyse.databinding.FragMainBinding
import com.uu.mahjong_analyse.utils.MagicFileChooser
import com.uu.mahjong_analyse.utils.SPUtils
import com.uu.mahjong_analyse.utils.getVM
import com.uu.mahjong_analyse.view.LiuJuDialog
import java.util.*

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */


class MainFragment : BaseFragment() {
    private lateinit var mBinding:FragMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragMainBinding.inflate(inflater,container,false).apply {
            vm = (activity as MainActivity).getVM(MainVM::class.java)
        }
        rotatePlayerName()
        initData()
        return mBinding.root
    }

    private fun rotatePlayerName() {
        ObjectAnimator.ofFloat(mBinding.llSouth, "rotation", 0f, -90f).start()
        ObjectAnimator.ofFloat(mBinding.llWest, "rotation", 0f, 180f).start()
        ObjectAnimator.ofFloat(mBinding.llNorth, "rotation", 0f, 90f).start()
    }




    val mShimmer=Shimmer()
    private var textViews= mutableListOf<ShimmerTextView>()//根据场次确定亲家
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
        for (richiPlayer in richiPlayers) {
            val tv = playerTvMap[richiPlayer]
            val score = SPUtils.getInt(richiPlayer, Integer.MIN_VALUE)
            tv?.text = (score - 1000).toString()
            SPUtils.putInt(richiPlayer, score - 1000)
            mBinding.vm?.gong?.set(mBinding.vm?.gong?.get()!!+1000)
        }
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
        if(mBinding.vm !=null) {
            mBinding.vm!!.benchang.set(0)
            mBinding.vm!!.chang.set(mBinding.vm!!.chang.get() + 1)
            mShimmer.cancel()
        }
//        mShimmer.start(getOyaTextView())
    }



    private var mLiujuDialog: LiuJuDialog? = null

    internal var mListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
//            R.id.ll_east -> openScorePage(if (mPlayers == null) null else mPlayers!![0])
//            R.id.ll_south -> openScorePage(if (mPlayers == null) null else mPlayers!![1])
//            R.id.ll_west -> openScorePage(if (mPlayers == null) null else mPlayers!![2])
//            R.id.ll_north -> openScorePage(if (mPlayers == null) null else mPlayers!![3])

        }
    }

    fun startGame() {
//        if (!isStart) {
//            mBinding.fabStart.setTitle("结束对局")
//            mBinding.fabStart.setIcon(R.drawable.stop)
//            isStart = true
//            //开局就将场次变成东一局
//            SPUtils.putInt(Constant.CHANG, 1)
//            mBinding.tvChang.setText(changMap.get(0))
//            //东家闪光，右边栏东一变色
//            mShimmer!!.start(mBinding.tvEast)
//
//            //将所有人分数初始化为25000
//            initScore()
//        } else {
//            (activity as BaseActivity).openPage(true, -1, SetGameScoreActiivty::class.java)
//            mBinding.fabStart.setTitle("开局")
//            mBinding.fabStart.setIcon(R.mipmap.start_game)
//            isStart = false
//        }
    }

//    private fun initScore() {
//        mBinding.tvEastPoint.setText(getString(R.string._25000))
//        mBinding.tvSouthPoint.setText(getString(R.string._25000))
//        mBinding.tvWestPoint.setText(getString(R.string._25000))
//        mBinding.tvNorthPoint.setText(getString(R.string._25000))
//        SPUtils.putInt(mPlayers!![0], 25000)
//        SPUtils.putInt(mPlayers!![1], 25000)
//        SPUtils.putInt(mPlayers!![2], 25000)
//        SPUtils.putInt(mPlayers!![3], 25000)
//    }

//    private fun openScorePage(player: String?) {
//        if (TextUtils.isEmpty(player)) {
//            Toast.makeText(activity, "人都还没选呢", Toast.LENGTH_SHORT).show()
//            return
//        }
//        hePlayer = player
//        RichiDialog(activity, mPlayers).show()
//        //        点击对话框的确定和取消后跳转到GetScoreActivity，逻辑在initEvent（）里
//    }


    private var backPressedTime: Long = 0

    override fun onFragShow() {
        with((activity as MainActivity).supportActionBar) {
            this?.title = resources.getString(R.string.app_name)
        }
        mBinding.vm?.fgShow?.set(MainActivity.TAG_FG_MAIN)
    }

    companion object {
        private val RC_PLAYERS = 1
        private val RC_GET_SCORE = 2
        private val RC_ALBUM = 3
        fun newInstance() = MainFragment()

    }
}