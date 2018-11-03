package com.belina.registration.dagger.module;

import com.belina.registration.dagger.module.qualifier.IpApiQualifier;
import com.belina.registration.dagger.scope.ApplicationScope;
import com.belina.registration.data.api.IpApi;
import com.belina.registration.utils.Constant;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {OkHttpClientModule.class, ApiModule.class})
public class IpApiModule {

    @Provides
    public IpApi ipApi(@IpApiQualifier Retrofit retrofit){
        return retrofit.create(IpApi.class);
    }

    @ApplicationScope
    @IpApiQualifier
    @Provides
    public Retrofit getRetrofit(OkHttpClient okHttpClient,
                                GsonConverterFactory gsonConverterFactory){

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.IP_API_ADDRESS)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
