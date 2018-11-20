package com.baseapp.common.utils;



/**
 * @author Android-Dev04
 * @date 2018/4/13
 * description:阿里Arouter工具类 提供跳转及简单传值跳转
 */

public class ArouterUtils {


//    /**
//     * startActivity（）
//     * 不传值
//     *
//     * @param path 路径
//     */
//    public static void setAroutePath(String path) {
//        ARouter.getInstance().build(path).navigation();
//    }
//
//    /**
//     * startActivityForResult()
//     * 不传值
//     *
//     * @param path 路径
//     */
//    public static void setAroutePath(String path, Activity mActivity, int requestCode) {
//        ARouter.getInstance().build(path).navigation(mActivity, requestCode);
//    }
//
//
//    /**
//     * startActivity（）
//     * 传单一类型值且数量为一
//     *
//     * @param path  路径
//     * @param key   所传值的key
//     * @param value 所传值的value
//     */
//    public static void setAroutePath(String path, String key, Object value) {
//
//        if (value instanceof String) {
//            ARouter.getInstance().build(path).withString(key, (String) value).navigation();
//
//        } else if (value instanceof Boolean) {
//            ARouter.getInstance().build(path).withBoolean(key, (Boolean) value).navigation();
//
//        } else if (value instanceof Integer) {
//            ARouter.getInstance().build(path).withInt(key, (Integer) value).navigation();
//
//        } else if (value instanceof Double) {
//            ARouter.getInstance().build(path).withDouble(key, (Double) value).navigation();
//
//        } else if (value instanceof Float) {
//            ARouter.getInstance().build(path).withFloat(key, (Float) value).navigation();
//
//        } else if (value instanceof Long) {
//            ARouter.getInstance().build(path).withLong(key, (Long) value).navigation();
//
//        }else {
//            ARouter.getInstance().build(path).withObject(key, value).navigation();
//
//        }
//
//    }
//
//    /**
//     * startActivity（）
//     * 传多种类型值或者数量不为一
//     *
//     * @param path   路径
//     * @param bundle 传递的值
//     */
//    public static void setAroutePath(String path, Bundle bundle) {
//        ARouter.getInstance().build(path).with(bundle).navigation();
//    }
//
//
//    /**
//     * startActivityForResult()
//     * 当传递的值为单一类型且数量为1使用此方法
//     *
//     * @param path        路径
//     * @param key         所传值的key
//     * @param value       所传值的value
//     * @param mActivity   当前activity
//     * @param requestCode 请求码
//     */
//    public static void setAroutePath(String path, String key, Object value, Activity mActivity, int requestCode) {
//
//        if (value instanceof String) {
//            ARouter.getInstance().build(path).withString(key, (String) value).navigation(mActivity, requestCode);
//
//        } else if (value instanceof Boolean) {
//            ARouter.getInstance().build(path).withBoolean(key, (Boolean) value).navigation(mActivity, requestCode);
//
//        } else if (value instanceof Integer) {
//            ARouter.getInstance().build(path).withInt(key, (Integer) value).navigation(mActivity, requestCode);
//
//        } else if (value instanceof Double) {
//            ARouter.getInstance().build(path).withDouble(key, (Double) value).navigation(mActivity, requestCode);
//
//        } else if (value instanceof Float) {
//            ARouter.getInstance().build(path).withFloat(key, (Float) value).navigation(mActivity, requestCode);
//
//        } else if (value instanceof Long) {
//            ARouter.getInstance().build(path).withLong(key, (Long) value).navigation(mActivity, requestCode);
//
//        } else {
//            ARouter.getInstance().build(path).withObject(key, value).navigation(mActivity, requestCode);
//
//        }
//
//    }
//
//
//    /**
//     * startActivityForResult()
//     * 当传递的值为多种类型使用此方法
//     *
//     * @param path        路径
//     * @param bundle      传递的值（Bundle）
//     * @param mActivity   当前activity
//     * @param requestCode 请求码
//     */
//    public static void setAroutePath(String path, Bundle bundle, Activity mActivity, int requestCode) {
//        ARouter.getInstance().build(path).with(bundle).navigation(mActivity, requestCode);
//    }


}