package com.example.blackninja.common;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

public class GeneralUtils {

    public static boolean verifyEmailString(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
