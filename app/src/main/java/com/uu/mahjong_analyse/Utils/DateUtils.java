package com.uu.mahjong_analyse.Utils;

import android.os.SystemClock;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @auther Nagisa.
 * @date 2016/7/2.
 */
public class DateUtils {
    public static String getToday() {
        long l = SystemClock.currentThreadTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(l));

    }
}
