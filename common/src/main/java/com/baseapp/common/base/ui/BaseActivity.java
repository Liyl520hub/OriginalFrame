package com.baseapp.common.base.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baseapp.common.R;
import com.baseapp.common.base.BaseApplication;
import com.baseapp.common.base.BasePresenter;
import com.baseapp.common.base.config.BaseConfig;
import com.baseapp.common.baserx.RxClickTransformer;
import com.baseapp.common.baserx.RxManager;
import com.baseapp.common.http.error.ErrorCode;
import com.baseapp.common.receiver.NetworkChangeReceiver;
import com.baseapp.common.utility.ActivityManager;
import com.baseapp.common.utils.ArouterUtils;
import com.baseapp.common.utils.EncryptSPUtils;
import com.baseapp.common.utils.Reflector;
import com.baseapp.common.utils.TUtil;
import com.baseapp.common.utils.UIUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * @Desc Activity 新版基类,关于页面数据刷新只使用下拉刷新，上拉加载activity不实现，请使用具有上拉加载功能的Fragment
 */

public abstract class BaseActivity<T extends BasePresenter> extends SwipeBackActivity {

    public final String TAG = getClass().getSimpleName() + "---";
    private final static int ACTION_DO_NOTHING = -1;
    private final static int ACTION_START_ACTIVITY_1 = 0;
    private final static int ACTION_START_ACTIVITY_2 = 1;


    public Context mContext;
    public Activity mActivity;
    public RxManager mRxManager;
    private boolean isConfigChange = false;  //是否横竖屏切换
    protected Unbinder mBinder;
    public T mPresenter;
    public int mCurrentPage = 1;  //分页加载当前页
    protected boolean isRefreshMode = true;  //默认下拉刷新模式
    private View mEmptyView;
    private View mLoadingView;

    private SmartRefreshLayout mSmartRefreshLayout;  //界面下拉刷新布局
    private RefreshHeader mRefreshHeader;  //SmartRefreshLayout的刷新header
    //网络请求LoadingView的容器
    private FrameLayout mLoadingViewContainer;
    private FrameLayout mEmptyErrorViewContainer; //空界面或者错误界面视图容器
    private long mToBackgroundTime = -1;
    private long mToForeground;

    private boolean isDoubleClickExit = false; //点击两次返回按键退出程序标志位，默认false不退出程序，而是回退
    private NetworkChangeReceiver mNetworkChangeReceiver;

    private boolean isAppOnBackgroundMoreThanThreeHours = false;
    public Bundle savedInstanceState;
    /**
     * 空界面或错误界面图片
     */
    private ImageView mEmptyErrorImage;
    /**
     * 空界面或错误界面描述信息
     */
    private TextView mEmptyErrorDescText;
    public static final int CODE_LIST_NO_NETWORK = 1; //列表请求无网络码制，显示错误空界面视图时使用
    public static final int CODE_EMPTY_LIST_DATA = 2;
    public static final int CODE_REFRESH_FAILED = 3; //刷新接口请求失败

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        mContext = this;
        mActivity = this;
        isConfigChange = false;
        mRxManager = new RxManager();
        //不绘制window默认主题背景色，考虑性能问题
//        getWindow().setBackgroundDrawable(null);

        initBeforeSetContentView();

        setContentView(getLayoutId());

//        startToBackgroundService();

        ActivityManager.getInstance().addActivityToStack(this);

        mBinder = ButterKnife.bind(this);

        mPresenter = TUtil.getT(this, 0);

        if (mPresenter != null) {
            mPresenter.setActivity(this);
            mPresenter.setFragment(null);
        }

        initPresenter();

        getSwipeLayout().setLeftSwipeEnabled(enableSwipeBack());

        initRefresh();

        //状态栏字体深色
        BarUtils.setStatusBarLightMode(this, true);

//        registerNetworkChangeReceiver();

        initView(savedInstanceState);

    }

    /**
     * 在方法setContentView()之前的初始化，有的初始化系统要求必须放在该方法之前，可以复写该方法。
     */
    @CallSuper
    protected void initBeforeSetContentView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        try {
            if (res.getConfiguration().fontScale != 1) {
                //非默认值
                Configuration newConfig = new Configuration();
                newConfig.setToDefaults();
                //设置默认
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 当用户按下屏幕、回到桌面、被其他app覆盖时，会执行此func
     * 先于onUserLeaveHint执行
     */
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    /**
     * 当此Activity不可见时会执行此func
     */
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        警告：暂不启用友盟统计，如果启用请注意Read—phone-state权限的申请
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (isFinishing()) {  //Activity正在销毁

            fixInputMethodManagerLeak();

            if (mPresenter != null) {
                mPresenter.onDestroy();
            }
            if (mRxManager != null) {
                mRxManager.clear();
            }
            if (!isConfigChange) {
                ActivityManager.getInstance().removeActivityFromStack(this);  //这里仅移除，用户finish也可以使用管理类
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消绑定
        mBinder.unbind();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isConfigChange = true;
    }

    @Override
    public void onBackPressed() {
        if (!isDoubleClickExit) {
            //说明可以回退,直接调用super，系统finish掉Activity，在onPause方法中从AppMananger中移除
            //exitAnim();
            super.onBackPressed();
        } else {
            //两次点击才能finish掉activity
            ActivityManager.getInstance().exitByDoubleClick(System.currentTimeMillis());
        }
    }

    private void registerNetworkChangeReceiver() {
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
//        mIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(mNetworkChangeReceiver, mIntentFilter);
    }

    private void unregisterNetworkChangeReceiver() {
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver);
        }
    }


//    /**
//     * 开始App进入后台提示toast的service
//     */
//    private void startToBackgroundService() {
//        Intent mSafeServiceIntent = new Intent(this, ToBackgroundService.class);
//        startService(mSafeServiceIntent);
//    }

    /**
     * 修复InputMethodManager持有Context导致的内存泄漏
     * 警告：该泄露应为SDK导致的泄漏，如果调用该方法导致问题请删除
     */
    private void fixInputMethodManagerLeak() {
        final Object imm = getSystemService(Context.INPUT_METHOD_SERVICE);

        final Reflector.TypedObject windowToken
                = new Reflector.TypedObject(getWindow().getDecorView().getWindowToken(), IBinder.class);

        Reflector.invokeMethodExceptionSafe(imm, "windowDismissed", windowToken);

        final Reflector.TypedObject view
                = new Reflector.TypedObject(null, View.class);

        Reflector.invokeMethodExceptionSafe(imm, "startGettingWindowFocus", view);
    }

    /**
     * 初始化刷新，子类可以复写以实现自己的逻辑
     */
    protected void initRefresh() {

        if (mSmartRefreshLayout != null) {

            mSmartRefreshLayout.setRefreshHeader(mRefreshHeader == null ? new MaterialHeader(mActivity) : mRefreshHeader);
            mSmartRefreshLayout.setEnableHeaderTranslationContent(false);
            //禁用上拉加载
            mSmartRefreshLayout.setEnableLoadmore(false);
            mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    isRefreshMode = true;
                    mCurrentPage = 1;
                    initNetWork();
                }
            });

        }
    }


    //</editor-fold >
    //<editor-fold desc="Activity跳转动画相关">

    //启动Activity跳转动画
    protected void startAnim() {
        overridePendingTransition(R.anim.open_activity_to_open_anim, R.anim.open_activity_to_exit_anim);
    }

    //回退Activity的动画
    protected void exitAnim() {
        overridePendingTransition(R.anim.close_activity_to_reshow_anim, R.anim.close_activity_to_exit_anim);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
        startAnim();
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
        startAnim();
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        startAnim();
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        startAnim();
    }
    /*********************************************关于SmartRefreshLayout的三个重要方法**********************************************/

    //</editor-fold >
    //<editor-fold desc="下拉刷新相关">

    /**
     * SmartRefreshLayout  自动刷新加载数据
     */
    public void autoRefresh() {
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.autoRefresh();
        }
    }

    /**
     * 加载列表数据成功时重置状态
     */
    public void resetStateWhenRefreshSuccess() {
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.finishRefresh(true);
        }

    }

    /**
     * 加载列表数据失败时重置状态
     */
    public void resetStateWhenRefreshFailed() {
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.finishRefresh(false);
        }
    }


    //</editor-fold >
    //<editor-fold desc="抽象方法">

    /**
     * 设置布局资源id
     */
    protected abstract int getLayoutId();

    /**
     * 使能手势滑动关闭Activity
     */
    protected abstract boolean enableSwipeBack();


    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    public abstract void initPresenter();

    /**
     * 初始化view
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 网络请求,下拉刷新时调用
     */
    protected void initNetWork() {

    }


    /*******************************************网络加载loading图**********************************************/

    //</editor-fold >
    //<editor-fold desc="LoadingView和空视图、错误视图相关">

    /**
     * 警告：该方法请勿手动调用，已经在{@link com.baseapp.common.baserx.RxSubscriber}中进行了调用
     * <p>
     * 开启加载进度View
     */
    public void showLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = View.inflate(this, R.layout.view_loading, null);

            mLoadingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //防止点穿
                }
            });
        }

        if (mLoadingView.getParent() != null) {
            ViewGroup parent = (ViewGroup) mLoadingView.getParent();
            parent.removeView(mLoadingView);
        }

        if (mLoadingViewContainer != null) {
            mLoadingViewContainer.addView(mLoadingView);
        } else {
            getContentContainer().addView(mLoadingView);
        }
    }


    /**
     * 警告：该方法请勿手动调用，已经在{@link com.baseapp.common.baserx.RxSubscriber}中进行了调用
     * <p>
     * 停止加载进度View
     */
    public void dismissLoadingView() {
        if (mLoadingView == null) {
            return;
        }

        if (mLoadingView.getParent() != null) {
            ViewGroup parent = (ViewGroup) mLoadingView.getParent();
            parent.removeView(mLoadingView);
        }
    }

    /**
     * 显示网络加载失败空视图
     *
     * @param errorCode {@link ErrorCode}中的错误码
     * @param message   界面显示的信息
     */
    public void showEmptyErrorView(int errorCode, String message) {

        if (mEmptyErrorViewContainer == null) {
            throw new IllegalArgumentException("使用空界面或错误界面，请使用setEmptyErrorViewContainer()设置视图容器");
        }

        if (mEmptyView == null) {
            mEmptyView = View.inflate(this, com.baseapp.common.R.layout.view_empty_error, null);

            mEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处什么都不做，仅为了防止点击事件传递到界面的view
                }
            });

//            mEmptyErrorRefreshButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clickEmptyErrorView();
//                }
//            });
        }

        switch (errorCode) {
            //404错误界面
            case ErrorCode.CODE_NOT_FOUND:
//                //根据需求，不再对404错误进行特殊处理，此处逻辑并未更改，只是使用了统一的图片资源
//                mEmptyErrorImage.setImageResource(R.mipmap.view_empty_error_image_error);
//                mEmptyErrorDescText.setText(message);
//                mEmptyErrorRefreshButton.setVisibility(View.VISIBLE);
                break;

            //非404的错误界面
            default:
//                mEmptyErrorImage.setImageResource(R.mipmap.view_empty_error_image_error);
//                mEmptyErrorDescText.setText(message);
//                mEmptyErrorRefreshButton.setVisibility(View.VISIBLE);
                break;
        }

        if (mEmptyView.getParent() != null) {
            ViewGroup parent = (ViewGroup) mEmptyView.getParent();
            parent.removeView(mEmptyView);
        }

        mEmptyErrorViewContainer.addView(mEmptyView);
    }

    /**
     * 显示404、网络加载失败、空视图
     *
     * @param errorCode {@link ErrorCode}中的错误码
     */
    public void showEmptyView(int errorCode, String message) {
        if (mEmptyView != null) {
            getContentContainer().removeView(mEmptyView);
        }
        mEmptyView = View.inflate(this, com.baseapp.common.R.layout.view_empty_error, null);
        mEmptyErrorImage = mEmptyView.findViewById(R.id.view_empty_error_image);
        mEmptyErrorDescText = mEmptyView.findViewById(R.id.view_empty_error_tip);
        getContentContainer().addView(mEmptyView);
        mEmptyErrorDescText.setText(message);
        RxView.clicks(mEmptyView).
                compose(RxClickTransformer.getClickTransformer()).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        initNetWork();
                    }
                });
        switch (errorCode) {
            case CODE_EMPTY_LIST_DATA:
                mEmptyErrorImage.setImageResource(com.baseapp.common.R.drawable.empty_error_view_no_integral);
                mEmptyErrorDescText.setText(UIUtils.getString(R.string.nonerecord));
                break;
            case CODE_LIST_NO_NETWORK:
            case CODE_REFRESH_FAILED:
                if (errorCode == CODE_LIST_NO_NETWORK) {
                    mEmptyErrorImage.setImageResource(com.baseapp.common.R.drawable.empty_error_view_no_network);
                    mEmptyErrorDescText.setText("网络连接已断开，请点击重试");
                } else if (errorCode == CODE_REFRESH_FAILED) {
                    mEmptyErrorImage.setImageResource(com.baseapp.common.R.drawable.empty_error_view_no_transaction);
                    mEmptyErrorDescText.setText("数据获取失败，请点击重试");
                }
                break;

            //接口请求错误，错误码404
            case ErrorCode.CODE_NOT_FOUND:
                break;
            case ErrorCode.CODE_INTERNAL_SERVER_ERROR:
                mEmptyErrorImage.setImageResource(com.baseapp.common.R.drawable.empty_error_view_server_under_maintance);
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏网络加载失败空视图
     */
    public void hideEmptyErrorView() {
        if (mEmptyView == null) {  //如果空视图为空直接返回
            return;
        }

        if (mEmptyView.getParent() != null) {
            ViewGroup parent = (ViewGroup) mEmptyView.getParent();
            parent.removeView(mEmptyView);
        }
    }

    /**
     * 点击空界面或错误界面的刷新按键会调用该方法，子类实现该方法进行接口重新请求
     */
    protected void clickEmptyErrorViewRefreshButton() {

    }


    /************************************************设置相关方法**************************************************/

    //</editor-fold >
    //<editor-fold desc="设置相关">

    /**
     * 设置SmartRefreshLayout，基类封装了刷新接口的逻辑
     *
     * @param layout
     */
    protected void setSmartRefreshLayout(SmartRefreshLayout layout) {
        this.mSmartRefreshLayout = layout;
    }

    /**
     * 设置SmartRefreshLayout 的下拉刷新header
     *
     * @param header
     */
    protected void setRefreshHeader(RefreshHeader header) {
        this.mRefreshHeader = header;
    }

    /**
     * 设置接口请求LoadingView的容器
     *
     * @param container
     */
    protected void setLoadingViewContainer(FrameLayout container) {
        this.mLoadingViewContainer = container;
    }

    /**
     * 设置空界面视图或者错误界面视图的容器
     *
     * @param container
     */
    protected void setEmptyErrorViewContainer(FrameLayout container) {
        this.mEmptyErrorViewContainer = container;
    }

    /**
     * 设置是否点击两次返回按键退出程序
     *
     * @param exit true：退出  false：界面退到上一个界面
     */
    protected void setDoubleClickExit(boolean exit) {
        this.isDoubleClickExit = exit;
    }
}
