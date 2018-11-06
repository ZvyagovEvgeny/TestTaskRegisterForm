package com.belina.registration.data.api;

import com.belina.registration.data.api.model.IpApiQueryResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IpApi {

    public enum Status{

        SUCCESS("success"),
        STATUS_FAIL("fail");

        String status;

        Status(String status){
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    @GET("json/{domain_address}")
    Observable<IpApiQueryResult> checkDomainName(@Path("domain_address")String address);



}
