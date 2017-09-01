package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MyClass {
    public static void main(String[] args) {
        single();
        System.exit(0);
    }

    static List<Integer> yama = new ArrayList<>();
    private static void single() {
        for (int i = 0; i < 34; i++) {
            for (int i1 = 0; i1 < 4; i1++) {
                yama.add(i);
            }
        }
        Collections.shuffle(yama);
              Iterator<Integer> iterator = yama.iterator();
        while (iterator.hasNext()) {
            System.out.println( "single: "+iterator.next());
        }

    }

    public static int compressBitmap() {
        try {
            int i = 2/0;
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return 1;
    }
}
