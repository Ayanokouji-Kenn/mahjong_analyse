package com.uu.mahjong_analyse.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseActivity;
import com.uu.mahjong_analyse.databinding.ActivityPracticeBinding;
import com.uu.mahjong_analyse.utils.HaiUtils;
import com.uu.mahjong_analyse.vm.PracticeVM;

public class PracticeActivity extends BaseActivity {
    private ActivityPracticeBinding mBinding;
    private PracticeVM practiceVM;
    private int checkHaiPosition = -1;
    private BaseQuickAdapter<Integer, BaseViewHolder> adapter;

    @Override
    public void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_practice);
        practiceVM = ViewModelProviders.of(this).get(PracticeVM.class);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mBinding.myHai.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.item_my_hais, practiceVM.player1) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                ImageView iv = helper.getView(R.id.iv_hai);
                HaiUtils.trans(iv, item);

            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (checkHaiPosition == position) {
                    practiceVM.dapai(position, adapter);
                    checkHaiPosition = -1;

                    practiceVM.getHai(practiceVM.player1, practiceVM.haiHills);
                    adapter.notifyDataSetChanged();
                } else {
                    view.setTranslationY(-30F);
                    if (checkHaiPosition != -1) {
                        mBinding.myHai.getLayoutManager().findViewByPosition(checkHaiPosition).setTranslationY(0);
                    }
                    checkHaiPosition = position;
                }
            }
        });
        mBinding.myHai.setAdapter(adapter);
    }

    @Override
    public void initData() {
        practiceVM.init();
        adapter.notifyItemInserted(0);
    }

    @Override
    public void initEvent() {
    }

}
