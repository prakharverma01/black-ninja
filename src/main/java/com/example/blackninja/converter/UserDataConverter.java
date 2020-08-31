package com.example.blackninja.converter;

import com.example.blackninja.dtos.request.UserRequest;
import com.example.blackninja.model.User;
import com.example.blackninja.service.security.BCryptService;

import java.util.UUID;

public class UserDataConverter {

    public static User convertUserRequestToUser(UserRequest ur) {
        String uuid = UUID.randomUUID().toString();
        String hashPassword = BCryptService.hashPassword(ur.getPassword());
        return new User(ur.getFirstname(), ur.getLastname(), ur.getEmail(), ur.getPhone(), uuid, hashPassword);
    }
}
