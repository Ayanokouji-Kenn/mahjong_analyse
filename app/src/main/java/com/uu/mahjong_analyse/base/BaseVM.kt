package com.uu.mahjong_analyse.base

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.uu.mahjong_analyse.data.local.*
import io.reactivex.disposables.CompositeDisposable

/**
 * @description
 * @auther xuzijian
 * @date 2017/9/12 14:57.
 */

open class BaseVM(@field:SuppressLint("StaticFieldLeak")
                  protected val mApp: Application) : AndroidViewModel(mApp){
    val playerRepository = PlayerRepository.getInstance(PlayerDataSourceImpl.getInstance(MajongDatabase.getInstance(mApp).playerDao()))
    val gameInfoRepository = TempGameInfoRepository.getInstance(TempGameInfoDataSourceImpl.getInstance(MajongDatabase.getInstance(mApp).tempGameInfoDao()))
    val mComposite=CompositeDisposable()
}
