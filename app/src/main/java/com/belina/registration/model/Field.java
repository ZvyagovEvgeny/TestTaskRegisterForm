package com.belina.registration.model;

import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

public class Field extends BaseObservable{

    public ObservableField<String> string = new ObservableField<>("");
    public ObservableField<String> errorMessage = new ObservableField<>("");
    public ObservableBoolean isValid = new ObservableBoolean();
    public ObservableField<ErrorType> errorType = new ObservableField(ErrorType.ERROR);

    private FieldChecker fieldChecker;

    public Field(){
        Field thisPtr = this;
        string.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(fieldChecker!=null){
                    fieldChecker.check(thisPtr);
                }
                notifyChange();
            }
        });
    }

    public void setFieldChecker(FieldChecker fieldChecker){
        this.fieldChecker = fieldChecker;
    }
}
