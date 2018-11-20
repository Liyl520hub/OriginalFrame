package com.yjj.coach.kaolakaola.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baseapp.common.base.adapter.BaseRecyclerViewAdapter;
import com.baseapp.common.baserx.RxClickTransformer;
import com.baseapp.common.view.CustomDialog;
import com.baseapp.common.view.PinEntryEditText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import com.jakewharton.rxbinding2.view.RxView;
import com.yjj.coach.kaolakaola.R;
import com.yjj.coach.kaolakaola.app.GlobalApp;
import com.yjj.coach.kaolakaola.utils.ResourceUtils;
import com.yjj.coach.kaolakaola.view.callback.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author
 * @date
 */

public class DialogWrapper {

    //仅提示性Dialog的builder使用进行按键配置
    public static final int BUTTON_SINGLE = 0; //单按键
    public static final int BUTTON_DOUBLE = 1; //双按键
    private static ImageView mDialogTipLayoutEmptyImageView;

    //提示性dialog使用进行按键点击监听
    public interface TipTypeButtonClickListener {

        void onLeftButtonClicked(TextView view);

        void onSingleButtonClicked(TextView view);

        abstract void onRightButtonClicked(TextView view);

    }

    /***************************************************************************************************************************************************/
    /**
     * 获取AlertDialog的builder
     * AlertDialog非系统AlertDialog，此处指View显示在界面中间，给用户提示的弹窗
     *
     * @return TipDialogBuilder
     */
    public static TipVerificationCodeBuilder tipVerificationCodeBuilder() {
        return new TipVerificationCodeBuilder();
    }

    public static class TipVerificationCodeBuilder {
        private Context mContext;
        private TipTypeButtonClickListener mButtonListener;
        private View mAlertDialogView;
        private TextView mLeftButton, mRightButton, mMessageTextView,mTitleTextView;
        private PinEntryEditText passwordView;

        public TextView getLeftButton() {
            return mLeftButton;
        }

        public TextView getMessageTextView() {
            return mMessageTextView;
        }
        public TextView getTitleTextView() {
            return mTitleTextView;
        }

        public TextView getRightButton() {
            return mRightButton;
        }

        public PinEntryEditText getPasswordView() {
            return passwordView;
        }

        private TipVerificationCodeBuilder() {
        }

        /**
         * 配置按键点击监听
         *
         * @param buttonListener
         * @return
         */
        public TipVerificationCodeBuilder buttonClickListener(TipTypeButtonClickListener buttonListener) {
            this.mButtonListener = buttonListener;
            return this;
        }

        /**
         * context
         *
         * @param context
         * @return
         */
        public TipVerificationCodeBuilder context(Context context) {
            this.mContext = context;
            return this;
        }

        public CustomDialog build() {
            if (mContext == null) {
                throw new IllegalArgumentException("请配置上下文环境context");
            }

            final CustomDialog mAlertDialog = new CustomDialog(mContext, R.style.CustomDialogTheme, Gravity.CENTER);
            //为了减少嵌套层级，布局使用了ConstraintLayout为根节点，但是设置了margin值以让ContentView左右两侧不贴近屏幕，为了让margin值生效，第三个参数不为空，请勿再调用Dialog的setContentView()
            mAlertDialogView = View.inflate(mContext, R.layout.dialog_tip_input_verification_code, (ViewGroup) mAlertDialog.getWindow().getDecorView());
            mTitleTextView = mAlertDialogView.findViewById(R.id.dialog_tip_layout_title);
            mMessageTextView = mAlertDialogView.findViewById(R.id.dialog_tip_layout_message);
            passwordView = mAlertDialog.findViewById(R.id.dialog_tip_layout_password_edit_text);
            mLeftButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_left_button);
            mRightButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_right_button);
            ImageView mCloseImage = mAlertDialogView.findViewById(R.id.dialog_tip_layout_close);
            mMessageTextView.setText(mContext.getResources().getString(R.string.tip_send_verification_code));
            if (mButtonListener != null) {
                RxView.
                        clicks(mLeftButton).
                        compose(RxClickTransformer.getClickTransformer()).
                        subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                mButtonListener.onLeftButtonClicked(mLeftButton);
                            }
                        });

                RxView.
                        clicks(mRightButton).
                        compose(RxClickTransformer.getClickTransformer()).
                        subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                mButtonListener.onRightButtonClicked(mRightButton);
                            }
                        });
            }


            RxView.
                    clicks(mCloseImage).
                    compose(RxClickTransformer.getClickTransformer()).
                    subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            mAlertDialog.dismiss();
                        }
                    });

            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);
            return mAlertDialog;
        }
    }

    public static TipInputBuilder tipInputBuilder() {
        return new TipInputBuilder();
    }

    public static class TipInputBuilder {
        private Context mContext;
        private TipTypeButtonClickListener mButtonListener;
        private View mAlertDialogView;
        private TextView mLeftButton, mRightButton;
        public EditText inputEditText;

        public EditText getInputEditText() {
            return inputEditText;
        }

        private TipInputBuilder() {
        }

        /**
         * 配置按键点击监听
         *
         * @param buttonListener
         * @return
         */
        public TipInputBuilder buttonClickListener(TipTypeButtonClickListener buttonListener) {
            this.mButtonListener = buttonListener;
            return this;
        }

        /**
         * context
         *
         * @param context
         * @return
         */
        public TipInputBuilder context(Context context) {
            this.mContext = context;
            return this;
        }

        public Dialog build() {
            if (mContext == null) {
                throw new IllegalArgumentException("请配置上下文环境context");
            }

            final CustomDialog mAlertDialog = new CustomDialog(mContext, R.style.CustomDialogTheme, Gravity.CENTER);

            mAlertDialogView = View.inflate(mContext, R.layout.dialog_modify_nickname, (ViewGroup) mAlertDialog.getWindow().getDecorView());
            inputEditText = mAlertDialogView.findViewById(R.id.dialog_tip_layout_edit_text);
            mLeftButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_left_button);
            mRightButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_right_button);
            mDialogTipLayoutEmptyImageView = mAlertDialogView.findViewById(R.id.m_dialog_tip_layout_empty_imageview);

            inputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        if (mDialogTipLayoutEmptyImageView != null) {
                            mDialogTipLayoutEmptyImageView.setVisibility(View.VISIBLE);
                            mDialogTipLayoutEmptyImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (inputEditText != null) {
                                        inputEditText.getText().clear();
                                    }
                                }
                            });
                        }
                    } else {
                        if (mDialogTipLayoutEmptyImageView != null) {
                            mDialogTipLayoutEmptyImageView.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (mButtonListener != null) {
                RxView.
                        clicks(mLeftButton).
                        compose(RxClickTransformer.getClickTransformer()).
                        subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                mAlertDialog.dismiss();
                                mButtonListener.onLeftButtonClicked(mLeftButton);
                            }
                        });

                RxView.
                        clicks(mRightButton).
                        compose(RxClickTransformer.getClickTransformer()).
                        subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                mButtonListener.onRightButtonClicked(mRightButton);
                            }
                        });
            }

            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);
            return mAlertDialog;
        }
    }

    /**
     * 广告撤销
     */
    public static TipAdvertRevocationBuilder tipAdvertRevocationBuilderBuilder() {
        return new TipAdvertRevocationBuilder();
    }

    public static class TipAdvertRevocationBuilder {
        private Context mContext;
        private TipTypeButtonClickListener mButtonListener;
        private View mAlertDialogView;
        TextView mLeftButton, mRightButton;
        TextView revocationTip1, revocationTip2, revocationTip3;

        public TextView getLeftButton() {
            return mLeftButton;
        }

        public TextView getRightButton() {
            return mRightButton;
        }

        private String tip1, tip2, tip3;

        private TipAdvertRevocationBuilder() {
        }

        /**
         * 配置按键点击监听
         *
         * @param buttonListener
         * @return
         */
        public TipAdvertRevocationBuilder buttonClickListener(TipTypeButtonClickListener buttonListener) {
            this.mButtonListener = buttonListener;
            return this;
        }

        /**
         * @param
         * @return
         */
        public TipAdvertRevocationBuilder setQuota(String tip1, String tip2, String tip3) {
            this.tip1 = tip1;
            this.tip2 = tip2;
            this.tip3 = tip3;
            return this;
        }

        /**
         * context
         *
         * @param context
         * @return
         */
        public TipAdvertRevocationBuilder context(Context context) {
            this.mContext = context;
            return this;
        }

        public Dialog build() {
            if (mContext == null) {
                throw new IllegalArgumentException("请配置上下文环境context");
            }

            final CustomDialog mAlertDialog = new CustomDialog(mContext, R.style.CustomDialogTheme, Gravity.CENTER);

            mAlertDialogView = View.inflate(mContext, R.layout.dialog_advert_revocation, (ViewGroup) mAlertDialog.getWindow().getDecorView());
            revocationTip1 = mAlertDialog.findViewById(R.id.revocation_tip_1);
            revocationTip2 = mAlertDialog.findViewById(R.id.revocation_tip_2);
            revocationTip3 = mAlertDialog.findViewById(R.id.revocation_tip_3);
            ImageView close = mAlertDialog.findViewById(R.id.dialog_tip_layout_title_close);
            revocationTip1.setText(tip1);
            revocationTip2.setText(tip2);
            revocationTip3.setText(tip3);
            mLeftButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_left_button);
            mLeftButton.setText(ResourceUtils.getString(R.string.let_me_think));
            mRightButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_right_button);
            mRightButton.setText(ResourceUtils.getString(R.string.action_ok));
            RxView.
                    clicks(close).
                    compose(RxClickTransformer.getClickTransformer()).
                    subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            mAlertDialog.dismiss();
                        }
                    });

            if (mButtonListener != null) {
                RxView.
                        clicks(mLeftButton).
                        compose(RxClickTransformer.getClickTransformer()).
                        subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                mAlertDialog.dismiss();
                                mButtonListener.onLeftButtonClicked(mLeftButton);
                            }
                        });

                RxView.
                        clicks(mRightButton).
                        compose(RxClickTransformer.getClickTransformer()).
                        subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                mButtonListener.onRightButtonClicked(mRightButton);
                                mAlertDialog.dismiss();
                            }
                        });
            }

            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);
            return mAlertDialog;
        }
    }

    /***************************************************************************************************************************************************/
    /**
     * 获取AlertDialog的builder
     * AlertDialog非系统AlertDialog，此处指View显示在界面中间，给用户提示的弹窗
     *
     * @return TipDialogBuilder
     */
    public static TipDialogBuilder tipDialog() {

        return new TipDialogBuilder();
    }

    public static class TipDialogBuilder {
        private Context mContext; //上下文环境
        private String mAlertTitle;    //提示性弹窗的标题
        private View mContentView; //自定义的ContentView
        private String mAlertMessage;   //提示性弹窗的提示信息
        private boolean isCloseImageVisible = true; //右上角关闭图片按键是否可见,默认可见
        private boolean mCancelable = false; //参照Dialog
        private boolean mCanceledOnTouchOutSide = false; //参照Dialog
        private int mButtonType = BUTTON_SINGLE; //Dialog按键类型，决定Dialog的按键为单按键还是双按键,默认单按键
        private String mLeftButtonText; //左侧按键显示文本
        private String mRightButtonText; //右侧按键显示文本
        private String mSingleButtonText; //单按键显示文本
        private TipTypeButtonClickListener mButtonListener;

        private TipDialogBuilder() {
        }

        /**
         * 配置Context
         *
         * @param context
         * @return
         */
        public TipDialogBuilder context(Context context) {
            this.mContext = context;
            return this;
        }

        /**
         * 配置提示标题
         *
         * @param title
         * @return
         */
        public TipDialogBuilder title(String title) {
            this.mAlertTitle = title;
            return this;
        }

        /**
         * 设置自定义的contentView
         * 描述：如果该方法不进行配置，则会默认dialog的提示信息只有一个TextView，如果提示信息不仅仅是一个TextView，请调用该方法进行性配置，配置后会隐藏掉默认的TextView提示信息显示控件
         *
         * @param contentView
         * @return
         */
        public TipDialogBuilder contentView(View contentView) {
            this.mContentView = contentView;
            return this;
        }

        /**
         * 配置提示信息
         *
         * @param message
         * @return
         */
        public TipDialogBuilder message(String message) {
            this.mAlertMessage = message;
            return this;
        }

        public TipDialogBuilder closeImageVisible(boolean isVisible) {
            this.isCloseImageVisible = isVisible;
            return this;
        }

        /**
         * 设置Dialog撤销
         *
         * @param cancelable
         * @param canceledOnTouchOutSide
         * @return
         */
        public TipDialogBuilder cancelable(boolean cancelable, boolean canceledOnTouchOutSide) {
            this.mCancelable = cancelable;
            this.mCanceledOnTouchOutSide = canceledOnTouchOutSide;
            return this;
        }

        /**
         * 配置按键类型，是单按键还是双按键，取值见{@link DialogWrapper#BUTTON_SINGLE}和{@link DialogWrapper#BUTTON_DOUBLE}
         *
         * @param buttonType
         * @return
         */
        public TipDialogBuilder buttonType(int buttonType) {
            this.mButtonType = buttonType;
            return this;
        }

        /**
         * 配置左侧按键文本
         *
         * @param text
         * @return
         */
        public TipDialogBuilder leftButtonText(String text) {
            this.mLeftButtonText = text;
            return this;
        }

        /**
         * 配置右侧按键文本
         *
         * @param text
         * @return
         */
        public TipDialogBuilder rightButtonText(String text) {
            this.mRightButtonText = text;
            return this;
        }

        /**
         * 配置单按键文本
         *
         * @param text
         * @return
         */
        public TipDialogBuilder singleButtonText(String text) {
            this.mSingleButtonText = text;
            return this;
        }

        /**
         * 配置按键点击监听
         *
         * @param buttonListener
         * @return
         */
        public TipDialogBuilder buttonClickListener(TipTypeButtonClickListener buttonListener) {
            this.mButtonListener = buttonListener;
            return this;
        }

        public Dialog build() {
            if (mContext == null) {
                throw new IllegalArgumentException("请配置上下文环境context");
            }

            final CustomDialog mAlertDialog = new CustomDialog(mContext, R.style.CustomDialogTheme, Gravity.CENTER);

            //为了减少嵌套层级，布局使用了ConstraintLayout为根节点，但是设置了margin值以让ContentView左右两侧不贴近屏幕，为了让margin值生效，第三个参数不为空，请勿再调用Dialog的setContentView()
//            View mAlertDialogView = View.inflate(mContext, R.layout.dialog_tip_layout, (ViewGroup) mAlertDialog.getWindow().getDecorView());
            View mAlertDialogView = View.inflate(mContext, R.layout.dialog_tip_layout, null);
            FrameLayout mContentContainer = mAlertDialogView.findViewById(R.id.dialog_tip_layout_content_container);
            TextView mTittleTV = mAlertDialogView.findViewById(R.id.dialog_tip_layout_title);
            TextView mMessageTV = mAlertDialogView.findViewById(R.id.dialog_tip_layout_message);
            ImageView mCloseImage = mAlertDialogView.findViewById(R.id.dialog_tip_layout_close);
            final TextView mLeftButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_left_button);
            final TextView mRightButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_right_button);
            final TextView mSingleButton = mAlertDialogView.findViewById(R.id.dialog_tip_layout_single_button);

            mTittleTV.setText(mAlertTitle);
            mMessageTV.setText(mAlertMessage);

            mCloseImage.setVisibility(isCloseImageVisible ? View.VISIBLE : View.INVISIBLE);

            //mContentView不为空，则启用设置的contentView
            if (mContentView != null) {
                mContentContainer.removeAllViews();
                mContentContainer.addView(mContentView);
            }

            if (mButtonType == BUTTON_SINGLE) {
                mLeftButton.setVisibility(View.GONE);
                mRightButton.setVisibility(View.GONE);
                mSingleButton.setVisibility(View.VISIBLE);
                mSingleButton.setText(mSingleButtonText);

                if (mButtonListener != null) {

                    RxView.
                            clicks(mSingleButton).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    mButtonListener.onSingleButtonClicked(mSingleButton);
                                }
                            });
                }
            } else {
                mLeftButton.setVisibility(View.VISIBLE);
                mRightButton.setVisibility(View.VISIBLE);
                mSingleButton.setVisibility(View.GONE);
                mLeftButton.setText(mLeftButtonText);
                mRightButton.setText(mRightButtonText);

                if (mButtonListener != null) {

                    RxView.
                            clicks(mLeftButton).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    mButtonListener.onLeftButtonClicked(mLeftButton);
                                }
                            });

                    RxView.
                            clicks(mRightButton).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    mButtonListener.onRightButtonClicked(mRightButton);
                                }
                            });
                }
            }

            RxView.
                    clicks(mCloseImage).
                    compose(RxClickTransformer.getClickTransformer()).
                    subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            mAlertDialog.dismiss();
                        }
                    });

            mAlertDialog.setCancelable(mCancelable);
            mAlertDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutSide);
            mAlertDialog.setContentView(mAlertDialogView);
            return mAlertDialog;
        }
    }
    /********************************************************************************************************************************************/
    /**
     * 获取AlertDialog的builder
     *
     * @return TipDialogBuilder
     */
    public static ListDialogBuilder listDialog() {
        return new ListDialogBuilder();
    }

    public static class ListDialogBuilder {
        private Context mContext;
        private boolean mCancelable = false;
        private boolean mCanceledOnTouchOutSide = false;
        private String[] mItemListData = null;
        private RecyclerView mRecyclerView;
        private TextView mCancel;
        private View mHeadView;
        private OnItemClickListener mOnItemClickListener;
        private boolean isUserBottomSheetStyle = false;

        /**
         * 配置Context
         *
         * @param context
         * @return
         */
        public ListDialogBuilder context(Context context) {
            this.mContext = context;
            return this;
        }

        /**
         * 设置Dialog撤销
         *
         * @param cancelable
         * @param canceledOnTouchOutSide
         * @return
         */
        public ListDialogBuilder cancelable(boolean cancelable, boolean canceledOnTouchOutSide) {
            this.mCancelable = cancelable;
            this.mCanceledOnTouchOutSide = canceledOnTouchOutSide;
            return this;
        }

        /**
         * @param itemListData
         * @return
         */
        public ListDialogBuilder setItemListData(String[] itemListData) {
            this.mItemListData = itemListData;
            return this;
        }

        public ListDialogBuilder setHeaderView(View mHeadView) {
            this.mHeadView = mHeadView;
            return this;
        }
        public ListDialogBuilder setIsUserBottomSheetStyle(boolean isUserBottomSheetStyle) {
            this.isUserBottomSheetStyle = isUserBottomSheetStyle;
            return this;
        }

        /**
         * @param mOnItemClickListener
         * @return
         */
        public ListDialogBuilder setItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
            return this;
        }


        public Dialog build() {

            //check
            if (mItemListData == null || mItemListData.length == 0) {
                throw new IllegalArgumentException(mContext.getString(R.string.exception_tip_illegal_argument));
            }
            final Dialog dialog;
            View mAlertDialogView = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_bottom_list, null);

            if (isUserBottomSheetStyle) {
                dialog = new BottomSheetDialog(mContext);
                dialog.setContentView(mAlertDialogView);
                View view = (View) mAlertDialogView.getParent();
                view.setBackgroundColor(Color.TRANSPARENT);
            } else {
                dialog = new CustomDialog(mContext, R.style.CustomDialogTheme, Gravity.BOTTOM);
            }
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutSide);

            mRecyclerView = mAlertDialogView.findViewById(R.id.view_dialog_bottom_list_recycler);
            List<RecyclerItemData> mListData = new ArrayList<>(mItemListData.length);
            for (String itemString : mItemListData) {
                RecyclerItemData recyclerItemData = new RecyclerItemData();
                recyclerItemData.setItemData(itemString);
                mListData.add(recyclerItemData);
            }
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(mContext, mListData);
            recyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    dialog.dismiss();
                    mOnItemClickListener.onItemClick(adapter, view, position);
                }
            });
            if (mHeadView != null) {
                recyclerAdapter.addHeaderView(mHeadView);
            }
            mRecyclerView.setAdapter(recyclerAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(linearLayoutManager);
            return dialog;
        }

        private class RecyclerAdapter extends BaseRecyclerViewAdapter<RecyclerItemData> {
            public RecyclerAdapter(Context context, List<RecyclerItemData> data) {
                super(context, data);
                addItemType(GlobalApp.SINGLE_ITEM_TYPE, R.layout.view_dialog_bottom_list_item);
            }

            @Override
            protected void convert(BaseViewHolder helper, RecyclerItemData item) {
                helper.setText(R.id.item_single_text, item.getItemData());
            }
        }

        private class RecyclerItemData implements MultiItemEntity {
            private String mItemData;

            public String getItemData() {
                return mItemData;
            }

            public void setItemData(String mItemData) {
                this.mItemData = mItemData;
            }

            @Override
            public int getItemType() {
                return GlobalApp.SINGLE_ITEM_TYPE;
            }
        }
    }

    /************************************************************************************************************************************************/

    /**
     * 获取自定义View的dialog的builder
     *
     * @return
     */
    public static CustomViewDialogBuilder customViewDialog() {

        return new CustomViewDialogBuilder();
    }

    public static class CustomViewDialogBuilder {

        private Context mContext;
        private View mContentView;
        private int mContentViewGravity;
        private boolean mCancelable = false;
        private boolean mCanceledOnTouchOutSide = false;

        private CustomViewDialogBuilder() {
        }

        /**
         * 配置Context
         *
         * @param context
         * @return
         */
        public CustomViewDialogBuilder context(Context context) {
            this.mContext = context;
            return this;
        }

        /**
         * 配置自定义View
         *
         * @param contentView
         * @return
         */
        public CustomViewDialogBuilder contentView(View contentView) {
            this.mContentView = contentView;
            return this;
        }

        /**
         * 配置自定义View显示的gravity
         *
         * @param gravity
         * @return
         */
        public CustomViewDialogBuilder contentViewGravity(int gravity) {
            this.mContentViewGravity = gravity;
            return this;
        }

        /**
         * 设置Dialog撤销
         *
         * @param cancelable
         * @param canceledOnTouchOutSide
         * @return
         */
        public CustomViewDialogBuilder cancelable(boolean cancelable, boolean canceledOnTouchOutSide) {
            this.mCancelable = cancelable;
            this.mCanceledOnTouchOutSide = canceledOnTouchOutSide;
            return this;
        }

        public Dialog build() {
            CustomDialog mCustomViewDialog = new CustomDialog(mContext, R.style.CustomDialogTheme, mContentViewGravity);
            mCustomViewDialog.setContentView(mContentView);
            mCustomViewDialog.setCancelable(mCancelable);
            mCustomViewDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutSide);
            return mCustomViewDialog;
        }
    }

    /**
     * 获取BottomSheetDialog的builder, BottomSheetDialog动画效果是从底部上划进入
     *
     * @return
     */
    public static BottomSheetDialogBuilder bottomSheetDialog() {
        return new BottomSheetDialogBuilder();
    }

    public static class BottomSheetDialogBuilder {

        private Context mContext;
        private boolean mCancelable;
        private boolean mCancelableOnTouchOutside;
        private View mContentView;

        public BottomSheetDialogBuilder context(Context context) {
            this.mContext = context;
            return this;
        }

        public BottomSheetDialogBuilder cancelable(boolean cancelable, boolean cancelableOnTouchOutside) {
            this.mCancelable = cancelable;
            this.mCancelableOnTouchOutside = cancelableOnTouchOutside;
            return this;
        }

        public BottomSheetDialogBuilder contentView(View contentView) {
            this.mContentView = contentView;
            return this;
        }

        public BottomSheetDialog build() {
            BottomSheetDialog mDialog = new BottomSheetDialog(mContext);
            mDialog.setCancelable(mCancelable);
            mDialog.setCanceledOnTouchOutside(mCancelableOnTouchOutside);

            mDialog.setContentView(mContentView);
            return mDialog;
        }
    }

    public static PromptDialogBuilder promptDialog() {

        return new PromptDialogBuilder();
    }

    public static class PromptDialogBuilder {
        private Context mContext; //上下文环境
        private String mAlertTitle;    //提示性弹窗的标题
        private View mContentView; //自定义的ContentView
        private String mAlertMessage;   //提示性弹窗的提示信息
        private boolean mCancelable = false; //参照Dialog
        private boolean mCanceledOnTouchOutSide = false; //参照Dialog
        private int mButtonType = BUTTON_SINGLE; //Dialog按键类型，决定Dialog的按键为单按键还是双按键,默认单按键
        private String mLeftButtonText; //左侧按键显示文本
        private String mRightButtonText; //右侧按键显示文本
        private String mSingleButtonText; //单按键显示文本
        private TipTypeButtonClickListener mButtonListener;

        private PromptDialogBuilder() {
        }

        /**
         * 配置Context
         *
         * @param context
         * @return
         */
        public PromptDialogBuilder context(Context context) {
            this.mContext = context;
            return this;
        }

        /**
         * 配置提示标题
         *
         * @param title
         * @return
         */
        public PromptDialogBuilder title(String title) {
            this.mAlertTitle = title;
            return this;
        }

        /**
         * 设置自定义的contentView
         * 描述：如果该方法不进行配置，则会默认dialog的提示信息只有一个TextView，如果提示信息不仅仅是一个TextView，请调用该方法进行性配置，配置后会隐藏掉默认的TextView提示信息显示控件
         *
         * @param contentView
         * @return
         */
        public PromptDialogBuilder contentView(View contentView) {
            this.mContentView = contentView;
            return this;
        }

        /**
         * 配置提示信息
         *
         * @param message
         * @return
         */
        public PromptDialogBuilder message(String message) {
            this.mAlertMessage = message;
            return this;
        }

        /**
         * 设置Dialog撤销
         *
         * @param cancelable
         * @param canceledOnTouchOutSide
         * @return
         */
        public PromptDialogBuilder cancelable(boolean cancelable, boolean canceledOnTouchOutSide) {
            this.mCancelable = cancelable;
            this.mCanceledOnTouchOutSide = canceledOnTouchOutSide;
            return this;
        }

        /**
         * 配置按键类型，是单按键还是双按键，取值见{@link DialogWrapper#BUTTON_SINGLE}和{@link DialogWrapper#BUTTON_DOUBLE}
         *
         * @param buttonType
         * @return
         */
        public PromptDialogBuilder buttonType(int buttonType) {
            this.mButtonType = buttonType;
            return this;
        }

        /**
         * 配置左侧按键文本
         *
         * @param text
         * @return
         */
        public PromptDialogBuilder leftButtonText(String text) {
            this.mLeftButtonText = text;
            return this;
        }

        /**
         * 配置右侧按键文本
         *
         * @param text
         * @return
         */
        public PromptDialogBuilder rightButtonText(String text) {
            this.mRightButtonText = text;
            return this;
        }

        /**
         * 配置单按键文本
         *
         * @param text
         * @return
         */
        public PromptDialogBuilder singleButtonText(String text) {
            this.mSingleButtonText = text;
            return this;
        }

        /**
         * 配置按键点击监听
         *
         * @param buttonListener
         * @return
         */
        public PromptDialogBuilder buttonClickListener(TipTypeButtonClickListener buttonListener) {
            this.mButtonListener = buttonListener;
            return this;
        }

        public Dialog build() {
            if (mContext == null) {
                throw new IllegalArgumentException("请配置上下文环境context");
            }

            final CustomDialog mAlertDialog = new CustomDialog(mContext, R.style.CustomDialogTheme, Gravity.CENTER);

            //为了减少嵌套层级，布局使用了ConstraintLayout为根节点，但是设置了margin值以让ContentView左右两侧不贴近屏幕，为了让margin值生效，第三个参数不为空，请勿再调用Dialog的setContentView()
            View mAlertDialogView = View.inflate(mContext, R.layout.layout_prompt_dialog, (ViewGroup) mAlertDialog.getWindow().getDecorView());
            FrameLayout mContentContainer = mAlertDialogView.findViewById(R.id.dialog_tip_layout_content_container);
            TextView mTittleTextView = mAlertDialogView.findViewById(R.id.m_prompt_title_textview);
            ImageView mCloseImageView = mAlertDialogView.findViewById(R.id.m_prompt_close_imageview);
            final TextView mLeftTextView = mAlertDialogView.findViewById(R.id.m_prompt_left_textview);
            final TextView mRightTextView = mAlertDialogView.findViewById(R.id.m_prompt_right_textview);
            final TextView mSingleTextView = mAlertDialogView.findViewById(R.id.m_prompt_singer_textview);

            mTittleTextView.setText(mAlertTitle);
//            mMessageTV.setText(mAlertMessage);

            //mContentView不为空，则启用设置的contentView
            if (mContentView != null) {
                mContentContainer.removeAllViews();
                mContentContainer.addView(mContentView);
            }

            if (mButtonType == BUTTON_SINGLE) {
                mLeftTextView.setVisibility(View.GONE);
                mRightTextView.setVisibility(View.GONE);
                mSingleTextView.setVisibility(View.VISIBLE);
                mSingleTextView.setText(mSingleButtonText);

                if (mButtonListener != null) {

                    RxView.
                            clicks(mSingleTextView).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    mButtonListener.onSingleButtonClicked(mSingleTextView);
                                }
                            });
                }
            } else {
                mLeftTextView.setVisibility(View.VISIBLE);
                mRightTextView.setVisibility(View.VISIBLE);
                mSingleTextView.setVisibility(View.GONE);
                mLeftTextView.setText(mLeftButtonText);
                mRightTextView.setText(mRightButtonText);

                if (mButtonListener != null) {

                    RxView.
                            clicks(mLeftTextView).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    mButtonListener.onLeftButtonClicked(mLeftTextView);
                                }
                            });

                    RxView.
                            clicks(mCloseImageView).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    mAlertDialog.dismiss();
                                }
                            });

                    RxView.
                            clicks(mRightTextView).
                            compose(RxClickTransformer.getClickTransformer()).
                            subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    mButtonListener.onRightButtonClicked(mRightTextView);
                                }
                            });
                }
            }
            mAlertDialog.setCancelable(mCancelable);
            mAlertDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutSide);
            return mAlertDialog;
        }
    }
}
