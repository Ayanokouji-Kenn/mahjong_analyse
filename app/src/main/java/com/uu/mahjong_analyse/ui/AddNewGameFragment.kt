package com.uu.mahjong_analyse.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import com.bigkoo.pickerview.OptionsPickerView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseFragment
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.databinding.FragAddNewGameBinding
import com.uu.mahjong_analyse.utils.Constant
import com.uu.mahjong_analyse.utils.SPUtils
import com.uu.mahjong_analyse.utils.getVM

/**
 * @description  有[MainActivity]的fab跳转
 * @author xuzijian
 * @date 2018/1/9 9:47
 */
class AddNewGameFragment : BaseFragment() {
    private lateinit var mBinding: FragAddNewGameBinding
    private val selectPlayerDialog: OptionsPickerView<Player> by lazy {
        val builder = OptionsPickerView.Builder(activity, OptionsPickerView.OnOptionsSelectListener { options1, _, _, v ->
            val selectPlayer = mBinding.vm?.totalPlayList!![options1]
            when (selectTvId) {
                R.id.tv_east -> {
                    mBinding.vm?.players!![0] = selectPlayer
                    SPUtils.putString(Constant.EAST, selectPlayer.name)
                }
                R.id.tv_south -> {
                    mBinding.vm?.players!![1] = selectPlayer
                    SPUtils.putString(Constant.SOUTH, selectPlayer.name)
                }
                R.id.tv_west -> {
                    mBinding.vm?.players!![2] = selectPlayer
                    SPUtils.putString(Constant.WEST, selectPlayer.name)
                }
                R.id.tv_north -> {
                    mBinding.vm?.players!![3] = selectPlayer
                    SPUtils.putString(Constant.NORTH, selectPlayer.name)
                }
            }
        })
                .setCancelColor(resources.getColor(R.color.colorAccent))
                .setSubmitColor(resources.getColor(R.color.colorAccent))
        OptionsPickerView<Player>(builder)
    }
    private val mListener: View.OnClickListener = View.OnClickListener { v ->
        val id = v.id
        if (id == R.id.tv_ok) {
            for (player in mBinding.vm?.players!!) {
                if (player == null) {
                    ToastUtils.showShort(getString(R.string.players_num_must_be_4))
                    return@OnClickListener
                }
            }
            fragmentManager.popBackStack()
        } else if (id == R.id.tv_east
                || id == R.id.tv_south
                || id == R.id.tv_west
                || id == R.id.tv_north) {
            selectTvId = id
//            mBinding.vm?.getPlayers {
                if (mBinding.vm?.totalPlayList?.isNotEmpty() == true) {
                    selectPlayerDialog.setPicker(mBinding.vm?.totalPlayList)
                    selectPlayerDialog.show()
                }
//            }
        }
    }

    var selectTvId = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragAddNewGameBinding.inflate(inflater, container, false).apply {
            listener = mListener
            vm = (activity as MainActivity).getVM(MainVM::class.java)
        }
        mBinding.vm?.getPlayers()
        setHasOptionsMenu(true)
        registEditorListener()
        return mBinding.root
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        with((activity as MainActivity).supportActionBar) {
            this?.title = resources.getString(R.string.choose_player)
//                this?.setDisplayHomeAsUpEnabled(true)
        }
    }


    fun registEditorListener() {
        mBinding.et.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                LogUtils.d(v.text.toString())
                KeyboardUtils.hideSoftInput(v)
                val name = v.text.toString().trim()
                if (!TextUtils.isEmpty(name)) {
                    mBinding.vm?.insertPlayer(name)
                    v.text = ""
                } else {
                    mBinding.textInputLayout.error = "小伙伴没名字？"
                }
                true
            } else false
        }
    }

    fun showDialog() {
        if (mBinding.vm?.totalPlayList?.isNotEmpty() == true) {
            selectPlayerDialog.setPicker(mBinding.vm?.totalPlayList)
            selectPlayerDialog.show()
        } else {
            ToastUtils.showShort(getString(R.string.please_add_players_first))
        }
    }

    override fun onFragShow() {
        mBinding.vm?.fgShow?.set(MainActivity.TAG_FG_ADD_NEW_GAME)
    }

    companion object {
        fun getInstance() = AddNewGameFragment()
    }
}
