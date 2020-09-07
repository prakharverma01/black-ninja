package com.example.blackninja.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralUtils {

    public static boolean verifyEmailString(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    public static String getFullName(String firstName, String lastName) {
        List<String> nameList = Arrays.asList(firstName, lastName).stream()
                .filter(StringUtils::isNotBlank)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        return String.join(" ", nameList);
    }
}
