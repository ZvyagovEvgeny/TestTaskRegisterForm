package com.belina.registration.model.email;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class EmailChecker {


    final String regex = "([a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256})\\@(([a-zA-Z0-9][a-zA-Z0-9\\-]{0,64})(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+)";



    private List<String> popularDomains;

    public EmailChecker(List<String> popularDomains){
        this.popularDomains = popularDomains;
    }

    public EmailValidationResult check(String email){

        Pattern pattern = Pattern.compile(regex);
        if(email.isEmpty())return new EmailValidationResult(EmailStatus.EMPTY);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {

            if(isPopularDomain(getEmailDomain(email))){
                return new EmailValidationResult(EmailStatus.OK);
            }else {
                return new EmailValidationResult(EmailStatus.UNKNOWN_DOMAIN_NAME);
            }

        } else {

            EmailStatus atSymbolSearhcResult = checkAtSymobol(email);
            if(atSymbolSearhcResult!=EmailStatus.OK)return new EmailValidationResult(atSymbolSearhcResult);

            Timber.d("Error at: "+indexOfLastMatch(pattern,email));
            return new EmailValidationResult(EmailStatus.NOT_VALID,indexOfLastMatch(pattern,email));
        }
    }

    private EmailStatus checkAtSymobol(String email){

        int count = countSymbolInString(email,'@');
        if(count==1) return EmailStatus.OK;
        if(count>1) return EmailStatus.AT_SYMBOL_OCCURS_SEVERAL_TIMES;
        else{
            return EmailStatus.NO_AT_SYMBOL;
        }
    }

    private static int countSymbolInString(String str, char symbol){
        int count = 0;
        for(char c:str.toCharArray()){
            if(c==symbol){
                count ++;
            }
        }
        return count;
    }

    public boolean isPopularDomain(String domainName){
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

    public String getEmailLokcalPart(String email){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            return matcher.group(1);
        }else return null;
    }


    private static int indexOfLastMatch(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        for (int i = input.length(); i > 0; --i) {
            Matcher region = matcher.region(0, i);
            if (region.matches() || region.hitEnd()) {
                return i;
            }
        }

        return 0;
    }


}
