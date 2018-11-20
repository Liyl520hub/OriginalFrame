package com.baseapp.common.utility;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baseapp.common.R;
import com.baseapp.common.base.config.ToolbarConfig;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.base.ui.BaseToolbar;
import com.baseapp.common.baserx.RxClickTransformer;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;

/**
 * @company: Coolbit
 * created by {Android-Dev02} on 2018/5/3 16:15
 * @desc:
 */
public class ToolbarBackTitleBGColor extends BaseToolbar {
    private String mTitle;
    private TextView mTitleText;
    private boolean enableIcon = true;
    private View rightViewLayout = null;
    public ToolbarBackTitleBGColor(BaseActivity activity, String title) {
        super(activity);
        mTitle = title;
    }

    public ToolbarBackTitleBGColor(BaseActivity activity, String title, View rightViewLayout) {
        super(activity);
        mTitle = title;
        this.rightViewLayout = rightViewLayout;
    }
    public ToolbarBackTitleBGColor(BaseActivity activity, String title, boolean enableIcon) {
        super(activity);
        mTitle = title;
        this.enableIcon = enableIcon;
    }

    @Override
    public Toolbar getToolbar() {
        Toolbar mToolbarView = (Toolbar) View.inflate(mActivity, R.layout.view_default_toolbar_bg_white, null);
        mTitleText = mToolbarView.findViewById(R.id.default_toolbar_title);
        if (!enableIcon) {
            mToolbarView.findViewById(R.id.default_toolbar_left_image).setVisibility(View.GONE);
        }
        if (rightViewLayout != null) {
            LinearLayout rightContainer = mToolbarView.findViewById(R.id.default_toolbar_right_container);
            rightContainer.addView(rightViewLayout);
        }
        mTitleText.setText(mTitle);
        getTitleTextView(mTitleText);
        RxView.
                clicks(mToolbarView.findViewById(R.id.default_toolbar_left_image)).
                compose(RxClickTransformer.getClickTransformer()).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mActivity.onBackPressed();
                    }
                });
        return mToolbarView;
    }

    public void getTitleTextView(TextView mTitleTextView) {

    }

    @Override
    public ToolbarConfig getToolbarConfig() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
