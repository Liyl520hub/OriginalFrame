package com.yjj.coach.kaolakaola.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.baseapp.common.base.callback.IToolbar;
import com.baseapp.common.base.ui.BaseActivity;
import com.yjj.coach.kaolakaola.R;
import com.yjj.coach.kaolakaola.ui.MainActivity;

import butterknife.BindView;

/**
 * @author lyl
 * <p>
 * created 2018/11/19
 * <p>
 * class use:
 */
public class LoginActivity extends BaseActivity{

    @BindView(R.id.tv_jump)
    TextView tvJump;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }

    @Override
    protected IToolbar getIToolbar() {
        return null;
    }
}
