package com.uu.mahjong_analyse.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.uu.mahjong_analyse.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description
 * @auther xuzijian
 * @date 2017/9/12 15:07.
 */

public class HaiUtils {
    private static final int M_BIT= 16;  //第五位表示万
    private static final int S_BIT= 32;  //第六位表示索
    private static final int P_BIT= 64; //第七位表示饼
    private static final int Z_BIT= 128;    //第八位表示字
    private static List<Integer> hills = new ArrayList<>();
    public static List<Integer> getHaiHills() {
        return hills;
    }
    static {
        for (int i = 1; i <= 9; i++) {
            for (int i1 = 0; i1 < 4; i1++) {
                hills.add(i | M_BIT);
                hills.add(i | S_BIT);
                hills.add(i | P_BIT);
                if (i <= 7) {
                    hills.add(i | Z_BIT);
                }
            }
        }


    }

    private static void init() {
        Collections.shuffle(hills);

//        一人一张发牌

    }


    static int[] mRes = {R.drawable.w1,R.drawable.w2,R.drawable.w3,R.drawable.w4,R.drawable.w5,R.drawable.w6,R.drawable.w7,R.drawable.w8,R.drawable.w9};
    static int[] sRes = {R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,R.drawable.s5,R.drawable.s6,R.drawable.s7,R.drawable.s8,R.drawable.s9};
    static int[] pRes = {R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9};
    static int[] zRes = {R.drawable.dong,R.drawable.nan,R.drawable.xi,R.drawable.bei,R.drawable.zhong,R.drawable.fa,R.drawable.bai};
    @BindingAdapter({"resInt"})
    public static void trans(ImageView iv, int i) {
        StringBuilder sb = new StringBuilder();
        if ((i & M_BIT) == M_BIT) {  //
            iv.setImageResource(mRes[(i&15)-1]);
        } else if ((i & S_BIT) == S_BIT) {
            iv.setImageResource(sRes[(i&15)-1]);
        } else if ((i & P_BIT) == P_BIT) {
            iv.setImageResource(pRes[(i&15)-1]);
        } else if ((i & Z_BIT) == Z_BIT) {
            iv.setImageResource(zRes[(i&15)-1]);
        }
    }


}
