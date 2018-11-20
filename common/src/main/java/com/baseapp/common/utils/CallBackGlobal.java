package com.baseapp.common.utils;

/**
 *
 * @author Android-Dev04
 * @date 2017/10/19
 * description:公共的回调接口
 */

public interface CallBackGlobal<T> {

    void returnSuccess(T t);

    void returnFail(String failstring);

}
