package com.liyl.xxxx.xxx.view.callback;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by Administrator on 2018/4/1.
 */

public interface OnItemClickListener {
    /**
     * Callback method to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param adapter  the adpater
     * @param view     The itemView within the RecyclerView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    void onItemClick(BaseQuickAdapter adapter, View view, int position);
}
