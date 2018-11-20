package com.baseapp.common.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baseapp.common.R;
import com.baseapp.common.base.BaseApplication;
import com.baseapp.common.base.config.BaseConfig;
import com.baseapp.common.utility.ActivityManager;
import com.baseapp.common.utils.EncryptSPUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/3/15 0015.
 *
 * @Desc: 监听App是否进入后台运行，如果进入后台，则给用户提示，出于安全设计如此做
 */
@Deprecated
public class ToBackgroundService extends Service {
    //是否已经显示App进入后台的用户提示
    private boolean hasShowBackgroundTip = false;
    private Disposable mDisposable;
    private long mToBackGroundTime;
    @Override
    public void onCreate() {
        super.onCreate();
        hasShowBackgroundTip = false;
        observeWhetherAppIsOnBackground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseRxJava();
    }

    /**
     * 监听App是否在后台运行
     */
    private void observeWhetherAppIsOnBackground() {

        mDisposable = Observable.
                interval(300, TimeUnit.MILLISECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (AppUtils.isAppForeground()) {
                            //App前台运行
                            hasShowBackgroundTip = false;
                        } else {
                            //App后台运行
                            mToBackGroundTime = System.currentTimeMillis();
                            hasShowBackgroundTip = true;
                            LogUtils.e("App名称进入后台运行");
                            ToastUtils.showLong(BaseApplication.getAppContext().getResources().getString(R.string.app_run_into_background));
                        }
                    }
                });

    }

    /**
     * RxJava解除订阅
     */
    private void releaseRxJava() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


}
