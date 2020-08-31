package com.example.blackninja.dtos.response;

public class LoginResponse {
    private String referenceId;
    private String email;
    private String firstName;
    private String lastName;

    public LoginResponse(String referenceId, String email, String firstName, String lastName) {
        this.referenceId = referenceId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
