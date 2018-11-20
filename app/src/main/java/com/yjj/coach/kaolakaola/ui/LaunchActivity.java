package com.yjj.coach.kaolakaola.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.baseapp.common.base.callback.IToolbar;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.utility.ActivityManager;
import com.blankj.utilcode.util.SPUtils;
import com.yjj.coach.kaolakaola.R;
import com.yjj.coach.kaolakaola.app.GlobalApp;
import com.yjj.coach.kaolakaola.ui.login.activity.LoginActivity;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

/**
 * @author liyalei
 * @desc: 启动页
 */

public class LaunchActivity extends BaseActivity {

    private static final String TAG = LaunchActivity.class.getSimpleName();
    @BindView(R.id.banner_guide_background)
    BGABanner mBackgroundBanner;
    @BindView(R.id.banner_guide_foreground)
    BGABanner mForegroundBanner;
    private boolean isFrist;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initBeforeSetContentView() {
        super.initBeforeSetContentView();
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isFrist = SPUtils.getInstance().getBoolean(GlobalApp.SHOULD_SHOW_GUIDE_ACTIVITY, true);
        if (!isTaskRoot()) {
            //解决点击home键，app进入后台，再点击icon，app重新启动的问题。（只是重新实例化了启动页添加到了任务栈，此处直接finish掉）
            ActivityManager.getInstance().finishSpecificActivity(this);
        }
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        //设置双击退出
        setDoubleClickExit(true);
        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
        setListener();
        processLogic();

        mBackgroundBanner.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFrist) {
                    mForegroundBanner.setVisibility(View.VISIBLE);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                    alphaAnimation.setDuration(1000);
                    mForegroundBanner.setAnimation(alphaAnimation);
                    mBackgroundBanner.setVisibility(View.GONE);
                } else {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    finish();
                }
            }
        },1000);


    }


    @Override
    protected IToolbar getIToolbar() {
        return null;
    }


    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, 0, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        mBackgroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.space_cat);
        mForegroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.page_one,
                R.drawable.page_two,
                R.drawable.page_three);
    }

}

