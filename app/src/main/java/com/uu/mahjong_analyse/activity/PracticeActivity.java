package com.uu.mahjong_analyse.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.uu.mahjong_analyse.R;
import com.uu.mahjong_analyse.mj.Hulib;
import com.uu.mahjong_analyse.mj.TableMgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {
    List<Integer> yama = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        init();
        single();

    }

    private boolean checkHu(int[] handPai) {
        int guiIndex = 34;
//        int[] cards = {
//                1, 1, 1, 0, 1, 1, 1, 0, 0,
//                1, 1, 1, 0, 0, 0, 3, 0, 0,
//                0, 0, 0, 0, 0, 0, 0, 0, 0,
//                2, 0, 0, 0, 0, 0, 0};

//        Program.print_cards(cards);
        return Hulib.getInstance().get_hu_info(handPai, 34, guiIndex);
    }

    private void init() {
        TableMgr.getInstance().load();  //加载所有的表
        for (int i = 0; i < 34; i++) {
            for (int i1 = 0; i1 < 4; i1++) {
                yama.add(i);
            }
        }

    }

    private void single() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 10; j++) {
                    int count = 0;
                    while (true) {
                        ++count;
                        Collections.shuffle(yama); //乱序
                        int[] hand = new int[34];
                        Iterator<Integer> iterator = yama.iterator();
                        for (int i = 0; i < 14; i++) {
                            ++hand[iterator.next()];
                        }
                        if (checkHu(hand)) {
                            break;
                        }
                    }
                    Log.d("ZFDT", "single: " + count);
                }
            }
        }).start();
    }
}
