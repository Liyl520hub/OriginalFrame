package com.yjj.coach.kaolakaola.utils;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Created by liyl on 2018/11/19
 *
 * @author liyalei
 * @Desc: 工程资源获取工具类
 */

public class ResourceUtils {

    private static Application mApplication = null;

    /**
     * 初始化
     *
     * @param app
     */
    public static void init(Application app) {
        mApplication = app;
    }

    public static Resources getResources() {
        return mApplication.getResources();
    }

    public static String getString(@StringRes int stringRes) {
        return getResources().getString(stringRes);
    }

    public static String[] getStringArray(@ArrayRes int stringResArray) {
        return getResources().getStringArray(stringResArray);
    }

    public static Drawable getDrawable(@DrawableRes int drawableRes) {
        return ResourcesCompat.getDrawable(getResources(), drawableRes, null);
    }

    public static int getColor(@ColorRes int colorRes) {
        return ResourcesCompat.getColor(getResources(), colorRes, null);
    }
}
