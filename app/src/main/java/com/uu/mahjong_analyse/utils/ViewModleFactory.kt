package com.uu.mahjong_analyse.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class ViewModleFactory(app: Application) :ViewModelProvider.AndroidViewModelFactory(app){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return super.create(modelClass)
    }
}