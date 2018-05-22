package com.uu.mahjong_analyse.base

import android.support.v4.app.Fragment
import me.yokeyword.fragmentation.SupportFragment

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/09
 *     desc  :
 * </pre>
 */


open class BaseFragment : SupportFragment() {

    companion object {
        fun getInstance() = BaseFragment()
    }
}