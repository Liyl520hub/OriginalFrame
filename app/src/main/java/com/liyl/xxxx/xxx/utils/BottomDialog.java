package com.liyl.xxxx.xxx.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liyl.xxxx.xxx.R;


/**
 * Created by liyl on 2017/3/20.
 * 底部弹框 带动画
 */

public class BottomDialog extends Dialog {

    private Context mContext;
    /**
     * 0 : 三种选择
     * 1 ：两种选择
     * 2 ：两种选择
     */
    private int type = 0;


    public BottomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public BottomDialog(Context context, int type) {
        super(context, type);
        this.mContext = context;
        this.type = type;

    }

    public static BottomDialog newInstance(Context context) {
        return new BottomDialog(context);
    }

    public static BottomDialog newInstance(Context context, int type) {
        return new BottomDialog(context, type);
    }

    private boolean isAnimation = false;
    private LinearLayout mRootView;
    private OnClickListener mListener;

    public void setListener(OnClickListener listener) {
        mListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.bottom_dialog_choice);
        initData();
        mRootView = (LinearLayout) findViewById(R.id.bottom_dialog_root_view);

        AnimationUtils.slideToUp(mRootView);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.dimAmount=0.6f;
        window.setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dismiss();
                }
                return true;
            }
        });
    }

    private void initData() {

        TextView tvIdcardCilck = (TextView) findViewById(R.id.tv_idcard_cilck);
        TextView tvPassportCilck = (TextView) findViewById(R.id.tv_passport_cilck);
        TextView tvOverseasCilck = (TextView) findViewById(R.id.tv_overseas_cilck);
        Button cancel = (Button) findViewById(R.id.btn_bottom_dialog_cancel);
        switch (type) {
            case 0: {
                tvIdcardCilck.setText("身份证");
                tvPassportCilck.setText("护照");
                tvOverseasCilck.setText("港澳／海外");
            }
            break;
            case 1: {
                tvIdcardCilck.setVisibility(View.GONE);
                tvPassportCilck.setText("拍照");
                tvOverseasCilck.setText("从相册中选择");
            }
            break;
            case 2: {
                tvIdcardCilck.setVisibility(View.GONE);
                tvPassportCilck.setText("男");
                tvOverseasCilck.setText("女");
            }
            break;
            case 3: {
                tvIdcardCilck.setVisibility(View.GONE);
                tvPassportCilck.setText("拍照");
                tvOverseasCilck.setText("从手机相册选择");
            }
            break;
            default:
        }
        tvIdcardCilck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.super.dismiss();
                mListener.tvIdcardCilckListener();
            }
        });
        tvPassportCilck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.super.dismiss();
                mListener.tvPassportCilckListener();

            }
        });
        tvOverseasCilck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.super.dismiss();
                mListener.tvOverseasCilckListener();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void dismiss() {
        if (isAnimation) {
            return;
        }
        isAnimation = true;
        AnimationUtils.slideToDown(mRootView, new AnimationUtils.AnimationListener() {
            @Override
            public void onFinish() {
                isAnimation = false;
                BottomDialog.super.dismiss();
            }
        });
    }

    public interface OnClickListener {
        /**
         * 身份证
         */
        void tvIdcardCilckListener();

        /**
         * 护照
         */
        void tvPassportCilckListener();

        /**
         * 港澳、海外
         */
        void tvOverseasCilckListener();
    }
}
