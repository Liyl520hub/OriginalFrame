package com.baseapp.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/3/17 0017.
 * @Desc: 调整了子View绘制顺序的线性布局，第一个子view比第二个子view后绘制，这样第二个子view属性动画上移，不会遮挡第一个子view
 */

public class ReorderLinearLayout extends LinearLayout{

    public ReorderLinearLayout(Context context) {
        super(context);
    }

    public ReorderLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReorderLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 调整了第一个和第二个子View的绘制顺序
     * @param childCount
     * @param i
     * @return
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i == 0){
            return 1;
        }else if (i == 1){
            return 0;
        }else {
            return super.getChildDrawingOrder(childCount, i);
        }
    }

    public void enableChildrenDrawingOrder(boolean enabled) {
        setChildrenDrawingOrderEnabled(enabled);
    }

}
