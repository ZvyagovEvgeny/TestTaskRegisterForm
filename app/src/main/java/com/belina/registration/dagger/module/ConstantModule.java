package com.belina.registration.dagger.module;


import android.content.Context;

import com.belina.registration.dagger.context.ApplicationContext;
import com.belina.registration.dagger.scope.ApplicationScope;
import com.belina.registration.utils.Constant;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
@ApplicationScope
public class ConstantModule {

    @Provides
    @ApplicationScope
    public Constant constant(@ApplicationContext Context context){
        return new Constant(context);
    }

}
