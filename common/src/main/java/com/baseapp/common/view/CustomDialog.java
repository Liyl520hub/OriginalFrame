package com.baseapp.common.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.ViewGroup;

import com.baseapp.common.utility.ActivityManager;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;

/**
 * @company: Coolbit
 * created by {Android-Dev01} on 2018/4/10 0010 上午 11:53
 * @desc:
 */

public class CustomDialog extends AppCompatDialog {

    private boolean flag_double_exit = false; //两次点击退出标志位
    private int mContentViewGravity; //指定ContentView的gravity
    private View mLoadingView; //网络请求LoadingV

    public CustomDialog(Context context, int theme, int gravity) {
        super(context, theme);
        this.mContentViewGravity = gravity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent); //去除decorView的深灰色背景
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
//        int mScreenHeight = ScreenUtils.getScreenHeight();
//        int mStatusBarHeight = BarUtils.getStatusBarHeight();
//        int mDialogHeight = mScreenHeight - mStatusBarHeight;
//
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mDialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : mDialogHeight); //指定ContentView全屏
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT); //指定ContentView全屏
        getWindow().setGravity(mContentViewGravity);
    }

    /**
     * dialog显示网络加载loadingview
     */
    public void showLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = View.inflate(getContext(), com.baseapp.common.R.layout.view_loading, null);

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

        ((ViewGroup) getWindow().getDecorView()).addView(mLoadingView);

    }

    /**
     * dialog隐藏网络加载loadingview
     */
    public void hideLoadingView() {
        if (mLoadingView != null) {
            if (mLoadingView.getParent() != null) {
                ViewGroup parent = (ViewGroup) mLoadingView.getParent();
                parent.removeView(mLoadingView);
            }
        }
    }

    public void setDoubleClickExitFlag(boolean exitByDoubleClick){
        flag_double_exit = exitByDoubleClick;
    }

    @Override
    public void onBackPressed() {

        if (flag_double_exit){
            ActivityManager.getInstance().exitByDoubleClick(System.currentTimeMillis());
            return;
        }
        super.onBackPressed();
    }
}
