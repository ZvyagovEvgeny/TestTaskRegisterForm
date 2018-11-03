package com.belina.registration.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailCheckerV2 {


    final String regex = "([a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256})\\@(([a-zA-Z0-9][a-zA-Z0-9\\-]{0,64})(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+)";



    private List<String> popularDomains;

    public EmailCheckerV2( List<String> popularDomains){
        this.popularDomains = popularDomains;
    }

    public CheckResult check(String email){

        Pattern pattern = Pattern.compile(regex);
        if(email.isEmpty())return CheckResult.EMPTY;

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            if(isPopularDomain(getEmailDomain(email))){
                return CheckResult.OK;
            }else {
                return CheckResult.UNKNOWN_DOMAIN_NAME;
            }

        } else {
            return CheckResult.NOT_VALID;
        }
    }

    boolean isPopularDomain(String domainName){
        for(String popDomain :popularDomains){
            if(popDomain.equals("@"+domainName)){
                return true;
            }
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

}
