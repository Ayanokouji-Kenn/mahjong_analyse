package com.uu.mahjong_analyse.base;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @auther Nagisa.
 * @date 2016/7/3.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    public Context mContext;
    public List<T> mDatas;
    public MyBaseAdapter(Context context,List<T> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
