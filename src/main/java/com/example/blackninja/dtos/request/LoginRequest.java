package com.example.blackninja.dtos.request;

public class LoginRequest {
    private String email;
    private String password;
    private String companyName;
    private String role;

    public LoginRequest(String email, String password, String companyName, String role) {
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.role = role;
    }

    public String getRole() { return role; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCompanyName() {
        return companyName;
    }
}
