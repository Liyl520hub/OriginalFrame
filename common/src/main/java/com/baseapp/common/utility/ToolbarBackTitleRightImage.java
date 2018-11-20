package com.baseapp.common.utility;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.baseapp.common.base.config.ToolbarConfig;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.base.ui.BaseRightToolbar;

/**
 * Created by Administrator on 2018/4/18.
 *
 * @Desc: 返回按键、标题的toolbar、右侧图片
 */
public class ToolbarBackTitleRightImage extends BaseRightToolbar {
    private String mTitle;
    private Drawable rightDrawable;
    ViewClick viewClick;

    public ToolbarBackTitleRightImage(BaseActivity activity, String tittle, Drawable rightDrawable, ViewClick viewClick) {
        super(activity);
        this.mTitle = tittle;
        this.rightDrawable = rightDrawable;
        this.viewClick = viewClick;
    }

    @Override
    public ToolbarConfig getToolbarConfig() {
        return ToolbarConfig.NORMAL;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void getTitleLeftView(ImageView mImageView) {

    }

    @Override
    public String getRightTitle() {
        return null;
    }

    @Override
    public void onRightTitleClicked() {

    }

    @Override
    public void onRightImageClicked() {
        super.onRightImageClicked();
        viewClick.rightClick();
    }

    @Override
    public Drawable getToolbarRightDrawable() {
        return rightDrawable;
    }

    public interface ViewClick {
        void rightClick();
    }

}
