package com.uu.mahjong_analyse.base

import android.support.v4.app.Fragment

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/09
 *     desc  :
 * </pre>
 */


open class BaseFragment : Fragment() {


    override fun onResume() {
        super.onResume()
        onFragShow()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!isHidden) onFragShow()
    }

    open fun onFragShow(){
    }
    companion object {
        fun getInstance() = BaseFragment()
    }
}