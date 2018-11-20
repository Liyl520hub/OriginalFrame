package com.baseapp.common.app;

import com.baseapp.common.base.BaseBean;
import com.baseapp.common.bean.JavaBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @项目名称: PublicService
 * @类描述: 2018/4/17
 * @创建时间： 2018/4/17
 * @作者: Android-Dev05
 */
public interface PublicService {

    /**
     * 查询xxx
     *
     * @param xxx  用户ID
     * @param xxxx 用户系统编号
     */
    @FormUrlEncoded
    @POST("/xxx/v1/xxx/xxxx")
    Observable<BaseBean<JavaBean>> xxxxxx(@Field("xxx") int xxx,
                                          @Field("xxxx") String xxxx);
}
