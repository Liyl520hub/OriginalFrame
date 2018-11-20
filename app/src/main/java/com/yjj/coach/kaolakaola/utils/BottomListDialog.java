package com.yjj.coach.kaolakaola.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.yjj.coach.kaolakaola.R;
import com.yjj.coach.kaolakaola.bean.CustomDataBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liyl on 2017/3/20.
 * 底部弹框 带动画  数据由list适配
 */

public class BottomListDialog extends Dialog {

    private Context mContext;
    private List<CustomDataBean> mData;
    private ListView bottomList;


    public BottomListDialog(Context context, List<CustomDataBean> datas) {
        super(context);
        this.mContext = context;
        this.mData = datas;
    }


    public static BottomListDialog newInstance(Context context, List<CustomDataBean> datas) {
        return new BottomListDialog(context, datas);
    }

    private boolean isAnimation = false;
    private LinearLayout mRootView;
    private OnClickBottomDialogListener mListener;

    public void setListener(OnClickBottomDialogListener listener) {
        mListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.bottom_list_dialog_choice);
        initData();
        mRootView = (LinearLayout) findViewById(R.id.bottom_dialog_root_view);

        AnimationUtils.slideToUp(mRootView);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.dimAmount = 0.6f;
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
    }

    private void initData() {

        bottomList = (ListView) findViewById(R.id.list_bottom_dialog);
        final BottomListAdapter bottomListAdapter = new BottomListAdapter(mData);
        bottomList.setAdapter(bottomListAdapter);
        bottomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.tvListItemCilckListener(bottomListAdapter.getItem(position));
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
                BottomListDialog.super.dismiss();
            }
        });
    }

    public interface OnClickBottomDialogListener {

        /**
         * list item的点击事件
         * @param CustomDataBean
         */
        void tvListItemCilckListener(CustomDataBean CustomDataBean);
    }

    /**
     * listview的适配器
     */
    public class BottomListAdapter extends BaseAdapter {

        private List<CustomDataBean> data = new ArrayList<>();
        private BottomListAdapterViewHolder bottomListAdapterViewHolder;

        public BottomListAdapter(List<CustomDataBean> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data != null ? data.size() : 0;
        }

        @Override
        public CustomDataBean getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                bottomListAdapterViewHolder = new BottomListAdapterViewHolder();
                convertView = View.inflate(mContext, R.layout.item_bottom_list_dialog_choice, null);
                bottomListAdapterViewHolder.mTextView = convertView.findViewById(R.id.tv_content);
                convertView.setTag(bottomListAdapterViewHolder);
            } else {
                bottomListAdapterViewHolder = (BottomListAdapterViewHolder) convertView.getTag();
            }
            bottomListAdapterViewHolder.mTextView.setText(data.get(position).getName());
            return convertView;
        }


        class BottomListAdapterViewHolder {
            TextView mTextView;
        }
    }
}
