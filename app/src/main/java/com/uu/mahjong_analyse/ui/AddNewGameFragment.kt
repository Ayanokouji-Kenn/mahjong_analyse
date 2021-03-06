package com.uu.mahjong_analyse.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import com.bigkoo.pickerview.OptionsPickerView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseFragment
import com.uu.mahjong_analyse.data.GameModle
import com.uu.mahjong_analyse.data.entity.Player
import com.uu.mahjong_analyse.databinding.FragAddNewGameBinding
import me.yokeyword.fragmentation.ISupportFragment

/**
 * @description  由[MainActivity]的fab跳转，新开一局的选人界面
 * @author xuzijian
 * @date 2018/1/9 9:47
 */
class AddNewGameFragment : BaseFragment() {
    private lateinit var mBinding: FragAddNewGameBinding
    private lateinit var vm: AddNewGameVM
    private val selectPlayerPicker: OptionsPickerView<Player> by lazy {
        val builder = OptionsPickerView.Builder(activity, OptionsPickerView.OnOptionsSelectListener { options1, _, _, v ->
            val selectPlayer = vm.players[options1]
            when (selectTvId) {
                R.id.tv_east -> {
                    mBinding.tvEast.text = selectPlayer.name
                    GameModle.getInstance().eastPlayer = selectPlayer
                }
                R.id.tv_south -> {
                    mBinding.tvSouth.text = selectPlayer.name
                    GameModle.getInstance().southPlayer = selectPlayer
                }
                R.id.tv_west -> {
                    mBinding.tvWest.text = selectPlayer.name
                    GameModle.getInstance().westPlayer = selectPlayer
                }
                R.id.tv_north -> {
                    mBinding.tvNorth.text = selectPlayer.name
                    GameModle.getInstance().northPlayer = selectPlayer
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
            if (GameModle.getInstance().eastPlayer == null
                    || GameModle.getInstance().westPlayer == null
                    || GameModle.getInstance().southPlayer == null
                    || GameModle.getInstance().northPlayer == null
            ) {
                ToastUtils.showShort(getString(R.string.players_num_must_be_4))
                return@OnClickListener
            }else {
                setFragmentResult(ISupportFragment.RESULT_OK, Bundle.EMPTY)
                pop()
            }
        } else if (id == R.id.tv_east
                || id == R.id.tv_south
                || id == R.id.tv_west
                || id == R.id.tv_north) {
            selectTvId = id
            if (vm.players.isEmpty()) {
                ToastUtils.showShort("无数据，请在下方输入玩家姓名")
            } else {
                selectPlayerPicker.setPicker(vm.players)
                selectPlayerPicker.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(AddNewGameVM::class.java)
    }

    private var selectTvId = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragAddNewGameBinding.inflate(inflater, container, false).apply {
            listener = mListener
        }
        vm.getPlayers()
        setHasOptionsMenu(true)
        registEditorListener()
        return mBinding.root
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        with((activity as MainActivity).supportActionBar) {
            this?.title = resources.getString(R.string.choose_player)
            this?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun registEditorListener() {
        mBinding.et.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardUtils.hideSoftInput(v)
                val name = v.text.toString().trim()
                if (!TextUtils.isEmpty(name)) {
                    vm.addPlayer(name)
                    v.text = ""
                } else {
                    mBinding.textInputLayout.error = "小伙伴没名字？"
                }
                true
            } else false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.mComposite.dispose()
    }


    companion object {
        fun getInstance() = AddNewGameFragment()
    }
}
