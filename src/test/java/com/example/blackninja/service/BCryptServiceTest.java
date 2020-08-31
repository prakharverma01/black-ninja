package com.example.blackninja.service;

import com.example.blackninja.service.security.BCryptService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BCryptServiceTest {

    @Test
    public void encryptionTest() {
        String test_passwd = "james@1234";
        String some_stored_hash = "$2a$12$5Nujh1Puiu9u2UYfm/cRW.v89m7pgxk0SmRZ.6tgaFRskljM4A0bS";
        String computed_hash = BCryptService.hashPassword(test_passwd);
        Assertions.assertTrue(BCryptService.checkPassword(test_passwd, some_stored_hash));
        Assertions.assertTrue(BCryptService.checkPassword(test_passwd, computed_hash));
    }
}