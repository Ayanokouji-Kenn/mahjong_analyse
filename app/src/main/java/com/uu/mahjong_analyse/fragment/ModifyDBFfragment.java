package com.uu.mahjong_analyse.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.uu.mahjong_analyse.utils.Constant;
import com.uu.mahjong_analyse.adapter.ModifyDBGridViewAdapter;
import com.uu.mahjong_analyse.db.DBDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther xuzijian
 * @date 2017/6/7 15:11.
 */

public class ModifyDBFfragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        return getGridData();
    }

    public GridView getGridData() {
        String table = getArguments().getString(Constant.Table.TABLE_NAME);
        GridView gridView = new GridView(getContext());
        SQLiteDatabase db = DBDao.getDataBase();
        Cursor cursor = db.query(table, null, null, null,null, null, null);
        List<String> datas = new ArrayList<>();
        if (cursor != null) {
            String[] columnNames = cursor.getColumnNames();
            //datas.addAll(Arrays.asList(columnNames));
            gridView.setNumColumns(columnNames.length);
            gridView.setGravity(Gravity.CENTER);
            while (cursor.moveToNext()) {
                if (TextUtils.equals(table, Constant.Table.TABLE_GAME_RECORD)) {
                    for (String column : cursor.getColumnNames()) {
                        datas.add(cursor.getString(cursor.getColumnIndex(column)));
                    }
                } else if (TextUtils.equals(table, Constant.Table.TABLE_PLAYER_RECORD)) {
                    for (String column : cursor.getColumnNames()) {
                        if (TextUtils.equals(column, "name")) {
                            datas.add(cursor.getString(cursor.getColumnIndex(column)));
                        }else {
                            datas.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(column))));
                        }
                    }
                }
            }
            gridView.setAdapter(new ModifyDBGridViewAdapter(getContext(),datas,columnNames));

            setOnItemClick(gridView);
            cursor.close();
            db.close();
        }


        return gridView;
    }

    private void setOnItemClick(GridView gridView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
