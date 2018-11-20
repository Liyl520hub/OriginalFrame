package com.baseapp.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baseapp.common.R;
import com.baseapp.common.base.BaseApplication;
import com.blankj.utilcode.util.StringUtils;


/**
 * Toast统一管理类
 *
 * @author Administrator
 */
public class ToastUitl {


    private static Toast toast;
    private static Toast toast_common;

    private static Toast initToast(CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getAppContext(), message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return toast;
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        initToast(message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    public static void showShort(int strResId) {
        initToast(BaseApplication.getAppContext().getResources().getText(strResId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        initToast(message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param strResId
     */
    public static void showLong(int strResId) {
        initToast(BaseApplication.getAppContext().getResources().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        initToast(message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param strResId
     * @param duration
     */
    public static void show(Context context, int strResId, int duration) {
        initToast(context.getResources().getText(strResId), duration).show();
    }


    /**
     * @param message 吐司内容
     */
    public static void show(Context context, int icon, CharSequence message) {

        if (!StringUtils.isTrimEmpty(message.toString()) && (message.toString().contains("9107") || message.toString().contains("9108"))) {
            return;
        }
        //解决短时间内连续吐司的情况 只会显示一个 不用一直实例
        if (toast_common == null) {
            toast_common = new Toast(context.getApplicationContext());
        }

        View inflate = View.inflate(context.getApplicationContext(), R.layout.login_dialog, null);
        TextView message_tv = (TextView) inflate.findViewById(R.id.message__dialog);
        ImageView imagview = (ImageView) inflate.findViewById(R.id.icon_dialog);
        if (message.equals("") || message == null) {
            message_tv.setVisibility(View.GONE);
        } else {
            message_tv.setVisibility(View.VISIBLE);
            message_tv.setText(message);

        }
        imagview.setBackground(UIUtils.getDrawable(icon));
        toast_common.setView(inflate);
        //对吐司的高度重新赋值，直接用工具类获取屏幕高度 除以2
        int screenHeight = DisplayUtil.getScreenHeight(BaseApplication.getAppContext()) / 2;
        int widgetHeight = DisplayUtil.getWidgetHeight(inflate) / 2;
        int toastHeight = screenHeight - widgetHeight;
        toast_common.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, toastHeight);
        toast_common.show();
    }

    /**
     * @param message 吐司内容
     * @param height  吐司的高度位置
     */
    public static void show(Context context, int icon, CharSequence message, int height) {
        //解决短时间内连续吐司的情况 只会显示一个 不用一直实例
        if (toast_common == null) {
            toast_common = new Toast(context.getApplicationContext());
        }

        View inflate = View.inflate(context.getApplicationContext(), R.layout.login_dialog, null);
        TextView message_tv = (TextView) inflate.findViewById(R.id.message__dialog);
        ImageView imagview = (ImageView) inflate.findViewById(R.id.icon_dialog);
        if (message.equals("") || message == null) {
            message_tv.setVisibility(View.GONE);
        } else {
            message_tv.setVisibility(View.VISIBLE);
            message_tv.setText(message);

        }
        imagview.setBackground(UIUtils.getDrawable(icon));
        toast_common.setView(inflate);
        toast_common.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, height);
        toast_common.show();
    }


    /**
     * @param message  吐司内容
     * @param duration 吐司时间
     * @param height   吐司的高度位置
     */
    public static void show(CharSequence message, int duration, int height) {
        initToast(message, duration);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, height);
        toast.show();
    }


}
