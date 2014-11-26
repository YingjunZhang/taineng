package com.lichuang.taineng.bean;

import android.util.Log;

/**
 * Created by Administrator on 2014/11/7.
 */
public class MyLog {
    private static boolean ifdebug= true;
    public static void LogInfo(String tag,String info){
        if (ifdebug){
            Log.i(tag,info);
        }
    }
}
