package com.baseapp.common.utility;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baseapp.common.R;
import com.baseapp.common.base.callback.IToolbar;
import com.baseapp.common.base.config.ToolbarConfig;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.baserx.RxClickTransformer;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;

/**
 * @company: Coolbit
 * created by {Android-Dev02} on 2018/4/9 14:38
 * @desc:
 */

public class ToolbarContainer implements IToolbar {

    protected BaseActivity mActivity;
    private ImageView mLeftImage;
    private LinearLayout mToolbarContainer;
    private int paddingRight,paddingLeft;
    private View mChildView = null;
    ViewClick viewClick;
    public ToolbarContainer(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    public ToolbarContainer(BaseActivity mActivity, View mView) {
        this.mActivity = mActivity;
        mChildView = mView;
        paddingLeft = mActivity.getResources().getDimensionPixelOffset(R.dimen.toolBarHeight);
    }
    public ToolbarContainer(BaseActivity mActivity, View mView,int paddingRight) {
        this.mActivity = mActivity;
        mChildView = mView;
        paddingLeft = mActivity.getResources().getDimensionPixelOffset(R.dimen.toolBarHeight);
        this.paddingRight = paddingRight;
    }
    @Override
    public Toolbar getToolbar() {
        Toolbar mToolbarView = (Toolbar) View.inflate(mActivity, R.layout.view_default_toolbar_container, null);
        mLeftImage = mToolbarView.findViewById(R.id.default_toolbar_left_image);
        mToolbarContainer = mToolbarView.findViewById(R.id.default_toolbar_container);
        mToolbarContainer.setPadding(paddingLeft,0,paddingRight,0);
//        mActivity.setSupportActionBar(mToolbarView);
//        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (mChildView != null) {
            mToolbarContainer.addView(mChildView);
        }
        initBackImageListener();
        return mToolbarView;
    }

    /**
     * 初始化返回按键点击事件
     */
    private void initBackImageListener() {
        RxView.
                clicks(mLeftImage).
                compose(RxClickTransformer.getClickTransformer()).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mActivity.onBackPressed();
                    }
                });

    }

    @Override
    public ToolbarConfig getToolbarConfig() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void getTitleTextView(TextView mTitleTextView) {

    }

    @Override
    public void getTitleLeftView(ImageView mImageView) {

    }

    @Override
    public Drawable getToolbarRightDrawable() {
        return null;
    }

    @Override
    public void onTitleClicked(String tittle) {

    }

    @Override
    public void onRightImageClicked() {
        viewClick.onRightImageClick();
    }
    public interface ViewClick{
        void onRightImageClick();
    }
}
