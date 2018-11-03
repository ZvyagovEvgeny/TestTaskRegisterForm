package com.belina.registration.dagger.component;

import com.belina.registration.dagger.module.ConstantModule;
import com.belina.registration.dagger.module.IpApiModule;
import com.belina.registration.dagger.scope.ApplicationScope;
import com.belina.registration.data.api.IpApi;
import com.belina.registration.utils.Constant;

import dagger.Component;

@ApplicationScope//singleton
@Component(modules = { IpApiModule.class, ConstantModule.class})
public interface ApplicationComponent {

    IpApi ipApi();
    Constant getConstants();

}
