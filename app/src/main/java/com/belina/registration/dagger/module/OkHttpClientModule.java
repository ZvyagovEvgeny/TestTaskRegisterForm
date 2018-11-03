package com.belina.registration.dagger.module;

import android.content.Context;

import com.belina.registration.dagger.context.ApplicationContext;
import com.belina.registration.dagger.scope.ApplicationScope;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;


@Module(includes = {ContextModule.class})
public class OkHttpClientModule {

    @Provides
    public OkHttpClient.Builder okHttpClientBuilder(Cache cache,
                                                    HttpLoggingInterceptor httpLoggingInterceptor){
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor);
    }

    @Provides
    public OkHttpClient okHttpClient(Cache cache,
                                        HttpLoggingInterceptor httpLoggingInterceptor){
         return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    public Cache cache(File file){
        return new Cache(file,10 * 1000 * 1000);

    }

    @ApplicationScope
    @Provides
    public File cacheFile(@ApplicationContext Context context){
        File file = new File(context.getCacheDir(), "HttpCache");
        file.mkdirs();
        return file;
    }

    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.d(message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


}
