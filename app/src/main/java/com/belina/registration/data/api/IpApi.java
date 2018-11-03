package com.belina.registration.data.api;

import com.belina.registration.data.api.model.IpApiQueryResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IpApi {

     String STATUS_SUCCESS = "success";
     String STATUS_FAILD = "fail";

    @GET("json/{domain_address}")
    Observable<IpApiQueryResult> checkDomainName(@Path("domain_address")String address);



}
