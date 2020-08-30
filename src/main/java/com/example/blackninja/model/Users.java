package com.example.blackninja.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class Users {

    private @Id Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String reference;

    public Users(
        Long id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String reference
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.reference = reference;
    }
}
