package com.assignment.dto.account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {
    private static final String EMAIL_PATTERN = "^(.+)@(\\S+)$";
    private static final String PASSWORD_PATTERN =  "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern patternPassword = Pattern.compile((PASSWORD_PATTERN));
    public static boolean isValidEmail(final String email){
        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidPassWord(final String passowrd){
        Matcher matcher = patternPassword.matcher(passowrd);
        return matcher.matches();
    }

}
