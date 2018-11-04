package com.belina.registration.data.balinasoft.api.model;

public enum ErrorCode {

    VALIDATION_ERROR("validation-error"), USER_ALREADY_EXISTS("security.signup.login-already-use");

    private String error;

    public String getError() {
        return error;
    }

    ErrorCode(String error) {
        this.error = error;
    }
}
