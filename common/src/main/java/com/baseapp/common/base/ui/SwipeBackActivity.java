package com.baseapp.common.base.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.baseapp.common.R;
import com.baseapp.common.base.callback.IToolbar;
import com.baseapp.common.base.config.BaseConfig;
import com.baseapp.common.view.ReorderLinearLayout;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.daimajia.swipe.SwipeLayout;

/**
 * @author Administrator
 * @Desc: 封装了SwipeBackLayout，支持手势滑动finish掉当前Activity，同时封装了ToolBar
 */
public abstract class SwipeBackActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName() + "---";
    public Context mContext;

    /**
     * <p>下拉选项的当前状态。</p>
     * <b>false</b> : 1、没有下拉选项，2、向下状态。<br>
     * <b>true</b> : 1、向上状态。
     */
    private boolean mDropDownOptionsState = false;

    private SwipeLayout mSwipeLayout;
    private View mBottomView;
    private ReorderLinearLayout mSurfaceView;
    private View mContentView;
    private View mNoNetworkTipView; //没有网络的提示View
    private FrameLayout mContentContainer;
    private FrameLayout mToolbarContainer;

    private boolean isFullScreen = false;
//    private NetworkChangeReceiver mNetworkReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setTranslucentStatusBar();
        super.setContentView(getRootView());
        mContentView = getLayoutInflater().inflate(layoutResID, mContentContainer);

        //更换了无网络提示方式，该方式类似微信提示方式
//        if (mNoNetworkTipView != null){
//            mNetworkReceiver = new NetworkChangeReceiver();
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            registerReceiver(mNetworkReceiver, filter);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mNetworkReceiver != null){
//            unregisterReceiver(mNetworkReceiver);
//        }
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            isFullScreen = true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

            isFullScreen = true;
        }else {
            isFullScreen = false;
        }
    }


    private View getRootView() {
        //产生SwipeLayout实例
        mSwipeLayout = new SwipeLayout(this);
        initBottomView();

        initSurfaceView();

        initToolbarContainer();

//        initNoNetworkTipView();

        initContentContainer();

        initSwipeLayout();

        setSurfaceViewContent();
        return mSwipeLayout;
    }

    /**
     * 初始化SwipeLayout的BottomView，即滑动过程中逐渐显示的View
     *
     * @return
     */
    private void initBottomView() {

        mBottomView = new View(this);
        FrameLayout.LayoutParams mBottomViewParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomView.setLayoutParams(mBottomViewParams);
        mBottomView.setBackgroundResource(R.color.colorSwipeLayoutShadow);
    }

    /**
     * 初始化SwipeLayout的SurfaceView，即未滑动状态界面显示的view
     * SurfaceView采用线性布局，上部放置标题栏，下部放置界面内容视图
     */
    private void initSurfaceView() {

        mSurfaceView = new ReorderLinearLayout(this);
        mSurfaceView.enableChildrenDrawingOrder(getSurfaceViewDrawingChildrenOrderEnabled());
        mSurfaceView.setOrientation(LinearLayout.VERTICAL);
        mSurfaceView.setFitsSystemWindows(false);
        FrameLayout.LayoutParams mContentViewContainerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSurfaceView.setLayoutParams(mContentViewContainerParams);

    }


    /**
     * 初始化内容视图容器
     */
    private void initContentContainer() {
        mContentContainer = new FrameLayout(this);
        LinearLayout.LayoutParams mContentContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentContainer.setLayoutParams(mContentContainerParams);
    }

    private void setSurfaceViewContent() {

        IToolbar mIToolbar = getIToolbar();
        Toolbar mToolbar = null;

        if (mIToolbar != null){
            mToolbar = mIToolbar.getToolbar();
        }

        if (mIToolbar == null || mToolbar ==null){

            //不设置IToolbar，或不设置view，视为不使用toolbar

        }else { //添加设置的toolbar的view
            mSurfaceView.addView(mToolbarContainer);

            //交换背景色，基于微弱性能考虑
            mToolbarContainer.setBackground(mToolbar.getBackground());
            mToolbar.setBackground(null);
            mToolbarContainer.addView(mToolbar);
            //手动添加顶部margin
            if (isFullScreen){
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mToolbar.getLayoutParams();
                int statusBarHeight = SPUtils.getInstance().getInt(BaseConfig.STATUS_BAR_HEIGHT);
                //有时状态栏获取不成功，此处如果状态栏像素小于10，认为没有获取到状态栏，给一个默认的状态栏高度
                statusBarHeight = statusBarHeight <10 ? (int) getResources().getDimension(R.dimen.y46) : statusBarHeight;
                params.topMargin = statusBarHeight;
                mToolbar.setLayoutParams(params);
            }
        }

//        //不为空时添加无网络提示的view，不需要相应方法子类空实现即可
//        if (mNoNetworkTipView != null){
//            mSurfaceView.addView(mNoNetworkTipView);
//        }

        mSurfaceView.addView(mContentContainer);
    }

    /**
     * 初始化SwipeLayout
     */
    private void initSwipeLayout() {

        mSwipeLayout.setFitsSystemWindows(false);

        mSwipeLayout.addView(mBottomView);
        mSwipeLayout.addView(mSurfaceView);

        mSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        mSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, mBottomView);

        mSwipeLayout.setTopSwipeEnabled(false);
        mSwipeLayout.setRightSwipeEnabled(false);
        mSwipeLayout.setBottomSwipeEnabled(false);
        mSwipeLayout.setLeftSwipeEnabled(true);  //支持左侧向右滑动

        mSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                mSwipeLayout.setVisibility(View.GONE);
                onBackPressed();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                float mBottomViewAlpha = 1f - leftOffset / (float) ScreenUtils.getScreenWidth();
                mBottomView.setAlpha(mBottomViewAlpha);
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
    }

    /**
     * 获取Toolbar的容器
     *
     * @return
     * @Desc： SDK中的Toolbar对沉浸式状态栏支持不太好，软键盘弹出，在沉浸式状态下，toolbar高度会拉伸，此处给
     * toolbar设置外层容器，外层容器支持沉浸式，toolbar不会拉伸，当然外层容器的背景色作为了toolbar的背景色
     */
    private void initToolbarContainer() {
        mToolbarContainer = new FrameLayout(this);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mToolbarContainer.setLayoutParams(mLayoutParams);
        mToolbarContainer.setFitsSystemWindows(false);
    }

//    /**
//     * 对于不需要网络提示的页面，子类需要对该方法空实现
//     */
//    protected void initNoNetworkTipView(){
//        mNoNetworkTipView = View.inflate(this, R.layout.view_no_network_tip,null);
//    }

//    /**
//     * 获取下拉选项的当前状态。<br>
//     * false : 1、没有下拉选项，2、向下状态。<br>
//     * true : 1、向上状态。
//     */
//    protected boolean getIsDropDownOptionsState() {
//        return mDropDownOptionsState;
//    }
//
//    /**
//     * 如果子页面需要在Toolbar的TitleTextView右边增加下拉选项，则在此初始化下拉选项配置
//     */
//    private void initTitleTextViewDropDownOptions() {
//
//        initAndReplaceTitleDrawable(R.drawable.down);
//
//        mDefaultToolbarTittleText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDropDownOptionsState) {
//                    mDropDownOptionsState = false;
//                } else {
//                    mDropDownOptionsState = true;
//                }
//                defaultToolbarTitleCallback(mDefaultToolbarTittleText, mDropDownOptionsState);
//            }
//        });
//    }
//
//    /**
//     * NORMAL类型的Toobar，右边的ImageView点击回调
//     */
//    protected void defaultToolbarMenuCallback(ImageView rightImageView) {
//
//    }
//
//    /**
//     * Toolbar中间的TitleTextView点击回调<br><br>
//     * 如果子页面需要下拉选项配置，则可重载此方法进行自定义点击事件。<br><br>
//     * 子类可以 <b>Super</b> 来更改此变量值。
//     */
//    protected void defaultToolbarTitleCallback(TextView mDefaultToolbarTittleText, boolean mDropDownOptionsState) {
//        this.mDropDownOptionsState = mDropDownOptionsState;
//        if (mDropDownOptionsState) {
//            initAndReplaceTitleDrawable(R.drawable.top);
//        } else {
//            initAndReplaceTitleDrawable(R.drawable.down);
//        }
//    }


    /**********************************************网络连接状况相关提示**********************************************/

//    @Override
//    public void showNoNetworkTipView(){
//        if (mNoNetworkTipView != null){
//            mNoNetworkTipView.setVisibility(View.VISIBLE);
//        }
//    }

//    @Override
//    public void hideNoNetworkTipView(){
//        if (mNoNetworkTipView != null){
//            mNoNetworkTipView.setVisibility(View.GONE);
//        }
//    }

    /************************************子类可调用方法*************************************/

    public SwipeLayout getSwipeLayout() {
        return mSwipeLayout;
    }

    protected View getBottomView() {
        return mBottomView;
    }

    protected LinearLayout getSurfaceView() {
        return mSurfaceView;
    }

    protected FrameLayout getContentContainer() {
        return mContentContainer;
    }

    protected View getContentView() {
        return mContentView;
    }

    protected FrameLayout getToolbarContainer() {
        return mToolbarContainer;
    }

    /**
     * 该方法用于决定SurfaceView也就是ReorderLinearLayout的前两个子View的绘制顺序
     * 当界面使用了Toolbar，同时界面有edit，弹出软键盘，界面除toolbar外的布局整体上移，会遮挡toolbar，该方法返回true，则内容视图先于toolbar绘制，上移内容视图不遮挡toolbar
     *
     * @return
     */
    protected boolean getSurfaceViewDrawingChildrenOrderEnabled() {
        return false;
    }

    /**
     * 对Toolbar进行配置
     */
    protected abstract IToolbar getIToolbar();
}
