package com.belina.registration.ui.base.editableField;

import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableField;

public class Field extends BaseObservable{

    public ObservableField<String> string = new ObservableField<>("");
    public ObservableField<String> errorMessage = new ObservableField<>("");
    public ObservableField<ErrorType> errorType = new ObservableField(ErrorType.ERROR);



    public Field(){
        string.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                notifyChange();
            }
        });
    }

}
