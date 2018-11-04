package com.belina.registration.model;

public class EmailValidationResult {

    public EmailStatus emailStatus;
    public int indexOfError;

    public EmailValidationResult(EmailStatus emailStatus, int indexOfError) {
        this.emailStatus = emailStatus;
        this.indexOfError = indexOfError;
    }

    public EmailValidationResult(EmailStatus emailStatus) {
        this.emailStatus = emailStatus;
    }
}
