package com.uu.mahjong_analyse.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentValues
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.annotation.IdRes
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.ArrayMap
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast

import com.bigkoo.pickerview.OptionsPickerView
import com.iflytek.cloud.ui.RecognizerDialog
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseActivity
import com.uu.mahjong_analyse.bean.PlayerRecord
import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.databinding.ActivityGetscoreBinding
import com.uu.mahjong_analyse.db.DBDao
import com.uu.mahjong_analyse.utils.Constant
import com.uu.mahjong_analyse.utils.SPUtils

import java.util.ArrayList

/**
 * 设置每局和了的数据  一炮双响的话，供托头跳
 * Created by Nagisa on 2016/7/1.
 */
class SetScoreActivity : BaseActivity() {

    private var mPlayers: MutableList<String>? = null
    private var mChongPlayer: String? = null
    private var mPlayer: String? = null
    private var mFan: String? = null
    private var mPoint_int: Int = 0
    private var oyaName: String? = null
    private var gong: Int = 0
    private var benchang: Int = 0
    private var isOya: Boolean = false

    private lateinit var mBinding: ActivityGetscoreBinding
    private lateinit var vm:SetScoreVM
    private val chongPicker : OptionsPickerView<Player> by lazy {
        val builder = OptionsPickerView.Builder(this,{options1, options2, options3, v ->
            GameModle.getInstance().chong = vm.players[options1]?.name
            mBinding.tvChong.text = GameModle.getInstance().chong
        })
        OptionsPickerView<Player>(builder).apply { setPicker(GameModle.getInstance().run {
            listOf(eastPlayer,southPlayer,westPlayer,northPlayer)
        }) }
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
                showFanDialog()
            } else {
                com.blankj.utilcode.util.ToastUtils.showShort("请选点击自摸或荣和")
            }
//            存数据
            R.id.btn_confirm -> if (checkData()) {
//                saveData()
            }
        }
    }

    override fun initData() {
        vm = ViewModelProviders.of(this).get(SetScoreVM::class.java)
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
            if (checkedId == R.id.rb_tsumo) {
                mBinding.tvChong.visibility = View.GONE
                mChongPlayer = ""
            } else {
                mBinding.tvChong.visibility = View.VISIBLE
            }
        }
        //        initVoiceDialog();
    }

    private fun checkData(): Boolean {
        if (mBinding.rbRonn.isChecked && TextUtils.isEmpty(mChongPlayer)) {
            com.blankj.utilcode.util.ToastUtils.showShort("没有选择放铳人")
            return false
        } else if (TextUtils.isEmpty(mFan)) {
            com.blankj.utilcode.util.ToastUtils.showShort("没有选择番种")
            return false
        } else if (mBinding.etPoint.text.length == 0) {
            com.blankj.utilcode.util.ToastUtils.showShort("没有输入点数")
            return false
        }
        return true
    }

    private fun showFanDialog() {

        val optionsPickerView = OptionsPickerView.Builder(mContext, OptionsPickerView.OnOptionsSelectListener { options1, options2, options3, v ->
            val key: String
            //满贯以下
            if (options1 == 0) {
                key = mFuList[options1][options2] + mFanlist2[options1][options2][options3] //这里取出来的应该是  110符1翻 这种字符串
            } else {
                key = mFanList[options1]
            }
            if (TextUtils.equals("25符2翻", key) && mBinding.rbTsumo.isChecked) {
                com.blankj.utilcode.util.ToastUtils.showShort("2翻的七对子不可能自摸哦~")
                return@OnOptionsSelectListener
            }
            if (isOya) {
                if (mBinding.rbTsumo.isChecked) {
                    mPoint_int = qinTsumoMap[key]!!
                } else if (mBinding.rbRonn.isChecked) {
                    mPoint_int = qinRonnMap[key]!!
                }
            } else {
                if (mBinding.rbTsumo.isChecked) {

                    mPoint_int = ziTsumoMap[key]!!
                } else if (mBinding.rbRonn.isChecked) {
                    mPoint_int = ziRonnMap[key]!!
                }
            }

            GameModle.getInstance().hePoint = mPoint_int
            setFan(key)
            mBinding.etPoint.setText(mPoint_int.toString())
        })
                .setSubmitColor(resources.getColor(R.color.colorAccent))
                .setCancelColor(resources.getColor(R.color.colorAccent))
                .build()
        optionsPickerView.setPicker(mFanList, mFuList, mFanlist2)
        optionsPickerView.show()

        //
        //        final WheelViewDialog<String> dialog = new WheelViewDialog<>(this);
        //        dialog.setTitle("选择番数").setItems(fan).setButtonText("确定").setDialogStyle(Color
        //                .parseColor("#fc97a9")).setCount(5).show();
        //        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener<String>() {
        //            @Override
        //            public void onItemClick(int position, String s) {
        //                setFan(s);
        //            }
        //        });

    }


    private fun setFan(s: String) {
        mFan = s
        mBinding.tvFan.text = "番数：$mFan"
    }


//    private fun saveData() {
//        val playerRecord = DBDao.selectPlayer(mPlayer)
//        val cv = ContentValues()
//        if (mBinding.cbRichi.isChecked) {  //立直
//            cv.put("richi_count", playerRecord.richi_count + 1)
//        }
//        if (mBinding.rbTsumo.isChecked) {  //自摸
//            cv.put("tsumo", playerRecord.tsumo + 1)
//            if (mBinding.cbRichi.isChecked) {  //立直和了
//                cv.put("richi_he", playerRecord.richi_he + 1)
//            }
//            cv.put("he_count", playerRecord.he_count + 1)
//        }
//        if (mBinding.rbRonn.isChecked) {   //荣和
//            cv.put("ronn", playerRecord.ronn + 1)
//            if (mBinding.cbRichi.isChecked) {  //立直和了
//                cv.put("richi_he", playerRecord.richi_he + 1)
//            }
//            cv.put("he_count", playerRecord.he_count + 1)
//        }
//        if (mBinding.cbIhhatsu.isChecked) {    //一发
//            cv.put("ihhatsu_count", playerRecord.ihhatsu_count + 1)
//        }
//
//        if (mFan != null) {
//            //            no_manguan integer" +          //满贯以下
//            //            ",manguan integer" +             //满贯
//            //                    ",tiaoman integer" +             //跳满
//            //                    ",beiman integer" +              //倍满
//            //                    ",sanbeiman integer" +           //三倍满
//            //                    ",yakuman integer" +
//            when (mFan) {
//                "满贯以下" -> cv.put("no_manguan", playerRecord.no_manguan + 1)
//                "满贯" -> cv.put("manguan", playerRecord.manguan + 1)
//                "跳满" -> cv.put("tiaoman", playerRecord.tiaoman + 1)
//                "倍满" -> cv.put("beiman", playerRecord.beiman + 1)
//                "三倍满" -> cv.put("sanbeiman", playerRecord.sanbeiman + 1)
//
//                "役满" -> cv.put("yakuman", playerRecord.yakuman + 1)
//            }
//        }
//
//        val point = mBinding.etPoint.text.toString().trim { it <= ' ' }
//        if (!TextUtils.isEmpty(point)) {
//            mPoint_int = Integer.parseInt(point)
//            //            和牌最大值要加上供托的
//            if (playerRecord.he_point_max < mPoint_int + gong + benchang * 300) {
//                cv.put("he_point_max", mPoint_int + gong + benchang * 300)
//            }
//
//            cv.put("he_point_sum", playerRecord.he_point_sum + mPoint_int + gong + benchang * 300)
//        }
//
//        DBDao.updatePlayerData(mPlayer, cv)
//
//
//        if (!TextUtils.isEmpty(mChongPlayer)) {
//            //存储放铳人数据
//            val chongPlayer = DBDao.selectPlayer(mChongPlayer)
//            val cv_chong = ContentValues()
//            cv_chong.put("chong_count", chongPlayer.chong_count + 1)
//            cv_chong.put("chong_point_sum", chongPlayer.chong_point_sum + mPoint_int)
//            DBDao.updatePlayerData(mChongPlayer, cv_chong)
//        }
//
//        //当有人自摸或者荣和的话，所有人的发牌次数都要+1
//
//        if (mBinding.rbTsumo.isChecked || mBinding.rbRonn.isChecked) {
//            for (player in mPlayers) {
//                val playerRecord1 = DBDao.selectPlayer(player)
//                val cv1 = ContentValues()
//                cv1.put("total_deal", playerRecord1.total_deal + 1)
//                DBDao.updatePlayerData(player, cv1)
//            }
//        }
//
//        //改变点数
//        //和牌人点数
//        SPUtils.putInt(mPlayer, SPUtils.getInt(mPlayer, Integer.MIN_VALUE) + mPoint_int + gong + benchang * 300)
//        //如果放铳的话，放铳人点数变化
//        if (mBinding.rbRonn.isChecked && !TextUtils.isEmpty(mChongPlayer)) {
//            SPUtils.putInt(mChongPlayer, SPUtils.getInt(mChongPlayer, Integer.MIN_VALUE) - mPoint_int - benchang * 300)
//        } else {
//            //亲家自摸，那么其他三家平分点数
//            if (TextUtils.equals(oyaName, mPlayer)) {
//                for (player in mPlayers) {
//                    if (!TextUtils.equals(player, mPlayer)) {
//                        SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - mPoint_int / 3 - benchang * 100)
//                    }
//                }
//            } else {
//                //子家自摸，亲家付二分之一，另2家各付四分之一。
//                // 这里有点小问题，比如40符1番，荣和是1300，自摸1500是700/400，并不是按照公式的750/350---已解决
//                for (player in mPlayers) {
//                    if (!TextUtils.equals(player, mPlayer)) {
//                        when (mPoint_int) {
//                            1100  //30符1翻
//                            -> setZiJiaTsumo(player, 500, 300)
//                            1500  //40符1翻  20符2翻
//                            -> setZiJiaTsumo(player, 700, 400)
//                            2700  //80符1翻  40符2翻  20符3翻
//                            -> setZiJiaTsumo(player, 1300, 700)
//                            3100  //90符1翻
//                            -> setZiJiaTsumo(player, 1500, 800)
//                            4700  //70符2翻
//                            -> setZiJiaTsumo(player, 2300, 1200)
//                            5900  //90符2翻
//                            -> setZiJiaTsumo(player, 2900, 1500)
//                            else -> setZiJiaTsumo(player, mPoint_int / 2, mPoint_int / 4)
//                        }
//                    }
//                }
//            }
//        }
//        Toast.makeText(this, "该场数据保存成功", Toast.LENGTH_SHORT).show()
//        setResult(Activity.RESULT_OK)
//        finish()
//    }

    private fun setZiJiaTsumo(player: String, oyaPoint: Int, ziPoint: Int) {
        if (TextUtils.equals(oyaName, player)) {
            SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - oyaPoint - benchang * 100)
        } else {
            SPUtils.putInt(player, SPUtils.getInt(player, Integer.MIN_VALUE) - ziPoint - benchang * 100)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
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
