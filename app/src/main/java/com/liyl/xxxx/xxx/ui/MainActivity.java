package com.liyl.xxxx.xxx.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.baseapp.common.base.callback.IToolbar;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.base.ui.BaseFragment;
import com.baseapp.common.baserx.RxClickTransformer;
import com.blankj.utilcode.util.SPUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jakewharton.rxbinding2.view.RxView;
import com.liyl.xxxx.xxx.R;
import com.liyl.xxxx.xxx.app.GlobalApp;
import com.liyl.xxxx.xxx.ui.community.fragment.CommunityFragment;
import com.liyl.xxxx.xxx.ui.main.fragment.MainFragment;
import com.liyl.xxxx.xxx.ui.message.fragment.MessageFragment;
import com.liyl.xxxx.xxx.view.CustomViewPager;
import com.liyl.xxxx.xxx.view.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author liyalei
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.activity_main_viewpager)
    CustomViewPager mViewPager;
    @BindView(R.id.activity_main_view_container)
    FrameLayout activityMainViewContainer;
    @BindView(R.id.m_main_tab_layout)
    CommonTabLayout mMainTabLayout;
    @BindView(R.id.main_root_top)
    ConstraintLayout mainRootTop;

    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();
    private MainFragment mMainFragment;
    private MessageFragment mMessageFragment;
    private CommunityFragment mCommunityFragment;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.mipmap.main
            , R.mipmap.message
            , R.mipmap.community};
    private int[] mIconSelectIds = {
            R.mipmap.main_selected
            , R.mipmap.message_selected
            , R.mipmap.community_selected};

    @Override
    protected IToolbar getIToolbar() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        SPUtils.getInstance().put(GlobalApp.SHOULD_SHOW_GUIDE_ACTIVITY, false);
        //设置双击退出
        setDoubleClickExit(true);
        initViewPager();
        initCommonTabLayout();
        initClickEvents();
    }

    private void initViewPager() {
        mMainFragment = new MainFragment();
        mMessageFragment = new MessageFragment();
        mCommunityFragment = new CommunityFragment();
        mFragmentList.add(mMainFragment);
        mFragmentList.add(mMessageFragment);
        mFragmentList.add(mCommunityFragment);
        MainPagerAdapter mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
        mViewPager.setCanScroll(false);
        mViewPager.enablePagerChangeAnimation(false);
    }

    private void initCommonTabLayout() {
        String[] coinNameArray = getResources().getStringArray(R.array.mainTab);
        for (int i = 0; i < coinNameArray.length; i++) {
            mTabEntities.add(new TabEntity(coinNameArray[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mMainTabLayout.setTabData(mTabEntities);
        mMainTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                switch (position) {
                    case 0:
                        mViewPager.setCurrentItem(position);
                        break;
                    case 1:
                        mViewPager.setCurrentItem(position);
                        break;
                    case 2:
                        mViewPager.setCurrentItem(position);
                        break;
                    default:
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initClickEvents() {

    }
}
