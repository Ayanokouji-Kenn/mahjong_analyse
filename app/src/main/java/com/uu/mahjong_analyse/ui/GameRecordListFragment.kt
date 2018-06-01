package com.uu.mahjong_analyse.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.base.BaseFragment
import com.uu.mahjong_analyse.data.entity.GameRecord
import com.uu.mahjong_analyse.utils.setupActionBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_game_record_list.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import me.yokeyword.fragmentation.SupportFragment

class GameRecordListFragment:BaseFragment() {
    lateinit var vm:GameRecordListVM
    override fun onCreate(savedInstanceState: Bundle?) {
        vm = ViewModelProviders.of(this).get(GameRecordListVM::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_record_list,container,false)
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.run {
            setTitle("对局记录")
            setHasOptionsMenu(true)
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { pop() }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        initToolbar()
        vm.gameRecordList.observe(this, Observer{
            rv.adapter.notifyDataSetChanged()
        })
    }


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = object:BaseQuickAdapter<GameRecord,BaseViewHolder>(R.layout.item_gamerecord,vm.gameRecordList.value){
            override fun convert(helper: BaseViewHolder, item: GameRecord?) {
                helper.setText(R.id.tv_date,item?.date)
                        .setText(R.id.tv_top,item?.top)
                        .setText(R.id.tv_second,item?.second)
                        .setText(R.id.tv_third,item?.third)
                        .setText(R.id.tv_last,item?.last)
            }
        }
        vm.getGameRecordList()
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        vm.gameRecordList.removeObservers(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
//        super.onCreateOptionsMenu(menu, inflater)
    }
    companion object {
        fun newInstance() = GameRecordListFragment()
    }

}