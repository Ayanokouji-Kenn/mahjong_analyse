package com.uu.mahjong_analyse.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.MyBaseAdapter;

import java.util.List;

/**
 * @auther xuzijian
 * @date 2017/7/6 10:52.
 */

public class ScanPointAdapter extends MyBaseAdapter<CharSequence> {


    public ScanPointAdapter(Context context, List<CharSequence> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_scan_point, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_item_scan_point);
        tv.setText(mDatas.get(position));
        if (position < 9 || (position >= 13 && (position - 4) % 9 == 0 )) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.pink_blue));
        }else {
            tv.setBackground(null);
        }
        return convertView;
    }
}
