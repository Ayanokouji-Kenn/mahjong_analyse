package com.uu.mahjong_analyse.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uu.mahjong_analyse.R;
/**
 * @auther Nagisa.
 * @date 2016/7/15.
 */
public class RightMenuFragment extends android.support.v4.app.Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_rightmenu, null);
        return view;
    }
}
