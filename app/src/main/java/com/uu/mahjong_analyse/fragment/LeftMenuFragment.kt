package com.uu.mahjong_analyse.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

import com.uu.mahjong_analyse.R
import com.uu.mahjong_analyse.ui.MainActivity
import com.uu.mahjong_analyse.ui.PlayerInfoActivity
import com.uu.mahjong_analyse.adapter.LeftMenuAdapter
import com.uu.mahjong_analyse.base.BaseFragment

import java.util.ArrayList

/**
 * @auther Nagisa.
 * @date 2016/7/3.
 */
class LeftMenuFragment : BaseFragment() {

    val mLeftmenuDatas= mutableListOf<String>()
    lateinit var mLeftMenuAdapter: LeftMenuAdapter
    private var mLeftmenuListview: ListView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.layout_leftmenu, container, false)
        mLeftmenuListview = view?.findViewById<View>(R.id.leftmenu_listview) as ListView
        initLeftMenu()
        return view
    }

    private fun initLeftMenu() {
        mLeftmenuDatas.add("东家")
        mLeftmenuDatas.add("西家")
        mLeftmenuDatas.add("南家")
        mLeftmenuDatas.add("北家")
        mLeftMenuAdapter = LeftMenuAdapter(activity, mLeftmenuDatas)
        mLeftmenuListview!!.adapter = mLeftMenuAdapter
        mLeftmenuListview!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            (activity as MainActivity).closeDrawerLayout()
            if (mLeftmenuDatas[position].length < 3) {                                     //设置玩家后，会在东家后拼上用户名字，所以长度不可能小于3
                Toast.makeText(context, "尚未设置玩家", Toast.LENGTH_SHORT).show()
                return@OnItemClickListener

            }
            val intent = Intent(activity, PlayerInfoActivity::class.java)
            intent.putExtra("player", mLeftmenuDatas[position])
            (activity as MainActivity).openPage(true, -1, intent)
        }
    }
    companion object {
        fun getInstance() = LeftMenuFragment()
    }
}
