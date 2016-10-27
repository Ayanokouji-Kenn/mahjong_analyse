package com.uu.mahjong_analyse.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.MyBaseAdapter;

import java.util.List;

/**
 * @auther Nagisa.
 * @date 2016/7/3.
 */
public class LeftMenuAdapter extends MyBaseAdapter<String> {
    public LeftMenuAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //leftmenu这里最多只有4个玩家，所以不需要复用convertview了
        convertView = View.inflate(mContext, R.layout.item_leftmenu, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_leftmenu);
        tv.setText(mDatas.get(position));
        return convertView;
    }
}
