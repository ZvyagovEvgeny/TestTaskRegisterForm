package com.belina.registration.ui.base.viewmodel;

public interface ViewModelFactory<T extends StoredViewModel> {

    T create();

}
