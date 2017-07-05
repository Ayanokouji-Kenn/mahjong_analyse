package com.example;

public class MyClass {
    public static void main(String[] args) {
        System.out.println(compressBitmap());
        System.exit(0);
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
