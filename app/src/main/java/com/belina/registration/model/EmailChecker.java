package com.belina.registration.model;

import android.util.Patterns;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class EmailChecker extends FieldChecker {
    //This reg exp is modification of reg exp included in a android.util.Patterns.EMAIL_ADDRESS, but devided by groups
    final String regex = "([a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256})\\@([a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})";

    final String userField = "([a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256})\\@";
    final String domaiNameField = "\\@([a-zA-Z0-9][a-zA-Z0-9\\-]{0,64})";
    final String com = "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})";


    @Override
    public boolean check(Field field) {
        String email = field.string.get();

       Pattern pattern = Pattern.compile(regex);

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            field.isValid.set(true);
            field.errorMessage.set("");
            return true;
        } else {
            field.isValid.set(false);
            if(!findError(field,email))
                field.errorMessage.set("invalid Email address");
        }
        return false;
    }

    public String getEmailDomain(String email){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            return matcher.group(2);
        }else return null;
    }



    private boolean findError(Field field, String email){

        Pattern userFieldDomain = Pattern.compile(userField);
        Pattern domainName = Pattern.compile(domaiNameField);
        Pattern comPattern = Pattern.compile(com);
        boolean errorFinded  = false;
        if(!userFieldDomain.matcher(email).find()){
            field.errorMessage.set("Local part invalid");
            errorFinded = true;
        }else if(!domainName.matcher(email).find()){
                field.errorMessage.set("domainName invalid");
            errorFinded = true;
            }
            else if(!comPattern.matcher(email).find()){
                  errorFinded = true;
                field.errorMessage.set("comPattern invalid");
        }
        return errorFinded;
    }



}
