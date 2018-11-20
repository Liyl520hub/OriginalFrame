package com.baseapp.common.utility;

import com.baseapp.common.base.config.ToolbarConfig;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.base.ui.BaseToolbar;

/**
 * Created by Administrator on 2018/3/27 0027.
 * @Desc: 仅仅有标题的toolbar的封装
 */

public class ToolbarTitle extends BaseToolbar{

    private String mTitle;

    public ToolbarTitle(BaseActivity activity, String title) {
        super(activity);
        this.mTitle = title;
    }

    @Override
    public ToolbarConfig getToolbarConfig() {
        return ToolbarConfig.JUST_TITLE;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
