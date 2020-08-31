package com.example.blackninja.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class UserRegistrationResponse {
    private String reference;
    private String email;
    private String firstName;
    private String lastname;

    public UserRegistrationResponse(String reference, String email, String firstName, String lastname) {
        this.reference = reference;
        this.email = email;
        this.firstName = firstName;
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegistrationResponse response = (UserRegistrationResponse) o;
        return Objects.equals(reference, response.reference) &&
                Objects.equals(email, response.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, email);
    }
}
