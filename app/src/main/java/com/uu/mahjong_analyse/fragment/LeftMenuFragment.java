package com.uu.mahjong_analyse.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.ui.MainActivity;
import com.uu.mahjong_analyse.ui.PlayerInfoActivity;
import com.uu.mahjong_analyse.adapter.LeftMenuAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 * @auther Nagisa.
 * @date 2016/7/3.
 */
public class LeftMenuFragment extends Fragment {

    public List<String> mLeftmenuDatas;
    public LeftMenuAdapter mLeftMenuAdapter;
    private ListView mLeftmenuListview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_leftmenu, container,false);

        mLeftmenuListview = (ListView) view.findViewById(R.id.leftmenu_listview);
        initLeftMenu();
        return view;
    }

    private void initLeftMenu() {
        mLeftmenuDatas = new ArrayList<>();
        mLeftmenuDatas.add("东家");
        mLeftmenuDatas.add("西家");
        mLeftmenuDatas.add("南家");
        mLeftmenuDatas.add("北家");
        mLeftMenuAdapter = new LeftMenuAdapter(getActivity(), mLeftmenuDatas);
        mLeftmenuListview.setAdapter(mLeftMenuAdapter);
        mLeftmenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).closeDrawerLayout();
                if(mLeftmenuDatas.get(position).length() < 3) {                                     //设置玩家后，会在东家后拼上用户名字，所以长度不可能小于3
                    Toast.makeText(getContext(), "尚未设置玩家", Toast.LENGTH_SHORT).show();
                    return;

                }
                Intent intent = new Intent(getActivity(),PlayerInfoActivity.class);
                intent.putExtra("player", mLeftmenuDatas.get(position));
                ((MainActivity)getActivity()).openPage(true,-1,intent);
            }
        });
    }
}
