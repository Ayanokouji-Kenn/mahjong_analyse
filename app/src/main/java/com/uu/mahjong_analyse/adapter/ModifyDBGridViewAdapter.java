package com.uu.mahjong_analyse.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uu.mahjong_analyse.base.MyBaseAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * @auther xuzijian
 * @date 2017/6/7 15:40.
 */

public class ModifyDBGridViewAdapter extends MyBaseAdapter<String> {
    private static final int TYPE_COLUMN= 1;
    private static final int TYPE_DATA= 2;
    private String[] colomnNames;
    public ModifyDBGridViewAdapter(Context context, List<String> datas, String[] columnNames) {
        super(context, datas);
        this.colomnNames = columnNames;
        mDatas.addAll(0, Arrays.asList(columnNames));
    }



    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position<colomnNames.length?TYPE_COLUMN:TYPE_DATA;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
//            convertView = new TextView(mContext);
//            ((TextView)convertView).setText(mDatas.get(position));
            convertView = new TextView(mContext);
            if (getItemViewType(position) == TYPE_COLUMN) {
                ((TextView)convertView).setText(colomnNames[position]);
            }else {
                ((TextView)convertView).setText(mDatas.get(position));
            }

        }
        return convertView;
    }
}
