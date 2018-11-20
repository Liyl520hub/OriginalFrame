package com.baseapp.common.base.adapter;


import android.content.Context;
import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/*
 *
 * @Desc:RecyclerView的adapter基类
 */
public abstract class BaseRecyclerViewAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T,BaseViewHolder> {
    protected Context mContext;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BaseRecyclerViewAdapter(Context context, List<T> data, @LayoutRes int layoutRes) {
        super(data);
        this.mContext=context;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> data) {
        super(data);
        this.mContext=context;
    }
}
