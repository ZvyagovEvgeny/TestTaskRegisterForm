package com.belina.registration.dagger.module;

import android.content.Context;

import com.belina.registration.dagger.context.ApplicationContext;
import com.belina.registration.dagger.scope.ApplicationScope;


import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @ApplicationContext
    @ApplicationScope
    @Provides
    public Context getContext() {
        return context.getApplicationContext();
    }
}
