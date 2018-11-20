package com.baseapp.common.baserx;

import com.baseapp.common.base.BaseBean;
import com.baseapp.common.http.error.ApiException;
import com.baseapp.common.http.error.ErrorType;
import com.baseapp.common.http.error.ExceptionConverter;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/26 0026.
 *
 * @Desc 统一封装网络请求错误, 同时调度了线程
 */

/**
 * 为了方便向RxSubscriber的错误回调中传递javabean的相关信息，该类弃用
 */
@Deprecated
public class RxHandleError {

    /**
     * @param <R> BaseBean的泛型
     * @param <T> 继承BaseBean的接口返回封装JavaBean
     * @return
     */
    public static <R, T extends BaseBean<R>> ObservableTransformer<T, R> getHandleErrorTransformer() {

        return new ObservableTransformer<T, R>() {

            @Override
            public ObservableSource<R> apply(Observable<T> upstream) {
                return upstream.
                        flatMap(new Function<T, ObservableSource<R>>() {
                            @Override
                            public ObservableSource<R> apply(T t) throws Exception {

                                if (t.getCode() == 1000) {  //返回1000成功码

                                    return Observable.just(t.getData());  //成功直接返回数据

                                } else {  //返回非1000的错误码

                                    Throwable mThrowable = new Throwable("接口返回了错误业务码-----" + t.getCode());

                                    throw new ApiException(t.getCode(), ErrorType.ERROR_API, t.getMessage(), mThrowable);
                                }

                            }
                        }).
                        subscribeOn(Schedulers.io()).
                        unsubscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        onErrorResumeNext(new Function<Throwable, ObservableSource<? extends R>>() {
                            @Override
                            public ObservableSource<? extends R> apply(Throwable throwable) throws Exception {

                                return Observable.error(ExceptionConverter.convertException(throwable));
                            }
                        });
            }
        };
    }
}
