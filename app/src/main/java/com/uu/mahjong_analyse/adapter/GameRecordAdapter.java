package com.uu.mahjong_analyse.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.MyBaseAdapter;
import com.uu.mahjong_analyse.bean.GameRecord;

import java.util.List;
/**
 * @auther Nagisa.
 * @date 2016/7/20.
 */
public class GameRecordAdapter extends MyBaseAdapter<GameRecord> {


    public GameRecordAdapter(Context context, List<GameRecord> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gamerecord, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(position);
        return convertView;
    }



    class ViewHolder {
        TextView mTvTop;
        TextView mTvSecond;
        TextView mTvThird;
        TextView mTvLast;
        TextView mTvDate;

        ViewHolder(View view) {
            mTvTop = (TextView) view.findViewById(R.id.tv_top);
            mTvSecond = (TextView) view.findViewById(R.id.tv_second);
            mTvThird = (TextView) view.findViewById(R.id.tv_third);
            mTvLast = (TextView) view.findViewById(R.id.tv_last);
            mTvDate = (TextView) view.findViewById(R.id.tv_date);
        }

        public void setData(int position) {
            mTvDate.setText(mDatas.get(position).date);
            mTvTop.setText(mDatas.get(position).top);
            mTvSecond.setText(mDatas.get(position).second);
            mTvThird.setText(mDatas.get(position).third);
            mTvLast.setText(mDatas.get(position).last);
        }
    }
}
