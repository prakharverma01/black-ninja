package com.example.blackninja.enums;

public enum UserRoles {

    USER("USER"),
    ADMIN("ADMIN");

    public final String label;

    private UserRoles(String label) {
        this.label = label;
    }

}
