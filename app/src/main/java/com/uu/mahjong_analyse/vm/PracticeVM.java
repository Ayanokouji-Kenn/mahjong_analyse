package com.uu.mahjong_analyse.vm;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
import com.uu.mahjong_analyse.BR;
import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.base.BaseVM;
import com.uu.mahjong_analyse.utils.HaiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @description
 * @auther xuzijian
 * @date 2017/9/12 14:57.
 */

public class PracticeVM extends BaseVM {
    public ArrayList<Integer> player1 = new ArrayList<>();
    public ArrayList<Integer> player2 = new ArrayList<>();
    public ArrayList<Integer> player3 = new ArrayList<>();
    public ArrayList<Integer> player4 = new ArrayList<>();
    public final ItemBinding<Integer> myItemBinding = ItemBinding.of(BR.item, R.layout.item_my_hais);
    public List<Integer> haiHills = new ArrayList<>();


    public PracticeVM(Application application) {
        super(application);
    }

    public void init() {
        haiHills.addAll(HaiUtils.getHaiHills());
        Collections.shuffle(haiHills);

        for (int i = 0; i < 13 * 4; i++) {
            switch (i % 4) {
                case 0:
                    getHai(player1, haiHills);
                    break;
                case 1:
                    getHai(player2, haiHills);
                    break;
                case 2:
                    getHai(player3, haiHills);
                    break;
                case 3:
                    getHai(player4, haiHills);
                    break;
            }
        }
        getHai(player1, haiHills);
//        排序
        TreeSet<Integer> set = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int d = o1 - o2;
                return d == 0 ? 1 : d;
            }
        });
        for (Integer integer : player1) {
            set.add(integer);
        }
        player1.clear();
        for (Integer integer : set) {
            player1.add(integer);
        }
        LogUtils.d(player1.size());
    }

    public void dapai(int position) {
        if (position == player1.size() - 1) {
            player1.remove(position);
            return;
        }
        player1.remove(position);
        int lastPai = player1.get(player1.size()-1);
        int insertPosition = 0;
        for (int i = 0; i < player1.size(); i++) {
            if (lastPai <= player1.get(i)) {
                insertPosition = i;
                player1.remove(player1.size() - 1);
                player1.add(insertPosition, lastPai);
                break;
            }
        }

    }

    public void getHai(List<Integer> who, List<Integer> haiHills) {
        who.add(haiHills.get(0));
        haiHills.remove(0);
    }
}
