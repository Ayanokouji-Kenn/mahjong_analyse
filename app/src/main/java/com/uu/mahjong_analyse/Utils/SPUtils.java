package com.uu.mahjong_analyse.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.uu.mahjong_analyse.base.MyApplication;

/**
 * @auther xuzijian
 * @date 2017/6/1 15:35.
 */

public class SPUtils {
    private static final String SP_NAME = "sp_data";

    private static SharedPreferences getInstance() {
        return MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void remove(String key) {
        getInstance().edit().remove(key).apply();
    }

    public static void clear() {
        getInstance().edit().clear().apply();
    }

    public static void putString(String key, String value) {
        getInstance().edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        return getInstance().getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        getInstance().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return getInstance().getInt(key, defValue);
    }

    public static void putLong(String key, long value) {
        getInstance().edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defValue) {
        return getInstance().getLong(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        getInstance().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, Boolean defValue) {
        return getInstance().getBoolean(key, defValue);
    }


}
