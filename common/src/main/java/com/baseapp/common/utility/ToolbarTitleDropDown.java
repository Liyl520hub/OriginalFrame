package com.baseapp.common.utility;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.baseapp.common.R;
import com.baseapp.common.base.config.ToolbarConfig;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.base.ui.BaseToolbar;
import com.blankj.utilcode.util.LogUtils;

/**
 *
 * @author Administrator
 * @date 2018/3/28
 */

public class ToolbarTitleDropDown extends BaseToolbar {
    private String mTitle;
    private boolean isChange = false;
    public ToolbarTitleDropDown(BaseActivity activity, String title) {
        super(activity);
        this.mTitle = title;
    }

    @Override
    public ToolbarConfig getToolbarConfig() {
        return ToolbarConfig.BACK_WITH_TITLE;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void onTitleClicked(String title) {
        super.onTitleClicked(title);
    }

    @Override
    public void getTitleTextView(TextView mTitleTextView) {
        super.getTitleTextView(mTitleTextView);
        Drawable mDrawable = ContextCompat.getDrawable(mActivity.mContext, R.drawable.top);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        mTitleTextView.setCompoundDrawables(null,null,mDrawable,null);
    }
}
