package com.belina.registration.dagger.module;

import dagger.Module;
import dagger.Provides;

import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    public GsonConverterFactory getGsonConverterFactory(){
        return GsonConverterFactory.create();
    }

}
