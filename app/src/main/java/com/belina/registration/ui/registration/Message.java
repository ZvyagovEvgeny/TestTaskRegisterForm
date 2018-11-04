package com.belina.registration.ui.registration;

public class Message {

    private String message;
    private String title;
    private Callback callback;
    private String okButton;
    private String cancelButton;

    public String getMessage() {
        return message;
    }

    public Message(String title, String message,String okButtonMessage, String caneclButtonMessage,  Callback callback) {
        this.message = message;
        this.title = title;
        this.callback = callback;
        this.okButton = okButtonMessage;
        this.cancelButton = caneclButtonMessage;
    }

    interface Callback{
        void call();
    }

    public void callOkCallback(){
        if(callback!=null)callback.call();
    }

    public String getTitle() {
        return title;
    }

    public Callback getCallback() {
        return callback;
    }

    public String getOkButton() {
        return okButton;
    }

    public String getCancelButton() {
        return cancelButton;
    }
}
