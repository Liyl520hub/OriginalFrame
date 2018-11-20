package com.liyl.xxxx.xxx.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.baseapp.common.base.BaseApplication;
import com.baseapp.common.baserx.RxSubscriber;
import com.baseapp.common.http.Api;
import com.baseapp.common.http.config.ApiConfig;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.liyl.xxxx.xxx.BuildConfig;
import com.liyl.xxxx.xxx.ui.login.activity.GlobalLoginOutActivity;
import com.liyl.xxxx.xxx.ui.login.activity.LoginActivity;
import com.liyl.xxxx.xxx.utils.ResourceUtils;

/**
 * @author lyl
 * <p>
 * created 2018/11/19
 * <p>
 * class use:
 */
/**
 * <pre>
 *     author:
 *                                      ___           ___           ___         ___
 *         _____                       /  /\         /__/\         /__/|       /  /\
 *        /  /::\                     /  /::\        \  \:\       |  |:|      /  /:/
 *       /  /:/\:\    ___     ___    /  /:/\:\        \  \:\      |  |:|     /__/::\
 *      /  /:/~/::\  /__/\   /  /\  /  /:/~/::\   _____\__\:\   __|  |:|     \__\/\:\
 *     /__/:/ /:/\:| \  \:\ /  /:/ /__/:/ /:/\:\ /__/::::::::\ /__/\_|:|____    \  \:\
 *     \  \:\/:/~/:/  \  \:\  /:/  \  \:\/:/__\/ \  \:\~~\~~\/ \  \:\/:::::/     \__\:\
 *      \  \::/ /:/    \  \:\/:/    \  \::/       \  \:\  ~~~   \  \::/~~~~      /  /:/
 *       \  \:\/:/      \  \::/      \  \:\        \  \:\        \  \:\         /__/:/
 *        \  \::/        \__\/        \  \:\        \  \:\        \  \:\        \__\/
 *         \__\/                       \__\/         \__\/         \__\/
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : utils about initialization
 * </pre>
 */
public class MyApplication extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(BuildConfig.IS_DEBUG);
        ResourceUtils.init(this);
        initApiConfig();
        registerGlobalErrorListener();

    }

    private void registerGlobalErrorListener() {
        RxSubscriber.registerGlobalErrorListener(new RxSubscriber.GlobalErrorListener() {
            @Override
            public void onReturn401Code(RxSubscriber rxSubscriber, String message) {
                LogUtils.e("LoginOut---onReturn401Code");
                loginOut(rxSubscriber, message, true);
            }

            @Override
            public void onReturn9105Code(RxSubscriber rxSubscriber, String message) { //token失效
                LogUtils.e("LoginOut---onReturn9105Code");
                loginOut(rxSubscriber, message, false);
            }

            @Override
            public void onReturn9107Code(RxSubscriber rxSubscriber, String message) { //多设备登录
                LogUtils.e("LoginOut---onReturn9107Code");
                loginOut(rxSubscriber, message, true);
            }

            @Override
            public void onReturn9108Code(RxSubscriber rxSubscriber, String message) {
                loginOut(rxSubscriber, message, true);
            }

            @Override
            public void onReturn9109Code(RxSubscriber rxSubscriber, String message) {
                loginOut(rxSubscriber, message, true);
            }
        });
    }

    private void initApiConfig() {
        ApiConfig mApiConfig = new ApiConfig();
        mApiConfig.setHostServer(BuildConfig.HOST_SERVER);
        mApiConfig.setReadTimeOut(BuildConfig.READ_TIME_OUT);
        mApiConfig.setConnectTimeOut(BuildConfig.CONNECT_TIME_OUT);
        Api.setConfig(mApiConfig);
    }

    private void loginOut(RxSubscriber rxSubscriber, String message, boolean needDialog){
        final Activity mActivity;

        if (rxSubscriber.getActivity() != null) {
            mActivity = rxSubscriber.getActivity();
        } else if (rxSubscriber.getFragment() != null) {
            mActivity = rxSubscriber.getFragment().getActivity();
        } else {
//                    mActivity = AppManager.getAppManager().currentActivity();
            mActivity = com.baseapp.common.utility.ActivityManager.getInstance().getCurrentActivity();
        }

        if (!needDialog){
//            JPushInterface.stopPush(this);
//            ArouterUtils.setAroutePath(Global.GO_LOGIN_ACTIVITY);
            Intent intent = new Intent(mActivity, LoginActivity.class);
            //不设置该flag，在oppo的一个型号上跳转会闪退
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            com.baseapp.common.utility.ActivityManager.getInstance().delayFinishAllExceptSpecificNameActivity("LoginActivity");
            ToastUtils.showLong(message);
        }else {
//            JPushInterface.stopPush(this);
            Intent mIntent = new Intent(mActivity,GlobalLoginOutActivity.class);
            mIntent.putExtra(GlobalLoginOutActivity.PARAM_KEY_SERVER_MESSAGE,message);
            mActivity.startActivity(mIntent);
        }

    }
}
