package com.uu.mahjong_analyse.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.adapter.PlayerScoreAdapter;

/**
 * Created by Nagisa on 2016/7/1.
 */
public class SetPlayerDataDialog extends Dialog {
    RecyclerView mRecyclerviewZi;
    RecyclerView mRecyclerviewQin;
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
        mRecyclerviewZi = (RecyclerView) view.findViewById(R.id.recyclerview_zi);
        mRecyclerviewQin = (RecyclerView) view.findViewById(R.id.recyclerview_qin);
        mBtnConfirm = (Button) view.findViewById(R.id.btn_confirm);
    }

}
