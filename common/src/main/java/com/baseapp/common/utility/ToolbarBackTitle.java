package com.baseapp.common.utility;

import android.widget.ImageView;
import android.widget.TextView;

import com.baseapp.common.base.config.ToolbarConfig;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.base.ui.BaseToolbar;

/**
 * Created by Administrator on 2018/3/27 0027.
 * @Desc: 返回按键和标题的toolbar封装
 */

public class ToolbarBackTitle extends BaseToolbar{

    private String mTitle;

    public ToolbarBackTitle(BaseActivity activity, String tittle) {
        super(activity);
        this.mTitle = tittle;
    }

    @Override
    public ToolbarConfig getToolbarConfig() {
        return ToolbarConfig.BACK_WITH_TITLE;
    }

    @Override
    public void getTitleTextView(TextView mTitleTextView) {
        super.getTitleTextView(mTitleTextView);
    }

    @Override
    public void getTitleLeftView(ImageView mImageView) {
        super.getTitleLeftView(mImageView);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
