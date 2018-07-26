package com.example.rxjava_retrofit;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface IpServiceForPost {
    @FormUrlEncoded
    @POST("getIpInfo.php")
    Observable<IpMode> getIpMsg(@Field("ip") String first);
}
