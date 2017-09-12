package com.uu.mahjong_analyse.vm;

import android.app.Application;
import android.databinding.ObservableArrayList;

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
    public final ObservableArrayList<Integer> player1 = new ObservableArrayList<>();
    public final ObservableArrayList<Integer> player2 = new ObservableArrayList<>();
    public final ObservableArrayList<Integer> player3 = new ObservableArrayList<>();
    public final ObservableArrayList<Integer> player4 = new ObservableArrayList<>();
    public final ItemBinding<Integer> myItemBinding = ItemBinding.of(BR.item, R.layout.item_my_hais);


    public PracticeVM(Application application) {
        super(application);
    }

    public void init() {
        List<Integer> haiHills = new ArrayList<>();
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
                int d = o1-o2;
                return d==0?1:d;
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

    private void getHai(List<Integer> who, List<Integer> haiHills) {
        who.add(haiHills.get(0));
        haiHills.remove(0);
    }
}
