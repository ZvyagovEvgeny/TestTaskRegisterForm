package com.belina.registration.model.password;

public class PasswordChecker {

    private int minLength;
    private int maxLength;

    public PasswordChecker(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public PasswordCheckResult check(String password){
        if(password.isEmpty())return PasswordCheckResult.EMPTY;

        if(password.length()<minLength)
            return PasswordCheckResult.PASSWORD_IS_SHORT;
        if(password.length()>maxLength)
            return PasswordCheckResult.PASSWORD_IS_LONG;

        return PasswordCheckResult.OK;

    }
}
