package com.uu.mahjong_analyse.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uu.mahjong_analyse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @auther Nagisa.
 * @date 2016/7/15.
 */
public class RightMenuFragment extends Fragment {
    @BindView(R.id.rb_east1)
    RadioButton mRbEast1;
    @BindView(R.id.rb_east2)
    RadioButton mRbEast2;
    @BindView(R.id.rb_east3)
    RadioButton mRbEast3;
    @BindView(R.id.rb_east4)
    RadioButton mRbEast4;
    @BindView(R.id.rb_south1)
    RadioButton mRbSouth1;
    @BindView(R.id.rb_south2)
    RadioButton mRbSouth2;
    @BindView(R.id.rb_south3)
    RadioButton mRbSouth3;
    @BindView(R.id.rb_south4)
    RadioButton mRbSouth4;
    @BindView(R.id.rightmenu_rg)
    RadioGroup mRightmenuRg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_rightmenu, null);
        ButterKnife.bind(this, view);


        return view;
    }
}
