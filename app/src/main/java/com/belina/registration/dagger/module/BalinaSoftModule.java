package com.belina.registration.dagger.module;


import com.belina.registration.dagger.module.qualifier.BalinaSoftAPIQualifier;
import com.belina.registration.dagger.module.qualifier.IpApiQualifier;
import com.belina.registration.dagger.scope.ApplicationScope;
import com.belina.registration.data.api.IpApi;
import com.belina.registration.data.balinasoft.api.BalinaSoftApi;
import com.belina.registration.utils.Constant;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class BalinaSoftModule {

    @Provides
    public BalinaSoftApi ipApi(@BalinaSoftAPIQualifier Retrofit retrofit){
        return retrofit.create(BalinaSoftApi.class);
    }

    @ApplicationScope
    @BalinaSoftAPIQualifier
    @Provides
    public Retrofit getRetrofit(OkHttpClient okHttpClient,
                                GsonConverterFactory gsonConverterFactory){

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.IP_BALINA_SOFT_API)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
