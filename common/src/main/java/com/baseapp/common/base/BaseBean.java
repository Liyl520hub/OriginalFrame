package com.baseapp.common.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26 0026.
 * @Desc 接口返回数据JavaBean的基类，所有JavaBean务必继承该基类，方便封装错误统一处理
 */

public class BaseBean<T> implements Serializable{

    //接口返回的业务码
    private int code;
    //接口返回信息
    private String message;
    //接口返回数据
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
