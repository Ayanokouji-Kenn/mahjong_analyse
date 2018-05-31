package com.uu.mahjong_analyse.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.ArrayMap
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bigkoo.pickerview.OptionsPickerView
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseActivity
import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.databinding.ActivityGetscoreBinding
import com.uu.mahjong_analyse.utils.*
import java.util.*

/**
 * 设置每局和了的数据  一炮双响的话，供托头跳
 * Created by Nagisa on 2016/7/1.
 */
class SetScoreActivity : BaseActivity() {

    private var mFan: String? = null

    private lateinit var mBinding: ActivityGetscoreBinding
    private lateinit var vm: SetScoreVM
    //    选择放铳人的选择器
    private val chongPicker: OptionsPickerView<Player> by lazy {
        val builder = OptionsPickerView.Builder(this, { options1, options2, options3, v ->
            GameModle.getInstance().chong = vm.players[options1]?.name
            mBinding.tvChong.text = "放铳人:${GameModle.getInstance().chong}"
        })
                .setSubmitColor(resources.getColor(R.color.colorAccent))
                .setCancelColor(resources.getColor(R.color.colorAccent))
        OptionsPickerView<Player>(builder).apply {
            setPicker(GameModle.getInstance().run {
                listOf(eastPlayer, southPlayer, westPlayer, northPlayer)
            })
        }
    }
    //    选择番符
    private val fanPicker: OptionsPickerView<String> by lazy {
        val builder = OptionsPickerView.Builder(this, { options1, options2, options3, v ->
            val key: String
            //满贯以下,需要记录多少符
            if (options1 == 0) {
                key = mFuList[options1][options2] + mFanlist2[options1][options2][options3] //这里取出来的应该是  110符1翻 这种字符串
            } else {
//                满贯以上直接记录
                key = mFanList[options1]
            }
            if (TextUtils.equals("25符2翻", key) && mBinding.rbTsumo.isChecked) {
                com.blankj.utilcode.util.ToastUtils.showShort("2翻的七对子不可能自摸哦~")
            } else {
                var pointInt = 0
                if (ConvertHelper.isOya(GameModle.getInstance().heName ?: "")) {
                    if (mBinding.rbTsumo.isChecked) {
                        pointInt = qinTsumoMap[key]!!
                    } else if (mBinding.rbRonn.isChecked) {
                        pointInt = qinRonnMap[key]!!
                    }
                } else {
                    if (mBinding.rbTsumo.isChecked) {
                        pointInt = ziTsumoMap[key]!!
                    } else if (mBinding.rbRonn.isChecked) {
                        pointInt = ziRonnMap[key]!!
                    }
                }
                //hepoint后面保存数据的时候，如果是自摸的话 需要分别计算其他三家失点，所以暂时不加上供托和本场棒
                GameModle.getInstance().hePoint = pointInt
                mFan = key
                mBinding.tvFan.text = "番数:$mFan"
//                显示的是加上供托的
                mBinding.etPoint.setText((GameModle.getInstance().hePoint + GameModle.getInstance().chang % 10 * 300 + GameModle.getInstance().gong).toString())
            }
        })
                .setSubmitColor(resources.getColor(R.color.colorAccent))
                .setCancelColor(resources.getColor(R.color.colorAccent))
        OptionsPickerView<String>(builder).apply { setPicker(mFanList, mFuList, mFanlist2) }
    }

    internal val mListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
//            选择放铳人
            R.id.tv_chong -> {
                if (!mBinding.rbRonn.isChecked) {
                    mBinding.rbRonn.isChecked = true
                }
                chongPicker.show()
            }
//            选择和的番数
            R.id.tv_fan -> if (mBinding.rbRonn.isChecked || mBinding.rbTsumo.isChecked) {
                fanPicker.show()
            } else {
                com.blankj.utilcode.util.ToastUtils.showShort("请选点击自摸或荣和")
            }
//            存数据
            R.id.btn_confirm -> if (checkData()) {
                heNames.add(GameModle.getInstance().heName!!)
                saveData()
            }
        }
    }

    override fun initData() {
        vm = ViewModelProviders.of(this).get(SetScoreVM::class.java)
        handleRichiByDialog()
    }

    /**
     * 根据跳转之前的立直框，判定当前用户是否立直，并刷新页面
     */
    private fun handleRichiByDialog() {
        //        前面点击的时候确认过这里和的人是谁了，立直的弹框也将richi改变了，如果前面勾选了该人立直，此页面默认勾上

        val isRichi = when (ConvertHelper.getSeat(GameModle.getInstance().heName ?: "")) {
            EAST -> GameModle.getInstance().richi and 0b1000 == 0b1000
            SOUTH -> GameModle.getInstance().richi and 0b0100 == 0b0100
            WEST -> GameModle.getInstance().richi and 0b0010 == 0b0010
            NORTH -> GameModle.getInstance().richi and 0b0001 == 0b0001
            else -> false
        }
        mBinding.cbRichi.isChecked = isRichi
    }

    override fun initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_getscore)
        mBinding.listener = mListener
        setSupportActionBar(findViewById<View>(R.id.toolbar) as Toolbar)
        title = "当局数据"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun initEvent() {
        mBinding.rgHepai.setOnCheckedChangeListener { group, checkedId ->
            //            自摸的话，隐藏放铳项
            if (checkedId == R.id.rb_tsumo) {
                mBinding.tvChong.visibility = View.GONE
                GameModle.getInstance().isTsumo = true
                GameModle.getInstance().chong = null
            } else {
                mBinding.tvChong.visibility = View.VISIBLE
                GameModle.getInstance().isTsumo = false
            }
        }
    }

    private fun checkData(): Boolean {
        if (mBinding.rbRonn.isChecked && GameModle.getInstance().chong==null) {
            com.blankj.utilcode.util.ToastUtils.showShort("没有选择放铳人")
            return false
        } else if (TextUtils.isEmpty(mFan)) {
            com.blankj.utilcode.util.ToastUtils.showShort("没有选择番种")
            return false
        } else if (mBinding.etPoint.text.isEmpty()) {
            com.blankj.utilcode.util.ToastUtils.showShort("没有输入点数")
            return false
        }
        return true
    }

    private fun saveData() {
        val player = ConvertHelper.getPlayerByName(GameModle.getInstance().heName) ?: return
//        不单独设置和牌人的立直了，根据richi所有人统一设置
//        if (mBinding.cbRichi.isChecked) {  //立直
//            player.richiCount++
//        }
        player.heCount++
        if (mBinding.rbTsumo.isChecked) {  //自摸
            player.tsumo++
            if (mBinding.cbRichi.isChecked) {  //立直和了
                player.richiHe++
            }
        }
        if (mBinding.rbRonn.isChecked) {   //荣和
            player.ronn++
            if (mBinding.cbRichi.isChecked) {  //立直和了
                player.richiHe++
            }
        }
        if (mBinding.cbIhhatsu.isChecked) {    //一发
            player.ihhatsuCount++
        }

        if (mFan != null) {
            when (mFan) {
                Constant.MANN_GANN -> player.manguan++
                Constant.HANE_MANN -> player.tiaoman++
                Constant.BAI_MANN -> player.beiman++
                Constant.SANN_BAI_MANN -> player.sanbeiman++
                Constant.YAKUMAN -> player.yakuman++
                else -> player.noManguan++
            }
        }

//        point是真正的得点 加上供托 本场和 hePoint的结果
        val point = mBinding.etPoint.text.toString().toIntOrNull()
//        设置和点最大值 和点总值
        point?.run {
            if (player.hePointMax < point) {
                player.hePointMax = point
            }
            player.hePointSum += (point ?: 0)
            when (ConvertHelper.getSeat(GameModle.getInstance().heName ?: "")) {
                EAST -> GameModle.getInstance().east += point
                SOUTH -> GameModle.getInstance().south += point
                WEST -> GameModle.getInstance().west += point
                NORTH -> GameModle.getInstance().north += point
            }
        }

        val players = GameModle.getInstance().run { listOf<Player?>(eastPlayer, southPlayer, westPlayer, northPlayer) }
        //存储放铳人数据
        GameModle.getInstance().chong?.run {
            val chongPlayer = ConvertHelper.getPlayerByName(GameModle.getInstance().chong)
            if (chongPlayer != null) {
                chongPlayer.chongCount++
                chongPlayer.chongPointSum += GameModle.getInstance().hePoint
            }
//            放铳人不需要支付供托
            val chongPoint = GameModle.getInstance().hePoint + GameModle.getInstance().chang % 10 * 300
            when (this) {
                GameModle.getInstance().eastPlayer?.name -> GameModle.getInstance().east -= chongPoint
                GameModle.getInstance().southPlayer?.name -> GameModle.getInstance().south -= chongPoint
                GameModle.getInstance().westPlayer?.name -> GameModle.getInstance().west -= chongPoint
                GameModle.getInstance().northPlayer?.name -> GameModle.getInstance().north -= chongPoint
            }
        }


        //所有人的发牌次数都要+1
        GameModle.getInstance().eastPlayer?.totalDeal?.plus(1)
        GameModle.getInstance().southPlayer?.totalDeal?.plus(1)
        GameModle.getInstance().westPlayer?.totalDeal?.plus(1)
        GameModle.getInstance().northPlayer?.totalDeal?.plus(1)


        //和牌人 和 放铳人之前算过了，下面处理自摸的情况

        //亲家自摸，那么其他三家平分点数
        if (GameModle.getInstance().isTsumo == true) {
            if (ConvertHelper.isOya(GameModle.getInstance().heName ?: "")) {
                if (GameModle.getInstance().eastPlayer?.name != GameModle.getInstance().heName)
                    GameModle.getInstance().east -= (GameModle.getInstance().hePoint / 3)
                if (GameModle.getInstance().southPlayer?.name != GameModle.getInstance().heName)
                    GameModle.getInstance().south -= (GameModle.getInstance().hePoint / 3)
                if (GameModle.getInstance().westPlayer?.name != GameModle.getInstance().heName)
                    GameModle.getInstance().west -= (GameModle.getInstance().hePoint / 3)
                if (GameModle.getInstance().northPlayer?.name != GameModle.getInstance().heName)
                    GameModle.getInstance().north -= (GameModle.getInstance().hePoint / 3)
            } else {
                //子家自摸，亲家付二分之一，另2家各付四分之一。
                // 这里有点小问题，比如40符1番，荣和是1300，自摸1500是700/400，并不是按照公式的750/350---已解决
                val oyaName = ConvertHelper.getOyaPlayer()?.name
//                自摸需要被其他三家平分的点数
                val tsumoPoint = GameModle.getInstance().hePoint + ConvertHelper.getBenchang() * 300
                val pair = ScoreCalcHelper.getTsumoPointPair(tsumoPoint)
                if (GameModle.getInstance().eastPlayer?.name != GameModle.getInstance().heName) {
                    GameModle.getInstance().east -= if (GameModle.getInstance().eastPlayer?.name == oyaName) pair.first
                    else pair.second
                }
                if (GameModle.getInstance().southPlayer?.name != GameModle.getInstance().heName) {
                    GameModle.getInstance().south -= if (GameModle.getInstance().eastPlayer?.name == oyaName) pair.first
                    else pair.second
                }
                if (GameModle.getInstance().westPlayer?.name != GameModle.getInstance().heName) {
                    GameModle.getInstance().west -= if (GameModle.getInstance().eastPlayer?.name == oyaName) pair.first
                    else pair.second
                }
                if (GameModle.getInstance().northPlayer?.name != GameModle.getInstance().heName) {
                    GameModle.getInstance().north -= if (GameModle.getInstance().eastPlayer?.name == oyaName) pair.first
                    else pair.second
                }
            }
        }
//        存进数据库
        vm.saveJu()
        vm.savePlayer()
//        清除相应的数据
        GameModle.clearJuData()
        Toast.makeText(this, "该场数据保存成功", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.mComposite.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
//        用来记录和牌的人，当开始下一局的时候清空，用来判断下一场是连庄还是下庄的，庄家和就连庄
        val heNames = mutableListOf<String>()

        private val qinRonnMap = ArrayMap<String, Int>()
        private val qinTsumoMap = ArrayMap<String, Int>()
        private val ziRonnMap = ArrayMap<String, Int>()
        private val ziTsumoMap = ArrayMap<String, Int>()
        private val mFanList = ArrayList<String>()
        private val mFuList = ArrayList<List<String>>()  //满贯以下 满贯  跳满..
        private val mFanlist2 = ArrayList<List<List<String>>>()  //第三列数据，1翻 2翻 3翻 4翻

        init {
            initFanData()
            initQinRonnMap()
            initQinTsumoMap()
            initZiTsumoMap()
            initZiRonnMap()
        }

        val RC_RECORD_AUDIO = 1

        private fun initZiRonnMap() {
            ziRonnMap["30符1翻"] = 1000
            ziRonnMap["40符1翻"] = 1300
            ziRonnMap["50符1翻"] = 1600
            ziRonnMap["60符1翻"] = 2000
            ziRonnMap["70符1翻"] = 2300
            ziRonnMap["80符1翻"] = 2600
            ziRonnMap["90符1翻"] = 2900
            ziRonnMap["100符1翻"] = 3200
            ziRonnMap["110符1翻"] = 3600

            ziRonnMap["20符2翻"] = 1300
            ziRonnMap["25符2翻"] = 1600
            ziRonnMap["30符2翻"] = 2000
            ziRonnMap["40符2翻"] = 2600
            ziRonnMap["50符2翻"] = 3200
            ziRonnMap["60符2翻"] = 3900
            ziRonnMap["70符2翻"] = 4500
            ziRonnMap["80符2翻"] = 5200
            ziRonnMap["90符2翻"] = 5800
            ziRonnMap["100符2翻"] = 6400
            ziRonnMap["110符2翻"] = 7100

            ziRonnMap["20符3翻"] = 2600
            ziRonnMap["25符3翻"] = 3200
            ziRonnMap["30符3翻"] = 3900
            ziRonnMap["40符3翻"] = 5200
            ziRonnMap["50符3翻"] = 6400

            ziRonnMap["20符4翻"] = 5200
            ziRonnMap["25符4翻"] = 6400

            ziRonnMap["满贯"] = 8000
            ziRonnMap["跳满"] = 12000
            ziRonnMap["倍满"] = 16000
            ziRonnMap["三倍满"] = 24000
            ziRonnMap["役满"] = 32000
        }

        private fun initZiTsumoMap() {
            ziTsumoMap["30符1翻"] = 1100
            ziTsumoMap["40符1翻"] = 1500
            ziTsumoMap["50符1翻"] = 1600
            ziTsumoMap["60符1翻"] = 2000
            ziTsumoMap["70符1翻"] = 2400
            ziTsumoMap["80符1翻"] = 2700
            ziTsumoMap["90符1翻"] = 3100
            ziTsumoMap["100符1翻"] = 3200
            ziTsumoMap["110符1翻"] = 3600

            ziTsumoMap["20符2翻"] = 1500
            ziTsumoMap["30符2翻"] = 2000
            ziTsumoMap["40符2翻"] = 2700
            ziTsumoMap["50符2翻"] = 3200
            ziTsumoMap["60符2翻"] = 4000
            ziTsumoMap["70符2翻"] = 4700
            ziTsumoMap["80符2翻"] = 5200
            ziTsumoMap["90符2翻"] = 5900
            ziTsumoMap["100符2翻"] = 6400
            ziTsumoMap["110符2翻"] = 7200

            ziTsumoMap["20符3翻"] = 2700
            ziTsumoMap["25符3翻"] = 3200
            ziTsumoMap["30符3翻"] = 4000
            ziTsumoMap["40符3翻"] = 5200
            ziTsumoMap["50符3翻"] = 6400

            ziTsumoMap["20符4翻"] = 5200
            ziTsumoMap["25符4翻"] = 6400

            ziTsumoMap["满贯"] = 8000
            ziTsumoMap["跳满"] = 12000
            ziTsumoMap["倍满"] = 160
            ziTsumoMap["三倍满"] = 24000
            ziTsumoMap["役满"] = 32000
        }

        private fun initQinTsumoMap() {
            qinTsumoMap["30符1翻"] = 1500
            qinTsumoMap["40符1翻"] = 2100
            qinTsumoMap["50符1翻"] = 2400
            qinTsumoMap["60符1翻"] = 3000
            qinTsumoMap["70符1翻"] = 3600
            qinTsumoMap["80符1翻"] = 3900
            qinTsumoMap["90符1翻"] = 4500
            qinTsumoMap["100符1翻"] = 4800
            qinTsumoMap["110符1翻"] = 5400

            qinTsumoMap["20符2翻"] = 2100
            qinTsumoMap["30符2翻"] = 3000
            qinTsumoMap["40符2翻"] = 3900
            qinTsumoMap["50符2翻"] = 4800
            qinTsumoMap["60符2翻"] = 6000
            qinTsumoMap["70符2翻"] = 6900
            qinTsumoMap["80符2翻"] = 7800
            qinTsumoMap["90符2翻"] = 8700
            qinTsumoMap["100符2翻"] = 9600
            qinTsumoMap["110符2翻"] = 10800

            qinTsumoMap["20符3翻"] = 3900
            qinTsumoMap["25符3翻"] = 6000
            qinTsumoMap["30符3翻"] = 5800
            qinTsumoMap["40符3翻"] = 7800
            qinTsumoMap["50符3翻"] = 9600

            qinTsumoMap["20符4翻"] = 7800
            qinTsumoMap["25符4翻"] = 9600

            qinTsumoMap["满贯"] = 12000
            qinTsumoMap["跳满"] = 18000
            qinTsumoMap["倍满"] = 24000
            qinTsumoMap["三倍满"] = 36000
            qinTsumoMap["役满"] = 48000
        }

        private fun initQinRonnMap() {
            qinRonnMap["30符1翻"] = 1500
            qinRonnMap["40符1翻"] = 2000
            qinRonnMap["50符1翻"] = 2400
            qinRonnMap["60符1翻"] = 2900
            qinRonnMap["70符1翻"] = 3400
            qinRonnMap["80符1翻"] = 3900
            qinRonnMap["90符1翻"] = 4400
            qinRonnMap["100符1翻"] = 4800
            qinRonnMap["110符1翻"] = 5300

            qinRonnMap["20符2翻"] = 2000
            qinRonnMap["25符2翻"] = 2400
            qinRonnMap["30符2翻"] = 2900
            qinRonnMap["40符2翻"] = 3900
            qinRonnMap["50符2翻"] = 4800
            qinRonnMap["60符2翻"] = 5800
            qinRonnMap["70符2翻"] = 6800
            qinRonnMap["80符2翻"] = 7700
            qinRonnMap["90符2翻"] = 8700
            qinRonnMap["100符2翻"] = 9600
            qinRonnMap["110符2翻"] = 10600

            qinRonnMap["20符3翻"] = 3900
            qinRonnMap["25符3翻"] = 4800
            qinRonnMap["30符3翻"] = 5800
            qinRonnMap["40符3翻"] = 7700
            qinRonnMap["50符3翻"] = 9600

            qinRonnMap["20符4翻"] = 7700
            qinRonnMap["25符4翻"] = 9600

            qinRonnMap["满贯"] = 12000
            qinRonnMap["跳满"] = 18000
            qinRonnMap["倍满"] = 24000
            qinRonnMap["三倍满"] = 36000
            qinRonnMap["役满"] = 48000
        }

        private fun initFanData() {
            //第一列显示番数
            mFanList.add("满贯以下")
            mFanList.add("满贯")
            mFanList.add("跳满")
            mFanList.add("倍满")
            mFanList.add("三倍满")
            mFanList.add("役满")
            //第二列显示符，只有满贯以下才需要符，所以添加5个空集合
            val fuList = ArrayList<String>()
            fuList.add("20符")
            fuList.add("25符")
            fuList.add("30符")
            fuList.add("40符")
            fuList.add("50符")
            fuList.add("60符")
            fuList.add("70符")
            fuList.add("80符")
            fuList.add("90符")
            fuList.add("100符")
            fuList.add("110符")
            val emptyList = ArrayList<String>()
            emptyList.add("")
            mFuList.add(fuList)
            mFuList.add(emptyList)
            mFuList.add(emptyList)
            mFuList.add(emptyList)
            mFuList.add(emptyList)
            mFuList.add(emptyList)
            //第三列显示满贯以下的哪一种
            val fu20_25fan = ArrayList<String>()
            val fu30_40_50fan = ArrayList<String>()
            val fu60_110fan = ArrayList<String>()
            val fan2 = ArrayList<List<String>>()
            val fan2Empty = ArrayList<List<String>>()
            fan2Empty.add(emptyList)

            fu20_25fan.add("2翻")
            fu20_25fan.add("3翻")
            fu20_25fan.add("4翻")

            fu30_40_50fan.add("1翻")
            fu30_40_50fan.add("2翻")
            fu30_40_50fan.add("3翻")

            fu60_110fan.add("1翻")
            fu60_110fan.add("2翻")

            fan2.add(fu20_25fan)   //20符和25符只有2 3 4翻
            fan2.add(fu20_25fan)
            fan2.add(fu30_40_50fan) //30 40 50符只有1 2 3翻
            fan2.add(fu30_40_50fan)
            fan2.add(fu30_40_50fan)
            fan2.add(fu60_110fan)   //60~110符 只有1 2翻
            fan2.add(fu60_110fan)
            fan2.add(fu60_110fan)
            fan2.add(fu60_110fan)
            fan2.add(fu60_110fan)
            fan2.add(fu60_110fan)
            mFanlist2.add(fan2)
            mFanlist2.add(fan2Empty)
            mFanlist2.add(fan2Empty)
            mFanlist2.add(fan2Empty)
            mFanlist2.add(fan2Empty)
            mFanlist2.add(fan2Empty)
        }
    }
}
