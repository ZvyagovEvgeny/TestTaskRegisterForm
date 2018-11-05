package com.belina.registration.ui.base.events;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

public class Message extends BaseObservable {
    public interface Callback{
        void call();
    }

    private String message;
    private String title;
    private Callback okCallback;
    private Callback cancelCallback;
    private String okButton;
    private String cancelButton;
    private boolean show = true;

    public String getMessage() {
        return message;
    }

    public Message(String title, String message,String okButtonMessage, String cancelButtonMessage,
                   Callback okCallback,Callback cancelCallback) {
        this.message = message;
        this.title = title;
        this.okCallback = okCallback;
        this.cancelCallback = cancelCallback;
        this.okButton = okButtonMessage;
        this.cancelButton = cancelButtonMessage;
    }

    public Message() {
    }

    public void callOkCallback(){
        if(okCallback!=null)okCallback.call();
    }
    public void callCancelCallback(){if(cancelCallback!=null) cancelCallback.call();}

    public String getTitle() {
        return title;
    }


    public String getOkButton() {
        return okButton;
    }

    public String getCancelButton() {
        return cancelButton;
    }

    public void setShow(boolean show) {
        this.show = show;
        notifyChange();
    }

    public boolean isShow() {
        return show;
    }
}
