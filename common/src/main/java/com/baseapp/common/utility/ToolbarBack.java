package com.baseapp.common.utility;

import com.baseapp.common.base.config.ToolbarConfig;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.base.ui.BaseToolbar;

/**
 * Created by Administrator on 2018/3/27 0027.
 * @Desc: 只有返回按键的Toolbar封装
 */

public class ToolbarBack extends BaseToolbar{

    public ToolbarBack(BaseActivity activity) {
        super(activity);
    }

    @Override
    public ToolbarConfig getToolbarConfig() {
        return ToolbarConfig.JUST_BACK;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
