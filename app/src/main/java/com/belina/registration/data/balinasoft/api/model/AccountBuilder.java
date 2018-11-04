package com.belina.registration.data.balinasoft.api.model;

public class AccountBuilder {
    private String email;
    private String password;

    public AccountBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public AccountBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public Account createAccount() {
        return new Account(email, password);
    }


}