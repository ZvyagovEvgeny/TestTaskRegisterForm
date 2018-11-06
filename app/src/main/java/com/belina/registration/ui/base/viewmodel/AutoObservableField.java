package com.belina.registration.ui.base.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public class AutoObservableField<T extends BaseObservable> extends ObservableField<T> {

    public AutoObservableField(T value){
        set(value);
    }

    @Override
    public void set(T value){

        if(get()!=null){
            get().removeOnPropertyChangedCallback(callback);
        }
        super.set(value);

        value.addOnPropertyChangedCallback(callback);
    }

    Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            notifyChange();
        }
    };

}
