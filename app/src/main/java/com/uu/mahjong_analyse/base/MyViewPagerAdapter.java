package com.uu.mahjong_analyse.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.uu.mahjong_analyse.utils.Constant;

import java.util.List;

/**
 * @auther xuzijian
 * @date 2017/6/7 15:01.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getArguments().getString(Constant.Table.TABLE_NAME);
    }
}
