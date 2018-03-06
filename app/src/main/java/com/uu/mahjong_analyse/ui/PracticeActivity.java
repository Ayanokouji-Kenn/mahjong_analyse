package com.uu.mahjong_analyse.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SnackbarUtils;
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
        final BaseQuickAdapter riverAdapter = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.item_my_hais, practiceVM.riverList) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                ImageView iv = helper.getView(R.id.iv_hai);
                HaiUtils.trans(iv, item);
            }
        };
        mBinding.riverHai.setLayoutManager(new GridLayoutManager(this,6));
        mBinding.riverHai.setAdapter(riverAdapter);



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
            public void onItemClick(final BaseQuickAdapter adapter, View view, int position) {
                if (checkHaiPosition == position) {
                    practiceVM.addToRiver(practiceVM.player1.get(position));
                    riverAdapter.notifyItemInserted(practiceVM.riverList.size());
                    mBinding.riverHai.smoothScrollToPosition(practiceVM.riverList.size()-1);

                    mBinding.myHai.getLayoutManager().findViewByPosition(checkHaiPosition).setTranslationY(0);
                    practiceVM.dapai(position);
                    practiceVM.getHai(practiceVM.player1, practiceVM.haiHills);
                    adapter.notifyDataSetChanged();
                    checkHaiPosition = -1;
                    if (HaiUtils.checkHu(practiceVM.player1)) {
                        SnackbarUtils.with(mBinding.myHai).setMessage("嗯..似乎和了")
                                .setDuration(SnackbarUtils.LENGTH_INDEFINITE)
                                .setAction("再来一局?", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        practiceVM.reset();
                                        riverAdapter.notifyDataSetChanged();
                                        adapter.notifyDataSetChanged();
                                    }
                                }).show();
                    }
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
