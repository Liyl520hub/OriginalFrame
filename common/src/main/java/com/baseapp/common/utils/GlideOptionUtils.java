package com.baseapp.common.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Administrator on 2018/1/19 0019.
 * @Desc 获取Glide的请求配置
 */

public class GlideOptionUtils {

    /**
     * Glide简单请求配置，没有加载中和加载失败的占位符图片
     * @return
     */
    public static RequestOptions getSimpleRequestOptions(){
        return new RequestOptions().
                centerCrop().
                diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    public static RequestOptions getPlaceHolderRequestOptions(@DrawableRes int placeHolder){
        return new RequestOptions().
                placeholder(placeHolder).
                centerCrop().
                diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    public static RequestOptions getPlaceHolderRequestOptions(Drawable placeHolder){
        return new RequestOptions().
                placeholder(placeHolder).
                centerCrop().
                diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    public static RequestOptions getPlaceHolderErrorRequestOptions(@DrawableRes int placeHolder, @DrawableRes int error){
        return new RequestOptions().
                placeholder(placeHolder).
                error(error).
                centerCrop().
                diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }
}
