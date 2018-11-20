package com.baseapp.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * 作用：缓存工具类  保存一些退出登录是不被清理的数据
 */
public class UnClearCacheUtils {
    /**
     * 保持数据
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp  = context.getSharedPreferences("yhyy", Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    /**
     * 得到缓存数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp  = context.getSharedPreferences("yhyy", Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void putBoolean(Context context, String key, boolean values) {
        SharedPreferences sp  = context.getSharedPreferences("yhyy", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, values).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp  = context.getSharedPreferences("yhyy", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
   /* *//**
     * 清除所有数据
     *
     * @param context
     *//*
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences("yhyy",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }*/
}
