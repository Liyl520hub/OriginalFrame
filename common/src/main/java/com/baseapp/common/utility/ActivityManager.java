package com.baseapp.common.utility;

import android.app.Activity;
import android.util.Log;

import com.baseapp.common.R;
import com.baseapp.common.base.BaseApplication;
import com.blankj.utilcode.util.ToastUtils;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/*
 * @Author
 * created at 2017/4/26 0026 上午 11:45
 *
 * @Desc:APP的activity的管理类，方法非静态，使用时需要先调用getActivityManager（）方法获得实例，然后再调用方法
 */
public class ActivityManager {

    private boolean canExitApp = false;
    private long mLastClickTime = 0; //上次点击退出的时间
    private static CopyOnWriteArrayList<Activity> activityStack = new CopyOnWriteArrayList<Activity>();
    private static ActivityManager instance;

    private ActivityManager() {

    }

    /**
     * 获取单一实例
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public CopyOnWriteArrayList<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 添加Activity到管理堆栈
     */
    public void addActivityToStack(Activity activity) {
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 从堆栈中将activity移除
     * 有的activity非调用ActivityManager的相关方法finish，如手势右滑finish，此时需要从堆栈中移除，防止持续引用activity造成内存泄漏。
     *
     * @param activity
     */
    public void removeActivityFromStack(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 获取当前Activity，即堆栈最顶端的activity
     */
    public Activity getCurrentActivity() {
        if (activityStack.size() > 0) {
            return activityStack.get(activityStack.size() - 1);
        } else {
            return null;
        }
    }

//    /**
//     * 警告：该方法finish掉了堆栈中最后add的activity，如果要使用该方法finish当前界面的activity，要确保堆栈中最上面的activity是当前界面activity
//     */
//    public void finishCurrentActivity() {
//        Activity activity = activityStack.get(activityStack.size() - 1);
//        if (activity != null) {
//            activity.finish();
//            activity = null;
//        }
//    }

    /**
     * 结束指定的Activity
     */
    public void finishSpecificActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     * 警告：未验证混淆，使用时如需配置混淆规则请配置
     * 使用该方法需要保证工程中每一个Activity的Simple名字都不一样
     */
    public void finishSpecificNameActivity(String classSimpleName) {
        for (Activity activity : activityStack) {
            if (activity != null && activity.getClass().getSimpleName().equals(classSimpleName)) {
                finishSpecificActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    private void finishAllActivity() {

        for (Activity activity : activityStack) {
            if (activity != null) {
                finishSpecificActivity(activity);
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(Class<?> cls) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                Activity activity = activityStack.get(i);
                Log.e("TAG", "name===" + activity.getComponentName());
                if (!activity.getClass().equals(cls)) {
                    activityStack.get(i).finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(Class<?> cls, Class<?> cls2) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                Activity activity = activityStack.get(i);
                if (!activity.getClass().equals(cls) && !activity.getClass().equals(cls2)) {
                    Log.e("finishAllActivity: ", cls.toString() + "==" + cls2.toString());
                    activityStack.get(i).finish();
                }
            }
        }
        activityStack.clear();
    }
//    /**
//     * 结束除了当前的Activity之外所有的Activity
//     */
//    public void finishAllExceptCurrentActivity() {
//        Activity activity = null;//  当前Activity的变量引用
//        for (int i = 0, size = activityStack.size(); i < size; i++) {
//            if (null != activityStack.get(i)) {
//                if (this.getCurrentActivity().equals(activityStack.get(i))) {
//                    activity = activityStack.get(i);
//                    // 保留最后一个Activity，便于启动Login界面
//                } else {
//                    activityStack.get(i).finish();
//                }
//            }
//        }
//        activityStack.clear();
//        if (activity != null) {   //将除当前Activity之外的Activity结束并且在stack中clear清除之后，再将当前activity添加进stack中
//            activityStack.add(activity);
//        }
//    }

    /**
     * finish掉堆栈中除特定activity之外的其余activity
     *
     * @param specificActivity
     */
    public void finishAllExceptSpecificActivity(Activity specificActivity) {
        for (Activity activity : activityStack) {
            if (activity != null) {
                if (!activity.equals(specificActivity)) {
                    finishSpecificActivity(activity);
                }
            }
        }

        activityStack.clear();
        activityStack.add(specificActivity);
    }

    /**
     * 如果通过startActivity
     * @param name
     */
    public void delayFinishAllExceptSpecificNameActivity(final String name) {
        Observable.
                empty().
                delay(1000, TimeUnit.MILLISECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<Object>() {

                    private Disposable mDelayDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDelayDisposable = d;
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        finishAllExceptSpecificNameActivity(name);
                        if (mDelayDisposable != null && !mDelayDisposable.isDisposed()){
                            mDelayDisposable.dispose();
                        }
                    }
                });
    }

    /**
     * finish掉堆栈中除特定名字之外的activity
     * 警告：未验证混淆规则，使用请注意是否需要配置混淆，需要请配置
     *
     * @param name
     */
    private void finishAllExceptSpecificNameActivity(String name) {
        Activity mActivity = null;
        for (Activity activity : activityStack) {
            if (activity != null && !activity.getClass().getSimpleName().equals(name)) {
                finishSpecificActivity(activity);
            } else {
                mActivity = activity;
            }
        }

        activityStack.clear();
        activityStack.add(mActivity);
        mActivity = null;
    }

    public void returnToSpecificNameActivity(String name){
        for (Activity activity : activityStack) {
            if (activity != null && !activity.getClass().getSimpleName().equals(name)) {
                finishSpecificActivity(activity);
            } else {
                break;
            }
        }
    }

    public boolean containsSpecificNameActivity(String simpleClassName) {
        boolean result = false;
        for (Activity activity : activityStack) {
            if (activity != null && activity.getClass().getSimpleName().equals(simpleClassName)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public Activity getSpecificNameActivity(String simpleClassName) {
        Activity mActivity = null;
        for (Activity activity : activityStack) {
            if (activity != null && activity.getClass().getSimpleName().equals(simpleClassName)) {
                mActivity = activity;
                break;
            }
        }

        return mActivity;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void exitByDoubleClick(long time) {

        if (time - mLastClickTime < 2000) {
            AppExit();
        } else {
            mLastClickTime = time;
            ToastUtils.showShort(BaseApplication.getAppContext().getResources().getString(R.string.app_exit));
        }
    }

}
