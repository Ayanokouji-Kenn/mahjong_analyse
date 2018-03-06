package com.uu.mahjong_analyse.utils

import com.uu.mahjong_analyse.data.ChangEnum
import com.uu.mahjong_analyse.ui.MainVM

/**
 * <pre>
 *     author: xzj
 *     time  : 2018/01/08
 *     desc  :
 * </pre>
 */


object BindingParseUtils {
    @JvmStatic
    fun parseBenchang(mainVM: MainVM):String = """${ChangEnum.values().get(mainVM.chang.get()).desc}
        |${mainVM.benchang.get()}本场""".trimMargin()
}