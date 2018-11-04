package com.belina.registration.ui.base.events;

public class ProgressBarMessage {

    private String message;
    private boolean show;

    public ProgressBarMessage(String message) {
        this.message = message;
        this.show = true;
    }

    public ProgressBarMessage(boolean show) {
        this.show = show;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShow() {
        return show;
    }
}
