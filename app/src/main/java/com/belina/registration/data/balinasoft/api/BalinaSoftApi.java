package com.belina.registration.data.balinasoft.api;

import com.belina.registration.data.balinasoft.api.model.Account;
import com.belina.registration.data.balinasoft.api.model.BalinasoftResponce;
import com.belina.registration.data.balinasoft.api.model.SignUpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BalinaSoftApi {

    @POST("api/account/signup")
    Observable<BalinasoftResponce> signup(@Body Account account);

}
