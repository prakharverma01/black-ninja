package com.example.blackninja.dtos.request;

public class UserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String companyName;
    private String role;
    private String password;

    public UserRequest(String firstname, String lastname, String email, String phone, String companyName, String role, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.companyName = companyName;
        this.role = role;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
