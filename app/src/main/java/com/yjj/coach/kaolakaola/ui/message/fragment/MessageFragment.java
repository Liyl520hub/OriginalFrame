package com.yjj.coach.kaolakaola.ui.message.fragment;

import android.support.v7.widget.RecyclerView;

import com.baseapp.common.base.adapter.BaseRecyclerViewAdapter;
import com.baseapp.common.base.ui.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yjj.coach.kaolakaola.R;

/**
 * @author lyl
 * <p>
 * created 2018/11/20
 * <p>
 * class use:
 */
public class MessageFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected SmartRefreshLayout getSmartRefreshLayout() {
        return null;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    protected BaseRecyclerViewAdapter getRecyclerViewAdapter() {
        return null;
    }

    @Override
    protected boolean enableAdapterLoadMore() {
        return false;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyLoad() {

    }
}
