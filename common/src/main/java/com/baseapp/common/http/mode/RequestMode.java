package com.baseapp.common.http.mode;

/**
 * Created by Administrator on 2018/1/17 0017.
 * @Desc： 网络请求模式  单请求/链式请求  请求模式只针对一个Activity或者Fragment而言，用于调度网络加载LoadingView的显示和隐藏
 *          链式请求设计的目的主要是避免LoadingView在多个网络请求链式调用的情况下频繁的显示和隐藏
 * 警告1：暂未设计并行网络请求，LoadingView的显示隐藏调度并未得到验证，开发中请勿使用并行网络请求
 *
 * 警告2：由于基类封装原因，对于http返回码200，但是服务器返回数据code不为1000也就是业务错误的情况下，结果仍回调到onNext()方法中，对于链式请求，此时对于非1000的业务错误码，需要手动隐藏LoadingView
 *        其余情况不需要手动隐藏。
 *        该情况出现原因：修改基类时，APP一期已经基本开发完毕，为避免大规模修改，未对错误统一处理进行优化后的设计，后期可考虑增加数据解析JavaBean的基类。
 */

public enum  RequestMode {

    SINGLE,

    /**
     * 警告：由于封装基类时，
     */
    CHAIN;
}
