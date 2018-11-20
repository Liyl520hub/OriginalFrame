package com.baseapp.common.base.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baseapp.common.base.BasePresenter;
import com.baseapp.common.base.ui.BaseFragment;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 子类含有ViewPager时，可以继承此类。主要解决viewPager里第一个Fragment第一次初始化时不自动加载BUG。
 * @author Administrator
 * @date 2018/3/8
 */

public abstract class BaseViewPagerFragment<T extends BasePresenter, R extends MultiItemEntity> extends BaseFragment<T, R> {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getFragmentListData() != null && getFragmentListData().size() > 0) {
            autoRefresh();
            getFragmentListData().get(0).setLoadableWhenInit(true);
        }

    }

    /**
     * 返回子类Fragment的list集合
     * */
    protected abstract List<BaseFragment> getFragmentListData();
}
