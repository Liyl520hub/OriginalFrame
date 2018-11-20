package com.baseapp.common.utility;

import com.baseapp.common.base.config.BaseConfig;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/1/12 0012.
 * @Desc 对于不使用Recycler的页面，泛型可以传递该对象
 */

public class MultiItemPlaceHolder implements MultiItemEntity {

    @Override
    public int getItemType() {
        return BaseConfig.SINGLE_ITEM_TYPE;
    }
}
