package com.belina.registration.model;

import android.util.Patterns;

public class EmailChecker extends FieldChecker {
    @Override
    public void check(Field field) {
        String email = field.string.get();

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            field.isValid.set(true);
            field.errorMessage.set("");
        } else {
            field.isValid.set(false);
            field.errorMessage.set("invalid Email address");
        }
    }
}
