package com.baseapp.common.base.callback;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baseapp.common.base.config.ToolbarConfig;

/**
 * Created by Administrator on 2018/3/27 0027.
 * @Desc： Toolbar的接口
 */

public interface IToolbar {

    Toolbar getToolbar();

    ToolbarConfig getToolbarConfig();

    String getTitle();

    void getTitleTextView(TextView mTitleTextView);

    void getTitleLeftView(ImageView mImageView);

    Drawable getToolbarRightDrawable();

    void onTitleClicked(String tittle);

    void onRightImageClicked();
}
