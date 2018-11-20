package com.baseapp.common.base.ui;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baseapp.common.R;
import com.baseapp.common.base.callback.IToolbarRight;
import com.baseapp.common.baserx.RxClickTransformer;
import com.baseapp.common.utils.UIUtils;
import com.blankj.utilcode.util.LogUtils;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/3/27 0027.
 *
 * @Desc: Toolbar封装基类
 */

public abstract class BaseRightToolbar implements IToolbarRight {

    protected BaseActivity mActivity;
    protected ImageView mLeftImage;
    protected TextView mTitleText;
    protected TextView tv_right;
    protected ImageView mRightImage;
    protected Toolbar mToolbarView;
    private int background;

    public BaseRightToolbar(BaseActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 对于使用默认布局的toolbar，继承该类的子类请谨慎重写该方法
     * 重写需要针对特定情境实现完成整逻辑
     *
     * @return
     */
    @Override
    public Toolbar getToolbar() {
        mToolbarView = (Toolbar) View.inflate(mActivity, R.layout.view_default_toolbar, null);
        mLeftImage = mToolbarView.findViewById(R.id.default_toolbar_left_image);
        mTitleText = mToolbarView.findViewById(R.id.default_toolbar_title);
        mRightImage = mToolbarView.findViewById(R.id.default_toolbar_right_image);
        tv_right = mToolbarView.findViewById(R.id.tv_right);

//        mActivity.setSupportActionBar(mToolbarView);
//        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbarView.setBackgroundColor(UIUtils.getColor(R.color.white));
        mLeftImage.setImageResource(R.drawable.icon_back);
        mTitleText.setTextColor(UIUtils.getColor(R.color.color_2b2d2e));
        tv_right.setTextColor(UIUtils.getColor(R.color.color_2b2d2e));
        mTitleText.setTextSize(19);
        tv_right.setTextSize(16);
        Toolbar.LayoutParams params=new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT,UIUtils.getDimen(R.dimen.toolBarHeight));
        mToolbarView.setLayoutParams(params);
        switch (getToolbarConfig()) {
            case JUST_BACK:
                mLeftImage.setVisibility(View.VISIBLE);
                mTitleText.setVisibility(View.GONE);
                mRightImage.setVisibility(View.GONE);
                initBackImageListener();
                break;
            case JUST_TITLE:
                mLeftImage.setVisibility(View.GONE);
                mTitleText.setVisibility(View.VISIBLE);
                mRightImage.setVisibility(View.GONE);
                mTitleText.setText(getTitle());
                getTitleTextView(mTitleText);
                initTitleClickListener();
                break;
            case BACK_WITH_TITLE:
                mLeftImage.setVisibility(View.VISIBLE);
                mTitleText.setVisibility(View.VISIBLE);
                mRightImage.setVisibility(View.GONE);
                mTitleText.setText(getTitle());
                getTitleTextView(mTitleText);

                initBackImageListener();
                initTitleClickListener();
                break;
            case NORMAL:
                mLeftImage.setVisibility(View.VISIBLE);
                mTitleText.setVisibility(View.VISIBLE);
                mRightImage.setVisibility(View.VISIBLE);

                if (getToolbarRightDrawable() != null) {
                    mRightImage.setImageDrawable(getToolbarRightDrawable());
                }

                mTitleText.setText(getTitle());
                getTitleTextView(mTitleText);
                initBackImageListener();
                initTitleClickListener();
                initRightImageClickListener();
                break;
            case JUST_RIGHT:
                mLeftImage.setVisibility(View.VISIBLE);
                mTitleText.setVisibility(View.VISIBLE);
                tv_right.setVisibility(View.VISIBLE);
                mRightImage.setVisibility(View.GONE);
                mTitleText.setText(getTitle());
                tv_right.setText(getRightTitle());
                getTitleTextView(mTitleText);
                initBackImageListener();
                initTitleClickListener();
                initRightTitleClickListener();
                break;
        }
        if (background != 0) {
            if (background == UIUtils.getColor(R.color.white)) {
                mLeftImage.setImageDrawable(UIUtils.getDrawable(R.drawable.icon_back));
            }
            mToolbarView.setBackgroundColor(background);
        }
        return mToolbarView;
    }

    @Override
    public void getTitleTextView(TextView mTitleTextView) {
        LogUtils.e("getTitleTextView");
    }

    /**
     * 不使用drawable请传递null
     *
     * @return
     */
    @Override
    public Drawable getToolbarRightDrawable() {
        return null;
    }

    /**
     * 标题点击空实现，有需要点击事件的界面重写该方法
     *
     * @param title
     */
    @Override
    public void onTitleClicked(String title) {

    }

    /**
     * 右侧图标点击空实现，有需要点击事件的界面重写该方法
     */
    @Override
    public void onRightImageClicked() {

    }

    @Override
    public void setRightTitle(String rightTile) {
        mTitleText.setText(rightTile);
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

    /**
     * 初始化标题点击事件
     */
    private void initTitleClickListener() {
        RxView.
                clicks(mTitleText).
                compose(RxClickTransformer.getClickTransformer()).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onTitleClicked(mTitleText.getText().toString());
                    }
                });
    }

    /**
     * 初始化右侧图标点击事件
     */
    private void initRightImageClickListener() {
        RxView.
                clicks(mRightImage).
                compose(RxClickTransformer.getClickTransformer()).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onRightImageClicked();
                    }
                });
    }

    /**
     * 初始化右侧图标点击事件
     */
    private void initRightTitleClickListener() {
        RxView.
                clicks(tv_right).
                compose(RxClickTransformer.getClickTransformer()).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onRightTitleClicked();
                    }
                });
    }

    public TextView getTv_right() {
        return tv_right;
    }

    public BaseRightToolbar setBackground(int color) {
        background = color;
        return this;
    }
}
