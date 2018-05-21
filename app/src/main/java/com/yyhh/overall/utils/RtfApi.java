package com.yyhh.overall.utils;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yh on 2017/11/10.
 */

public interface RtfApi {
    /**
     *
     * @param key 用户申请的appkey
     * @param name  梦源关键字
     * @param page 当前页（默认为1）
     * @param size 每页显示大小（默认为20）
     * @return
     */
    @FormUrlEncoded
    @POST("appstore/dream/search")
    Observable<String>ZHOUGONGJIEDREAM(@Field("key")String key,
                                       @Field("name")String name,
                                       @Field("page")int page,
                                       @Field("size")int size);
}
