package com.uu.mahjong_analyse.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.adapter.PlayerScoreAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nagisa on 2016/7/1.
 */
public class SetPlayerDataDialog extends Dialog {
    @BindView(R.id.recyclerview_zi)
    RecyclerView mRecyclerviewZi;
    @BindView(R.id.recyclerview_qin)
    RecyclerView mRecyclerviewQin;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    int[] zi_score= {};

    public SetPlayerDataDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initData() {
        mRecyclerviewZi.setLayoutManager(new StaggeredGridLayoutManager(11,StaggeredGridLayoutManager.HORIZONTAL));
        PlayerScoreAdapter adapter = new PlayerScoreAdapter(getContext(), zi_score);
        mRecyclerviewZi.setAdapter(adapter);
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.dialog_set_player_data, null);
        setContentView(view);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_confirm)
    public void onClick() {
    }


}
