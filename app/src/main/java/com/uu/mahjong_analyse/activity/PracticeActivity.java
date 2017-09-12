package com.uu.mahjong_analyse.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.databinding.ActivityPracticeBinding;
import com.uu.mahjong_analyse.vm.PracticeVM;

public class PracticeActivity extends BaseActivity {
    private ActivityPracticeBinding mBinding;
    private PracticeVM practiceVM;

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_practice);
        practiceVM = ViewModelProviders.of(this).get(PracticeVM.class);
        mBinding.setVm(practiceVM);
        mBinding.myHai.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    public void initData() {
        practiceVM.init();
    }

    @Override
    public void initEvent() {

    }

}
