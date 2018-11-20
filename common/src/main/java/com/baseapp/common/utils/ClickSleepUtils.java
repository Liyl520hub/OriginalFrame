package com.baseapp.common.utils;

/**
 * Created by Android-Dev05 on 2017/12/11.避免重复点击提交按钮
 */

public class ClickSleepUtils {
    // 判断按钮是否快速点击
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
