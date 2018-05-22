package com.uu.mahjong_analyse.base;

import android.app.Application;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.uu.mahjong_analyse.BuildConfig;

import java.io.File;
import java.io.IOException;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * Created by Nagisa on 2016/6/25.
 */
public class MyApplication extends Application {
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
        Utils.init(this);
        //chrome debug bridge
        Stetho.initializeWithDefaults(this);
        Fragmentation.builder()
                // show stack view. Mode: BUBBLE, SHAKE, NONE
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(AppUtils.isAppDebug())
             .install();

        //初始化bugly
//        CrashReport.initCrashReport(getApplicationContext(), "2a6c84812d", true);
        //科大讯飞初始化
//        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59279059");
    }

    public static Context getInstance() {
        return instance;
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象对象
     *
     * @param name
     */
    @Override
    public File getDatabasePath(String name) {
        String dbDir;

        //判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if(!sdExist) {//如果不存在,
            String filePath = getFilesDir().getAbsolutePath();
            dbDir = filePath.replace("file", "database");
        } else {//如果存在
            //获取sd卡路径
            dbDir = android.os.Environment.getExternalStorageDirectory().toString();
            dbDir += "/mahjong";//数据库所在目录
        }
        String dbPath = dbDir + "/" + name;//数据库路径
        //判断目录是否存在，不存在则创建该目录
        File dirFile = new File(dbDir);
        if(!dirFile.exists())
            dirFile.mkdirs();

        //数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        //判断文件是否存在，不存在则创建该文件
        File dbFile = new File(dbPath);
        if(!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();//创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            isFileCreateSuccess = true;

        //返回数据库文件对象
        if(isFileCreateSuccess) {
            LogUtils.d(dbFile.getAbsolutePath());
            return dbFile;
        }
        else
            return null;

    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int,
     * android.database.sqlite.SQLiteDatabase.CursorFactory,
     * android.database.DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}

