package com.baseapp.common.base.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baseapp.common.alerter.Alerter;
import com.baseapp.common.base.BasePresenter;
import com.baseapp.common.base.adapter.BaseRecyclerViewAdapter;
import com.baseapp.common.baserx.RxClickTransformer;
import com.baseapp.common.http.error.ErrorCode;
import com.baseapp.common.utility.MultiItemPlaceHolder;
import com.baseapp.common.utils.TUtil;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jakewharton.rxbinding2.view.RxView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/1/12 0012.
 *
 * @Desc Fragment基类
 * 1、如果不进行网络请求，泛型不用传递
 * 2、如果进行网络请求，需要传递泛型，当不使用列表的时候，第二个泛型请传递{@link MultiItemPlaceHolder}作为占位符，时间问题没有优化设计
 * 3、Fragment没有封装Toolbar，如有需要请自行实现。
 */

public abstract class BaseFragment<T extends BasePresenter, R extends MultiItemEntity> extends Fragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    //日志TAG
    public final String TAG = getClass().getSimpleName() + "---";

    protected Context mContext;
    protected BaseActivity mActivity;
    protected View mRootView;
    public T mPresenter;
    protected Unbinder mBinder;
    /**
     * 是否是下拉刷新模式
     */
    protected boolean isRefreshMode = true;
    /**
     * 分页加载当前请求页
     */
    protected int mCurrentPage = 1;
    /**
     * RecyclerView的adapter
     */
    protected BaseRecyclerViewAdapter<R> mRecyclerViewAdapter;
    /**
     * 服务器返回的数据总页数
     */
    protected int mTotalPages = -1;
    /**
     * RecyclerView的adapter的数据集
     */
    protected List<R> mDataList;
    private View mEmptyView;
    /**
     * 空视图容器
     */
    private FrameLayout mEmptyViewContainer;
    /**
     * 网络加载loading视图的容器
     */
    private FrameLayout mLoadingViewContainer;
    private boolean isVisibleToUser = false;
    /**
     * 空界面或错误界面图片
     */
    private ImageView mEmptyErrorImage;
    /**
     * 空界面或错误界面描述信息
     */
    private TextView mEmptyErrorDescText;
    /**
     * 空界面或错误界面刷新按键
     */
    private TextView mEmptyErrorRefreshButton;
    private String mEmptyListDataMessage = "列表数据为空的显示信息请调用基类相关方法设置";

    private TextView mEmptyErrorTipText;
    /**
     * 空界面或错误界面刷新按键
     */
    @DrawableRes
    private int mEmptyErrorViewImageResource = -1;
    private String mEmptyErrorViewTipMessage = "列表数据为空的显示信息请调用基类相关方法设置";

    private static final int CODE_LIST_NO_NETWORK = 1; //列表请求无网络码制，显示错误空界面视图时使用
    private static final int CODE_EMPTY_LIST_DATA = 2;
    private static final int CODE_REFRESH_FAILED = 3; //刷新接口请求失败
    private View mLoadingView;

    /**
     * 设置当初始化的时候，可以执行lazyLoad加载
     * <h3>注意：只有子类Fragment是父类里ViewPager第一个Item时才进行设置的<h3/>
     */
    private boolean mCanBeLoadedWhenInitialized = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        //重新进入Fragment
        if (mRootView != null && isVisibleToUser) {
            lazyLoad();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        mBinder = ButterKnife.bind(this, mRootView);

        this.mActivity = (BaseActivity) getActivity();

        mPresenter = TUtil.getT(this, 0);

        if (mPresenter != null) {
            mPresenter.setActivity(mActivity);
            mPresenter.setFragment(this);
        }

        return mRootView;
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPresenter();

        initSmartRefreshLayout();

        initLoadMoreAdapter();

        initView();

        if (mCanBeLoadedWhenInitialized) {
            autoRefresh();
        }

    }

    /**
     * 设置当初始化的时候，可以执行autoRefresh()加载
     * <h3>注意：这个方式是只在子类Fragment是父类里ViewPager第一个Item时才进行设置的<h3/>
     */
    public void setLoadableWhenInit(boolean mCanBeLoadedWhenInitialized) {
        this.mCanBeLoadedWhenInitialized = mCanBeLoadedWhenInitialized;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    /**
     * 初始化刷新数据方法，下拉刷新使用SmartRefreshLayout,上拉加载使用BaseRecycelrViewAdapterHelper
     */
    public void initSmartRefreshLayout() {
        if (getSmartRefreshLayout() != null) {

            //下拉刷新
            getSmartRefreshLayout().setRefreshHeader(new MaterialHeader(mActivity));
            getSmartRefreshLayout().setEnableHeaderTranslationContent(false);
            getSmartRefreshLayout().setEnableRefresh(true);
            //使用Adapter的上拉加载
            getSmartRefreshLayout().setEnableAutoLoadmore(false);
            getSmartRefreshLayout().setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    isRefreshMode = true;
                    mCurrentPage = 1;
                    mDataList = null;

                    try { //此处没有申请运行时权限readphonestate
                        if (!NetworkUtils.isConnected()){
                            if (mRecyclerViewAdapter.getData().size() > 0){
                                Alerter.create(mActivity)
                                        .setTitle("当前无网络，请检查网络")
                                        .setBackgroundColorInt(Color.parseColor("ff5640"))
                                        .setIcon(com.baseapp.common.R.drawable.icon_alert)
                                        .enableSwipeToDismiss()
                                        .setDuration(1000)
                                        .show();
                            }else {
                                showEmptyErrorView(CODE_LIST_NO_NETWORK,mEmptyErrorViewTipMessage);
                            }
                            getSmartRefreshLayout().finishRefresh();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    initListRequest(mCurrentPage);
                }
            });

        }

    }

    /**
     * 初始化上拉加载adapter
     */
    public void initLoadMoreAdapter() {
        mRecyclerViewAdapter = getRecyclerViewAdapter();

        if (mRecyclerViewAdapter != null && getRecyclerView() != null) {

            getRecyclerView().setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

            getRecyclerView().setAdapter(mRecyclerViewAdapter);
            mRecyclerViewAdapter.bindToRecyclerView(getRecyclerView());

            mRecyclerViewAdapter.setOnItemClickListener(this);
            mRecyclerViewAdapter.setOnItemChildClickListener(this);

            if (enableAdapterLoadMore()) {
                mRecyclerViewAdapter.setEnableLoadMore(true);
                mRecyclerViewAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        isRefreshMode = false;
                        mCurrentPage++;
                        initListRequest(mCurrentPage);
                    }
                }, getRecyclerView());
            } else {
                mRecyclerViewAdapter.setEnableLoadMore(false);
            }

        }
    }

    /************************************************************子类必须实现的抽象方法**********************************************************************/

    // </editor-fold>
    //<editor-fold desc="抽象方法 abstract method">

    /**
     * 设置布局资源id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 设置SmartRefreshLayout,界面使用下拉刷新时设置
     *
     * @return
     */
    protected abstract SmartRefreshLayout getSmartRefreshLayout();


    /**
     * 设置RecyclerView，界面使用列表功能时设置
     *
     * @return
     */
    protected abstract RecyclerView getRecyclerView();

    /**
     * 设置RecyclerView的adapter，界面使用列表功能时设置
     *
     * @return
     */
    protected abstract BaseRecyclerViewAdapter<R> getRecyclerViewAdapter();

    /**
     * 使能BaseRecyclerViewAdapterHelper的上拉加载更多。
     *
     * @return
     */
    protected abstract boolean enableAdapterLoadMore();


    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    protected abstract void initPresenter();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 懒加载，在Fragment未销毁且重新进入时会调用。
     * 注意，所谓重新进入是指Fragment作为ViewPager的一个pager切换为用户可见的pager。
     * 嵌套viewpager的fragment暂未验证
     */
    protected abstract void lazyLoad();


    /**
     * 初始化列表数据加载
     */
    protected void initListRequest(int page) {

    }


    /*********************************************关于刷新的三个重要方法**********************************************/

    // </editor-fold>
    //<editor-fold desc="列表相关方法  Recycler Related">

    /**
     * SmartRefreshLayout  自动刷新加载数据
     */
    public void autoRefresh() {
        if (getSmartRefreshLayout() != null) {
            getSmartRefreshLayout().autoRefresh();
        }
    }

    /**
     * 加载列表数据成功时重置状态，已经包含数据加载功能
     */
    public void resetStateWhenLoadDataSuccess(@Nullable List<R> dataList) {

        if (getSmartRefreshLayout() != null) {
            if (isRefreshMode) {
                getSmartRefreshLayout().finishRefresh(true);
                if (mRecyclerViewAdapter != null) {
                    mRecyclerViewAdapter.setNewData(dataList);
                    getRecyclerView().smoothScrollToPosition(0);
                    mRecyclerViewAdapter.disableLoadMoreIfNotFullPage();
                    if (mRecyclerViewAdapter.getData() == null || mRecyclerViewAdapter.getData().size() == 0) {
                        showEmptyErrorView(CODE_EMPTY_LIST_DATA, mEmptyErrorViewTipMessage);
                    }else {
                        //以下一行代码是为了避免发布按键遮挡“没有更多数据”提示，此处进行了全局处理
                        if (getRecyclerView().getPaddingBottom() == 0){
                            getRecyclerView().setPadding(0,0,0,50);
                            getRecyclerView().setClipToPadding(false);
                        }
                    }
                }
            }
        }

        if (mRecyclerViewAdapter != null && enableAdapterLoadMore()) {
            if (!isRefreshMode) {
                mRecyclerViewAdapter.addData(dataList);
                if (dataList == null || dataList.isEmpty()) {
                    mRecyclerViewAdapter.loadMoreEnd();
                } else {
                    mRecyclerViewAdapter.loadMoreComplete();
                }
            }
        }


    }

    /**
     * 加载列表数据失败时重置状态
     * @param errorCode
     * @param message
     */
    public void resetStateWhenLoadDataFailed(int errorCode, String message) {
        if (getSmartRefreshLayout() != null) {
            if (isRefreshMode) {
                getSmartRefreshLayout().finishRefresh(false);
                if (mRecyclerViewAdapter.getData().size() > 0){
                     //刷新时列表有数据
                    if (!"9107".equals(message)) {
                        ToastUtils.showShort(message);
                    }
                }else {
                    showEmptyErrorView(CODE_REFRESH_FAILED, message);
                }
            }
        }

        if (mRecyclerViewAdapter != null && enableAdapterLoadMore()) {
            if (!isRefreshMode) {
                mCurrentPage--;
                mRecyclerViewAdapter.loadMoreFail();
            }
        }

    }


    /**
     * 获取分页加载当前页,也可以直接使用mCurrentPage
     *
     * @return
     */
    protected int getCurrentPage() {
        return this.mCurrentPage;
    }

    /**********************************************************************************************************************************************/

    // </editor-fold>
    //<editor-fold desc="空界面错误界面">

    // TODO: 2018/2/1 0001 添加布局文件

    /**
     * 显示网络加载空视图
     *
     * @param errorCode {@link ErrorCode} 中的错误码
     * @param message
     */
    public void showEmptyErrorView(int errorCode, String message) {
        mRecyclerViewAdapter.setHeaderAndEmpty(true); //这样设置，避免显示空界面时下标越界崩溃
//        if (mEmptyErrorViewImageResource == -1){
//            throw new IllegalArgumentException("使用空界面视图，请调用setEmptyErrorViewData()设置界面图片等数据");
//        }

        if (mEmptyView == null) {
            mEmptyView = View.inflate(mContext, com.baseapp.common.R.layout.view_empty_error, null);
            mEmptyErrorImage = mEmptyView.findViewById(com.baseapp.common.R.id.view_empty_error_image);
            mEmptyErrorTipText = mEmptyView.findViewById(com.baseapp.common.R.id.view_empty_error_tip);

        }

        mEmptyErrorImage.setImageResource(mEmptyErrorViewImageResource);
        mEmptyErrorTipText.setText(message);

        if (errorCode == CODE_LIST_NO_NETWORK || errorCode == CODE_REFRESH_FAILED){
            mEmptyErrorTipText.setBackground(getResources().getDrawable(com.baseapp.common.R.drawable.shape_round_corner_stroke_e4e4e4));
        }else {
            mEmptyErrorTipText.setBackground(null);
        }
        switch (errorCode) {
            //列表空数据
            case CODE_LIST_NO_NETWORK:
            case CODE_EMPTY_LIST_DATA:
            case CODE_REFRESH_FAILED:
                if (mRecyclerViewAdapter == null) {
                    throw new IllegalArgumentException("使用列表空数据空视图，你必须在getRecyclerViewAdapter()方法中返回RecyclerView的adapter对象");
                }

                //有数据时为了避免发布按键遮挡“没有更多数据”，设置了padding，此处重置0，防止界面视图和底部有间隔
                if (getRecyclerView().getPaddingBottom() > 0){
                    getRecyclerView().setPadding(0,0,0,0);
                }

                if (errorCode == CODE_LIST_NO_NETWORK) {
                    mEmptyErrorImage.setImageResource(com.baseapp.common.R.drawable.empty_error_view_no_network);
                    mEmptyErrorTipText.setText("网络连接已断开，请点击重试");
                    RxView.
                            clicks(mEmptyView).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
//                                    clickEmptyErrorView();
                                    autoRefresh();
                                }
                            });

                }else if (errorCode == CODE_REFRESH_FAILED){
                    mEmptyErrorImage.setImageResource(com.baseapp.common.R.drawable.empty_error_view_no_transaction);
                    mEmptyErrorTipText.setText("数据获取失败，请点击重试");
                    RxView.
                            clicks(mEmptyView).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
//                                    clickEmptyErrorView();
                                    autoRefresh();
                                }
                            });
                }

                if (mRecyclerViewAdapter.getEmptyView() == null) {
                    if (mEmptyView.getParent() != null) {
                        ViewGroup parent = (ViewGroup) mEmptyView.getParent();
                        parent.removeView(mEmptyView);
                    }
                }

                mRecyclerViewAdapter.setEmptyView(mEmptyView);

                break;

            //接口请求错误，错误码404
            case ErrorCode.CODE_NOT_FOUND:

                if (mEmptyViewContainer == null) {
                    throw new IllegalStateException("使用空视图你必须在初始化时调用方法setEmptyViewContainer（）设置空视图容器");
                }

                if (mEmptyView.getParent() != null) {
                    ViewGroup parent = (ViewGroup) mEmptyView.getParent();
                    parent.removeView(mEmptyView);
                }

                mEmptyViewContainer.addView(mEmptyView);
                break;

            //非列表空数据，非404返回码的错误界面视图
            default:

                if (mEmptyViewContainer == null) {
                    throw new IllegalStateException("使用空视图你必须在初始化时调用方法setEmptyViewContainer（）设置空视图容器");
                }

                if (mEmptyView.getParent() != null) {
                    ViewGroup parent = (ViewGroup) mEmptyView.getParent();
                    parent.removeView(mEmptyView);
                }

                mEmptyViewContainer.addView(mEmptyView);
                break;
        }

    }


    /**
     * 隐藏网络加载空视图
     */
    public void hideEmptyErrorView() {
        if (mEmptyView != null && mEmptyView.getParent() != null) {
            mEmptyViewContainer.removeView(mEmptyView);
        }
    }

    /**
     * 设置列表数据为空时显示的空界面的描述信息
     *
     * @param image
     * @param message
     */
    public void setEmptyErrorViewData(@DrawableRes int image, String message) {
        this.mEmptyErrorViewImageResource = image;
        this.mEmptyErrorViewTipMessage = message;
    }

    // TODO: 2018/2/1 0001 添加布局文件

    /**
     * 警告：该方法请勿手动调用，已经在{@link com.baseapp.common.baserx.RxSubscriber}中进行了调用
     * <p>
     * 开启加载进度View
     */
    public void showLoadingView() {

        if (mLoadingViewContainer == null) {
            mActivity.showLoadingView();
            return;
        }

        if (mLoadingView == null) {
//            mLoadingView = View.inflate(mContext, com.baseapp.common.R.layout.dialog_loading, null);
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

        mLoadingViewContainer.addView(mLoadingView);
    }

    /**
     * 警告：该方法请勿手动调用，已经在{@link com.baseapp.common.baserx.RxSubscriber}中进行了调用
     * <p>
     * 停止加载进度View
     */
    public void dismissLoadingView() {
        if (mLoadingViewContainer == null) {
            mActivity.dismissLoadingView();
            return;
        }

        if (mLoadingView == null) {
            return;
        }

        if (mLoadingView.getParent() != null) {
            ViewGroup parent = (ViewGroup) mLoadingView.getParent();
            parent.removeView(mLoadingView);
        }
    }

    /**********************************************************列表Item点击的回调事件***************************************************************/

    //<editor-fold desc="基类点击方法">
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    /**
     * 点击空界面或错误界面会调用该方法，子类实现该方法进行接口重新请求
     */
    protected void clickEmptyErrorView() {

    }

    /************************************************相关设置方法******************************************************************************/

    // </editor-fold>
    //<editor-fold desc="设置方法">

    /**
     * 设置下拉刷新头
     *
     * @param header
     */
    protected void setRefreshHeader(RefreshHeader header) {
        getSmartRefreshLayout().setRefreshHeader(header);
    }

    /**
     * 设置内容布局的容器
     * 注意：进行设置时，可以和{@link #setEmptyViewContainer(FrameLayout)} 设置的容器是同一个view
     *
     * @return
     */
    protected void setEmptyViewContainer(FrameLayout container) {
        this.mEmptyViewContainer = container;
    }

    /**
     * 设置网络加载loading视图的容器view
     * 说明：如果不调用该方法进行设置，则会默认使用持有fragment的activity的loading视图的容器
     * 注意：进行设置时，可以和{@link #setEmptyViewContainer(FrameLayout)} 设置的容器是同一个view
     *
     * @param container
     */
    protected void setLoadingViewContainer(FrameLayout container) {
        this.mLoadingViewContainer = container;
    }
}
