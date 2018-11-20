package com.liyl.xxxx.xxx.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.baseapp.common.base.callback.IToolbar;
import com.baseapp.common.base.ui.BaseActivity;
import com.baseapp.common.view.CustomDialog;
import com.baseapp.common.view.DialogWrapper;
import com.liyl.xxxx.xxx.R;
import com.liyl.xxxx.xxx.utils.ResourceUtils;

/**
 * @author lyl
 * <p>
 * created 2018/11/19
 * <p>
 * class use:
 */
public class GlobalLoginOutActivity extends BaseActivity {

    public static final String PARAM_KEY_SERVER_MESSAGE = "ServerMessage";
    private CustomDialog mLoginOutDialog;
    private String mServerMessage;


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mServerMessage = getIntent().getStringExtra(PARAM_KEY_SERVER_MESSAGE);

    }

    @Override
    protected IToolbar getIToolbar() {
        return null;
    }

    private void showLoginOutDialog() {
        if (mLoginOutDialog == null) {
            mLoginOutDialog = (CustomDialog) DialogWrapper.
                    tipDialog().
                    context(mActivity).
                    cancelable(false, false).
                    title(ResourceUtils.getString(R.string.tip)).
                    message(mServerMessage).
                    closeImageVisible(false).
                    buttonType(DialogWrapper.BUTTON_SINGLE).
                    singleButtonText(ResourceUtils.getString(R.string.confirm)).
                    buttonClickListener(new DialogWrapper.TipTypeButtonClickListener() {
                        @Override
                        public void onLeftButtonClicked(TextView view) {

                        }

                        @Override
                        public void onSingleButtonClicked(TextView view) {
                            mLoginOutDialog.dismiss();
                            Intent intent = new Intent(GlobalLoginOutActivity.this, LoginActivity.class);
                            startActivity(intent);
                            com.baseapp.common.utility.ActivityManager.getInstance().delayFinishAllExceptSpecificNameActivity("LoginActivity");
                        }

                        @Override
                        public void onRightButtonClicked(TextView view) {

                        }
                    }).
                    build();
        }

        mLoginOutDialog.show();
    }
}
