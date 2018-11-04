package com.belina.registration.data.balinasoft.api.model;

public enum StatusCode {

    LOGIN_ALREADY_IN_USE(400),
    SUCCESS(200);

    private int code;
    private StatusCode(int code ){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
