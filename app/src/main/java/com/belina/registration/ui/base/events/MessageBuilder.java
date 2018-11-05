package com.belina.registration.ui.base.events;

public class MessageBuilder {
    private String title;
    private String message;
    private String okButtonMessage;
    private String cancelButtonMessage;
    private Message.Callback okCallback;
    private Message.Callback cancelCallback;

    public MessageBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public MessageBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public MessageBuilder setOkButtonMessage(String okButtonMessage) {
        this.okButtonMessage = okButtonMessage;
        return this;
    }

    public MessageBuilder setCancelButtonMessage(String cancelButtonMessage) {
        this.cancelButtonMessage = cancelButtonMessage;
        return this;
    }

    public MessageBuilder setOkCallback(Message.Callback okCallback) {
        this.okCallback = okCallback;
        return this;
    }

    public MessageBuilder setCancelCallback(Message.Callback cancelCallback) {
        this.cancelCallback = cancelCallback;
        return this;
    }

    public Message createMessage() {
        return new Message(title, message, okButtonMessage, cancelButtonMessage, okCallback, cancelCallback);
    }
}