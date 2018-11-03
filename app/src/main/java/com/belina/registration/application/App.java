package com.belina.registration.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.belina.registration.dagger.component.ApplicationComponent;
import com.belina.registration.dagger.component.DaggerApplicationComponent;
import com.belina.registration.dagger.module.ContextModule;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class App extends Application {

    private Scheduler scheduler;

    private ApplicationComponent moviesDBComponent;

    public static App get(Activity activity){
        return (App) activity.getApplication();
    }

    private static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public static App create(Context context) {
        return App.get(context);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        moviesDBComponent = DaggerApplicationComponent
                .builder()
                .contextModule(
                        new ContextModule(this))
                .build();

    }

    public ApplicationComponent getMoviesDBComponent() {
        return moviesDBComponent;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();

        }

        return scheduler;
    }
}
