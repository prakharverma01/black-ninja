package com.example.blackninja.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneralUtilsTest {

    @Test
    void verifyEmailString() {
        Assertions.assertTrue(GeneralUtils.verifyEmailString("abc@mail.com"));
        Assertions.assertTrue(GeneralUtils.verifyEmailString("abc@mail.in"));
        Assertions.assertTrue(GeneralUtils.verifyEmailString("abc@mail.gov.in"));
        Assertions.assertTrue(GeneralUtils.verifyEmailString("abc@mail.gov.au.in"));
        Assertions.assertFalse(GeneralUtils.verifyEmailString("abc@@mail.com"));
        Assertions.assertFalse(GeneralUtils.verifyEmailString("abc@@mail"));
        Assertions.assertFalse(GeneralUtils.verifyEmailString("@."));
    }

    @Test
    void getFullName() {
        Assertions.assertEquals(GeneralUtils.getFullName("Prakhar", "Verma"), "Prakhar Verma");
        Assertions.assertEquals(GeneralUtils.getFullName("Prakhar", null), "Prakhar");
    }
}