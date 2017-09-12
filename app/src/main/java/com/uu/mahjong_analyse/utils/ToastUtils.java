package com.uu.mahjong_analyse.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @auther xuzijian
 * @date 2017/6/1.
 */

public class ToastUtils {
    private static Toast mToast;

    private static void init(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }
    }

    private static void init(Context context, int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
    }

    public static void show(Context context, CharSequence message) {
        init(context, message);
        mToast.show();
    }

    public static void show(Context context, int resId) {
        init(context, resId);
        mToast.show();
    }

    public static void showBottom(Context context, CharSequence message, int yoff) {
        init(context, message);
        mToast.setGravity(Gravity.BOTTOM, 0, yoff);
        mToast.show();
    }

    public static void showTop(Context context, CharSequence message, int yoff) {
        init(context, message);
        mToast.setGravity(Gravity.TOP, 0, yoff);
        mToast.show();
    }
}
