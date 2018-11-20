package com.baseapp.common.utility;

import android.widget.ImageView;
import android.widget.TextView;

import com.baseapp.common.base.config.ToolbarConfig;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.base.ui.BaseRightToolbar;

/**
 * Created by Administrator on 2018/3/27 0027.
 * @Desc: 返回按键和标题的toolbar封装
 */

public class ToolbarRightTitle extends BaseRightToolbar{

    private String mTitle;
    private String rightTitle;
    ViewClick viewClick;
    public ToolbarRightTitle(BaseActivity activity, String tittle,String rightTitle) {
        super(activity);
        this.mTitle = tittle;
        this.rightTitle = rightTitle;
    }
    public ToolbarRightTitle(BaseActivity activity, String tittle,String rightTitle,ViewClick viewClick) {
        super(activity);
        this.mTitle = tittle;
        this.rightTitle = rightTitle;
        this.viewClick=viewClick;
    }

    public void setViewClick(ViewClick viewClick) {
        this.viewClick = viewClick;
    }

    @Override
    public void setRightTitle(String rightTitle) {
        getTv_right().setText(rightTitle);
    }

    @Override
    public ToolbarConfig getToolbarConfig() {
        return ToolbarConfig.JUST_RIGHT;
    }

    @Override
    public void getTitleTextView(TextView mTitleTextView) {
        super.getTitleTextView(mTitleTextView);

    }

    @Override
    public void getTitleLeftView(ImageView mImageView) {

    }

    @Override
    public void onRightTitleClicked() {
        super.onRightImageClicked();
        viewClick.rightClick();
    }


    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getRightTitle() {
        return rightTitle;
    }

    public interface ViewClick{
        void rightClick();
    }

}
