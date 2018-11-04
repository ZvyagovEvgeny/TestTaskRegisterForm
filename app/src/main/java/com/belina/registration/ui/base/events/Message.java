package com.belina.registration.ui.base.events;

public class Message {

    private String message;
    private String title;
    private Callback okCallback;
    private Callback cancelCallback;
    private String okButton;
    private String cancelButton;

    public String getMessage() {
        return message;
    }

    public Message(String title, String message,String okButtonMessage, String cancelButtonMessage,  Callback okCallback,Callback cancelCallback) {
        this.message = message;
        this.title = title;
        this.okCallback = okCallback;
        this.cancelCallback = cancelCallback;
        this.okButton = okButtonMessage;
        this.cancelButton = cancelButtonMessage;
    }

    public Message(String title, String message,String okButtonMessage, Callback callback) {
        this.message = message;
        this.title = title;
        this.okCallback = callback;
        this.okButton = okButtonMessage;
    }

    public interface Callback{
        void call();
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
}
