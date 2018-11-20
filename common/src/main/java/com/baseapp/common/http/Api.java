package com.baseapp.common.http;

import android.support.annotation.NonNull;

import com.baseapp.common.BuildConfig;
import com.baseapp.common.base.BaseApplication;
import com.baseapp.common.base.BaseBean;
import com.baseapp.common.http.config.ApiConfig;
import com.baseapp.common.http.config.RequestConfig;
import com.baseapp.common.http.interceptor.HeaderInterceptor;
import com.baseapp.common.http.interceptor.ParamsInterceptor;
import com.baseapp.common.http.interceptor.RewriteCacheControlInterceptor;
import com.baseapp.common.utils.NetWorkUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    /**
     * 网络请求添加认证头
     */
    public static final int TYPE_HEADER = 1;

    private static ApiConfig mApiConfig;

    private static List<Retrofit> mRetrofitList = new ArrayList<>();

    private static Retrofit mRetrofit = null;

    /*************************缓存设置*********************/
/*
   1. noCache 不使用缓存，全部走网络

    2. noStore 不使用缓存，也不存储缓存

    3. onlyIfCached 只使用缓存

    4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

    5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

    6. minFresh 设置有效时间，依旧如上

    7. FORCE_NETWORK 只走网络

    8. FORCE_CACHE 只走缓存*/

    /**
     * 设缓存有效期为两天
     */
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";


    /**
     * 设置Api的配置类，该方法请在Application中调用
     *
     * @param config
     */
    public static void setConfig(ApiConfig config) {
        mApiConfig = config;
        LogUtils.e("HostServer---set" + config.toString());
    }

    private static Retrofit getRetrofit(int headerType) {
        //Log拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        //网络缓存文件夹
        File cacheFile = new File(BaseApplication.getAppContext().getCacheDir(), "cache");
        //100Mb;
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        RewriteCacheControlInterceptor cacheInterceptor = new RewriteCacheControlInterceptor();

        OkHttpClient.Builder mOkHttpClientBuild = new OkHttpClient.Builder()
                .readTimeout(mApiConfig.getReadTimeOut(), TimeUnit.MILLISECONDS)
                .connectTimeout(mApiConfig.getConnectTimeOut(), TimeUnit.MILLISECONDS)
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .addInterceptor(new HeaderInterceptor(headerType))
                .addInterceptor(new ParamsInterceptor(BaseApplication.getAppContext()))
                .addInterceptor(logInterceptor)
                .cache(cache);

        if (!BuildConfig.IS_DEBUG) {
            mOkHttpClientBuild.proxy(Proxy.NO_PROXY);
        }
        OkHttpClient mOkHttpClient = mOkHttpClientBuild.build();
        LogUtils.e("HostServer---API" + mApiConfig.toString());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        String url;
        switch (headerType) {
            case TYPE_HEADER: {
                url = mApiConfig.getHostServer();
            }
            break;
            default:
                url = mApiConfig.getHostServer();
        }


        return mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }


    /**
     * 根据网络状况获取缓存的策略
     */
    @NonNull
    public static String getCacheControl() {
        return NetWorkUtils.isNetConnected(BaseApplication.getAppContext()) ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }

    public static <T> T getService(Class<T> clazz) {
        if (mRetrofitList.size() < 1 || mRetrofitList.get(0) == null) {
            mRetrofitList.add(0, getRetrofit(TYPE_HEADER));
        }

        return mRetrofitList.get(0).create(clazz);
    }

    public static <T> T getHeaderService(Class<T> clazz) {

        if (mRetrofitList.size() < 2 || mRetrofitList.get(1) == null) {

            mRetrofitList.add(1, getRetrofit(TYPE_HEADER));
        }

        return mRetrofitList.get(1).create(clazz);
    }

    public static <R, T extends BaseBean<R>> RequestConfig observable(Observable<T> observable) {

        RequestConfig<R, T> mRequestConfig = new RequestConfig<R, T>();
        mRequestConfig.observable(observable);
        return mRequestConfig;
    }


}