package com.belina.registration.ui.base.events;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;

public class ProgressBarMessage extends BaseObservable{

    private String message;
    private ObservableBoolean show = new ObservableBoolean(false);

    public ProgressBarMessage() {
        this.message = "";
        this.show.set(false);
    }

    public void showMessage(String message){
        this.message = message;
        show.set(true);
        notifyChange();
    }

    public void close(){
        show.set(false);
        notifyChange();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShow() {
        return show.get();
    }
}
