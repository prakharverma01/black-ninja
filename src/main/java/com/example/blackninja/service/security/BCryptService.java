package com.example.blackninja.service.security;

import org.springframework.stereotype.Service;

@Service
public class BCryptService {

    public static String hashPassword(String password_plaintext) {

        // Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.
        int workload = 12;
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        return(hashed_password);
    }

    public static boolean checkPassword(String password_plaintext, String stored_hash) {

        boolean password_verified = false;
        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);
        return(password_verified);
    }
}