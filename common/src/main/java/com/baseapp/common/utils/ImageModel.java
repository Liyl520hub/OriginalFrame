package com.baseapp.common.utils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/1/19 0019.
 * 选择图片的model
 */

public class ImageModel implements MultiItemEntity{

    public static final int TYPE_IMAGE_ADD=0;
    public static final int TYPE_IMAGE_SELECTED=1;

    public static final String ADD_IMAGE_URI = "add";

    private String uris;  //图片的uri
    private int itemType;  //图片类型  0：添加图片  1：已选择图片

    public String getUris() {
        return uris;
    }

    public void setUris(String uris) {
        this.uris = uris;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return ADD_IMAGE_URI.equals(getUris()) ? TYPE_IMAGE_ADD:TYPE_IMAGE_SELECTED;
    }
}
