package com.liyl.xxxx.xxx.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * @Desc : 可以设置ViewPager是否支持滑动切换页面，是否启用页面切换动画
 */

public class CustomViewPager extends ViewPager {

    private boolean canScroll = true;  //ViewPager是否能够滑动
    private boolean isPagerChangeAnimationEnabled = true;  //ViewPager的pager切换动画使能

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置ViewPager是否能滑动换页
     * @param isCanScroll false 不可滑动换页， true 可以滑动换页
     */
    public void setCanScroll(boolean isCanScroll) {
        this.canScroll = isCanScroll;
    }

    /**
     * 设置ViewPager的pager切换动画使能状态
     * @param isEnabled  false:Pager切换无动画   true：Pager切换有动画
     */
    public void enablePagerChangeAnimation(boolean isEnabled){
        this.isPagerChangeAnimationEnabled = isEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return canScroll && super.onTouchEvent(ev);

    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,isPagerChangeAnimationEnabled);
    }
}
