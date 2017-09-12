package com.uu.mahjong_analyse.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

/**
 * @description
 * @auther xuzijian
 * @date 2017/9/12 14:57.
 */

public class BaseVM extends AndroidViewModel {
    protected Application mApp;
    public BaseVM(Application application) {
        super(application);
        mApp= application;
    }
}
